/**
 * @author: rusiru
 */

var sectionTable;

/*------------------------------------------- CRUD Functions ------------------*/

var generateFinalObjectForSection = ()=>{
	return {
		code:$("#code").val()||"",
		description:$("#description").val()||"",
		statusCode:$("#status").val()||""
	}
};

var successFunctionForSection = (data)=>{
	if(data.code === Constant.CODE_SUCCESS){
		DialogBox.openSuccessMsgBox(data.message);		
		sectionTable.ajax.reload();
		clearDataForSection();
	}
	else{
		alert(data.message);
	}
};

var failedFunctionForSection = (data)=>{
	alert("Server Error");
};

var validatorForSection = ()=>{
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


var saveForSection = ()=>{
	if(validatorForSection()){
		let url = "/section/save";
		let method = "POST";		
		
		callToserver(url,method,generateFinalObjectForSection(),successFunctionForSection,failedFunctionForSection);
	}
	
};

var updateForSection = ()=>{
	if(validatorForSection()){
		let url = "/section/update";
		let method = "POST";
				
		callToserver(url,method,generateFinalObjectForSection(),successFunctionForSection,failedFunctionForSection);
	}
};

var deleteForSection = ()=>{
	if(validatorForSection()){
		let url = "/section/delete";
		let method = "POST";
		
		callToserver(url,method,generateFinalObjectForSection(),successFunctionForSection,failedFunctionForSection);
	}
};

var findDetailByCodeForSection = (code,callback)=>{
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
	let url = "/section/loadSectionByCode";
	let method = "POST";
	callToserver(url,method,{code:code},successFunction,failedFunction);
	
};

/*-------------------------------- Reference Data , Data Table and Common --------------------*/

var populateFormForSection = (data) => {
	if(data){
		$("#code")[0].parentElement.MaterialTextfield.change(data.code || "");
		$("#description")[0].parentElement.MaterialTextfield.change(data.description || "");
		$("#status")[0].parentElement.MaterialSelectfield.change(data.statusCode || "");
	}	
};

var loadReferenceDataForSection = (callback)=>{
	$.ajax({
        type: "POST",
        url: "/section/loadRefDataForSection",        
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

var loadSectionTable = ()=>{
	sectionTable = $('#sectionTable').DataTable( {
                        ajax: {
                            url : "/section/loadSections",
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
		                            		return `<button onClick="updateIconClickForSection('${data}')" class="mdl-button mdl-js-button mdl-button--icon mdl-button--colored">
													  <i id="icon-update-${data}" class="material-icons">create</i>
													  <div class="mdl-tooltip" data-mdl-for="icon-update-${data}">
														Update
													  </div>
													</button>
													<button onClick="deleteIconClickForSection('${data}')" class="mdl-button mdl-js-button mdl-button--icon mdl-button--colored">
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

var clearDataForSection = ()=>{
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

	FormTransition.closeForm('#sectionForm','#sectionGrid');
	
};

/*-------------------------------- Inline Event  ----------------------*/
var clickAddForSection = ()=>{
	clearDataForSection();
	$("#formHeading").html("Add Section");
	FormTransition.openForm('#sectionForm','#sectionGrid');
};

var updateIconClickForSection = (code)=>{
	let _sF = (data)=>{
		$("#btnSave").hide();
		$("#btnUpdate").show();
		$("#btnDelete").hide();
		populateFormForSection(data);
		$("#code")[0].parentElement.MaterialTextfield.disable();
		$("#formHeading").html("Update Section");
		FormTransition.openForm('#sectionForm','#sectionGrid');
	};
	clearDataForSection();
	findDetailByCodeForSection(code,_sF);
};

var deleteIconClickForSection = (code)=>{
	let _sF = (data)=>{
		$("#btnSave").hide();
		$("#btnUpdate").hide();
		$("#btnDelete").show();
		populateFormForSection(data);
		$("#code")[0].parentElement.MaterialTextfield.disable();
		$("#description")[0].parentElement.MaterialTextfield.disable();
		$("#status")[0].parentElement.MaterialSelectfield.disable();
		$("#formHeading").html("Delete Section");
		FormTransition.openForm('#sectionForm','#sectionGrid');
	};
	clearDataForSection();
	findDetailByCodeForSection(code,_sF);
};

/*-------------------------------- Dynamic Event  ----------------------*/

var evenBinderForSection = ()=>{
	$("#btnSectionAdd").off().on("click",function(){
		clickAddForSection();
	});

	$("#btnSave").off().on("click",function(){
		saveForSection();
	});
	
	$("#btnUpdate").off().on("click",function(){
		updateForSection();
	});
	
	$("#btnDelete").off().on("click",function(){
		deleteForSection();
	});
	
	$("#btnCancel").off().on("click",function(){
		clearDataForSection();
	});

};

/*-------------------------------- Document Ready ----------------------*/


$(document).ready(()=>{	

 	let _callback_1 = ()=>{
 		componentHandler.upgradeDom(); 	 		
	};	
	loadReferenceDataForSection(_callback_1);
	evenBinderForSection();
	loadSectionTable();
	 

});