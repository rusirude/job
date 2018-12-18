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


var loadDataTableForSysRoleCode = (sysRoleCode)=>{
	userRoleAutorityTable = $('#sysRoleAuthorityTable').DataTable({
        ajax: {
            url: "/sysRoleAuthority/loadAuthoritiesForSysRole",
            type: 'POST',
            contentType: "application/json",
            data: function () {
                return JSON.stringify({code:sysRoleCode});
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
            {data: "authorityDescription"},
            {data: "sysRoleDescription"},
            {
            	data: null,
            	render: function (data, type, full) {
            		return `<label class="mdl-switch mdl-js-switch mdl-js-ripple-effect" for="switch-${data.authorityCode}">
							  <input type="checkbox" id="switch-${data.authorityCode}" class="mdl-switch__input" ${(data.enable)?"checked":""}>
							  <span class="mdl-switch__label"></span>
							</label>`;
            	}
    		}
        ]
	});
}
/*-------------------------------- Document Ready ----------------------*/

$(document).ready(()=>{	

 	let _callback_1 = ()=>{
 		componentHandler.upgradeDom(); 	
 		loadDataTableForSysRoleCode("");
	};	
	loadReferenceDataForUserRoleAuthority(_callback_1); 
	

});
