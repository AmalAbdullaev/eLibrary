(function () {
        'use strict';

        angular
            .module('eLibraryApp')
            .controller('BookController', BookController);

<<<<<<< HEAD
        BookController.$inject = ['$scope', '$http', 'DataUtils', 'Book', 'FavoriteBook', 'ParseLinks', 'AlertService', 'paginationConstants','Principal','Profile','$timeout','ReadBook'];

        function BookController($scope, $http, DataUtils, Book, FavoriteBook, ParseLinks, AlertService, paginationConstants,Principal,Profile,$timeout,ReadBook) {
=======
        BookController.$inject = ['$scope', '$http', 'DataUtils', 'Book', 'ReadBook', 'FavoriteBook', 'ParseLinks', 'AlertService', 'paginationConstants', 'Principal', 'Profile', '$timeout'];

        function BookController($scope, $http, DataUtils, Book, ReadBook, FavoriteBook, ParseLinks, AlertService, paginationConstants, Principal, Profile, $timeout) {
>>>>>>> 7ace50eaaf9ae335a28564601b0db277a2e9f53f

            var vm = this;

            vm.books = [];
            vm.genres = [];
            vm.recommendedBooks = [];
            vm.currentGenre = null;
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

            vm.profile = null;
            $scope.genres = [];

            $scope.isRead = function (id) {
                ReadBook.query({
                    'bookId.equals': id,
                    'profileId.equals': vm.profile.id
                });
            };

<<<<<<< HEAD
            



            function load(){
=======
            function load() {
>>>>>>> 7ace50eaaf9ae335a28564601b0db277a2e9f53f
                Principal.identity().then(function (user) {
                    if (user !== null) {
                        Profile.getProfile({userId: user.id}, onSuccess);
                    }
                });
<<<<<<< HEAD
    
                vm.favoriteBook = FavoriteBook.query();
                vm.readBook = ReadBook.query();

    
=======

>>>>>>> 7ace50eaaf9ae335a28564601b0db277a2e9f53f
                function onSuccess(result) {
                    vm.profile = result;
                    vm.favoriteBook = FavoriteBook.query({'profileId.equals': vm.profile.id});
                }
            }

            load();


<<<<<<< HEAD

            $scope.isRead = function(bookId){

                if(vm.profile===null){
                    return false
                }

                var bool = false;
                vm.readBook.forEach(function(readBook){
                    if(readBook.bookId === bookId && readBook.profileId === vm.profile.id){
                        bool = true;
                        return;
                    }
                });
                return bool;
            }

            $scope.isFavorite = function(bookId){
=======
            $scope.favorite = function (bookId) {
>>>>>>> 7ace50eaaf9ae335a28564601b0db277a2e9f53f

                if (vm.profile === null) {
                    return false
                }

                var bool = false;
                vm.favoriteBook.forEach(function (favoriteBook) {
                    if (favoriteBook.bookId === bookId)
                        bool = true;
                });
                return bool;
            };

<<<<<<< HEAD
            $scope.getFavorite = function(bookId){
                if($scope.isFavorite(bookId)){
                    FavoriteBook.query({'profileId.equals': vm.profile.id,'bookId.equals':bookId},onSuccess);
                        function onSuccess(result){
                            var id = result[0].id; 
                            FavoriteBook.delete({'id': id},success);
                            function success(){
                                load();
                            }
                        }

=======
            $scope.getFavorite = function (bookId) {
                if ($scope.favorite(bookId)) {
                    FavoriteBook.query({'profileId.equals': vm.profile.id, 'bookId.equals': bookId}, onSuccess);
>>>>>>> 7ace50eaaf9ae335a28564601b0db277a2e9f53f
                }
                else {
                    var favoriteBook = new FavoriteBook();
                    favoriteBook.bookId = bookId;
                    favoriteBook.profileId = vm.profile.id;
                    FavoriteBook.save(favoriteBook, success);
                }

                function onSuccess(result) {
                    var id = result[0].id;
                    FavoriteBook.delete({'id': id}, success);

                    function success() {
                        load();
                    }
                }

                function success() {
                    load();
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
                selected: {name: "По возрастанию", predicate: "title", reverse: false}
            };

            loadAll();

            $scope.reloadAll = function () {
                vm.predicate = $scope.options.selected.predicate;
                vm.reverse = $scope.options.selected.reverse;
                reset();
            };

            function loadAll() {
                if (vm.currentGenre != null)
                    Book.query({
                        page: vm.page,
                        size: vm.itemsPerPage,
                        sort: sort(),
                        'genreId.equals': vm.currentGenre
                    }, onSuccess, onError);
                else
                    Book.query({
                        page: vm.page,
                        size: vm.itemsPerPage,
                        sort: sort()
                    }, onSuccess, onError);

                $http.get('/api/genres').success(function (data) {
                    vm.genres = data;
                });
                $http.get('/api/favorite-books/top').success(function (data) {
                    vm.recommendedBooks = data;
                });

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
