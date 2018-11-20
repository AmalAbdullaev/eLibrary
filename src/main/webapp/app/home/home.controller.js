(function () {
    'use strict';

    angular
        .module('eLibraryApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$http', '$scope', 'Principal', 'LoginService', '$state'];

    function HomeController($http, $scope, Principal, LoginService, $state) {
        var vm = this;

        vm.newBooks = [];
        vm.recommendedBooks = [];
        vm.topProfiles = [];
        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function () {
            getAccount();
        });

        loadAll();

        function loadAll() {
            $http.get('/api/favorite-books/top').success(function (data) {
                vm.recommendedBooks = data;
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
