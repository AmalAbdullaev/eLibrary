(function () {
        'use strict';

        angular
            .module('eLibraryApp')
            .controller('BookController', BookController);

        BookController.$inject = ['$scope', '$http', 'DataUtils', 'Book', 'FavoriteBook', 'ParseLinks', 'AlertService', 'paginationConstants','Principal','Profile','$timeout','ReadBook'];

        function BookController($scope, $http, DataUtils, Book, FavoriteBook, ParseLinks, AlertService, paginationConstants,Principal,Profile,$timeout,ReadBook) {

            var vm = this;

            vm.books = [];
            vm.genres = [];
            vm.recommendedBooks = [];
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


            



            function load(){
                Principal.identity().then(function (user) {
                    if(user!==null){
                        Profile.getProfile({userId: user.id}, onSuccess);
                    }
                });
    
                vm.favoriteBook = FavoriteBook.query();
                vm.readBook = ReadBook.query();

    
                function onSuccess(result) {
                    vm.profile = result;
                }
            }
            load();



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

                    if(vm.profile===null){
                        return false
                    }

                    var bool = false;
                    vm.favoriteBook.forEach(function(favoriteBook){
                        if(favoriteBook.bookId === bookId && favoriteBook.profileId === vm.profile.id){
                            bool = true;
                            return;
                        }
                    });
                    return bool;
            };

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

                }
                else {
                    var favoriteBook = new FavoriteBook();
                    favoriteBook.bookId = bookId;
                    favoriteBook.profileId = vm.profile.id;
                    FavoriteBook.save(favoriteBook,success);
                    function success(){
                        load();
                    }
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
