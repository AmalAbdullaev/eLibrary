(function () {
    'use strict';

    angular
        .module('eLibraryApp')
        .controller('FavoriteBookController', FavoriteBookController);

    FavoriteBookController.$inject = ['FavoriteBook', '$state', 'Profile', 'Principal', 'currentProfile'];

    function FavoriteBookController(FavoriteBook, $state, Profile, Principal, currentProfile) {

        var vm = this;
        vm.page = 0;
        vm.busy = false;
        vm.itemsPerPage = 20;
        vm.loadAll = loadAll;
        vm.maxPage = Number.POSITIVE_INFINITY;
        vm.favoriteBooks = [];
        vm.profile = currentProfile;
        console.log(vm.profile.id);

        vm.bookDelete = function (index, bookId) {
            vm.favoriteBooks.splice(index, 1);
            FavoriteBook.delete({id: bookId});
        };

        function loadAll() {
            if (vm.busy) return;
            vm.busy = true;

            FavoriteBook.query({
                page: vm.page,
                size: vm.itemsPerPage,
                'profileId.equals': vm.profile.id
            }, function (data, headers) {
                vm.totalItems = headers('X-Total-Count');
                vm.maxPage = vm.totalItems / vm.itemsPerPage;
                for (var i = 0; i < data.length; i++)
                    vm.favoriteBooks.push(data[i]);
                vm.page++;
                vm.busy = false;
            });
        }
    }
})();
