(function () {
    'use strict';

    angular
        .module('eLibraryApp')
        .controller('ProfileController', ProfileController);

    ProfileController.$inject = ['$state', '$scope', '$http', 'Book', 'Profile'];

    function ProfileController($state, $scope, $http, Book, Profile) {

        var vm = this;

        vm.profiles = [];
        vm.books = [];
        vm.searchText = undefined;
        // vm.keyPress = keyPress;
        vm.search = search;

        loadAll();

        function search(profile) {
            if (vm.searchText === undefined)
                return true;
            return profile.userLogin.indexOf(vm.searchText) !== -1;
        }

        // function keyPress(event) {
        //     if (event.which === 13)
        //         search();
        // }

        // function search() {
        //     $http.get('/api/users?search=' + vm.searchText).success(function (profiles) {
        //         vm.profiles = profiles;
        //     });
        // }

        function loadAll() {
            Profile.query(function (result) {
                vm.profiles = result;
            });
            vm.books = Book.query();
        }
    }
})();
