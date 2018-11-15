(function() {
    'use strict';
    angular
        .module('eLibraryApp')
        .factory('Profile', Profile);

    Profile.$inject = ['$resource'];

    function Profile ($resource) {
        var resourceUrl =  'api/profiles/:id/:userId';


        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'getProfile':{method:'GET',params: {id:'user',userId:'userId'}},
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
