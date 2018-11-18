(function () {
    'use strict';

    angular
        .module('eLibraryApp')
        .controller('BookNotificationController', BookNotificationController);


    BookNotificationController.$inject = ['$http', 'Book', 'entity', '$scope', '$stateParams', '$state', '$timeout'];

    function BookNotificationController($http, Book, entity, $scope, $stateParams, $state, $timeout) {

        var vm = this;

        vm.books = [];
        $scope.isAlertVisible = false;
        $scope.alert = {
            type: 'success',
            message: null
        };

        loadAll();

        function loadAll() {
            $http.get('/api/books?approved.equals=false').success(function (response) {
                vm.books = response;
            })
        }

        $scope.publish = function (book, index) {
            book.approved = true;
            Book.update(book).$promise.then(function (response) {
                vm.books.splice(index, 1);
                showAlert('success', 'Книга ' + book.title + ' успешно опубликована');
            });
        };

        $scope.closeAlert = function () {
            $scope.alert.type = null;
            $scope.alert.message = null;
            $scope.isAlertVisible = false;
        };

        function showAlert(type, message) {
            $scope.alert.type = type;
            $scope.alert.message = message;
            $scope.isAlertVisible = true;
        }
    }
})();
