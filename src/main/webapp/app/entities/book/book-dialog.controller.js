(function () {
    'use strict';

    angular
        .module('eLibraryApp')
        .controller('BookDialogController', BookDialogController);

    BookDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Book', 'Profile', 'Genre', 'Upload'];

    function BookDialogController($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Book, Profile, Genre, Upload) {
        var vm = this;

        vm.book = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.profiles = Profile.query();
        vm.genres = Genre.query();

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function upload(file, id, type) {
            Upload.upload({
                url: '/api/books/upload',
                data: {file: file, id: id, type: type}
            }).then(function (resp) {
                console.log('Success ' + resp.config.data.file.name + ' uploaded');
                $scope.$emit('eLibraryApp:bookUpdate', result);
                uploadCover();
            }, function (resp) {
                console.log('Error status: ' + resp.status);
            }, function (evt) {
                $scope.progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
            });
        }

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function save() {
            vm.isSaving = true;
            if (vm.book.id !== null) {
                Book.update(vm.book, onSaveSuccess, onSaveError);
            } else {
                Book.save(vm.book, onSaveSuccess, onSaveError);
            }
        }

        function uploadCover() {
            upload(vm.coverFile, vm.book.id, 'cover');
            $scope.$emit('eLibraryApp:bookUpdate', result);
        }

        function onSaveSuccess(result) {
            $scope.$emit('eLibraryApp:bookUpdate', result);
            vm.book.id = result.id;
            upload(vm.bookFile, vm.book.id, 'book');
            upload(vm.coverFile, vm.book.id, 'cover');
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.lastModifiedDate = false;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
