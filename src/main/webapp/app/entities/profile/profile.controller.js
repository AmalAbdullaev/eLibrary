(function() {
    'use strict';

    angular
        .module('eLibraryApp')
        .controller('ProfileController', ProfileController);

    ProfileController.$inject = ['Profile','Book'];

    function ProfileController(Profile,Book) {

        var vm = this;

        vm.profiles = [];
        vm.books = [];


    

        loadAll();

        
        function loadAll() {
            Profile.query(function(result) {
                vm.profiles = result;
                console.log(vm.profiles);
                vm.searchQuery = null;
            });
            vm.books = Book.query();


        }
    }
})();
