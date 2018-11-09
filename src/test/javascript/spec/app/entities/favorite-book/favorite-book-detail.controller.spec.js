'use strict';

describe('Controller Tests', function() {

    describe('FavoriteBook Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockFavoriteBook, MockBook, MockProfile;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockFavoriteBook = jasmine.createSpy('MockFavoriteBook');
            MockBook = jasmine.createSpy('MockBook');
            MockProfile = jasmine.createSpy('MockProfile');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'FavoriteBook': MockFavoriteBook,
                'Book': MockBook,
                'Profile': MockProfile
            };
            createController = function() {
                $injector.get('$controller')("FavoriteBookDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'eLibraryApp:favoriteBookUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
