(function () {
    'use strict';

    angular
        .module('eLibraryApp')
        .controller('BookController', BookController);

    BookController.$inject = ['$scope', 'DataUtils', 'Book', 'ParseLinks', 'AlertService', 'paginationConstants'];

    function BookController($scope, DataUtils, Book, ParseLinks, AlertService, paginationConstants) {

        var vm = this;

        vm.books = [];
        vm.loadPage = loadPage;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        vm.coverSrc = function (book) {
            var title = book.title.replace(/ /g, '%20');
            return '/content/images/' + book.profileId
                + '/' + book.id + '-cover-' + title;
        };

        loadAll();

        function loadAll() {
            Book.query({
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);

            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.books.push(data[i]);
                }
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset() {
            vm.page = 0;
            vm.books = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }
    }
})();
