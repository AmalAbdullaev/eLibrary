(function() {
    'use strict';

    angular
        .module('eLibraryApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('read-book', {
            parent: 'entity',
            url: '/read-book',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'eLibraryApp.readBook.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/read-book/read-books.html',
                    controller: 'ReadBookController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('readBook');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('read-book-detail', {
            parent: 'read-book',
            url: '/read-book/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'eLibraryApp.readBook.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/read-book/read-book-detail.html',
                    controller: 'ReadBookDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('readBook');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ReadBook', function($stateParams, ReadBook) {
                    return ReadBook.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'read-book',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('read-book-detail.edit', {
            parent: 'read-book-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/read-book/read-book-dialog.html',
                    controller: 'ReadBookDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ReadBook', function(ReadBook) {
                            return ReadBook.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('read-book.new', {
            parent: 'read-book',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/read-book/read-book-dialog.html',
                    controller: 'ReadBookDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('read-book', null, { reload: 'read-book' });
                }, function() {
                    $state.go('read-book');
                });
            }]
        })
        .state('read-book.edit', {
            parent: 'read-book',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/read-book/read-book-dialog.html',
                    controller: 'ReadBookDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ReadBook', function(ReadBook) {
                            return ReadBook.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('read-book', null, { reload: 'read-book' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('read-book.delete', {
            parent: 'read-book',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/read-book/read-book-delete-dialog.html',
                    controller: 'ReadBookDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ReadBook', function(ReadBook) {
                            return ReadBook.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('read-book', null, { reload: 'read-book' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
