var authorityTable;
/*------------------------------------------- CRUD Functions ------------------*/

var generateFinalObjectForAuthority = ()=>{
	return {
		code:$("#code").val()||"",
		description:$("#description").val()||"",
		url:$("#url").val()||"",
		authCode: $("#authCode").val()||"",
		sectionCode:$("#section").val()||"",
		statusCode:$("#status").val()||""
	}
};

var successFunctionForAuthority = (data)=>{
	if(data.code === Constant.CODE_SUCCESS){
		DialogBox.openSuccessMsgBox(data.message);		
		authorityTable.ajax.reload();
		clearDataForAuthority();
	}
	else{
		alert(data.message);
	}
};

var failedFunctionForAuthority = (data)=>{
	alert("Server Error");
};

var validatorForAuthority = ()=>{
	let isValid = true;
	
	let code = $("#code");
	let description = $("#description");
	let url = $("#url");	
	let authCode = $("#authCode");	
	let section = $("#section");
	let status = $("#status");
	
	if(! code.val()){		
		//code.prop("required",true);		
		return isValid = false;
	}
	if(! description.val()){
		//description.prop("required",true);		
		return isValid = false;
	}
	if(! url.val()){
		//description.prop("required",true);		
		return isValid = false;
	}
	if(! authCode.val()){
		//description.prop("required",true);		
		return isValid = false;
	}
	if(! section.val()){
		//description.prop("required",true);		
		return isValid = false;
	}
	if(! status.val()){
		//status.prop("required",true);
		return isValid = false;
	}
	return isValid;
};


var saveForAuthority = ()=>{
	if(validatorForAuthority()){
		let url = "/authority/save";
		let method = "POST";		
		
		callToserver(url,method,generateFinalObjectForAuthority(),successFunctionForAuthority,failedFunctionForAuthority);
	}
	
};

var updateForAuthority = ()=>{
	if(validatorForAuthority()){
		let url = "/authority/update";
		let method = "POST";
				
		callToserver(url,method,generateFinalObjectForAuthority(),successFunctionForAuthority,failedFunctionForAuthority);
	}
};

var deleteForAuthority = ()=>{
	if(validatorForAuthority()){
		let url = "/authority/delete";
		let method = "POST";
		
		callToserver(url,method,generateFinalObjectForAuthority(),successFunctionForAuthority,failedFunctionForAuthority);
	}
};

var findDetailByCodeForAuthority = (code,callback)=>{
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
	let url = "/authority/loadAuthorityByCode";
	let method = "POST";
	callToserver(url,method,{code:code},successFunction,failedFunction);
	
};

/*-------------------------------- Reference Data , Data Table and Common --------------------*/

var populateFormForAuthority = (data) => {
	if(data){
		$("#code")[0].parentElement.MaterialTextfield.change(data.code || "");
		$("#description")[0].parentElement.MaterialTextfield.change(data.description || "");
		$("#url")[0].parentElement.MaterialTextfield.change(data.url || "");
		$("#authCode")[0].parentElement.MaterialTextfield.change(data.authCode || "");
		$("#section")[0].parentElement.MaterialSelectfield.change(data.sectionCode || "");
		$("#status")[0].parentElement.MaterialSelectfield.change(data.statusCode || "");
	}	
};

var loadReferenceDataForAuthority = (callback)=>{
	$.ajax({
        type: "POST",
        url: "/authority/loadRefDataForAuthority",        
        contentType: "application/json",
        dataType: "json",
        success: function(data){    
        	
        	if(data.code === "SUCCESS"){
            	for(let s of data.data.status||[]){            		
            		$("#status").append(`<option value="${s.code}">${s.description}</option>`);
            	}
            	for(let sc of data.data.sections||[]){            		
            		$("#section").append(`<option value="${sc.code}">${sc.description}</option>`);
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


var loadAuthorityTable = ()=>{
	authorityTable = $('#authorityTable').DataTable( {
                        ajax: {
                            url : "/authority/loadAuthorities",
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
                            { data: "code"                ,name:"code"          ,class:"mdl-data-table__cell--non-numeric"},
                            { data: "description"         ,name:"description"   ,class:"mdl-data-table__cell--non-numeric"},
                            { data: "url"                 ,name:"url"           ,class:"mdl-data-table__cell--non-numeric"},             
                            { data: "authCode"            ,name:"authCode"      ,class:"mdl-data-table__cell--non-numeric"},                            
                            { data: "sectionDescription"  ,name:"section"       ,class:"mdl-data-table__cell--non-numeric"},
                            { data: "statusDescription"   ,name:"status"        ,class:"mdl-data-table__cell--non-numeric"},
                            { data: "createdBy"           ,name:"createdBy"     ,class:"mdl-data-table__cell--non-numeric"},
                            { data: "createdOn"           ,name:"createdOn"     ,class:"mdl-data-table__cell--non-numeric"},
                            { data: "updatedBy"           ,name:"updatedBy"     ,class:"mdl-data-table__cell--non-numeric"},
                            { data: "updatedOn"           ,name:"updatedOn"     ,class:"mdl-data-table__cell--non-numeric"},
                            {
                            	data: "code",
                            	class:"mdl-data-table__cell--non-numeric",
                            	render: function (data, type, full) {
		                            		return `<button onClick="updateIconClickForAuthority('${data}')" class="mdl-button mdl-js-button mdl-button--icon mdl-button--colored">
													  <i id="icon-update-${data}" class="material-icons">create</i>
													  <div class="mdl-tooltip" data-mdl-for="icon-update-${data}">
														Update
													  </div>
													</button>
													<button onClick="deleteIconClickForAuthority('${data}')" class="mdl-button mdl-js-button mdl-button--icon mdl-button--colored">
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

var clearDataForAuthority = ()=>{
	let code = $("#code");
	let description = $("#description");
	let url = $("#url");
	let authCode = $("#authCode");
	let section = $("#section");
	let status = $("#status");
	
	$("#btnSave").show();
	$("#btnUpdate").hide();
	$("#btnDelete").hide();

	
	code[0].parentElement.MaterialTextfield.enable();
	description[0].parentElement.MaterialTextfield.enable();
	url[0].parentElement.MaterialTextfield.enable();
	authCode[0].parentElement.MaterialTextfield.enable();
	section[0].parentElement.MaterialSelectfield.enable();	
	status[0].parentElement.MaterialSelectfield.enable();	
	

	
	code[0].parentElement.MaterialTextfield.change("");
	description[0].parentElement.MaterialTextfield.change("");
	url[0].parentElement.MaterialTextfield.change("");
	authCode[0].parentElement.MaterialTextfield.change("");
	section.val("");
	section[0].parentElement.MaterialSelectfield.change("");
	status.val("");
	status[0].parentElement.MaterialSelectfield.change("");
	
};


/*-------------------------------- Inline Event  ----------------------*/

var updateIconClickForAuthority = (code)=>{
	let _sF = (data)=>{
		$("#btnSave").hide();
		$("#btnUpdate").show();
		$("#btnDelete").hide();
		populateFormForAuthority(data);
		$("#code")[0].parentElement.MaterialTextfield.disable();
	}	
	clearDataForAuthority();
	findDetailByCodeForAuthority(code,_sF);
};

var deleteIconClickForAuthority = (code)=>{
	let _sF = (data)=>{
		$("#btnSave").hide();
		$("#btnUpdate").hide();
		$("#btnDelete").show();
		populateFormForAuthority(data);
		$("#code")[0].parentElement.MaterialTextfield.disable();
		$("#description")[0].parentElement.MaterialTextfield.disable();
		$("#url")[0].parentElement.MaterialTextfield.disable();
		$("#authCode")[0].parentElement.MaterialTextfield.disable();
		$("#section")[0].parentElement.MaterialSelectfield.disable();
		$("#status")[0].parentElement.MaterialSelectfield.disable();
	}
	clearDataForAuthority();
	findDetailByCodeForAuthority(code,_sF);
};

/*-------------------------------- Dynamic Event  ----------------------*/

var evenBinderForAuthority = ()=>{
	$("#btnSave").off().on("click",function(){
		saveForAuthority();
	});
	
	$("#btnUpdate").off().on("click",function(){
		updateForAuthority();
	});
	
	$("#btnDelete").off().on("click",function(){
		deleteForAuthority();
	});
	
	$("#btnCancel").off().on("click",function(){
		clearDataForAuthority();
	});

};


/*-------------------------------- Document Ready ----------------------*/


$(document).ready(()=>{	
	
 	let _callback_1 = ()=>{
 		componentHandler.upgradeDom(); 	 		
	};	
	loadReferenceDataForAuthority(_callback_1);	 
	loadAuthorityTable();
	evenBinderForAuthority();

});