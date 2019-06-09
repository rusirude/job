/**
 * @author: rusiru
 */

var titleTable;

/*------------------------------------------- CRUD Functions ------------------*/

var generateFinalObjectForTitle = ()=>{
	return {
		code:$("#code").val()||"",
		description:$("#description").val()||"",
		statusCode:$("#status").val()||""
	}
};

var successFunctionForTitle = (data)=>{
	if(data.code === Constant.CODE_SUCCESS){
		DialogBox.openSuccessMsgBox(data.message);		
		titleTable.ajax.reload();
		clearDataForTitle();
	}
	else{
		alert(data.message);
	}
};

var failedFunctionForTitle = (data)=>{
	alert("Server Error");
};

var validatorForTitle = ()=>{
	let isValid = true;
	
	let code = $("#code");
	let description = $("#description");
	let status = $("#status");
	
	if(! code.val()){			
		code.prop("required",true);		
		isValid = false;
	}
	if(! description.val()){
		description.prop("required",true);		
		isValid = false;
	}
	if(! status.val()){
		status.prop("required",true);
		isValid = false;
	}
	code[0].parentElement.MaterialTextfield.change("");
	description[0].parentElement.MaterialTextfield.change("");	
	status[0].parentElement.MaterialSelectfield.change("");
	return isValid;
};


var saveForTitle = ()=>{
	if(validatorForTitle()){
		let url = "/title/save";
		let method = "POST";		
		
		callToserver(url,method,generateFinalObjectForTitle(),successFunctionForTitle,failedFunctionForTitle);
	}
	
};

var updateForTitle = ()=>{
	if(validatorForTitle()){
		let url = "/title/update";
		let method = "POST";
				
		callToserver(url,method,generateFinalObjectForTitle(),successFunctionForTitle,failedFunctionForTitle);
	}
};

var deleteForTitle = ()=>{
	if(validatorForTitle()){
		let url = "/title/delete";
		let method = "POST";
		
		callToserver(url,method,generateFinalObjectForTitle(),successFunctionForTitle,failedFunctionForTitle);
	}
};

var findDetailByCodeForTitle = (code,callback)=>{
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
	let url = "/title/loadTitleByCode";
	let method = "POST";
	callToserver(url,method,{code:code},successFunction,failedFunction);
	
};

/*-------------------------------- Reference Data , Data Table and Common --------------------*/

var populateFormForTitle = (data) => {
	if(data){
		$("#code")[0].parentElement.MaterialTextfield.change(data.code || "");
		$("#description")[0].parentElement.MaterialTextfield.change(data.description || "");
		$("#status")[0].parentElement.MaterialSelectfield.change(data.statusCode || "");
	}	
};

var loadReferenceDataForTitle = (callback)=>{
	$.ajax({
        type: "POST",
        url: "/title/loadRefDataForTitle",        
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

var loadTitleTable = ()=>{
	titleTable = $('#titleTable').DataTable( {
                        ajax: {
                            url : "/title/loadTitles",
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
                            { data: "statusDescription"   ,name:"status"        ,class:"mdl-data-table__cell--non-numeric"},
                            { data: "createdBy"           ,name:"createdBy"     ,class:"mdl-data-table__cell--non-numeric"},
                            { data: "createdOn"           ,name:"createdOn"     ,class:"mdl-data-table__cell--non-numeric"},
                            { data: "updatedBy"           ,name:"updatedBy"     ,class:"mdl-data-table__cell--non-numeric"},
                            { data: "updatedOn"           ,name:"updatedOn"     ,class:"mdl-data-table__cell--non-numeric"},
                            {
                            	data: "code",
                            	class:"mdl-data-table__cell--non-numeric",
                            	render: function (data, type, full) {
		                            		return `<button onClick="updateIconClickForTitle('${data}')" class="mdl-button mdl-js-button mdl-button--icon mdl-button--colored">
													  <i id="icon-update-${data}" class="material-icons">create</i>
													  <div class="mdl-tooltip" data-mdl-for="icon-update-${data}">
														Update
													  </div>
													</button>
													<button onClick="deleteIconClickForTitle('${data}')" class="mdl-button mdl-js-button mdl-button--icon mdl-button--colored">
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

var clearDataForTitle = ()=>{
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
	status.val("");
	status[0].parentElement.MaterialSelectfield.change("");
	
};

/*-------------------------------- Inline Event  ----------------------*/

var updateIconClickForTitle = (code)=>{
	let _sF = (data)=>{
		$("#btnSave").hide();
		$("#btnUpdate").show();
		$("#btnDelete").hide();
		populateFormForTitle(data);
		$("#code")[0].parentElement.MaterialTextfield.disable();
	}	
	clearDataForTitle();
	findDetailByCodeForTitle(code,_sF);
};

var deleteIconClickForTitle = (code)=>{
	let _sF = (data)=>{
		$("#btnSave").hide();
		$("#btnUpdate").hide();
		$("#btnDelete").show();
		populateFormForTitle(data);
		$("#code")[0].parentElement.MaterialTextfield.disable();
		$("#description")[0].parentElement.MaterialTextfield.disable();
		$("#status")[0].parentElement.MaterialSelectfield.disable();		
	}
	clearDataForTitle();
	findDetailByCodeForTitle(code,_sF);
};

/*-------------------------------- Dynamic Event  ----------------------*/

var evenBinderForTitle = ()=>{
	$("#btnSave").off().on("click",function(){
		saveForTitle();
	});
	
	$("#btnUpdate").off().on("click",function(){
		updateForTitle();
	});
	
	$("#btnDelete").off().on("click",function(){
		deleteForTitle();
	});
	
	$("#btnCancel").off().on("click",function(){
		clearDataForTitle();
	});

};

/*-------------------------------- Document Ready ----------------------*/


$(document).ready(()=>{	

 	let _callback_1 = ()=>{
 		componentHandler.upgradeDom(); 	 		
	};	
	loadReferenceDataForTitle(_callback_1);
	evenBinderForTitle();
	loadTitleTable();
	 

});