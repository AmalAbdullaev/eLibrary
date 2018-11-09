(function() {
    'use strict';

    angular
        .module('eLibraryApp')
        .controller('ReadBookDialogController', ReadBookDialogController);

    ReadBookDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ReadBook', 'Book', 'Profile'];

    function ReadBookDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ReadBook, Book, Profile) {
        var vm = this;

        vm.readBook = entity;
        vm.clear = clear;
        vm.save = save;
        vm.books = Book.query();
        vm.profiles = Profile.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.readBook.id !== null) {
                ReadBook.update(vm.readBook, onSaveSuccess, onSaveError);
            } else {
                ReadBook.save(vm.readBook, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('eLibraryApp:readBookUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
