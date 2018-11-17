(function () {
    'use strict';

    angular
        .module('eLibraryApp')
        .controller('BookAddController', BookAddController);

    BookAddController.$inject = ['$timeout', '$scope', '$stateParams', 'DataUtils', 'entity', 'Book', 'Profile', 'Genre', 'Upload', 'Principal'];

    function BookAddController($timeout, $scope, $stateParams, DataUtils, entity, Book, Profile, Genre, Upload, Principal) {
        var vm = this;

        vm.book = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.account = null;
        vm.genres = Genre.query();

        $scope.isCoverUploading = false;
        $scope.isBookUploading = false;
        $scope.isAlertVisible = false;
        $scope.alert = {
            type: 'success',
            message: null
        };

        $scope.closeAlert = function () {
            $scope.alert.type = null;
            $scope.alert.message = null;
            $scope.isAlertVisible = false;
        };

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();

        });

        function showAlert(type, message) {
            $scope.alert.type = type;
            $scope.alert.message = message;
            $scope.isAlertVisible = true;
        }

        function clear() {
            //  $uibModalInstance.dismiss('cancel');
        }

        Principal.identity().then(function (user) {
            Profile.getProfile({userId: user.id}, onSuccess);
        });

        function onSuccess(result) {
            vm.profileId = result.id;
            vm.book.profileId = result.id;
            console.log(vm.book.profileId);
        }

        function save() {
            vm.isSaving = true;
            if (vm.book.id !== null) {
                console.log('before Book.update');
                console.log('book id: ' + vm.book.id);
                console.log('book profile id: ' + vm.book.profileId);
                console.log(vm.book);
                Book.update(vm.book, onSaveSuccess, onSaveError);
            } else {
                console.log('before Book.save');
                console.log('book id: ' + vm.book.id);
                console.log('book profile id: ' + vm.book.profileId);
                console.log(vm.book);
                Book.save(vm.book, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('eLibraryApp:bookUpdate', result);
            vm.book.id = result.id;
            Upload.upload({
                url: '/api/books/upload',
                data: {file: vm.bookFile, id: vm.book.id, type: 'book'}
            }).then(function (resp) {
                $scope.isBookUploading = false;
                console.log('Success ' + resp.config.data.file.name + ' uploaded');
                $scope.$emit('eLibraryApp:bookUpdate', resp);
                Upload.upload({
                    url: '/api/books/upload',
                    data: {file: vm.coverFile, id: vm.book.id, type: 'cover'}
                }).then(function (resp) {
                    $scope.isCoverUploading = false;
                    console.log('Success ' + resp.config.data.file.name + ' uploaded');
                    $scope.$emit('eLibraryApp:bookUpdate', resp);
                    showAlert('success', 'Книга ' + vm.book.title + ' успешно загружена');
                }, function (resp) {
                    showAlert('success', 'Не удалось загрузить книгу '
                        + vm.book.title + '. Статус: ' + resp.status);
                    console.log('Error status: ' + resp.status);
                }, function (evt) {
                    $scope.isCoverUploading = true;
                    $scope.coverProgressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                });
            }, function (resp) {
                showAlert('success', 'Не удалось загрузить книгу '
                    + vm.book.title + '. Статус: ' + resp.status);
                console.log('Error status: ' + resp.status);
            }, function (evt) {
                $scope.isBookUploading = true;
                $scope.bookProgressPercentage = parseInt(100.0 * evt.loaded / evt.total);
            });
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
