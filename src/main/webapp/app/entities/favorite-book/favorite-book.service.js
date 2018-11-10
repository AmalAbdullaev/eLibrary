(function() {
    'use strict';
    angular
        .module('eLibraryApp')
        .factory('FavoriteBook', FavoriteBook);

    FavoriteBook.$inject = ['$resource'];

    function FavoriteBook ($resource) {
        var resourceUrl =  'api/favorite-books/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
