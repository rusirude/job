/**
 * @author: rusiru
 */

var sysUserAutorityTable;


/*-------------------------------- Reference Data , Data Table and Common --------------------*/

var loadReferenceDataForSysUserAuthority = (callback)=>{
	$.ajax({
        type: "POST",
        url: "/sysUserAuthority/loadRefDataForSysUserAuthority",        
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


var loadDataTableByUsernameForSysUserAuthority = ()=>{
	sysUserAutorityTable = $('#sysUserAuthorityTable').DataTable({
        ajax: {
            url: "/sysUserAuthority/loadAuthoritiesForSysUser",
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
            {data: "authorityDescription", class:"mdl-data-table__cell--non-numeric"},
            {data: "username", class:"mdl-data-table__cell--non-numeric"},
            {
            	data: null,
            	class:"mdl-data-table__cell--non-numeric",
            	render: function (data, type, full) {
            		return `<label class="mdl-switch mdl-js-switch mdl-js-ripple-effect" for="switch-${data.authorityCode}">
							  <input onChange="privilegeGrantOrRevorkForForSysUserAuthority(this,'${data.username}','${data.authorityCode}')" type="checkbox" id="switch-${data.authorityCode}" class="mdl-switch__input" ${(data.enable)?"checked":""}>
							  <span class="mdl-switch__label"></span>
							</label>`;
            	}
    		}
        ]
	});
}

var successFunctionForSysUserAuthority = (data)=>{
	if(data.code === Constant.CODE_FAIELD){
		
	}
};

var failedFunctionForSysUserAuthority = (data)=>{
	alert("Server Error");
};

var savePrivilageForSysUserAuthority = (username,authorityCode)=>{
	let url = "/sysUserAuthority/save";
	let method = "POST";
	let data = {username:username,authorityCode:authorityCode}
	callToserver(url,method,data,successFunctionForSysUserAuthority,failedFunctionForSysUserAuthority);

}

var deletePrivilageForSysUserAuthority = (username,authorityCode)=>{
	let url = "/sysUserAuthority/delete";
	let method = "POST";
	let data = {username:username,authorityCode:authorityCode}
	callToserver(url,method,data,successFunctionForSysUserAuthority,failedFunctionForSysUserAuthority);

}

var clearDataForSysUserAuthority = ()=>{

	let sysUser = $("#sysUser");
	sysUser.val("");
	sysUser[0].parentElement.MaterialSelectfield.change("");
	sysUserAutorityTable.ajax.reload();	
	
};


/*-------------------------------- Inline Event  ----------------------*/

function privilegeGrantOrRevorkForForSysUserAuthority(ele,username,authorityCode){
	if($(ele).is(":checked")){
		savePrivilageForSysUserAuthority(username,authorityCode);
	}
	else{
		deletePrivilageForSysUserAuthority(username,authorityCode);
	}	
} 

/*-------------------------------- Dynamic Event  ----------------------*/

var evenBinderForSysUserAuthority = ()=>{
	$("#sysUser").off().on("change",function(){
		sysUserAutorityTable.ajax.reload();		
	});
	$("#btnCancel").off().on("click",function(){
		clearDataForSysUserAuthority();
	});
};
/*-------------------------------- Document Ready ----------------------*/

$(document).ready(()=>{	

 	let _callback_1 = ()=>{
 		componentHandler.upgradeDom(); 	
 		loadDataTableByUsernameForSysUserAuthority();
	};	
	loadReferenceDataForSysUserAuthority(_callback_1); 
	evenBinderForSysUserAuthority();
	

});
