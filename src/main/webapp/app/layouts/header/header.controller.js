(function () {
    'use strict';

    angular
        .module('eLibraryApp')
        .controller('HeaderController', HeaderController);

    HeaderController.$inject = ['$scope', '$http', '$state', 'Auth', 'Principal', 'ProfileService', 'LoginService','Profile','$stateParams'];

    function HeaderController($scope, $http, $state, Auth, Principal, ProfileService, LoginService,Profile,$stateParams) {
        var vm = this;

        vm.isNavbarCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;



        ProfileService.getProfileInfo().then(function (response) {
            vm.inProduction = response.inProduction;
            vm.swaggerEnabled = response.swaggerEnabled;
        });

        $scope.searchBarOpen = false;

        vm.login = login;
        vm.logout = logout;
        vm.toggleNavbar = toggleNavbar;
        vm.collapseNavbar = collapseNavbar;
        vm.$state = $state;
        vm.foundBooks = [];
        vm.getAccount = getAccount;

        function getAccount(){
            Principal.identity().then(function (user) {
                if(user===null){
                    return null;
                }
                Profile.getProfile({userId: user.id}, onSuccess);
            });
            function onSuccess(result){
                vm.profile = result;
            }
        }
         getAccount();

        function search() {
            vm.foundBooks = [];


            $http({
                method: 'GET',
                url: '/api/books?search=' + $scope.searchText
            }).success(function (data) {
                var toArray = data;
                vm.foundBooks = toArray.slice(0,10);
                $scope.searchBarOpen = true;
            });
        }

        
        $scope.keyPressed = function (event) {
            if (event.which === 13) {
                $scope.searchBarOpen = true;
                search();
            }
        };

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
