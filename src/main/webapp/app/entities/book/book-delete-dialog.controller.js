(function() {
    'use strict';

    angular
        .module('eLibraryApp')
        .controller('BookDeleteController',BookDeleteController);

    BookDeleteController.$inject = ['$uibModalInstance', 'entity', 'Book'];

    function BookDeleteController($uibModalInstance, entity, Book) {
        var vm = this;

        vm.book = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id, reason) {
            Book.delete({id: id, reason: reason},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
