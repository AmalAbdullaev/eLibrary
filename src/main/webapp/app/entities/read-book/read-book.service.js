(function() {
    'use strict';
    angular
        .module('eLibraryApp')
        .factory('ReadBook', ReadBook);

    ReadBook.$inject = ['$resource'];

    function ReadBook ($resource) {
        var resourceUrl =  'api/read-books/:id';

        return $resource(resourceUrl, {}, {
            'getReadBookByProfileId':{method:'GET',isArray: true,params: {profileId:'profileId'}},
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
