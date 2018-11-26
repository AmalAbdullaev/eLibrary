(function () {
    'use strict';

    angular
        .module('eLibraryApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$http', '$scope', 'Principal', 'LoginService', '$state','Profile','ReadBook', 'FavoriteBook','$timeout'];

    function HomeController($http, $scope, Principal, LoginService, $state,Profile,ReadBook, FavoriteBook,$timeout) {
        var vm = this;

        vm.newBooks = [];
        vm.popularBooks = [];
        vm.topProfiles = [];
        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function () {
            $state.reload();
            getAccount();
        });

        
        vm.favoriteBook = null;
        vm.readBook = null;
        vm.user = null;


        $scope.isRead = function(bookId){

            if(vm.readBook===null ){
                return false
            }

            var bool = false;
            vm.readBook.forEach(function(readBook){
                if(readBook.bookId === bookId){
                    bool = true;
                    return;
                }
            });
            return bool;
        }


        
        function load(){
            
            Principal.identity().then(function (user) {

                if(user===null){
                    console.log('user is  unauthorized');
                    return;
                }
                Profile.getProfile({userId: user.id},onSuccess);
                
                function onSuccess(result){
                    vm.profile = result;
                    
                    FavoriteBook.query({'profileId.equals': vm.profile.id},onSuccess);
                    function onSuccess(result){
                        vm.favoriteBook = result;
                    }

                                
                    ReadBook.query({'profileId.equals': vm.profile.id},onReadSuccess);
                    function onReadSuccess(result){
                        vm.readBook = result;
                    }

                }                
            })
        };

        $scope.isFavorite = function(bookId){

            if(vm.favoriteBook===null){
                return false;
            }
            var bool = false;
            vm.favoriteBook.forEach(function (favoriteBook) {
                if (favoriteBook.bookId === bookId)
                    bool = true;
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
                FavoriteBook.save(favoriteBook, success);

                function success() {
                    load();
                }
            }
        };


        loadAll();

        function loadAll() {
            $http.get('/api/favorite-books/top').success(function (data) {
                vm.popularBooks = data;
            });
            var oneWeekAgo = new Date();
            oneWeekAgo.setDate(oneWeekAgo.getDate() - 7);
            $http.get('/api/books?approved.equals=true&createDate.greaterThen=' + oneWeekAgo.toISOString())
                .success(function (data) {
                    vm.newBooks = data;
                });
            $http.get('/api/profiles/top').success(function (data) {
                vm.topProfiles = data;
            })

            load();
        }

        getAccount();

        function getAccount() {
            Principal.identity().then(function (account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }

        function register() {
            $state.go('register');
        }
    }
})();
