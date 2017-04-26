(function() {
    'use strict';

    angular
        .module('atletasApp')
        .controller('AtletaDetailController', AtletaDetailController);

    AtletaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Atleta'];

    function AtletaDetailController($scope, $rootScope, $stateParams, previousState, entity, Atleta) {
        var vm = this;

        vm.atleta = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('atletasApp:atletaUpdate', function(event, result) {
            vm.atleta = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
