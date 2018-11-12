(function() {
    'use strict';

    angular
        .module('eLibraryApp')
        .controller('FavoriteBookController', FavoriteBookController);

    FavoriteBookController.$inject = ['FavoriteBook'];

    function FavoriteBookController(FavoriteBook) {

        var vm = this;

        vm.favoriteBooks = [];

        loadAll();

        function loadAll() {
            FavoriteBook.query(function(result) {
                vm.favoriteBooks = result;
                vm.searchQuery = null;
            });
        }
    }
})();
