(function() {
    'use strict';

    angular
        .module('eLibraryApp')
        .controller('ReadBookDetailController', ReadBookDetailController);

    ReadBookDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ReadBook', 'Book', 'Profile'];

    function ReadBookDetailController($scope, $rootScope, $stateParams, previousState, entity, ReadBook, Book, Profile) {
        var vm = this;

        vm.readBook = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('eLibraryApp:readBookUpdate', function(event, result) {
            vm.readBook = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
