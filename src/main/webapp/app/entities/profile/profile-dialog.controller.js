(function() {
    'use strict';

    angular
        .module('eLibraryApp')
        .controller('ProfileDialogController', ProfileDialogController);

    ProfileDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Profile', 'User'];

    function ProfileDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Profile, User) {
        var vm = this;

        vm.profile = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();


        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        $scope.isBanned = function(){
            vm.profile = entity;
            return vm.profile.banned;
        };

        $scope.isTrusted = function(){
            vm.profile = entity;
            return vm.profile.trusted;
        };

        vm.banned = function(){
            vm.profile.banned = !vm.profile.banned;
        };

        vm.trusted = function(){
            vm.profile.trusted = !vm.profile.trusted;
        };

        function save () {
            vm.isSaving = true;
            if (vm.profile.id !== null) {
                Profile.update(vm.profile, onSaveSuccess, onSaveError);
            } else {
                Profile.save(vm.profile, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('eLibraryApp:profileUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
    }
})();
