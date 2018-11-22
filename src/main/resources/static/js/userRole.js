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
            		$("#statusList").append(`<li class="mdl-menu__item" data-val="${s.code}">${s.description}</li>`);
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
	

};


$(document).ready(()=>{	

 	let _callback_1 = ()=>{
 		componentHandler.upgradeDom();6
 		getmdlSelect.init('.getmdl-select');
	};	
	loadReferenceDataForUserRole(_callback_1);
	evenBinderForUserRole();
	 

})