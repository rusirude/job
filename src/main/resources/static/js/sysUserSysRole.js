/**
 * @author: rusiru
 */

var sysUserSysRoleTable;


/*-------------------------------- Reference Data , Data Table and Common --------------------*/

var loadReferenceDataForSysUserSysRole = (callback)=>{
	$.ajax({
        type: "POST",
        url: "/sysUserSysRole/loadRefDataForSysUserSysRole",        
        contentType: "application/json",
        dataType: "json",
        success: function(data){    
        	
        	if(data.code === "SUCCESS"){
            	for(let s of data.data.sysUser){            		
            		$("#sysUser").append(`<option value="${s.code}">${s.description}</option>`);
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


var loadDataTableBySysUserUsernameForSysUserSysRole = ()=>{
	sysUserSysRoleTable = $('#sysUserSysRoleTable').DataTable({
        ajax: {
            url: "/sysUserSysRole/loadSysRolesForSysUser",
            type: 'POST',
            contentType: "application/json",
            data: function () {
                return JSON.stringify({username:($("#sysUser").val()||"")});
            },
            error: function (jqXHR, textStatus, errorThrown) {
            	alert("System Failer Occur....! :-(");
            }
        },
        scrollY: true,
        scrollX: true,
        scrollCollapse: true,
        drawCallback: function( settings ) {
        	componentHandler.upgradeDom();
        },
        pagingType: "full_numbers",
        columns: [
            {data: "sysRoleDescription", class:"mdl-data-table__cell--non-numeric"},
            {data: "username", class:"mdl-data-table__cell--non-numeric"},
            {
            	data: null,
            	class:"mdl-data-table__cell--non-numeric",
            	render: function (data, type, full) {
            		return `<label class="mdl-switch mdl-js-switch mdl-js-ripple-effect" for="switch-${data.sysRoleCode}">
							  <input onChange="privilegeGrantOrRevorkForForSysUserSysRole(this,'${data.username}','${data.sysRoleCode}')" type="checkbox" id="switch-${data.sysRoleCode}" class="mdl-switch__input" ${(data.enable)?"checked":""}>
							  <span class="mdl-switch__label"></span>
							</label>`;
            	}
    		}
        ]
	});
}

var successFunctionForSysUserSysRole = (data)=>{
	if(data.code === Constant.CODE_FAIELD){
		
	}
};

var failedFunctionForSysUserSysRole = (data)=>{
	alert("Server Error");
};

var savePrivilageForSysUserSysRole = (username,sysRoleCode)=>{
	let url = "/sysUserSysRole/save";
	let method = "POST";
	let data = {username:username,sysRoleCode:sysRoleCode}
	callToserver(url,method,data,successFunctionForSysUserSysRole,failedFunctionForSysUserSysRole);

}

var deletePrivilageForSysUserSysRole = (username,sysRoleCode)=>{
	let url = "/sysUserSysRole/delete";
	let method = "POST";
	let data = {username:username,sysRoleCode:sysRoleCode}
	callToserver(url,method,data,successFunctionForSysUserSysRole,failedFunctionForSysUserSysRole);

}

var clearDataForSysUserSysRole = ()=>{

	let sysUser = $("#sysUser");
	sysUser.val("");
	sysUser[0].parentElement.MaterialSelectfield.change("");
	sysUserSysRoleTable.ajax.reload();	
	
};


/*-------------------------------- Inline Event  ----------------------*/

function privilegeGrantOrRevorkForForSysUserSysRole(ele,username,sysRoleCode){
	if($(ele).is(":checked")){
		savePrivilageForSysUserSysRole(username,sysRoleCode);
	}
	else{
		deletePrivilageForSysUserSysRole(username,sysRoleCode);
	}	
} 

/*-------------------------------- Dynamic Event  ----------------------*/

var evenBinderForSysUserSysRole = ()=>{
	$("#sysUser").off().on("change",function(){
		sysUserSysRoleTable.ajax.reload();		
	});
	$("#btnCancel").off().on("click",function(){
		clearDataForSysUserSysRole();
	});
};
/*-------------------------------- Document Ready ----------------------*/

$(document).ready(()=>{	

 	let _callback_1 = ()=>{
 		componentHandler.upgradeDom(); 	
 		loadDataTableBySysUserUsernameForSysUserSysRole();
	};	
	loadReferenceDataForSysUserSysRole(_callback_1); 
	evenBinderForSysUserSysRole();
	

});
