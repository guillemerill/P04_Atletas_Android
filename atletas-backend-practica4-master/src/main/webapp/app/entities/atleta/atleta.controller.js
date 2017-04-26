(function() {
    'use strict';

    angular
        .module('atletasApp')
        .controller('AtletaController', AtletaController);

    AtletaController.$inject = ['$scope', '$state', 'Atleta'];

    function AtletaController ($scope, $state, Atleta) {
        var vm = this;

        vm.atletas = [];

        loadAll();

        function loadAll() {
            Atleta.query(function(result) {
                vm.atletas = result;
                vm.searchQuery = null;
            });
        }
    }
})();
