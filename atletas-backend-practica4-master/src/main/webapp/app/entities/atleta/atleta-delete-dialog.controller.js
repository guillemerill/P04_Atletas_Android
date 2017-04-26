(function() {
    'use strict';

    angular
        .module('atletasApp')
        .controller('AtletaDeleteController',AtletaDeleteController);

    AtletaDeleteController.$inject = ['$uibModalInstance', 'entity', 'Atleta'];

    function AtletaDeleteController($uibModalInstance, entity, Atleta) {
        var vm = this;

        vm.atleta = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Atleta.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
