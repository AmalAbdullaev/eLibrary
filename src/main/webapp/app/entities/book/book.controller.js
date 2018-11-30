(function () {
        'use strict';

        angular
            .module('eLibraryApp')
            .controller('BookController', BookController);

        BookController.$inject = ['$scope', '$http', 'DataUtils', 'Book', 'FavoriteBook', 'ParseLinks', 'AlertService', 'paginationConstants', 'Principal', 'Profile', 'ReadBook','$state'];

        function BookController($scope, $http, DataUtils, Book, FavoriteBook, ParseLinks, AlertService, paginationConstants, Principal, Profile, ReadBook,$state) {

            var vm = this;

            vm.books = [];
            vm.genres = [];
            vm.popularBooks = [];
            vm.currentGenre = null;
            vm.loadPage = loadPage;
            vm.sendFeedback = sendFeedback;
            vm.loadAll = loadAll;
            vm.itemsPerPage = 16;
            vm.page = 0;
            vm.maxPage = Number.POSITIVE_INFINITY;
            vm.busy = false;
            vm.links = {
                last: 0
            };

            $scope.isAlertVisible = false;
            $scope.alert = {
                type: 'success',
                message: null
            };

            vm.predicate = 'id';
            vm.reset = reset;
            vm.reverse = true;
            vm.openFile = DataUtils.openFile;
            vm.byteSize = DataUtils.byteSize;

            vm.user = null;
            $scope.genres = [];

            vm.favoriteBook = null;
            vm.readBook = null;

            $scope.isRead = function (bookId) {

                if (vm.readBook === null) {
                    return false
                }

                var bool = false;
                vm.readBook.forEach(function (readBook) {
                    if (readBook.bookId === bookId) {
                        bool = true;
                        return;
                    }
                });
                return bool;
            };

            function closeAlert() {

                $scope.alert.type = null;
                $scope.alert.message = null;
                $scope.isAlertVisible = false;
            }

            function showAlert(type, message) {
                $scope.alert.type = type;
                $scope.alert.message = message;
                $scope.isAlertVisible = true;
            }

            function load() {
                Principal.identity().then(function (user) {
                    if (user === null) {
                        console.log('user is unauthorized');
                        return;
                    }
                    Profile.getProfile({userId: user.id}, onSuccess);

                    function onSuccess(result) {
                        vm.profile = result;

                        FavoriteBook.query({'profileId.equals': vm.profile.id}, onSuccess);

                        function onSuccess(result) {
                            vm.favoriteBook = result;
                        }


                        ReadBook.query({'profileId.equals': vm.profile.id}, onReadSuccess);

                        function onReadSuccess(result) {
                            vm.readBook = result;
                        }

                    }
                })
            }

            $scope.isFavorite = function (bookId) {

                if (vm.favoriteBook === null) {
                    return false;
                }
                var bool = false;
                vm.favoriteBook.forEach(function (favoriteBook) {
                    if (favoriteBook.bookId === bookId)
                        bool = true;
                });
                return bool;
            };

            $scope.getFavorite = function (bookId) {
                if ($scope.isFavorite(bookId)) {
                    FavoriteBook.query({'profileId.equals': vm.profile.id, 'bookId.equals': bookId}, onSuccess);

                    function onSuccess(result) {
                        var id = result[0].id;
                        FavoriteBook.delete({'id': id}, success);

                        function success() {
                            load();
                        }
                    }

                }
                else {
                    var favoriteBook = new FavoriteBook();
                    favoriteBook.bookId = bookId;
                    favoriteBook.profileId = vm.profile.id;
                    FavoriteBook.save(favoriteBook, success);

                    function success() {
                        load();
                    }
                }
            };

            $scope.selectGenre = function (genreId) {
                vm.currentGenre = genreId;
                $scope.reloadAll();
            };

            $scope.options = {
                data: [
                    {
                        name: "По возрастанию",
                        predicate: "title",
                        reverse: true
                    },
                    {
                        name: "По убыванию",
                        predicate: "title",
                        reverse: false
                    },
                    {
                        name: "Сначала новые",
                        predicate: "lastModifiedDate",
                        reverse: false
                    },
                    {
                        name: "Сначала старые",
                        predicate: "lastModifiedDate",
                        reverse: true
                    }
                ],
                selected: {name: "По возрастанию", predicate: "title", reverse: true}
            };

            $scope.reloadAll = function () {
                vm.predicate = $scope.options.selected.predicate;
                vm.reverse = $scope.options.selected.reverse;
                reset();
            };


            $scope.getAll =  function(){
                $state.reload();
            }

            function loadAll() {
                if (vm.busy) return;
                vm.busy = true;

                if (vm.page === 0) {
                    vm.predicate = $scope.options.selected.predicate;
                    vm.reverse = $scope.options.selected.reverse;

                    $http.get('/api/genres').success(function (data) {
                        vm.genres = data;
                    });
                    $http.get('/api/favorite-books/top').success(function (data) {
                        vm.popularBooks = data;
                    });
                }

                load();

                if (vm.currentGenre != null)
                    Book.query({
                        page: vm.page,
                        size: vm.itemsPerPage,
                        sort: sort(),
                        'genreId.equals': vm.currentGenre,
                        'approved.equals':true

                    }, onSuccess, onError);
                else
                    Book.query({
                        page: vm.page,
                        size: vm.itemsPerPage,
                        sort: sort(),
                        'approved.equals':true

                    }, onSuccess, onError);

                function sort() {
                    var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                    if (vm.predicate !== 'id') {
                        result.push('id');
                    }
                    return result;
                }

                function onSuccess(data, headers) {
                    vm.links = ParseLinks.parse(headers('link'));
                    vm.totalItems = headers('X-Total-Count');
                    vm.maxPage = vm.totalItems / vm.itemsPerPage;
                    for (var i = 0; i < data.length; i++)
                        vm.books.push(data[i]);
                    vm.page++;
                    vm.busy = false;
                }

                function onError(error) {
                    AlertService.error(error.data.message);
                }
            }

            function reset() {
                vm.page = 0;
                vm.books = [];
                loadAll();
            }

            function loadPage(page) {
                vm.page = page;
                loadAll();
            }

            $scope.feedback = {};

            function sendFeedback() {
                console.log($scope.feedback);
                $http.post('/api/feedback', $scope.feedback).success(function () {
                    showAlert('success', 'Ваш отзыв успешно отправлен.');
                    $scope.feedback = {};
                });
            }
        }
    }
)();
