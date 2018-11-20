/**
 * @author: rusiru
 */

/**
 * Load Reference Data For Drop downs
 */
var loadReferenceDataForUserRole = (callback)=>{
	$.ajax({
        type: "POST",
        url: "/userRole/loadRefDataForSysRole",        
        contentType: "application/json",
        dataType: "json",
        success: function(data){    
        	
        	if(data.code === "SUCCESS"){
            	for(let s of data.data.status){
            		$("#status").append(`<option value="${s.code}">${s.description}</option>`);
            	}
            	
            	if(callback){
            		callback();
            	}
        	}
        	else{
        		alert("System Failer Occur....! :-(");
        	}
        	

    	},
        failure: function(errMsg) {
            alert(errMsg);
        }
  });
};

/*
 * Modal Open Button Event
 */
var openUserRoleEditModal = ()=>{
	$("#sysRoleEditModal").modal("open");
}

/**
 * Event Binder
 */

var evenBinderForUserRole = ()=>{
	
	$("#addNewBtn").on("click",()=>{
		openUserRoleEditModal();
	});
};


$(document).ready(()=>{	
	
	let _callback_1 = ()=>{
		$("#sysRoleEditModal").modal();
		$('select').formSelect();
	};	
	loadReferenceDataForUserRole(_callback_1);
	evenBinderForUserRole();

})