/**
 * @author: rusiru
 */

var questionTable;

/*------------------------------------------- CRUD Functions ------------------*/

var generateFinalObjectForQuestion = ()=>{
	return {
		code:$("#code").val()||"",
		description:$("#description").val()||"",
		statusCode:$("#status").val()||""
	}
};

var successFunctionForQuestion = (data)=>{
	if(data.code === Constant.CODE_SUCCESS){
		FormTransition.closeForm('#questionForm','#questionGrid');
		DialogBox.openSuccessMsgBox(data.message);		
		questionTable.ajax.reload();
		clearDataForQuestion();
	}
	else{
		alert(data.message);
	}
};

var failedFunctionForQuestion = (data)=>{
	alert("Server Error");
};

var validatorForQuestion = ()=>{


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


var saveForQuestion = ()=>{
	if(validatorForQuestion()){
		let url = "/question/save";
		let method = "POST";		
		
		callToserver(url,method,generateFinalObjectForQuestion(),successFunctionForQuestion,failedFunctionForQuestion);
	}
	
};

var updateForQuestion = ()=>{
	if(validatorForQuestion()){
		let url = "/question/update";
		let method = "POST";
				
		callToserver(url,method,generateFinalObjectForQuestion(),successFunctionForQuestion,failedFunctionForQuestion);
	}
};

var deleteForQuestion = ()=>{
	if(validatorForQuestion()){
		let url = "/question/delete";
		let method = "POST";
		
		callToserver(url,method,generateFinalObjectForQuestion(),successFunctionForQuestion,failedFunctionForQuestion);
	}
};

var findDetailByCodeForQuestion = (code,callback)=>{
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
	let url = "/question/loadQuestionByCode";
	let method = "POST";
	callToserver(url,method,{code:code},successFunction,failedFunction);
	
};

/*-------------------------------- Reference Data , Data Table and Common --------------------*/

var populateFormForQuestion = (data) => {
	if(data){
		$("#code")[0].parentElement.MaterialTextfield.change(data.code || "");
		$("#description")[0].parentElement.MaterialTextfield.change(data.description || "");
		$("#status")[0].parentElement.MaterialSelectfield.change(data.statusCode || "");
	}	
};

var loadReferenceDataForQuestion = (callback)=>{
	$.ajax({
        type: "POST",
        url: "/question/loadRefDataForQuestion",
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

var loadQuestionTable = ()=>{
	questionTable = $('#questionTable').DataTable( {
                        ajax: {
                            url : "/question/loadQuestions",
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
		                            		return `<button onClick="updateIconClickForQuestion('${data}')" class="mdl-button mdl-js-button mdl-button--icon mdl-button--colored">
													  <i id="icon-update-${data}" class="material-icons">create</i>
													  <div class="mdl-tooltip" data-mdl-for="icon-update-${data}">
														Update
													  </div>
													</button>
													<button onClick="deleteIconClickForQuestion('${data}')" class="mdl-button mdl-js-button mdl-button--icon mdl-button--colored">
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

var clearDataForQuestion = ()=>{
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

	FormTransition.closeForm('#questionForm','#questionGrid');
	
};

/*-------------------------------- Inline Event  ----------------------*/

var clickAddForQuestion = ()=>{
	clearDataForQuestion();
	$("#formHeading").html("Add Question");
	FormTransition.openForm('#questionForm','#questionGrid');
};

var updateIconClickForQuestion = (code)=>{
	let _sF = (data)=>{
		$("#btnSave").hide();
		$("#btnUpdate").show();
		$("#btnDelete").hide();
		populateFormForQuestion(data);
		$("#code")[0].parentElement.MaterialTextfield.disable();
		$("#formHeading").html("Update Question");
		FormTransition.openForm('#questionForm','#questionGrid');
	};
	clearDataForQuestion();
	findDetailByCodeForQuestion(code,_sF);
};

var deleteIconClickForQuestion = (code)=>{
	let _sF = (data)=>{
		$("#btnSave").hide();
		$("#btnUpdate").hide();
		$("#btnDelete").show();
		populateFormForQuestion(data);
		$("#code")[0].parentElement.MaterialTextfield.disable();
		$("#description")[0].parentElement.MaterialTextfield.disable();
		$("#status")[0].parentElement.MaterialSelectfield.disable();
		$("#formHeading").html("Delete Question");
		FormTransition.openForm('#questionForm','#questionGrid');
	};
	clearDataForQuestion();
	findDetailByCodeForQuestion(code,_sF);
};

/*-------------------------------- Dynamic Event  ----------------------*/

var evenBinderForQuestion = ()=>{
	$("#btnQuestionAdd").off().on("click",function(){
		clickAddForQuestion();
	});

	$("#btnSave").off().on("click",function(){
		saveForQuestion();
	});
	
	$("#btnUpdate").off().on("click",function(){
		updateForQuestion();
	});
	
	$("#btnDelete").off().on("click",function(){
		deleteForQuestion();
	});
	
	$("#btnCancel").off().on("click",function(){
		clearDataForQuestion();
	});

};

/*-------------------------------- Document Ready ----------------------*/


$(document).ready(()=>{	

 	let _callback_1 = ()=>{
 		componentHandler.upgradeDom(); 	 		
	};
	componentHandler.upgradeDom();
	//loadReferenceDataForQuestion(_callback_1);
	evenBinderForQuestion();
	//loadQuestionTable();
	 

});