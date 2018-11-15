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
        vm.books = null;
        vm.booksRecieved = false;
        vm.foundBooks = [];

        function getAllBooks() {
            vm.foundBooks = [];
            $http({
                method: 'GET',
                url: '/api/books?approved.equals=true'
            }).success(function (data) {
                filter(data);
            });
            console.log(vm.foundBooks);
        }

        function search() {
            getAllBooks();
        }

        function filter(data) {
            vm.books = data;
            vm.booksRecieved = true;
            vm.books.forEach(function (book) {
                var title = book.title.toLowerCase();
                var authorFirstName = book.authorFirstName.toLowerCase();
                var authorLastName = book.authorLastName.toLowerCase();
                var s = $scope.searchText.toLowerCase();
                if (title.indexOf(s) !== -1
                    || authorFirstName.indexOf(s) !== -1
                    || authorLastName.indexOf(s) !== -1)
                    vm.foundBooks.push(book);
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
