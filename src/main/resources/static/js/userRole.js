/**
 * @author: rusiru
 */

var userRoleTable;


/**
 * Generate Final Save Object
 */
var generateFinalObjectForUserRole = ()=>{
	return {
		code:$("#code").val()||"",
		description:$("#description").val()||"",
		statusCode:$("#status").val()||""
	}
};

var successFunctionForUserRole = (data)=>{
	if(data.code === Constant.CODE_SUCCESS){
		alert(data.message);
	}
	else{
		alert(data.message);
	}
};

var failedFunctionForUserRole = (data)=>{
	alert("Server Error");
};

var validatorForUserRole = ()=>{
	let isValid = true;
	if(! $("#code").val()){
		alert("Code Can not be empty");
		return isValid = false;
	}
	if(! $("#description").val()){
		alert("Description Can not be empty");
		return isValid = false;
	}
	if(! $("#status").val()){
		alert("Status Can not be empty");
		return isValid = false;
	}
	return isValid;
};

var saveForUserRole = ()=>{
	if(validatorForUserRole()){
		let url = "/userRole/save";
		let method = "POST";		
		
		callToserver(url,method,generateFinalObjectForUserRole(),successFunctionForUserRole,failedFunctionForUserRole);
	}
	
};

var updateForUserRole = ()=>{
	if(validatorForUserRole()){
		let url = "/userRole/update";
		let method = "POST";
				
		callToserver(url,method,generateFinalObjectForUserRole(),successFunctionForUserRole,failedFunctionForUserRole);
	}
};

var deleteForUserRole = ()=>{
	if(validatorForUserRole()){
		let url = "/userRole/delete";
		let method = "POST";
		
		callToserver(url,method,generateFinalObjectForUserRole(),successFunctionForUserRole,failedFunctionForUserRole);
	}
};


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


var loadUserRoleTable = ()=>{
	userRoleTable = $('#userRoleTable').DataTable( {
                        ajax: {
                            url : "/userRole/loadUserRoles",
                            contentType:"application/json",
                            type:"POST",
                            data:function(d){
                                return JSON.stringify(createCommonDataTableRequset(d));
                    		}
                        },
                        processing: true,
                        serverSide: true,
                        scrollY:        true,
                        scrollX:        true,
                        scrollCollapse: true,
                        paging:         true,
                        pagingType: "full_numbers",
                        columns: [
                            { data: "code"                ,name:"code"         ,class:"leaf-text-left"},
                            { data: "description"         ,name:"description"},
                            { data: "statusDescription"   ,name:"status"},
                            { data: "createdBy"           ,name:"createdBy"},
                            { data: "createdOn"           ,name:"createdOn"},
                            { data: "updatedBy"           ,name:"updatedBy"},
                            { data: "updatedOn"           ,name:"updatedOn"},
                            { data: "code"}
                        ]
                    } );
}


/**
 * Event Binder
 */

var evenBinderForUserRole = ()=>{
	$("#btnSave").off().on("click",function(){
		saveForUserRole();
	});
	
	$("#btnUpdate").off().on("click",function(){
		updateForUserRole();
	});
	
	$("#btnDelete").off().on("click",function(){
		deleteForUserRole();
	});
	
	$("#btnCancel").off().on("click",function(){});

};


$(document).ready(()=>{	

 	let _callback_1 = ()=>{
 		componentHandler.upgradeDom(); 		
	};	
	loadReferenceDataForUserRole(_callback_1);
	evenBinderForUserRole();
	loadUserRoleTable();
	 

});