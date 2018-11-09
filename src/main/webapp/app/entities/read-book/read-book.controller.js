(function() {
    'use strict';

    angular
        .module('eLibraryApp')
        .controller('ReadBookController', ReadBookController);

    ReadBookController.$inject = ['ReadBook'];

    function ReadBookController(ReadBook) {

        var vm = this;

        vm.readBooks = [];

        loadAll();

        function loadAll() {
            ReadBook.query(function(result) {
                vm.readBooks = result;
                vm.searchQuery = null;
            });
        }
    }
})();
