var resupplyApp = angular.module('resupplyApp', {});

resupplyApp.controller('ItemController', function ($scope, $http, $sce) {
    $http.get('/api/item/').
        success(function (data) {
        	if (data == null || data == "") {
        		$scope.items = {};
        	} else {
            	$scope.items = data;
        	}	
            $scope.disabled = false;
        });

    $scope.save = function () {
        $http.post('/api/item/', $scope.items).
            success(function (data, status, headers, config) {
            	alert("Items saved successfully");
            }).
            error(function (data, status, headers, config) {
            	alert("Items save failure");
            });
    }
    
    $scope.add = function() {
    	var newItem = {
    		name: "Item Name",
    		description: "Item Description",
    		sizeTypes: [ "size type 1", "size type 2"],
    		available: true
    	};
    	$scope.items.push(newItem);
    }
    
    $scope.delete = function () {
    	$http.delete('/api/character/' + id).
        success(function (data, status, headers, config) {
        	alert("Delete success");
        	window.location.href = "/user";
        }).
        error(function (data, status, headers, config) {
        	alert("Delete failure");
        });
    }
});

resupplyApp.controller('OrderController', function ($scope, $http, $sce) {
    $http.get('/api/order/').
        success(function (data) {
        	if (data == null || data == "") {
        		$scope.order = {};
        	} else {
            	$scope.order = data[0];
        	}
            $scope.disabled = false;
        });

    $scope.save = function () {
        var saveOrder = $scope.order;
        $http.post('/api/order/', saveOrder).
            success(function (data, status, headers, config) {
                $scope.order.sent = true;
            	alert("Resupply sent");
            }).
            error(function (data, status, headers, config) {
            	alert("Problem sending resupply");
            });
    }
});

resupplyApp.controller('ManageOrderController', function ($scope, $http, $sce) {
    $http.get('/api/order/').
        success(function (data) {
            if (data == null || data == "") {
                $scope.orders = {};
            } else {
                $scope.orders = data;
            }
            $scope.disabled = false;
        });

    $scope.save = function () {
        var saveOrders = $scope.orders;
        $http.post('/api/order/saveManaged', saveOrders).
            success(function (data, status, headers, config) {
                alert("Resupply statuses updated");
            }).
            error(function (data, status, headers, config) {
                alert("Problem saving resupply statuses");
            });
    }


    $scope.saveSingle = function (order) {
        $http.post('/api/order/saveManaged', order).
            success(function (data, status, headers, config) {
                alert("Resupply status updated");
            }).
            error(function (data, status, headers, config) {
                alert("Problem saving resupply status");
            });
    }
});

resupplyApp.controller('SettingController', function ($scope, $http, $sce) {
	var id= $("#objId").val();
    $http.get('/api/setting/' + id).
        success(function (data) {
        	if (data == null || data == "") {
        		$scope.char = {};
        	} else {
            	$scope.char = data;
        	}	
            if (null == $scope.char.url) {
            	$scope.char.url = "/images/blank.png";
            }
            $scope.char.description = $sce.trustAsHtml($scope.char.description);
            $scope.orightml = $scope.char.description;
            $scope.htmlcontent = $scope.orightml;
            $scope.disabled = false;
        });

    $scope.save = function () {
        var saveCharacter = $scope.char;
        if ($scope.htmlcontent !== null && $scope.htmlcontent !== "") {
        	saveCharacter.description = $scope.htmlcontent.toString();
        } else {
        	saveCharacter.description = "";
        }
        saveCharacter.id = $("#objId").val();
        $http.post('/api/setting/' + id, saveCharacter).
            success(function (data, status, headers, config) {
            	alert("Save success");
            }).
            error(function (data, status, headers, config) {
            	alert("Save failure");
            });
    }
    
    $scope.delete = function () {
    	$http.delete('/api/setting/' + id).
        success(function (data, status, headers, config) {
        	alert("Delete success");
        	window.location.href = "/user";
        }).
        error(function (data, status, headers, config) {
        	alert("Delete failure");
        });
    }
    
    $scope.deleteMember = function(memberId) {
    	for (var i = 0; i < $scope.char.members.length; i++) {
    		if ($scope.char.members[i].id == memberId) {
    			$scope.char.members.splice(i,1);
    		}
    	}
    }
    
    $scope.deleteLocation = function(locationId) {
    	for (var i = 0; i < $scope.char.locations.length; i++) {
    		if ($scope.char.locations[i].id == locationId) {
    			$scope.char.locations.splice(i,1);
    		}
    	}
    }
    
    $scope.tab = 1;

    $scope.setTab = function (tabId) {
    	$scope.tab = tabId;
    };

    $scope.isSet = function (tabId) {
        return $scope.tab === tabId;
    };    
});

resupplyApp.controller('RegistrationController', function ($scope, $http, $sce) {
    $scope.submit = function () {
        var saveUser = {};
        saveUser.email = $scope.email;
        saveUser.password = $scope.password;
        
        $http.post('/api/user', saveUser).
            success(function (data, status, headers, config) {
            	window.location.href = "/login";
            }).
            error(function (data, status, headers, config) {
            	alert("A user with that email already exists");
            });
    }
});

resupplyApp.controller('UserController', function ($scope, $http, $sce) {
	$scope.username = $("#currentName").val();
    $scope.save = function () {
        $http.post('/api/user/username', $scope.username).
            success(function (data, status, headers, config) {
            	alert("Save success");
            }).
            error(function (data, status, headers, config) {
            	if (status == 400) {
            		alert("Sorry, that name is already in use!");
            	} else {
            		alert("Save failed");
            	}
            });
    }
});

resupplyApp.filter('iif', function () {
    return function(input, trueValue, falseValue) {
        return input ? trueValue : falseValue;
    };
});

