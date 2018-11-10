(function() {
    'use strict';

    angular
        .module('eLibraryApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('favorite-book', {
            parent: 'entity',
            url: '/favorite-book',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'eLibraryApp.favoriteBook.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/favorite-book/favorite-books.html',
                    controller: 'FavoriteBookController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('favoriteBook');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('favorite-book-detail', {
            parent: 'favorite-book',
            url: '/favorite-book/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'eLibraryApp.favoriteBook.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/favorite-book/favorite-book-detail.html',
                    controller: 'FavoriteBookDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('favoriteBook');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'FavoriteBook', function($stateParams, FavoriteBook) {
                    return FavoriteBook.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'favorite-book',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('favorite-book-detail.edit', {
            parent: 'favorite-book-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/favorite-book/favorite-book-dialog.html',
                    controller: 'FavoriteBookDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FavoriteBook', function(FavoriteBook) {
                            return FavoriteBook.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('favorite-book.new', {
            parent: 'favorite-book',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/favorite-book/favorite-book-dialog.html',
                    controller: 'FavoriteBookDialogController',
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
                    $state.go('favorite-book', null, { reload: 'favorite-book' });
                }, function() {
                    $state.go('favorite-book');
                });
            }]
        })
        .state('favorite-book.edit', {
            parent: 'favorite-book',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/favorite-book/favorite-book-dialog.html',
                    controller: 'FavoriteBookDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FavoriteBook', function(FavoriteBook) {
                            return FavoriteBook.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('favorite-book', null, { reload: 'favorite-book' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('favorite-book.delete', {
            parent: 'favorite-book',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/favorite-book/favorite-book-delete-dialog.html',
                    controller: 'FavoriteBookDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FavoriteBook', function(FavoriteBook) {
                            return FavoriteBook.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('favorite-book', null, { reload: 'favorite-book' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
