var examinationTable;
/*------------------------------------------- CRUD Functions ------------------*/

var generateFinalObjectForExamination = ()=>{
	return {
		code:$("#code").val()||"",
		description:$("#description").val()||"",
		questionCategoryCode: $("#questionCategory").val()||"",
		noQuestion: $("#noQuestion").val()||"",
		duration: $("#duration").val()||"",
		statusCode:$("#status").val()||"",
		effectiveOn:$("#effectiveOn").val()||"",
		expireOn:$("#expireOn").val()||"",
	}
};

var successFunctionForExamination = (data)=>{
	if(data.code === Constant.CODE_SUCCESS){
		DialogBox.openMsgBox(data.message,'success');
		examinationTable.ajax.reload();
		clearDataForExamination();
	}
	else{
		DialogBox.openMsgBox(data.message,'error');
	}
};

var failedFunctionForExamination = (data)=>{
	DialogBox.openMsgBox("Server Error",'error');
};

var validatorForExamination = ()=>{
	let isValid = true;
	console.log("k");
	let code = $("#code");
	let description = $("#description");
	let noQuestion = $("#noQuestion");
	let duration = $("#duration");
	let questionCategory = $("#questionCategory");
	let status = $("#status");
	let effectiveOn = $("#effectiveOn");
	let expireOn = $("#expireOn");

	if(! code.val()){
		InputsValidator.inlineEmptyValidation(code);
		isValid = false;
	}
	if(! description.val()){
		InputsValidator.inlineEmptyValidation(description);
		isValid = false;
	}
	if(! parseInt(noQuestion.val())){
		InputsValidator.inlineEmptyValidation(noQuestion);
		isValid = false;
	}
	if(! duration.val()){
		InputsValidator.inlineEmptyValidation(duration);
		isValid = false;
	}
	if(! effectiveOn.val()){
		InputsValidator.inlineEmptyValidation(effectiveOn);
		isValid = false;
	}
	if(! expireOn.val()){
		InputsValidator.inlineEmptyValidation(expireOn);
		isValid = false;
	}
	if(! questionCategory.val()){
		InputsValidator.inlineEmptyValidationSelect(questionCategory);
		isValid = false;
	}
	if(! status.val()){
		InputsValidator.inlineEmptyValidationSelect(status);
		isValid = false;
	}
	return isValid;
};


var saveForExamination = ()=>{
	if(validatorForExamination()){
		let url = "/examination/save";
		let method = "POST";

		callToserver(url,method,generateFinalObjectForExamination(),successFunctionForExamination,failedFunctionForExamination);
	}

};

var updateForExamination = ()=>{
	if(validatorForExamination()){
		let url = "/examination/update";
		let method = "POST";

		callToserver(url,method,generateFinalObjectForExamination(),successFunctionForExamination,failedFunctionForExamination);
	}
};

var deleteForExamination = ()=>{
	if(validatorForExamination()){
		let url = "/examination/delete";
		let method = "POST";

		callToserver(url,method,generateFinalObjectForExamination(),successFunctionForExamination,failedFunctionForExamination);
	}
};

var findDetailByCodeForExamination = (code,callback)=>{
	let successFunction = (data)=>{
		if(data.code === Constant.CODE_SUCCESS){
			if(callback){
				callback(data.data);
			}
		}
		else{
			DialogBox.openMsgBox(data.message,'error');
		}
	};
	let failedFunction = (data)=>{
		DialogBox.openMsgBox("Server Error",'error');
	};
	let url = "/examination/loadExaminationByCode";
	let method = "POST";
	callToserver(url,method,{code:code},successFunction,failedFunction);

};

/*-------------------------------- Reference Data , Data Table and Common --------------------*/

var populateFormForExamination = (data) => {
	if(data){
		$("#code").val(data.code || "");
		$("#description").val(data.description || "");
		$("#noQuestion").val(data.noQuestion || 0);
		$("#duration").val(data.duration || "");
		$("#questionCategory").val(data.questionCategoryCode || "");
		$("#status").val(data.statusCode || "");
		$("#effectiveOn").val(data.effectiveOn || "");
		$("#expireOn").val(data.expireOn || "");
	}
};

var loadReferenceDataForExamination = (callback)=>{
	$.ajax({
        type: "POST",
        url: "/examination/loadRefDataForExamination",
        contentType: "application/json",
        dataType: "json",
        success: function(data){

        	if(data.code === Constant.CODE_SUCCESS){
            	for(let s of data.data.status||[]){
            		$("#status").append(`<option value="${s.code}">${s.description}</option>`);
            	}
            	for(let sc of data.data.questionCategory||[]){
            		$("#questionCategory").append(`<option value="${sc.code}">${sc.description}</option>`);
            	}

            	if(callback){
            		callback();
            	}
        	}
        	else{
				DialogBox.openMsgBox("System Failer Occur....! :-(",'error');
			}


    	},
        failure: function(errMsg) {
			DialogBox.openMsgBox(errMsg,'error');
        }
  });
};


var loadExaminationTable = ()=>{
	examinationTable = $('#examinationTable').DataTable( {
                        ajax: {
                            url : "/examination/loadExaminations",
                            contentType:"application/json",
                            type:"POST",
                            data:function(d){
                                return JSON.stringify(createCommonDataTableRequset(d));
                    		}
                        },
						paging: true,
						lengthChange: false,
						searching: true,
						ordering: true,
						info: true,
						autoWidth: false,
                        processing: true,
                        serverSide: true,
						scrollX:        true,
                        columns: [
                            { data: "code"                           ,name:"code"            },
                            { data: "description"         			 ,name:"description"     },
                            { data: "questionCategoryDescription"    ,name:"questionCategory"},
                            // { data: "effectiveOn"                    ,name:"effectiveOn"     },
                            // { data: "expireOn"                       ,name:"expireOn"        },
                            { data: "statusDescription"              ,name:"status"          },
                            { data: "createdBy"                      ,name:"createdBy"       },
                            { data: "createdOn"                      ,name:"createdOn"       },
                            { data: "updatedBy"                      ,name:"updatedBy"       },
                            { data: "updatedOn"                      ,name:"updatedOn"       },
                            {
                            	data: "code",
                            	render: function (data, type, full) {
											return `<button onClick="updateIconClickForExamination('${data}')" type="button" class="btn btn-outline-primary btn-sm" data-toggle="tooltip" data-placement="bottom" title="Update">
														<i class="fa fa-pencil-alt"></i>
													</button>
													<button onClick="deleteIconClickForExamination('${data}')" type="button" class="btn btn-outline-danger btn-sm" data-toggle="tooltip" data-placement="bottom" title="Delete">
														<i class="fa fa-trash-alt"></i>
													</button>`;
		                            	}
                    		}
                        ]
                    } );
};

var clearDataForExamination = ()=>{
	let code = $("#code");
	let description = $("#description");
	let questionCategory = $("#questionCategory");
	let noQuestion = $("#noQuestion");
	let duration = $("#duration");
	let status = $("#status");
	let effectiveOn = $("#effectiveOn");
	let expireOn = $("#expireOn");

	$("#btnSave").show();
	$("#btnUpdate").hide();
	$("#btnDelete").hide();

	$("#formHeading").html("");

	code.prop("disabled",false);
	description.prop("disabled",false);
	questionCategory.prop("disabled",false);
	noQuestion.prop("disabled",false);
	duration.prop("disabled",false);
	status.prop("disabled",false);
	effectiveOn.prop("disabled",false);
	expireOn.prop("disabled",false);


	InputsValidator.removeInlineValidation(code);
	InputsValidator.removeInlineValidation(description);
	InputsValidator.removeInlineValidation(questionCategory);
	InputsValidator.removeInlineValidation(noQuestion);
	InputsValidator.removeInlineValidation(duration);
	InputsValidator.removeInlineValidation(status);
	InputsValidator.removeInlineValidation(effectiveOn);
	InputsValidator.removeInlineValidation(expireOn);

	code.val("");
	description.val("");
	questionCategory.val("");
	noQuestion.val(0);
	duration.val("");
	status.val("");
	effectiveOn.val("");
	expireOn.val("");

	FormTransition.closeModal('#examinationModal');

};


/*-------------------------------- Inline Event  ----------------------*/
var clickAddForExamination = ()=>{
	clearDataForExamination();
	$("#formHeading").html("Add Examination");
	FormTransition.openModal('#examinationModal');
};

var updateIconClickForExamination = (code)=>{
	let _sF = (data)=>{
		$("#btnSave").hide();
		$("#btnUpdate").show();
		$("#btnDelete").hide();
		populateFormForExamination(data);
		$("#code").prop("disabled",true);
		$("#formHeading").html("Update Examination");
		FormTransition.openModal('#examinationModal');
	};
	clearDataForExamination();
	findDetailByCodeForExamination(code,_sF);
};

var deleteIconClickForExamination = (code)=>{
	let _sF = (data)=>{
		$("#btnSave").hide();
		$("#btnUpdate").hide();
		$("#btnDelete").show();
		populateFormForExamination(data);
		$("#code").prop("disabled",true);
		$("#description").prop("disabled",true);
		$("#questionCategory").prop("disabled",true);
		$("#noQuestion").prop("disabled",true);
		$("#duration").prop("disabled",true);
		$("#status").prop("disabled",true);
		$("#effectiveOn").prop("disabled",true);
		$("#expireOn").prop("disabled",true);
		$("#formHeading").html("Delete Examination");
		FormTransition.openModal('#examinationModal');
	};
	clearDataForExamination();
	findDetailByCodeForExamination(code,_sF);
};

/*-------------------------------- Dynamic Event  ----------------------*/

var evenBinderForExamination = ()=>{
	$("#btnExaminationAdd").off().on("click",function(){
		clickAddForExamination();
	});

	$("#btnSave").off().on("click",function(){
		saveForExamination();
	});

	$("#btnUpdate").off().on("click",function(){
		updateForExamination();
	});

	$("#btnDelete").off().on("click",function(){
		deleteForExamination();
	});

	$("#btnCancel").off().on("click",function(){
		clearDataForExamination();
	});

};


/*-------------------------------- Document Ready ----------------------*/


$(document).ready(()=>{
	$('#expireOnDiv').datetimepicker({ icons: { time: 'far fa-clock' } });
	$('#effectiveOnDiv').datetimepicker({ icons: { time: 'far fa-clock' } });
	loadReferenceDataForExamination();
	loadExaminationTable();
	evenBinderForExamination();
});