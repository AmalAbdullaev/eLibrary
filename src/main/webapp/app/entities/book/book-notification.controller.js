(function () {
    'use strict';

    angular
        .module('eLibraryApp')
        .controller('BookNotificationController', BookNotificationController);


    BookNotificationController.$inject = ['Book', 'entity','$scope','$stateParams','$state','$timeout'];

    function BookNotificationController( Book, entity,$scope,$stateParams,$state,$timeout) {

        var vm = this;
        
        
        vm.book = entity;
        vm.allBooks = Book.query();
        vm.publish = publish;

        $scope.isAlertVisible = false;
        $scope.alert = {
            type: 'success',
            message: null
        };

        function publish(bookId){
            Book.get({id : bookId},onSuccess);
        }

    
       function onSuccess(result){
            var props = ['title','description','pages','path','coverPath','createdBy','createdDate','lastModifiedBy','lastModifiedDate' , 'yearOfPublishing', 'authorFirstName','authorLastName','id','profileId','genreId','genreName'];

            props.forEach(function(prop) {
                vm.book[prop] = result[prop];
            });

            vm.book.approved = true;
            Book.update(vm.book, onSaveSuccess, onSaveError);
            
       }

       function closeAlert () {

        $scope.alert.type = null;
        $scope.alert.message = null;
        $scope.isAlertVisible = false;
    }; 
       function onSaveSuccess(result) {
            console.log("Book updated, aprooved is true");
            showAlert('success', 'Книга ' + vm.book.title + ' успешно опубликована');
       }

       function showAlert(type, message) {
        $scope.alert.type = type;
        $scope.alert.message = message;
        $scope.isAlertVisible = true;
        $timeout(function(){
            closeAlert();
            $state.reload();
        },2000);
    }

       function onSaveError(){ 
        console.log('Error book update');
       }

    }
})();
