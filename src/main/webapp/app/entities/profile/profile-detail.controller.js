(function() {
    'use strict';

    angular
        .module('eLibraryApp')
        .controller('ProfileDetailController', ProfileDetailController);

    ProfileDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Profile', 'User','Book','ReadBook','$state'];

    function ProfileDetailController($scope, $rootScope, $stateParams, previousState, entity, Profile, User,Book,ReadBook,$state) {
        var vm = this;

        vm.profile = entity;
        vm.previousState = previousState.name;
        if (previousState.name === 'book-detail')
            $scope.bookId = previousState.params.id;

        Book.query({'profileId.equals':vm.profile.id,'approved.equals':true},onSuccess);

        function onSuccess(result){
            vm.books = result;
        }

        ReadBook.query({'profileId.equals':vm.profile.id},onSuccessForReadBooks);

        function onSuccessForReadBooks(result){
            vm.readBooks = result;
        }

        var unsubscribe = $rootScope.$on('eLibraryApp:profileUpdate', function(event, result) {
            vm.profile = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
