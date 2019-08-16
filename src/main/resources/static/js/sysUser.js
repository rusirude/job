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
	let name = $("#name");
	let status = $("#status");
	
	if(! username.val()){
		InputsValidator.inlineEmptyValidation(username);
		isValid = false;
	}
	if(! title.val()){
		InputsValidator.inlineEmptyValidation(title);
		isValid = false;
	}
	if(! name.val()){
		InputsValidator.inlineEmptyValidation(name);
		isValid = false;
	}
	if(! status.val()){
		InputsValidator.inlineEmptyValidation(status);
		isValid = false;
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
	if(validatorForSysUser()){
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
	let url = "/sysUser/loadSysUserByUsername";
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
        	
        	if(data.code === Constant.CODE_SUCCESS){
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

var loadSysUserTable = ()=>{
	sysUserTable = $('#sysUserTable').DataTable( {
                        ajax: {
                            url : "/sysUser/loadSysUsers",
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
                            { data: "username"            ,name:"username"            ,class:"mdl-data-table__cell--non-numeric"},
                            { data: "titleDescription"    ,name:"title"               ,class:"mdl-data-table__cell--non-numeric"},
                            { data: "name"                ,name:"name"                ,class:"mdl-data-table__cell--non-numeric"},
                            { data: "statusDescription"   ,name:"status"              ,class:"mdl-data-table__cell--non-numeric"},
                            { data: "createdBy"           ,name:"createdBy"           ,class:"mdl-data-table__cell--non-numeric"},
                            { data: "createdOn"           ,name:"createdOn"           ,class:"mdl-data-table__cell--non-numeric"},
                            { data: "updatedBy"           ,name:"updatedBy"           ,class:"mdl-data-table__cell--non-numeric"},
                            { data: "updatedOn"           ,name:"updatedOn"           ,class:"mdl-data-table__cell--non-numeric"},
                            {
                            	data: "username",
                            	class:"mdl-data-table__cell--non-numeric",
                            	render: function (data, type, full) {
		                            		return `<button onClick="updateIconClickForSysUser('${data}')" class="mdl-button mdl-js-button mdl-button--icon mdl-button--colored">
													  <i id="icon-update-${data}" class="material-icons">create</i>
													  <div class="mdl-tooltip" data-mdl-for="icon-update-${data}">
														Update
													  </div>
													</button>
													<button onClick="deleteIconClickForSysUser('${data}')" class="mdl-button mdl-js-button mdl-button--icon mdl-button--colored">
													  <i id="icon-delete-${data}" class="material-icons">delete</i>
													  <div class="mdl-tooltip" data-mdl-for="icon-delete-${data}">
														Delete
													  </div>
													</button>`;
		                            	}
                    		}
                        ]
                    } );
};

var clearDataForSysUser = ()=>{
	let username = $("#username");
	let title = $("#title");
	let name = $("#name");
	let status = $("#status");
	
	$("#btnSave").show();
	$("#btnUpdate").hide();
	$("#btnDelete").hide();

	$("#formHeading").html("");

	username[0].parentElement.MaterialTextfield.enable();
	title[0].parentElement.MaterialSelectfield.enable();
	name[0].parentElement.MaterialTextfield.enable();
	status[0].parentElement.MaterialSelectfield.enable();

	InputsValidator.removeInlineValidation(username);
	InputsValidator.removeInlineValidation(title);
	InputsValidator.removeInlineValidation(name);
	InputsValidator.removeInlineValidation(status);

	username[0].parentElement.MaterialTextfield.change("");
	title.val("");
	title[0].parentElement.MaterialSelectfield.change("");
	name[0].parentElement.MaterialTextfield.change("");
	status.val("");
	status[0].parentElement.MaterialSelectfield.change("");

	FormTransition.closeForm('#sysUserForm','#sysUserGrid');
	
};

/*-------------------------------- Inline Event  ----------------------*/
var clickAddForSysUser = ()=>{
	clearDataForSysUser();
	$("#formHeading").html("Add System User");
	FormTransition.openForm('#sysUserForm','#sysUserGrid');
};

var updateIconClickForSysUser = (username)=>{
	let _sF = (data)=>{
		$("#btnSave").hide();
		$("#btnUpdate").show();
		$("#btnDelete").hide();
		populateFormForSysUser(data);
		$("#username")[0].parentElement.MaterialTextfield.disable();
		$("#formHeading").html("Update System User");
		FormTransition.openForm('#sysUserForm','#sysUserGrid');
	};
	clearDataForSysUser();
	findDetailByCodeForSysUser(username,_sF);
};

var deleteIconClickForSysUser = (username)=>{
	let _sF = (data)=>{
		$("#btnSave").hide();
		$("#btnUpdate").hide();
		$("#btnDelete").show();
		populateFormForSysUser(data);
		$("#username")[0].parentElement.MaterialTextfield.disable();
		$("#title")[0].parentElement.MaterialSelectfield.disable();
		$("#name")[0].parentElement.MaterialTextfield.disable();
		$("#status")[0].parentElement.MaterialSelectfield.disable();
		$("#formHeading").html("Delete System User");
		FormTransition.openForm('#sysUserForm','#sysUserGrid');
	};
	clearDataForSysUser();
	findDetailByCodeForSysUser(username,_sF);
};

/*-------------------------------- Dynamic Event  ----------------------*/

var evenBinderForSysUser = ()=>{
	$("#btnSysUserAdd").off().on("click",function(){
		clickAddForSysUser();
	});


	$("#btnSave").off().on("click",function(){
		saveForSysUser();
	});
	
	$("#btnUpdate").off().on("click",function(){
		updateForSysUser();
	});
	
	$("#btnDelete").off().on("click",function(){
		deleteForSysUser();
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
	loadSysUserTable();
	evenBinderForSysUser();

});
