(function() {
    'use strict';

    angular
        .module('atletasApp')
        .controller('AtletaDialogController', AtletaDialogController);

    AtletaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Atleta'];

    function AtletaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Atleta) {
        var vm = this;

        vm.atleta = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.atleta.id !== null) {
                Atleta.update(vm.atleta, onSaveSuccess, onSaveError);
            } else {
                Atleta.save(vm.atleta, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('atletasApp:atletaUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.fechaNacimiento = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
