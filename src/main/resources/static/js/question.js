/**
 * @author: rusiru
 */
var stepper;
var rowCount = 0;
var questionTable;



var rowCreator = ()=>{
	rowCount++;
	return `<div class="row body">
						<div class="form-group col-sm-9">
							<input type="hidden" name="answers_${rowCount}"/>
							<input type="text" name="answers_${rowCount}" class="form-control form-control-sm" id="answers_${rowCount}" aria-describedby="answers_${rowCount}-error" aria-invalid="false"/>
							<span id="answers_${rowCount}-error" class="error invalid-feedback"></span>
						</div>
						<div class="form-group col-sm-1">
							<div class="icheck-primary d-inline">
								<input type="radio" id="correct_${rowCount}" name="correct"/>
								<label for="correct_${rowCount}">
								</label>
							</div>
						</div>
						<div class="form-group col-sm-2">
							<button onclick="addRow(this)" type="button" class="btn  bg-gradient-primary btn-xs">
								<i class="fa fa-plus"></i>
							</button>
							<button onclick="removeRow(this)" type="button" class="btn  bg-gradient-primary btn-xs">
								<i class="fa fa-minus"></i>
							</button>
						</div>
					</div>`;
};

function addRow(ele){
	let subLink = $(ele).parent().parent();
	subLink.after(rowCreator());
}
function removeRow(ele){
	$("#answerSection")
		.find('.row.body:first')
		.find('button:has(i.fa-minus)').remove();
	$(ele).parent().parent().remove();
}

/*------------------------------------------- CRUD Functions ------------------*/

var generateFinalObjectForQuestion = ()=>{
	return {
		code:$("#code").val()||"",
		statusCode:$("#status").val()||"",
		description:$("#description").val()||"",
		questionCategories: $("#questionCategory").val().map(x=> ({code:x})),
		authCode: $("#authCode").val()||"",
		questionAnswers: generateAnswerObjects(),
	}
};

var generateAnswerObjects = ()=>{
	let _r = [];
	let i =1;
	for(let ele of $("#answerSection").find('.row.body')){
		let o = {};
		o.id = $(ele).find('input[type=hidden]').val()||0;
		o.description = $(ele).find('input[type=text]').val()||'';
		o.position = i++;
		o.correct = $(ele).find('input[type=radio]').is(':checked');
		_r.push(o);
	}
	return _r;
};

var successFunctionForQuestion = (data)=>{
	if(data.code === Constant.CODE_SUCCESS){
		DialogBox.openMsgBox(data.message,'success');
		$("#questionFormDiv").hide();
		$("#questionTableDiv").show();
		questionTable.ajax.reload();
		clearDataForQuestion();
	}
	else{
		DialogBox.openMsgBox(data.message,'error');
	}
};

var failedFunctionForQuestion = (data)=>{
	DialogBox.openMsgBox("Server Error",'error');
};

var validatorForQuestion = ()=>{
	let isValid = true;

	// let code = $("#code");
	// let description = $("#description");
	// let url = $("#url");
	// let authCode = $("#authCode");
	// let section = $("#section");
	// let status = $("#status");
	//
	// if(! code.val()){
	// 	InputsValidator.inlineEmptyValidation(code);
	// 	isValid = false;
	// }
	// if(! description.val()){
	// 	InputsValidator.inlineEmptyValidation(description);
	// 	isValid = false;
	// }
	// if(! url.val()){
	// 	InputsValidator.inlineEmptyValidation(url);
	// 	isValid = false;
	// }
	// if(! authCode.val()){
	// 	InputsValidator.inlineEmptyValidation(authCode);
	// 	isValid = false;
	// }
	// if(! section.val()){
	// 	InputsValidator.inlineEmptyValidationSelect(section);
	// 	isValid = false;
	// }
	// if(! status.val()){
	// 	InputsValidator.inlineEmptyValidationSelect(status);
	// 	isValid = false;
	// }
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
			}
		}
		else{
			DialogBox.openMsgBox(data.message,'error');
		}
	};
	let failedFunction = (data)=>{
		DialogBox.openMsgBox("Server Error",'error');
	};
	let url = "/question/loadQuestionByCode";
	let method = "POST";
	callToserver(url,method,{code:code},successFunction,failedFunction);

};
/*-------------------------------- Reference Data , Data Table and Common --------------------*/
var populateFormForQuestion = (data) => {
	if(data){
		$("#code").val(data.code || "");
		$("#description").val(data.description || "");
		$("#status").val(data.statusCode || "");
		let cat = data.questionCategories.map(x=>(x.code));
		$("#questionCategory").val(cat);
		$("#questionCategory").select2();
		console.log('ddd');
		if((data.questionAnswers||[]).length){
			rowCount = 0;
			$("#answerSection")
				.find('.row.body').remove();

			for(let e of data.questionAnswers){
				let row = $(rowCreator());
				row.find("input[type=hidden]").val(e.id);
				row.find("input[type=text]").val(e.description);
				row.find("input[type=radio]").prop('checked',e.correct);
				$("#answerSection").append(row);
			}
		}


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
			{ data: "code"                ,name:"code"          },
			{ data: "description"         ,name:"description"   },
			{ data: "statusDescription"   ,name:"status"        },
			{ data: "createdBy"           ,name:"createdBy"     },
			{ data: "createdOn"           ,name:"createdOn"     },
			{
				data: "code",
				render: function (data, type, full) {
					return `<button onClick="updateIconClickForQuestion('${data}')" type="button" class="btn btn-outline-primary btn-sm" data-toggle="tooltip" data-placement="bottom" title="Update">
														<i class="fa fa-pencil-alt"></i>
													</button>
													<button onClick="deleteIconClickForQuestion('${data}')" type="button" class="btn btn-outline-danger btn-sm" data-toggle="tooltip" data-placement="bottom" title="Delete">
														<i class="fa fa-trash-alt"></i>
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
	 let questionCategory =$("#questionCategory");

	$("#btnSave").show();
	$("#btnUpdate").hide();
	$("#btnDelete").hide();

	code.prop("disabled",false);
	description.prop("disabled",false);
    status.prop("disabled",false);
    questionCategory.prop("disabled",false);

    rowCount = 0;
	$("#answerSection")
		.find('.row.body').remove();

	$("#answerSection").append(rowCreator());

	InputsValidator.removeInlineValidation(code);
	InputsValidator.removeInlineValidation(description);
	InputsValidator.removeInlineValidation(status);

	code.val("");
	description.val("");
	status.val("");
	questionCategory.val([]);
	questionCategory.select2();
	stepper.previous();
	stepper.previous();

	// FormTransition.closeModal('#questionModal');

};

/*-------------------------------- Inline Event  ----------------------*/
var clickAddForQuestion = ()=>{
	clearDataForQuestion();
	$("#formHeading").html("Add Question");
	$("#questionTableDiv").hide();
	$("#questionFormDiv").show();
};

var updateIconClickForQuestion = (code)=>{
	let _sF = (data)=>{
		$("#btnSave").hide();
		$("#btnUpdate").show();
		$("#btnDelete").hide();
		populateFormForQuestion(data);
		$("#code").prop("disabled",true);
		$("#formHeading").html("Update Question");
		$("#questionTableDiv").hide();
		$("#questionFormDiv").show();
	};
	clearDataForQuestion();
	findDetailByCodeForQuestion(code,_sF);
};

var deleteIconClickForQuestion = (code)=>{
	console.log("ffffff");
	let _sF = (data)=>{
		console.log("ddd")
		$("#btnSave").hide();
		$("#btnUpdate").hide();
		$("#btnDelete").show();
		populateFormForQuestion(data);
		$("#code").prop("disabled",true);
		$("#description").prop("disabled",true);
		$("#url").prop("disabled",true);
		$("#authCode").prop("disabled",true);
		$("#status").prop("disabled",true);
		$("#section").prop("disabled",true);
		$("#formHeading").html("Delete Question");
		$("#questionTableDiv").hide();
		$("#questionFormDiv").show();
	};
	clearDataForQuestion();
	findDetailByCodeForQuestion(code,_sF);
};

var cancelForm = ()=>{
	clearDataForQuestion();
	$("#questionFormDiv").hide();
	$("#questionTableDiv").show();
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


};


/*-------------------------------- Document Ready ----------------------*/
$(document).ready(()=>{
	let _callback_1 = ()=>{
		stepper = new Stepper($('.bs-stepper')[0]);
		$('#questionCategory').select2();
	};
	loadReferenceDataForQuestion(_callback_1);
	loadQuestionTable();
	evenBinderForQuestion();
});