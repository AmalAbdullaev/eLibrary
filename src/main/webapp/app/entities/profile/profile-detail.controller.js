(function () {
    'use strict';

    angular
        .module('eLibraryApp')
        .controller('ProfileDetailController', ProfileDetailController);

    ProfileDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Profile', 'User', 'Book', 'ReadBook', '$state'];

    function ProfileDetailController($scope, $rootScope, $stateParams, previousState, entity, Profile, User, Book, ReadBook, $state) {
        var vm = this;

        vm.books = [];
        vm.readBooks = [];
        vm.busy = false;
        vm.pageInMyBooks = 0;
        vm.pageInReadBooks = 0;
        vm.itemsPerPage = 20;
        vm.profile = entity;
        vm.previousState = previousState.name;
        vm.loadAllMyBooks = loadAllMyBooks;
        vm.loadAllReadBooks = loadAllReadBooks;

        if (previousState.name === 'book-detail')
            $scope.bookId = previousState.params.id;

        var unsubscribe = $rootScope.$on('eLibraryApp:profileUpdate', function (event, result) {
            vm.profile = result;
        });
        $scope.$on('$destroy', unsubscribe);

        function loadAllMyBooks() {
            if (vm.busy) return;
            vm.busy = true;
            Book.query({
                page: vm.pageInMyBooks,
                size: vm.itemsPerPage,
                'profileId.equals': vm.profile.id,
                'approved.equals': true
            }, function (data, headers) {
                vm.totalItems = headers('X-Total-Count');
                vm.maxPageInMyBooks = vm.totalItems / vm.itemsPerPage;
                for (var i = 0; i < data.length; i++)
                    vm.books.push(data[i]);
                vm.pageInMyBooks++;
                vm.busy = false;
            })
        }

        function loadAllReadBooks() {
            if (vm.busy) return;
            vm.busy = true;
            ReadBook.query({
                page: vm.pageInReadBooks,
                size: vm.itemsPerPage,
                'profileId.equals': vm.profile.id
            }, function (data, headers) {
                vm.totalItems = headers('X-Total-Count');
                vm.maxPageInReadBooks = vm.totalItems / vm.itemsPerPage;
                console.log('total books in read books');
                console.log(vm.totalItems);
                for (var i = 0; i < data.length; i++)
                    vm.readBooks.push(data[i]);
                vm.pageInReadBooks++;
                vm.busy = false;
            })
        }
    }
})();
