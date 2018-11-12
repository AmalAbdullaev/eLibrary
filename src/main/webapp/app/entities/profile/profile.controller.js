(function() {
    'use strict';

    angular
        .module('eLibraryApp')
        .controller('ProfileController', ProfileController);

    ProfileController.$inject = ['Profile'];

    function ProfileController(Profile) {

        var vm = this;

        vm.profiles = [];

        loadAll();

        function loadAll() {
            Profile.query(function(result) {
                vm.profiles = result;
                vm.searchQuery = null;
            });
        }
    }
})();
