(function() {
    'use strict';

    angular
        .module('eLibraryApp')
        .controller('BookDetailController', BookDetailController);

    BookDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Book', 'Profile', 'Genre'];

    function BookDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Book, Profile, Genre) {
        var vm = this;

        vm.book = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('eLibraryApp:bookUpdate', function(event, result) {
            vm.book = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
