(function() {
    'use strict';

    angular
        .module('eLibraryApp')
        .controller('FavoriteBookDetailController', FavoriteBookDetailController);

    FavoriteBookDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FavoriteBook', 'Book', 'Profile'];

    function FavoriteBookDetailController($scope, $rootScope, $stateParams, previousState, entity, FavoriteBook, Book, Profile) {
        var vm = this;

        vm.favoriteBook = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('eLibraryApp:favoriteBookUpdate', function(event, result) {
            vm.favoriteBook = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
