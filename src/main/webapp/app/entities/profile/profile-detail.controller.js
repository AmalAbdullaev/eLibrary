(function() {
    'use strict';

    angular
        .module('eLibraryApp')
        .controller('ProfileDetailController', ProfileDetailController);

    ProfileDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Profile', 'User','Book','ReadBook'];

    function ProfileDetailController($scope, $rootScope, $stateParams, previousState, entity, Profile, User,Book,ReadBook) {
        var vm = this;

        vm.profile = entity;
        vm.previousState = previousState.name;

        Book.query({'profileId.equals':vm.profile.id},onSuccess);

        function onSuccess(result){
            vm.books = result;
        }

        ReadBook.getReadBookByProfileId({profileId:vm.profile.id},onSuccessForReadBooks);

        function onSuccessForReadBooks(result){
            vm.readBooks = result;
            console.log(vm.readBooks);
        }

        var unsubscribe = $rootScope.$on('eLibraryApp:profileUpdate', function(event, result) {
            vm.profile = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
