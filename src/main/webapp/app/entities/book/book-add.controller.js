(function () {
    'use strict';

    angular
        .module('eLibraryApp')
        .controller('BookAddController', BookAddController);

    BookAddController.$inject = ['$sce', '$timeout', '$scope', '$stateParams', 'DataUtils', 'ParseLinks', 'entity', 'Book', 'Profile', 'Genre', 'Upload', 'Principal', '$state'];

    function BookAddController($sce, $timeout, $scope, $stateParams, DataUtils, ParseLinks, entity, Book, Profile, Genre, Upload, Principal, $state) {

        var vm = this;

        vm.page = 0;
        vm.busy = false;
        vm.book = entity;
        vm.profile = null;
        vm.itemsPerPage = 20;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.maxPage = Number.POSITIVE_INFINITY;
        vm.save = save;
        vm.reset = reset;
        vm.loadAll = loadAll;
        vm.predicate = 'id';
        vm.genres = Genre.query();


        vm.books = [];
        vm.unconfirmedBooks = [];

        $scope.isCoverUploading = false;
        $scope.isBookUploading = false;
        $scope.isAlertVisible = false;
        $scope.alert = {
            type: 'success',
            message: null
        };

        $scope.options = {
            data: [
                {
                    name: "По возрастанию",
                    predicate: "title",
                    reverse: true
                },
                {
                    name: "По убыванию",
                    predicate: "title",
                    reverse: false
                },
                {
                    name: "Сначала новые",
                    predicate: "lastModifiedDate",
                    reverse: false
                },
                {
                    name: "Сначала старые",
                    predicate: "lastModifiedDate",
                    reverse: true
                }
            ],
            selected: {name: "По возрастанию", predicate: "title", reverse: true}
        };

        $scope.reloadAll = function () {
            vm.predicate = $scope.options.selected.predicate;
            vm.reverse = $scope.options.selected.reverse;
            reset();
        };


        function loadAll() {

            Principal.identity().then(function (user) {
                Profile.getProfile({userId: user.id}, onSuccess);
            });
    
            function onSuccess(result) {
                vm.profile = result;



                if (vm.busy) return;
                vm.busy = true;

                if (vm.page === 0) {
                    vm.predicate = $scope.options.selected.predicate;
                    vm.reverse = $scope.options.selected.reverse;
                }

                Book.query({
                    page: vm.page,
                    size: vm.itemsPerPage,
                    sort: sort(),
                    'profileId.equals': vm.profile.id,
                    'approved.equals': true
                }, onSuccess, onError);

                function sort() {
                    var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                    if (vm.predicate !== 'id') {
                        result.push('id');
                    }
                    return result;
                }

                function onSuccess(data, headers) {
                    vm.links = ParseLinks.parse(headers('link'));
                    vm.totalItems = headers('X-Total-Count');
                    vm.maxPage = vm.totalItems / vm.itemsPerPage;
                    console.log(vm.maxPage);
                    for (var i = 0; i < data.length; i++) {
                        vm.books.push(data[i]);
                    }
                    vm.page++;
                    vm.busy = false;
                }

                function onError(error) {
                    AlertService.error(error.data.message);
                }
            }
        }

        function reset() {
            vm.page = 0;
            vm.books = [];
            loadAll();
        }

        function closeAlert() {
            $scope.alert.type = null;
            $scope.alert.message = null;
            $scope.isAlertVisible = false;
        }

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function showAlert(type, message) {


            $scope.alert.type = type;
            $scope.alert.message = message;
            $scope.isAlertVisible = true;
            $timeout(function () {
                $state.reload();
            },500);
        }


        Principal.identity().then(function (user) {
            Profile.getProfile({userId: user.id}, onSuccess);
        });

        function onSuccess(result) {
            vm.profile = result;
            vm.book.profileId = result.id;
            disableUploadIfBanned(vm.profile);
            if (vm.profile.id === 3) {
                Book.query({'approved.equals': false}, function (data) {
                    vm.unconfirmedBooks = data;
                });
            }
            else {
                Book.query({'approved.equals': false, 'profileId.equals': vm.profile.id}, function (data) {
                    vm.unconfirmedBooks = data;
                });
            }
        }

        function disableUploadIfBanned(profile) {
            if (profile.banned) {
                $scope.bannedPopover = $sce.trustAsHtml('Вы были <span style=\"background: #e4002b; ' +
                    'padding: 0 5px; border-radius: 5px\">заблокированы</span> администратором,' +
                    'поэтому функция публикации книги для вас <span style="background: #e4002b; ' +
                    'padding: 0 5px; border-radius: 5px\">недоступна</span>')
            }
        }

        function save() {
            vm.isSaving = true;
            if (vm.book.id !== null) {
                Book.update(vm.book, onSaveSuccess, onSaveError);
            } else {
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
