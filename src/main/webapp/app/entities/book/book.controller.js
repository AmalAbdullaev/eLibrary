(function () {
        'use strict';

        angular
            .module('eLibraryApp')
            .controller('BookController', BookController);

        BookController.$inject = ['$scope', '$http', 'DataUtils', 'Book', 'FavoriteBook', 'ParseLinks', 'AlertService', 'paginationConstants'];

        function BookController($scope, $http, DataUtils, Book, FavoriteBook, ParseLinks, AlertService, paginationConstants) {

            var vm = this;

            vm.books = [];
            vm.genres = [];
            vm.loadPage = loadPage;
            vm.itemsPerPage = paginationConstants.itemsPerPage;
            vm.page = 0;
            vm.links = {
                last: 0
            };
            vm.predicate = 'id';
            vm.reset = reset;
            vm.reverse = true;
            vm.openFile = DataUtils.openFile;
            vm.byteSize = DataUtils.byteSize;

            $scope.genres = [];

            $scope.makeFavorite = function ($event) {
                var elem = angular.element($event.currentTarget);
                var book = elem.parents('.rBook').scope().book;
                if (elem.hasClass('btnLibFavoriteBook')) {
                    var favoriteBook = new FavoriteBook();
                    favoriteBook.bookId = book.id;
                    favoriteBook.profileId = book.profileId;
                    elem.removeClass();
                    elem.addClass('btnLibFavoriteBookChecked');
                    FavoriteBook.save(favoriteBook);
                }
                else {
                    elem.removeClass();
                    elem.addClass('btnLibFavoriteBook');
                    $http.get('/api/favorite-books?bookId.equals=' + book.id
                        + '&profileId.equals=' + book.profileId).success(function (response) {
                        response = response[0];
                        FavoriteBook.delete({id: response.id})
                    });
                }
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
                selected: {name: "По возрастанию", predicate: "title", reverse: false}
            };

            loadAll();
            loadAllGenres();

            $scope.reloadAll = function () {
                vm.predicate = $scope.options.selected.predicate;
                vm.reverse = $scope.options.selected.reverse;
                reset();
            };

            function loadAll() {
                Book.query({
                    page: vm.page,
                    size: vm.itemsPerPage,
                    sort: sort()
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
                    for (var i = 0; i < data.length; i++) {
                        vm.books.push(data[i]);
                    }
                }

                function onError(error) {
                    AlertService.error(error.data.message);
                }
            }

            function loadAllGenres() {
                $http.get('/api/genres').success(function (data) {
                    vm.genres = data;
                })
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
        }
    }

)();
