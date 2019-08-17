/**
 * @author: rusiru
 */

var sysRoleTable;


/*------------------------------------------- CRUD Functions ------------------*/

var generateFinalObjectForSysRole = ()=>{
	return {
		code:$("#code").val()||"",
		description:$("#description").val()||"",
		statusCode:$("#status").val()||""
	}
};

var successFunctionForSysRole = (data)=>{
	if(data.code === Constant.CODE_SUCCESS){
		DialogBox.openSuccessMsgBox(data.message);		
		sysRoleTable.ajax.reload();
		clearDataForSysRole();
	}
	else{
		alert(data.message);
	}
};

var failedFunctionForSysRole = (data)=>{
	alert("Server Error");
};

var validatorForSysRole = ()=>{
	let isValid = true;
	
	let code = $("#code");
	let description = $("#description");
	let status = $("#status");
	
	if(! code.val()){
		InputsValidator.inlineEmptyValidation(code);
		isValid = false;
	}
	if(! description.val()){
		InputsValidator.inlineEmptyValidation(description);
		isValid = false;
	}
	if(! status.val()){
		InputsValidator.inlineEmptyValidation(status);
		isValid = false;
	}
	return isValid;
};

var saveForSysRole = ()=>{
	if(validatorForSysRole()){
		let url = "/sysRole/save";
		let method = "POST";		
		
		callToserver(url,method,generateFinalObjectForSysRole(),successFunctionForSysRole,failedFunctionForSysRole);
	}
	
};

var updateForSysRole = ()=>{
	if(validatorForSysRole()){
		let url = "/sysRole/update";
		let method = "POST";
				
		callToserver(url,method,generateFinalObjectForSysRole(),successFunctionForSysRole,failedFunctionForSysRole);
	}
};

var deleteForSysRole = ()=>{
	if(validatorForSysRole()){
		let url = "/sysRole/delete";
		let method = "POST";
		
		callToserver(url,method,generateFinalObjectForSysRole(),successFunctionForSysRole,failedFunctionForSysRole);
	}
};

var findDetailByCodeForSysRole = (code,callback)=>{
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
	let url = "/sysRole/loadSysRoleByCode";
	let method = "POST";
	callToserver(url,method,{code:code},successFunction,failedFunction);
	
};


/*-------------------------------- Reference Data , Data Table and Common --------------------*/

var populateFormForSysRole = (data) => {
	if(data){
		$("#code")[0].parentElement.MaterialTextfield.change(data.code || "");
		$("#description")[0].parentElement.MaterialTextfield.change(data.description || "");
		$("#status")[0].parentElement.MaterialSelectfield.change(data.statusCode || "");
	}	
};

var loadReferenceDataForSysRole = (callback)=>{
	$.ajax({
        type: "POST",
        url: "/sysRole/loadRefDataForSysRole",
        contentType: "application/json",
        dataType: "json",
        success: function(data){    
        	
        	if(data.code === Constant.CODE_SUCCESS){
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


var loadSysRoleTable = ()=>{
	sysRoleTable = $('#sysRoleTable').DataTable( {
                        ajax: {
                            url : "/sysRole/loadSysRoles",
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
                            { data: "code"                ,name:"code"                ,class:"mdl-data-table__cell--non-numeric"},
                            { data: "description"         ,name:"description"         ,class:"mdl-data-table__cell--non-numeric"},
                            { data: "statusDescription"   ,name:"status"              ,class:"mdl-data-table__cell--non-numeric"},
                            { data: "createdBy"           ,name:"createdBy"           ,class:"mdl-data-table__cell--non-numeric"},
                            { data: "createdOn"           ,name:"createdOn"           ,class:"mdl-data-table__cell--non-numeric"},
                            { data: "updatedBy"           ,name:"updatedBy"           ,class:"mdl-data-table__cell--non-numeric"},
                            { data: "updatedOn"           ,name:"updatedOn"           ,class:"mdl-data-table__cell--non-numeric"},
                            {
                            	data: "code",
                            	class:"mdl-data-table__cell--non-numeric",
                            	render: function (data, type, full) {
		                            		return `<button onClick="updateIconClickForSysRole('${data}')" class="mdl-button mdl-js-button mdl-button--icon mdl-button--colored">
													  <i id="icon-update-${data}" class="material-icons">create</i>
													  <div class="mdl-tooltip" data-mdl-for="icon-update-${data}">
														Update
													  </div>
													</button>
													<button onClick="deleteIconClickForSysRole('${data}')" class="mdl-button mdl-js-button mdl-button--icon mdl-button--colored">
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

var clearDataForSysRole = ()=>{
	let code = $("#code");
	let description = $("#description");
	let status = $("#status");
	
	$("#btnSave").show();
	$("#btnUpdate").hide();
	$("#btnDelete").hide();

	$("#formHeading").html("");
	
	code[0].parentElement.MaterialTextfield.enable();
	description[0].parentElement.MaterialTextfield.enable();
	status[0].parentElement.MaterialSelectfield.enable();

	InputsValidator.removeInlineValidation(code);
	InputsValidator.removeInlineValidation(description);
	InputsValidator.removeInlineValidation(status);
	
	code[0].parentElement.MaterialTextfield.change("");
	description[0].parentElement.MaterialTextfield.change("");
	status.val("");
	status[0].parentElement.MaterialSelectfield.change("");

	FormTransition.closeForm('#sysRoleForm','#sysRoleGrid');
	
};


/*-------------------------------- Inline Event  ----------------------*/
var clickAddForSysRole = ()=>{
	clearDataForSysRole();
	$("#formHeading").html("Add System Role");
	FormTransition.openForm('#sysRoleForm','#sysRoleGrid');
};

var updateIconClickForSysRole = (code)=>{
	let _sF = (data)=>{
		$("#btnSave").hide();
		$("#btnUpdate").show();
		$("#btnDelete").hide();
		populateFormForSysRole(data);
		$("#code")[0].parentElement.MaterialTextfield.disable();
		$("#formHeading").html("Update System Role");
		FormTransition.openForm('#sysRoleForm','#sysRoleGrid');
	};
	clearDataForSysRole();
	findDetailByCodeForSysRole(code,_sF);
};

var deleteIconClickForSysRole = (code)=>{
	let _sF = (data)=>{
		$("#btnSave").hide();
		$("#btnUpdate").hide();
		$("#btnDelete").show();
		populateFormForSysRole(data);
		$("#code")[0].parentElement.MaterialTextfield.disable();
		$("#description")[0].parentElement.MaterialTextfield.disable();
		$("#status")[0].parentElement.MaterialSelectfield.disable();
		$("#formHeading").html("Delete System Role");
		FormTransition.openForm('#sysRoleForm','#sysRoleGrid');
	};
	clearDataForSysRole();
	findDetailByCodeForSysRole(code,_sF);
};


/*-------------------------------- Dynamic Event  ----------------------*/

var evenBinderForSysRole = ()=>{
	$("#btnSysRoleAdd").off().on("click",function(){
		clickAddForSysRole();
	});

	$("#btnSave").off().on("click",function(){
		saveForSysRole();
	});
	
	$("#btnUpdate").off().on("click",function(){
		updateForSysRole();
	});
	
	$("#btnDelete").off().on("click",function(){
		deleteForSysRole();
	});
	
	$("#btnCancel").off().on("click",function(){
		clearDataForSysRole();
	});

};

/*-------------------------------- Document Ready ----------------------*/


$(document).ready(()=>{	

 	let _callback_1 = ()=>{
 		componentHandler.upgradeDom(); 		
	};	
	loadReferenceDataForSysRole(_callback_1);
	evenBinderForSysRole();
	loadSysRoleTable();
	 

});