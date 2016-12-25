var capOneApp = angular.module('capOneApp', []);

capOneApp.controller('myMoneyCtrl', function($scope, $http, $log) {
    $scope.ignoreDonuts = false;
    $scope.crystalBall = false;
    $scope.ignoreCCPayments = false;
    
    $scope.load = function() {
	    $http.get("/capitalone/resources/mymoney?ignoreDonuts="+$scope.ignoreDonuts+"&crystalBall="+$scope.crystalBall+"&ignoreCCPayments="+$scope.ignoreCCPayments).then(function(response) {
	    	$log.debug(response.data);
	        var reports = angular.fromJson(response.data);
	        $scope.monthlys = reports;
	        $scope.average = jsonPath(reports, "$.[?(@.name=='average')]")[0];
	    });
    };
    
    $scope.load();
});

