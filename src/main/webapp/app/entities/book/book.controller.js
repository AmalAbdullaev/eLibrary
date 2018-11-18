(function () {
        'use strict';

        angular
            .module('eLibraryApp')
            .controller('BookController', BookController);

        BookController.$inject = ['$scope', '$http', 'DataUtils', 'Book', 'ParseLinks', 'AlertService', 'paginationConstants'];

        function BookController($scope, $http, DataUtils, Book, ParseLinks, AlertService, paginationConstants) {

            var vm = this;

            vm.books = [];
            vm.genres = [];
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

            $scope.genres = [];
            $scope.options = {
                data: [
                    {
                        name: "По возрастанию",
                        predicate: "title",
                        reverse: true
                    },
                    {
                        name: "По убыванию",
                        predicate: "title",
                        reverse: false
                    },
                    {
                        name: "Сначала новые",
                        predicate: "lastModifiedDate",
                        reverse: false
                    },
                    {
                        name: "Сначала старые",
                        predicate: "lastModifiedDate",
                        reverse: true
                    }
                ],
                selected: {name: "По возрастанию", predicate: "title", reverse: false}
            };

            loadAll();
            loadAllGenres();

            $scope.reloadAll = function () {
                vm.books = [];
                vm.predicate = $scope.options.selected.predicate;
                vm.reverse = $scope.options.selected.reverse;
                console.log(vm.predicate);
                console.log(vm.reverse);
                loadAll();
            };

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
                    console.log(result);
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

            function loadAllGenres() {
                $http.get('/api/genres').success(function (data) {
                    vm.genres = data;
                })
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
    }

)();
