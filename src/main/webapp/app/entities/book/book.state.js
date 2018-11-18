(function() {
    'use strict';

    angular
        .module('eLibraryApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('book', {
            parent: 'entity',
            url: '/book',
            data: {
                // authorities: ['ROLE_ANONYMOUS'],
                // authorities: ['ROLE_USER'],
                authorities: [],
                pageTitle: 'eLibraryApp.book.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/book/books.html',
                    controller: 'BookController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('book');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })

        .state('book-detail', {
            parent: 'book',
            url: '/book/{id}',
            data: {
                authorities: [],
                // authorities: ['ROLE_USER'],
                // authorities: ['ROLE_ANONYMOUS'],
                pageTitle: 'eLibraryApp.book.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/book/book-detail.html',
                    controller: 'BookDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('book');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Book', function($stateParams, Book) {
                    return Book.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'book',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })

        .state('book.new', {
            parent: 'book',
            url: '/new',
            views: {
                'content@': {
                    templateUrl: 'app/entities/book/book-add.html',
                    controller: 'BookAddController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ["$stateParams", 'Book', function($stateParams,Book) {
                    return {
                        title: null,
                        description: null,
                        pages: null,
                        approved: false,
                        path: null,
                        coverPath: null,
                        createdBy: null,
                        createdDate: null,
                        lastModifiedBy: null,
                        lastModifiedDate: null,
                        yearOfPublishing: null,
                        authorFirstName: null,
                        authorLastName: null,
                        id: null
                    };
                }]
            }
        })

        .state('book-detail.edit', {
            parent: 'book-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/book/book-dialog.html',
                    controller: 'BookDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Book', function(Book) {
                            return Book.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })

        .state('book.edit', {
            parent: 'book',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', 'm', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/book/book-add.html',
                    // templateUrl: 'app/entities/book/book-dialog.html',
                    controller: 'BookDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Book', function(Book) {
                            return Book.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('book', null, { reload: 'book' });
                }, function() {
                    $state.go('^');
                });
            }]
        })

        // .state('book.delete', {
        //     parent: 'book',
        //     url: '/{id}/delete',
        //     data: {
        //         authorities: ['ROLE_USER']
        //     },
        //     onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
        //         $uibModal.open({
        //             templateUrl: 'app/entities/book/book-delete-dialog.html',
        //             controller: 'BookDeleteController',
        //             controllerAs: 'vm',
        //             size: 'md',
        //             resolve: {
        //                 entity: ['Book', function(Book) {
        //                     return Book.get({id : $stateParams.id}).$promise;
        //                 }]
        //             }
        //         }).result.then(function() {
        //             $state.go('book', null, { reload: 'book' });
        //         }, function() {
        //             $state.go('^');
        //         });
        //     }]
        // })

        .state('book.delete', {
            parent: 'book',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/book/book-delete-dialog.html',
                    controller: 'BookDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Book', function(Book) {
                            return Book.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('book', null, { reload: 'book' });
                }, function() {
                    $state.go('^');
                });
            }]
        })

        .state('notification', {
            parent: 'entity',
            url: '/notification',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'eLibraryApp.book.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/book/book-notification.html',
                    controller: 'BookNotificationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ["$stateParams", 'Book', function($stateParams,Book) {
                    return {
                        approved: false,
                        id: null
                    };
                }],
                
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('book');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        });
    }

})();
