(function() {
    'use strict';

    angular
        .module('eLibraryApp')
        .controller('FavoriteBookController', FavoriteBookController);

    FavoriteBookController.$inject = ['FavoriteBook','$state','Profile','Principal'];

    function FavoriteBookController(FavoriteBook,$state,Profile,Principal) {

        var vm = this;

        vm.favoriteBooks = [];

        loadAll();

        vm.bookDelete = function(bookId){
            FavoriteBook.delete({id:bookId},success);
            function success(){
                $state.reload();
            }
            
        }

        function loadAll() {

            Principal.identity().then(function (user) {
                Profile.getProfile({userId: user.id},onSuccess);
                    function onSuccess(result){
                        vm.profile = result;
                        FavoriteBook.query({'profileId.equals': vm.profile.id},function(result) {
                            vm.favoriteBooks = result;
                            vm.searchQuery = null;
                        });
                    }                
            })
        }
    }
})();
