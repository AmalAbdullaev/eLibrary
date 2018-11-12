(function () {
    'use strict';

    angular
        .module('eLibraryApp')
        .controller('BookDetailController', BookDetailController);

    BookDetailController.$inject = ['$scope', '$http', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Book', 'Profile', 'Genre'];

    function BookDetailController($scope, $http, $rootScope, $stateParams, previousState, DataUtils, entity, Book, Profile, Genre) {
        var vm = this;

        vm.book = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('eLibraryApp:bookUpdate', function (event, result) {
            vm.book = result;
        });

        $scope.$on('$destroy', unsubscribe);

        $scope.download = function () {
            $http({
                method: 'GET',
                url: '/api/books/' + vm.book.id + '/download',
                responseType: 'blob'
            }).success(function (data, status, headers) {
                var contentType = headers('Content-Type');
                var disposition = headers('Content-Disposition');
                var startPos = disposition.indexOf('\"') + 1;
                var endPos = disposition.indexOf('\"', startPos);
                var filename = disposition.substring(startPos, endPos);
                var file = new Blob([data], {type: contentType});
                var objectUrl = URL.createObjectURL(file);
                var a = document.createElement('a');
                a.href = objectUrl;
                a.target = '_blank';
                a.download = filename;
                document.body.appendChild(a);
                a.click();
                document.body.removeChild(a);
            })
        };
    }
})();
