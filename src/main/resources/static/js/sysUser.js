/**
 * @author: rusiru
 */

var sysUserTable;

/*------------------------------------------- CRUD Functions ------------------*/

var generateFinalObjectForSysUser = ()=>{
	return {
		username:$("#username").val()||"",
		titleCode:$("#title").val()||"",
		name:$("#name").val()||"",
		statusCode:$("#status").val()||""
	}
};

var successFunctionForSysUser = (data)=>{
	if(data.code === Constant.CODE_SUCCESS){
		DialogBox.openSuccessMsgBox(data.message);		
		sysUserTable.ajax.reload();
		clearDataForSysUser();
	}
	else{
		alert(data.message);
	}
};

var failedFunctionForSysUser = (data)=>{
	alert("Server Error");
};

var validatorForSysUser = ()=>{
	let isValid = true;
	
	let username = $("#username");
	let title = $("#title");
	let name = $("#title");
	let status = $("#status");
	
	if(! username.val()){		
		//code.prop("required",true);		
		return isValid = false;
	}
	if(! title.val()){
		//description.prop("required",true);		
		return isValid = false;
	}
	if(! name.val()){
		//description.prop("required",true);		
		return isValid = false;
	}
	if(! status.val()){
		//status.prop("required",true);
		return isValid = false;
	}
	return isValid;
};

var saveForSysUser = ()=>{
	if(validatorForSysUser()){
		let url = "/sysUser/save";
		let method = "POST";		
		
		callToserver(url,method,generateFinalObjectForSysUser(),successFunctionForSysUser,failedFunctionForSysUser);
	}
	
};

var updateForSysUser = ()=>{
	if(validatorForUserRole()){
		let url = "/sysUser/update";
		let method = "POST";
				
		callToserver(url,method,generateFinalObjectForSysUser(),successFunctionForSysUser,failedFunctionForSysUser);
	}
};

var deleteForSysUser = ()=>{
	if(validatorForSysUser()){
		let url = "/sysUser/delete";
		let method = "POST";
		
		callToserver(url,method,generateFinalObjectForSysUser(),successFunctionForSysUser,failedFunctionForSysUser);
	}
};

var findDetailByCodeForSysUser = (username,callback)=>{
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
	let url = "/sysUser/loadSysUserByUsename";
	let method = "POST";
	callToserver(url,method,{username:username},successFunction,failedFunction);
	
};

/*-------------------------------- Reference Data , Data Table and Common --------------------*/
var populateFormForSysUser = (data) => {
	if(data){
		$("#username")[0].parentElement.MaterialTextfield.change(data.username || "");
		$("#title")[0].parentElement.MaterialSelectfield.change(data.titleCode || "");
		$("#name")[0].parentElement.MaterialTextfield.change(data.name || "");
		$("#status")[0].parentElement.MaterialSelectfield.change(data.statusCode || "");
	}	
};

var loadReferenceDataForSysUser= (callback)=>{
	$.ajax({
        type: "POST",
        url: "/sysUser/loadRefDataForSysUser",        
        contentType: "application/json",
        dataType: "json",
        success: function(data){    
        	
        	if(data.code === "SUCCESS"){
            	for(let s of data.data.status){            		
            		$("#status").append(`<option value="${s.code}">${s.description}</option>`);
            	}
            	for(let t of data.data.title){            		
            		$("#title").append(`<option value="${t.code}">${t.description}</option>`);
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

//todo- Datatable Here

var clearDataForSysRole = ()=>{
	let username = $("#username");
	let title = $("#title");
	let name = $("#name");
	let status = $("#status");
	
	$("#btnSave").show();
	$("#btnUpdate").hide();
	$("#btnDelete").hide();

	
	username[0].parentElement.MaterialTextfield.enable();
	title[0].parentElement.MaterialSelectfield.enable();
	name[0].parentElement.MaterialTextfield.enable();
	status[0].parentElement.MaterialSelectfield.enable();
	

	
	username[0].parentElement.MaterialTextfield.change("");
	title.val("");
	title[0].parentElement.MaterialSelectfield.change("");
	name[0].parentElement.MaterialTextfield.change("");
	status.val("");
	status[0].parentElement.MaterialSelectfield.change("");
	
};

/*-------------------------------- Inline Event  ----------------------*/

var updateIconClickForSysUser = (username)=>{
	let _sF = (data)=>{
		$("#btnSave").hide();
		$("#btnUpdate").show();
		$("#btnDelete").hide();
		populateFormForSysRole(data);
		$("#username")[0].parentElement.MaterialTextfield.disable();
	}
	clearDataForSysUser();
	findDetailByCodeForSysUser(username,_sF);
};

var deleteIconClickForSysUser = (username)=>{
	let _sF = (data)=>{
		$("#btnSave").hide();
		$("#btnUpdate").hide();
		$("#btnDelete").show();
		populateFormForUserRole(data);
		$("#username")[0].parentElement.MaterialTextfield.disable();
		$("#title")[0].parentElement.MaterialSelectfield.disable();
		$("#name")[0].parentElement.MaterialTextfield.disable();
		$("#status")[0].parentElement.MaterialSelectfield.disable();		
	}
	clearDataForSysUser();
	findDetailByCodeForSysUser(username,_sF);
};

/*-------------------------------- Dynamic Event  ----------------------*/

var evenBinderForSysUser = ()=>{
	$("#btnSave").off().on("click",function(){
		saveForSysUser();
	});
	
	$("#btnUpdate").off().on("click",function(){
		updateForSysUser();
	});
	
	$("#btnDelete").off().on("click",function(){
		deleteForSysUsere();
	});
	
	$("#btnCancel").off().on("click",function(){
		clearDataForSysUser();
	});

};


/*-------------------------------- Document Ready ----------------------*/


$(document).ready(()=>{	

 	let _callback_1 = ()=>{
 		componentHandler.upgradeDom(); 		
	};	
	loadReferenceDataForSysUser(_callback_1); 
	evenBinderForSysUser();

});
