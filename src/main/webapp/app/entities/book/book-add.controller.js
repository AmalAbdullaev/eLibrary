(function () {
    'use strict';

    angular
        .module('eLibraryApp')
        .controller('BookAddController', BookAddController);

    BookAddController.$inject = ['$timeout', '$scope', '$stateParams',  'DataUtils', 'entity', 'Book', 'Profile', 'Genre', 'Upload','Principal'];

    function BookAddController($timeout, $scope, $stateParams,DataUtils, entity, Book, Profile, Genre, Upload,Principal) {
        var vm = this;

        vm.book = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        //vm.profiles = Profile.query();
        vm.profile = Profile.getProfile({userId:951})
        vm.account = null;
        vm.genres = Genre.query();



        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
            console.log(vm.account.id);
            console.log(vm.profile);
        });


        function clear() {
          //  $uibModalInstance.dismiss('cancel');
        }


        Principal.identity().then(function(account) {
             vm.account = account;
        });



        function save() {
            vm.isSaving = true;
            if (vm.book.id !== null) {
                Book.update(vm.book, onSaveSuccess, onSaveError);
            } else {
                Book.save(vm.book, onSaveSuccess, onSaveError);
            }
        }

        // function uploadCover() {
        //     upload(vm.coverFile, vm.book.id, 'cover');
        // }

        function onSaveSuccess(result) {
            $scope.$emit('eLibraryApp:bookUpdate', result);
            vm.book.id = result.id;
            Upload.upload({
                url: '/api/books/upload',
                data: {file: vm.bookFile, id: vm.book.id, type: 'book'}
            }).then(function (resp) {
                console.log('Success ' + resp.config.data.file.name + ' uploaded');
                $scope.$emit('eLibraryApp:bookUpdate', resp);
                Upload.upload({
                    url: '/api/books/upload',
                    data: {file: vm.coverFile, id: vm.book.id, type: 'cover'}
                }).then(function (resp) {
                    console.log('Success ' + resp.config.data.file.name + ' uploaded');
                    $scope.$emit('eLibraryApp:bookUpdate', resp);
                }, function (resp) {
                    console.log('Error status: ' + resp.status);
                }, function (evt) {
                    $scope.progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                });
            }, function (resp) {
                console.log('Error status: ' + resp.status);
            }, function (evt) {
                $scope.progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
            });
            // upload(vm.bookFile, vm.book.id, 'book');
            // upload(vm.coverFile, vm.book.id, 'cover');
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
