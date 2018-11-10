(function() {
    'use strict';

    angular
        .module('eLibraryApp')
        .controller('FavoriteBookDeleteController',FavoriteBookDeleteController);

    FavoriteBookDeleteController.$inject = ['$uibModalInstance', 'entity', 'FavoriteBook'];

    function FavoriteBookDeleteController($uibModalInstance, entity, FavoriteBook) {
        var vm = this;

        vm.favoriteBook = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FavoriteBook.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
