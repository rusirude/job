/**
 * @author: rusiru
 */

var userRoleAutorityTable;


/*-------------------------------- Reference Data , Data Table and Common --------------------*/

var loadReferenceDataForUserRoleAuthority = (callback)=>{
	$.ajax({
        type: "POST",
        url: "/sysRoleAuthority/loadRefDataForSysRoleAuthority",        
        contentType: "application/json",
        dataType: "json",
        success: function(data){    
        	
        	if(data.code === "SUCCESS"){
            	for(let s of data.data.sysRole){            		
            		$("#sysRole").append(`<option value="${s.code}">${s.description}</option>`);
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


var loadDataTableBySysRoleCodeForUserRoleAuthority = ()=>{
	userRoleAutorityTable = $('#sysRoleAuthorityTable').DataTable({
        ajax: {
            url: "/sysRoleAuthority/loadAuthoritiesForSysRole",
            type: 'POST',
            contentType: "application/json",
            data: function () {
                return JSON.stringify({code:($("#sysRole").val()||"")});
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
            {data: "authorityDescription", class:"mdl-data-table__cell--non-numeric"},
            {data: "sysRoleDescription", class:"mdl-data-table__cell--non-numeric"},
            {
            	data: null,
            	class:"mdl-data-table__cell--non-numeric",
            	render: function (data, type, full) {
            		return `<label class="mdl-switch mdl-js-switch mdl-js-ripple-effect" for="switch-${data.authorityCode}">
							  <input onChange="privilegeGrantOrRevorkForForUserRoleAuthority(this,'${data.sysRoleCode}','${data.authorityCode}')" type="checkbox" id="switch-${data.authorityCode}" class="mdl-switch__input" ${(data.enable)?"checked":""}>
							  <span class="mdl-switch__label"></span>
							</label>`;
            	}
    		}
        ]
	});
}

var successFunctionForUserRoleAuthority = (data)=>{
	if(data.code === Constant.CODE_FAIELD){
		
	}
};

var failedFunctionForUserRoleAuthority = (data)=>{
	alert("Server Error");
};

var savePrivilageForUserRoleAuthority = (sysRoleCode,authorityCode)=>{
	let url = "/sysRoleAuthority/save";
	let method = "POST";
	let data = {sysRoleCode:sysRoleCode,authorityCode:authorityCode}
	callToserver(url,method,data,successFunctionForUserRoleAuthority,failedFunctionForUserRoleAuthority);

}

var deletePrivilageForUserRoleAuthority = (sysRoleCode,authorityCode)=>{
	let url = "/sysRoleAuthority/delete";
	let method = "POST";
	let data = {sysRoleCode:sysRoleCode,authorityCode:authorityCode}
	callToserver(url,method,data,successFunctionForUserRoleAuthority,failedFunctionForUserRoleAuthority);

}

var clearDataForUserRoleAuthority = ()=>{

	let sysRole = $("#sysRole");
	sysRole.val("");
	sysRole[0].parentElement.MaterialSelectfield.change("");
	userRoleAutorityTable.ajax.reload();	
	
};


/*-------------------------------- Inline Event  ----------------------*/

function privilegeGrantOrRevorkForForUserRoleAuthority(ele,sysRoleCode,authorityCode){
	if($(ele).is(":checked")){
		savePrivilageForUserRoleAuthority(sysRoleCode,authorityCode);
	}
	else{
		deletePrivilageForUserRoleAuthority(sysRoleCode,authorityCode);
	}	
} 

/*-------------------------------- Dynamic Event  ----------------------*/

var evenBinderForUserRoleAuthority = ()=>{
	$("#sysRole").off().on("change",function(){
		userRoleAutorityTable.ajax.reload();		
	});
	$("#btnCancel").off().on("click",function(){
		clearDataForUserRoleAuthority();
	});
};
/*-------------------------------- Document Ready ----------------------*/

$(document).ready(()=>{	

 	let _callback_1 = ()=>{
 		componentHandler.upgradeDom(); 	
 		loadDataTableBySysRoleCodeForUserRoleAuthority();
	};	
	loadReferenceDataForUserRoleAuthority(_callback_1); 
	evenBinderForUserRoleAuthority();
	

});
