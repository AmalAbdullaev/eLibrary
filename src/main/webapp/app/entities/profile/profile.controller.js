(function () {
    'use strict';

    angular
        .module('eLibraryApp')
        .controller('ProfileController', ProfileController);

    ProfileController.$inject = ['$state', '$scope', '$resource', '$http', 'Book', 'Profile'];

    function ProfileController($state, $scope, $resource, $http, Book, Profile) {

        var vm = this;

        vm.profiles = [];
        vm.books = [];
        vm.searchText = undefined;
        vm.busy = false;
        vm.page = 0;
        vm.pageInSearch = 0;
        vm.itemsPerPage = 20;
        vm.maxPage = Number.POSITIVE_INFINITY;
        vm.maxPageInSearch = Number.POSITIVE_INFINITY;
        vm.loadAll = loadAll;
        vm.onChange = onChange;

        Book.query({'approved.equals': true}, function (books, headers) {
            vm.bookCount = headers('X-Total-Count');
        });

        function onChange() {
            vm.profiles = [];
            vm.pageInSearch = 0;
            vm.page = 0;
            loadAll();
        }

        function search() {
            $resource('/api/users/search', {}).query({
                text: vm.searchText,
                page: vm.pageInSearch,
                size: vm.itemsPerPage
            }, function (profiles, headers) {
                vm.totalItemsInSearch = headers('X-Total-Count');
                vm.maxPageInSearch = vm.totalItemsInSearch / vm.itemsPerPage;
                for (var i = 0; i < profiles.length; i++)
                    vm.profiles.push(profiles[i]);
                vm.pageInSearch++;
                vm.busy = false;
            });
        }

        function loadAll() {
            if (vm.busy) return;
            vm.busy = true;
            if (vm.searchText) {
                search();
            }
            else {
                Profile.query({
                        page: vm.page,
                        size: vm.itemsPerPage
                    },
                    function (data, headers) {
                        vm.maxPageInSearch = Number.POSITIVE_INFINITY;
                        vm.totalItems = headers('X-Total-Count');
                        vm.maxPage = vm.totalItems / vm.itemsPerPage;
                        for (var i = 0; i < data.length; i++)
                            vm.profiles.push(data[i]);
                        vm.page++;
                        vm.busy = false;
                    });
            }
        }
    }
})();
