(function() {
    'use strict';

    angular
        .module('eLibraryApp')
        .controller('ReadBookDeleteController',ReadBookDeleteController);

    ReadBookDeleteController.$inject = ['$uibModalInstance', 'entity', 'ReadBook'];

    function ReadBookDeleteController($uibModalInstance, entity, ReadBook) {
        var vm = this;

        vm.readBook = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ReadBook.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
