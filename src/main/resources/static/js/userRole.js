var loadReferenceDataForSysRole = ()=>{
	$.ajax({
        type: "POST",
        url: "/userRole/loadRefDataForSysRole",        
        contentType: "application/json",
        dataType: "json",
        success: function(data){
        	alert(JSON.stringify(data));
    	},
        failure: function(errMsg) {
            alert(errMsg);
        }
  });
};