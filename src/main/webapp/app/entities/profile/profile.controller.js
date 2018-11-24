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
        vm.busy = false;
        vm.page = 0;
        vm.itemsPerPage = 20;
        vm.maxPage = Number.POSITIVE_INFINITY;
        vm.keyPress = keyPress;
        vm.search = search;
        vm.loadAll = loadAll;

        function keyPress(event) {
            if (event.keyCode === 13)
                search();
            else if (event.keyCode === 8 && vm.searchText.length === 0)
                search();
        }

        function search() {
            $http.get('/api/users/search?text=' + vm.searchText).success(function (profiles) {
                vm.profiles = profiles;
            });
        }

        function loadAll() {
            if (vm.busy) return;
            vm.busy = true;
            Profile.query(function (data, headers) {
                vm.totalItems = headers('X-Total-Count');
                vm.maxPage = vm.totalItems / vm.itemsPerPage;
                for (var i = 0; i < data.length; i++)
                    vm.profiles.push(data[i]);
                vm.page++;
                vm.busy = false;
            });
        }
    }
})();
