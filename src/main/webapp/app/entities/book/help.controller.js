(function () {
    'use strict';

    angular
        .module('eLibraryApp')
        .controller('HelpController', HelpController);

    HelpController.$inject = ['$scope', '$http'];

    function HelpController($scope, $http) {
        var vm = this;

        vm.sendFeedback = sendFeedback;

        $scope.feedback = {
            firstName: null,
            lastName: null,
            email: null,
            message: null
        };

        function sendFeedback() {
            console.log('in sendFeedback');

            var formData = new FormData();

            formData.append('firstName', $scope.feedback.firstName);
            formData.append('lastName', $scope.feedback.lastName);
            formData.append('email', $scope.feedback.email);
            formData.append('message', $scope.feedback.message);

            console.log('Feedback:');
            console.log(formData);

            $http.post('/api/feedback', {
                data: formData,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).success(function () {
                console.log('Ваш отзыва успешно отправлен.');
            })
        }
    }
})();
