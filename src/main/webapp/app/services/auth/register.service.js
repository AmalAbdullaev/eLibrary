(function () {
    'use strict';

    angular
        .module('eLibraryApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
