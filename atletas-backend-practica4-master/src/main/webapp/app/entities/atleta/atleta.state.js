(function() {
    'use strict';

    angular
        .module('atletasApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('atleta', {
            parent: 'entity',
            url: '/atleta',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'atletasApp.atleta.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/atleta/atletas.html',
                    controller: 'AtletaController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('atleta');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('atleta-detail', {
            parent: 'entity',
            url: '/atleta/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'atletasApp.atleta.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/atleta/atleta-detail.html',
                    controller: 'AtletaDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('atleta');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Atleta', function($stateParams, Atleta) {
                    return Atleta.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'atleta',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('atleta-detail.edit', {
            parent: 'atleta-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/atleta/atleta-dialog.html',
                    controller: 'AtletaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Atleta', function(Atleta) {
                            return Atleta.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('atleta.new', {
            parent: 'atleta',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/atleta/atleta-dialog.html',
                    controller: 'AtletaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nombre: null,
                                apellido: null,
                                nacionalidad: null,
                                fechaNacimiento: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('atleta', null, { reload: 'atleta' });
                }, function() {
                    $state.go('atleta');
                });
            }]
        })
        .state('atleta.edit', {
            parent: 'atleta',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/atleta/atleta-dialog.html',
                    controller: 'AtletaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Atleta', function(Atleta) {
                            return Atleta.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('atleta', null, { reload: 'atleta' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('atleta.delete', {
            parent: 'atleta',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/atleta/atleta-delete-dialog.html',
                    controller: 'AtletaDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Atleta', function(Atleta) {
                            return Atleta.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('atleta', null, { reload: 'atleta' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
