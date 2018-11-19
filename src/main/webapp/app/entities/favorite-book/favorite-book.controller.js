(function() {
    'use strict';

    angular
        .module('eLibraryApp')
        .controller('FavoriteBookController', FavoriteBookController);

    FavoriteBookController.$inject = ['FavoriteBook','$state'];

    function FavoriteBookController(FavoriteBook,$state) {

        var vm = this;

        vm.favoriteBooks = [];

        loadAll();

        vm.bookDelete = function(bookId){
            FavoriteBook.delete({id:bookId},success);
            function success(){
                $state.reload();
            }
            
        }

        function loadAll() {
            FavoriteBook.query(function(result) {
                vm.favoriteBooks = result;
                vm.searchQuery = null;
            });
        }
    }
})();
