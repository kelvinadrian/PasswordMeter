var app = angular.module("ValidatorManagement", []);

app.controller("PasswordValidationController", function($scope, $http) {
    $scope.senha = {"complexity": "too short", "score": "0"}

    $scope.validar = function(obj) {
        console.log(obj);
        $http({
            url: '/validar',
            method: "POST",
            data: { 'senha' : obj }
        }).then(function(response) {
                $scope.senha = response.data;
                document.getElementById("scorebar").style.backgroundPositionX = $scope.senha.score*-4 + "px";
            });
    };
});