/**
 * @author: rusiru
 */

var questionCategoryTable;

/*------------------------------------------- CRUD Functions ------------------*/

var generateFinalObjectForQuestionCategory = ()=>{
	return {
		code:$("#code").val()||"",
		description:$("#description").val()||"",
		statusCode:$("#status").val()||""
	}
};

var successFunctionForQuestionCategory = (data)=>{
	if(data.code === Constant.CODE_SUCCESS){
		FormTransition.closeForm('#questionCategoryForm','#questionCategoryGrid');
		DialogBox.openSuccessMsgBox(data.message);		
		questionCategoryTable.ajax.reload();
		clearDataForQuestionCategory();
	}
	else{
		alert(data.message);
	}
};

var failedFunctionForQuestionCategory = (data)=>{
	alert("Server Error");
};

var validatorForQuestionCategory = ()=>{


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


var saveForQuestionCategory = ()=>{
	if(validatorForQuestionCategory()){
		let url = "/questionCategory/save";
		let method = "POST";		
		
		callToserver(url,method,generateFinalObjectForQuestionCategory(),successFunctionForQuestionCategory,failedFunctionForQuestionCategory);
	}
	
};

var updateForQuestionCategory = ()=>{
	if(validatorForQuestionCategory()){
		let url = "/questionCategory/update";
		let method = "POST";
				
		callToserver(url,method,generateFinalObjectForQuestionCategory(),successFunctionForQuestionCategory,failedFunctionForQuestionCategory);
	}
};

var deleteForQuestionCategory = ()=>{
	if(validatorForQuestionCategory()){
		let url = "/questionCategory/delete";
		let method = "POST";
		
		callToserver(url,method,generateFinalObjectForQuestionCategory(),successFunctionForQuestionCategory,failedFunctionForQuestionCategory);
	}
};

var findDetailByCodeForQuestionCategory = (code,callback)=>{
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
	let url = "/questionCategory/loadQuestionCategoryByCode";
	let method = "POST";
	callToserver(url,method,{code:code},successFunction,failedFunction);
	
};

/*-------------------------------- Reference Data , Data Table and Common --------------------*/

var populateFormForQuestionCategory = (data) => {
	if(data){
		$("#code")[0].parentElement.MaterialTextfield.change(data.code || "");
		$("#description")[0].parentElement.MaterialTextfield.change(data.description || "");
		$("#status")[0].parentElement.MaterialSelectfield.change(data.statusCode || "");
	}	
};

var loadReferenceDataForQuestionCategory = (callback)=>{
	$.ajax({
        type: "POST",
        url: "/questionCategory/loadRefDataForQuestionCategory",
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

var loadQuestionCategoryTable = ()=>{
	questionCategoryTable = $('#questionCategoryTable').DataTable( {
                        ajax: {
                            url : "/questionCategory/loadQuestionCategories",
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
		                            		return `<button onClick="updateIconClickForQuestionCategory('${data}')" class="mdl-button mdl-js-button mdl-button--icon mdl-button--colored">
													  <i id="icon-update-${data}" class="material-icons">create</i>
													  <div class="mdl-tooltip" data-mdl-for="icon-update-${data}">
														Update
													  </div>
													</button>
													<button onClick="deleteIconClickForQuestionCategory('${data}')" class="mdl-button mdl-js-button mdl-button--icon mdl-button--colored">
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

var clearDataForQuestionCategory = ()=>{
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
	

	
	code[0].parentElement.MaterialTextfield.change("");
	description[0].parentElement.MaterialTextfield.change("");
	status.val("");
	status[0].parentElement.MaterialSelectfield.change("");

	InputsValidator.removeInlineValidation(code);
	InputsValidator.removeInlineValidation(description);
	InputsValidator.removeInlineValidation(status);

	FormTransition.closeForm('#questionCategoryForm','#questionCategoryGrid');
	
};

/*-------------------------------- Inline Event  ----------------------*/

var clickAddForQuestionCategory = ()=>{
	clearDataForQuestionCategory();
	$("#formHeading").html("Add QuestionCategory");
	FormTransition.openForm('#questionCategoryForm','#questionCategoryGrid');
};

var updateIconClickForQuestionCategory = (code)=>{
	let _sF = (data)=>{
		$("#btnSave").hide();
		$("#btnUpdate").show();
		$("#btnDelete").hide();
		populateFormForQuestionCategory(data);
		$("#code")[0].parentElement.MaterialTextfield.disable();
		$("#formHeading").html("Update QuestionCategory");
		FormTransition.openForm('#questionCategoryForm','#questionCategoryGrid');
	};
	clearDataForQuestionCategory();
	findDetailByCodeForQuestionCategory(code,_sF);
};

var deleteIconClickForQuestionCategory = (code)=>{
	let _sF = (data)=>{
		$("#btnSave").hide();
		$("#btnUpdate").hide();
		$("#btnDelete").show();
		populateFormForQuestionCategory(data);
		$("#code")[0].parentElement.MaterialTextfield.disable();
		$("#description")[0].parentElement.MaterialTextfield.disable();
		$("#status")[0].parentElement.MaterialSelectfield.disable();
		$("#formHeading").html("Delete QuestionCategory");
		FormTransition.openForm('#questionCategoryForm','#questionCategoryGrid');
	};
	clearDataForQuestionCategory();
	findDetailByCodeForQuestionCategory(code,_sF);
};

/*-------------------------------- Dynamic Event  ----------------------*/

var evenBinderForQuestionCategory = ()=>{
	$("#btnQuestionCategoryAdd").off().on("click",function(){
		clickAddForQuestionCategory();
	});

	$("#btnSave").off().on("click",function(){
		saveForQuestionCategory();
	});
	
	$("#btnUpdate").off().on("click",function(){
		updateForQuestionCategory();
	});
	
	$("#btnDelete").off().on("click",function(){
		deleteForQuestionCategory();
	});
	
	$("#btnCancel").off().on("click",function(){
		clearDataForQuestionCategory();
	});

};

/*-------------------------------- Document Ready ----------------------*/


$(document).ready(()=>{	

 	let _callback_1 = ()=>{
 		componentHandler.upgradeDom(); 	 		
	};	
	loadReferenceDataForQuestionCategory(_callback_1);
	evenBinderForQuestionCategory();
	loadQuestionCategoryTable();
	 

});