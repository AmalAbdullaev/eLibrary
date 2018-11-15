(function () {
    'use strict';

    angular
        .module('eLibraryApp')
        .controller('HeaderController', HeaderController);

    HeaderController.$inject = ['$scope', '$http', '$state', 'Auth', 'Principal', 'ProfileService', 'LoginService'];

    function HeaderController($scope, $http, $state, Auth, Principal, ProfileService, LoginService) {
        var vm = this;

        vm.isNavbarCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;

        ProfileService.getProfileInfo().then(function (response) {
            vm.inProduction = response.inProduction;
            vm.swaggerEnabled = response.swaggerEnabled;
        });

        vm.login = login;
        vm.logout = logout;
        vm.toggleNavbar = toggleNavbar;
        vm.collapseNavbar = collapseNavbar;
        vm.$state = $state;
        vm.foundBooks = [];

        function search() {
            vm.foundBooks = [];
            $http({
                method: 'GET',
                url: '/api/books?search=' + $scope.searchText
            }).success(function (data) {
                vm.foundBooks = data;
            });
        }

        $scope.toggled = function () {
            search();
        };

        function login() {
            collapseNavbar();
            LoginService.open();
        }

        function logout() {
            collapseNavbar();
            Auth.logout();
            $state.go('home');
        }

        function toggleNavbar() {
            vm.isNavbarCollapsed = !vm.isNavbarCollapsed;
        }

        function collapseNavbar() {
            vm.isNavbarCollapsed = true;
        }
    }
})
();
