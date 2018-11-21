(function() {
    'use strict';

    angular
        .module('eLibraryApp')
        .controller('ReadBookController', ReadBookController);

    ReadBookController.$inject = ['ReadBook','Profile','Principal'];

    function ReadBookController(ReadBook,Profile,Principal) {

        var vm = this;

        vm.readBooks = [];

        loadAll();

        function loadAll() {
            Principal.identity().then(function (user) {
                Profile.getProfile({userId: user.id},onSuccess);
                    function onSuccess(result){
                        vm.profile = result;
                        ReadBook.query({'profileId.equals': vm.profile.id},function(result) {
                            vm.readBooks = result;
                            vm.searchQuery = null;
                        });
                    }                
            })
        }
    }
})();
