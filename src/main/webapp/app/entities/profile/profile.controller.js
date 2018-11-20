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
        vm.keyPress = keyPress;
        vm.search = search;

        loadAll();

        function keyPress(event) {
            if (event.keyCode === 13)
                search();
            else if(event.keyCode === 8 && vm.searchText.length === 0)
                    search();
        }

        function search() {
            $http.get('/api/users/search?text=' + vm.searchText).success(function (profiles) {
                vm.profiles = profiles;
            });
        }

        function loadAll() {
            Profile.query(function (result) {
                vm.profiles = result;
            });
            vm.books = Book.query();
        }
    }
})();
