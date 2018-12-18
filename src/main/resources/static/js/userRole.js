/**
 * @author: rusiru
 */

var userRoleTable;


/*------------------------------------------- CRUD Functions ------------------*/

var generateFinalObjectForUserRole = ()=>{
	return {
		code:$("#code").val()||"",
		description:$("#description").val()||"",
		statusCode:$("#status").val()||""
	}
};

var successFunctionForUserRole = (data)=>{
	if(data.code === Constant.CODE_SUCCESS){
		DialogBox.openSuccessMsgBox(data.message);		
		userRoleTable.ajax.reload();
		clearDataForUserRole();
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
	
	let code = $("#code");
	let description = $("#description");
	let status = $("#status");
	
	if(! code.val()){		
		//code.prop("required",true);		
		return isValid = false;
	}
	if(! description.val()){
		//description.prop("required",true);		
		return isValid = false;
	}
	if(! status.val()){
		//status.prop("required",true);
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

var findDetailByCodeForUserRole = (code,callback)=>{
	let successFunction = (data)=>{
		if(data.code === Constant.CODE_SUCCESS){			
			if(callback){
				callback(data.data);
				componentHandler.upgradeDom();
			}
		}
		else{
			alert(data.message);
		}
	};
	let failedFunction = (data)=>{
		alert("Server Error");
	};
	let url = "/userRole/loadUserRoleByCode";
	let method = "POST";
	callToserver(url,method,{code:code},successFunction,failedFunction);
	
};


/*-------------------------------- Reference Data , Data Table and Common --------------------*/

var populateFormForUserRole = (data) => {
	if(data){
		$("#code")[0].parentElement.MaterialTextfield.change(data.code || "");
		$("#description")[0].parentElement.MaterialTextfield.change(data.description || "");
		$("#status")[0].parentElement.MaterialSelectfield.change(data.statusCode || "");
	}	
};

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
                        drawCallback: function( settings ) {
                        	componentHandler.upgradeDom();
                        },
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
                            {
                            	data: "code",
                            	render: function (data, type, full) {
		                            		return `<button onClick="updateIconClickForUserRole('${data}')" class="mdl-button mdl-js-button mdl-button--icon mdl-button--colored">
													  <i id="icon-update-${data}" class="material-icons">create</i>
													  <div class="mdl-tooltip" data-mdl-for="icon-update-${data}">
														Update
													  </div>
													</button>
													<button onClick="deleteIconClickForUserRole('${data}')" class="mdl-button mdl-js-button mdl-button--icon mdl-button--colored">
													  <i id="icon-delete-${data}" class="material-icons">delete</i>
													  <div class="mdl-tooltip" data-mdl-for="icon-delete-${data}">
														Delete
													  </div>
													</button>`;
		                            	}
                    		}
                        ]
                    } );
}

var clearDataForUserRole = ()=>{
	let code = $("#code");
	let description = $("#description");
	let status = $("#status");
	
	$("#btnSave").show();
	$("#btnUpdate").hide();
	$("#btnDelete").hide();

	
	code[0].parentElement.MaterialTextfield.enable();
	description[0].parentElement.MaterialTextfield.enable();
	status[0].parentElement.MaterialSelectfield.enable();	
	
	code.prop("required",false);	
	description.prop("required",false);	
	status.prop("required",false);	
	
	code[0].parentElement.MaterialTextfield.change("");
	description[0].parentElement.MaterialTextfield.change("");
	status[0].parentElement.MaterialSelectfield.change("");
	
};


/*-------------------------------- Inline Event  ----------------------*/

var updateIconClickForUserRole = (code)=>{
	let _sF = (data)=>{
		$("#btnSave").hide();
		$("#btnUpdate").show();
		$("#btnDelete").hide();
		populateFormForUserRole(data);
		$("#code")[0].parentElement.MaterialTextfield.disable();
	}
	clearDataForUserRole();
	findDetailByCodeForUserRole(code,_sF);
};

var deleteIconClickForUserRole = (code)=>{
	let _sF = (data)=>{
		$("#btnSave").hide();
		$("#btnUpdate").hide();
		$("#btnDelete").show();
		populateFormForUserRole(data);
		$("#code")[0].parentElement.MaterialTextfield.disable();
		$("#description")[0].parentElement.MaterialTextfield.disable();
		$("#status")[0].parentElement.MaterialSelectfield.disable();		
	}
	clearDataForUserRole();
	findDetailByCodeForUserRole(code,_sF);
};


/*-------------------------------- Dynamic Event  ----------------------*/

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
	
	$("#btnCancel").off().on("click",function(){
		clearDataForUserRole();
	});

};

/*-------------------------------- Document Ready ----------------------*/


$(document).ready(()=>{	

 	let _callback_1 = ()=>{
 		componentHandler.upgradeDom(); 		
	};	
	loadReferenceDataForUserRole(_callback_1);
	evenBinderForUserRole();
	loadUserRoleTable();
	 

});