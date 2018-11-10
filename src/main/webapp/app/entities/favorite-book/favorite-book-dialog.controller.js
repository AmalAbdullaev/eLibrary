(function() {
    'use strict';

    angular
        .module('eLibraryApp')
        .controller('FavoriteBookDialogController', FavoriteBookDialogController);

    FavoriteBookDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FavoriteBook', 'Book', 'Profile'];

    function FavoriteBookDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FavoriteBook, Book, Profile) {
        var vm = this;

        vm.favoriteBook = entity;
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
            if (vm.favoriteBook.id !== null) {
                FavoriteBook.update(vm.favoriteBook, onSaveSuccess, onSaveError);
            } else {
                FavoriteBook.save(vm.favoriteBook, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('eLibraryApp:favoriteBookUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
