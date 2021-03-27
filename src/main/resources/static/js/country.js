/**
 * @author: rusiru
 */

var countryTable;


/*------------------------------------------- CRUD Functions ------------------*/

var generateFinalObjectForCountry = ()=>{
	return {
		code:$("#code").val()||"",
		description:$("#description").val()||"",
		statusCode:$("#status").val()||""
	}
};

var successFunctionForCountry = (data)=>{
	if(data.code === Constant.CODE_SUCCESS){
		DialogBox.openMsgBox(data.message,'success');
		countryTable.ajax.reload();
		clearDataForCountry();
	}
	else{
		DialogBox.openMsgBox(data.message,'error');
	}
};

var failedFunctionForCountry = (data)=>{
	DialogBox.openMsgBox("Server Error",'error');
};

var validatorForCountry = ()=>{
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
		InputsValidator.inlineEmptyValidationSelect(status);
		isValid = false;
	}
	return isValid;
};

var saveForCountry = ()=>{
	if(validatorForCountry()){
		let url = "/country/save";
		let method = "POST";

		callToserver(url,method,generateFinalObjectForCountry(),successFunctionForCountry,failedFunctionForCountry);
	}

};

var updateForCountry = ()=>{
	if(validatorForCountry()){
		let url = "/country/update";
		let method = "POST";

		callToserver(url,method,generateFinalObjectForCountry(),successFunctionForCountry,failedFunctionForCountry);
	}
};

var deleteForCountry = ()=>{
	if(validatorForCountry()){
		let url = "/country/delete";
		let method = "POST";

		callToserver(url,method,generateFinalObjectForCountry(),successFunctionForCountry,failedFunctionForCountry);
	}
};

var findDetailByCodeForCountry = (code,callback)=>{
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
	let url = "/country/loadCountryByCode";
	let method = "POST";
	callToserver(url,method,{code:code},successFunction,failedFunction);

};


/*-------------------------------- Reference Data , Data Table and Common --------------------*/

var populateFormForCountry = (data) => {
	if(data){
		$("#code").val(data.code || "");
		$("#description").val(data.description || "");
		$("#status").val(data.statusCode || "");
	}
};

var loadReferenceDataForCountry = (callback)=>{
	$.ajax({
		type: "POST",
		url: "/country/loadRefDataForCountry",
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
				DialogBox.openMsgBox("System Failer Occur....! :-(",'error');
			}


		},
		failure: function(errMsg) {
			DialogBox.openMsgBox(errMsg,'error');
		}
	});
};


var loadCountryTable = ()=>{
	countryTable = $('#countryTable').DataTable( {
		ajax: {
			url : "/country/loadCountries",
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
		responsive: true,
		processing: true,
		serverSide: true,
		columns: [
			{ data: "code"                ,name:"code"                },
			{ data: "description"         ,name:"description"         },
			{ data: "statusDescription"   ,name:"status"              },
			{ data: "createdBy"           ,name:"createdBy"           },
			{ data: "createdOn"           ,name:"createdOn"           },
			{ data: "updatedBy"           ,name:"updatedBy"           },
			{ data: "updatedOn"           ,name:"updatedOn"           },
			{
				data: "code",
				render: function (data, type, full) {
					return `<button onClick="updateIconClickForCountry('${data}')" type="button" class="btn btn-outline-primary btn-sm" data-toggle="tooltip" data-placement="bottom" title="Update">
														<i class="fa fa-pencil-alt"></i>
													</button>
													<button onClick="deleteIconClickForCountry('${data}')" type="button" class="btn btn-outline-danger btn-sm" data-toggle="tooltip" data-placement="bottom" title="Delete">
														<i class="fa fa-trash-alt"></i>
													</button>`;
				}
			}
		]
	} );
}

var clearDataForCountry = ()=>{
	let code = $("#code");
	let description = $("#description");
	let status = $("#status");

	$("#btnSave").show();
	$("#btnUpdate").hide();
	$("#btnDelete").hide();

	$("#formHeading").html("");

	code.prop("disabled",false);
	description.prop("disabled",false);
	status.prop("disabled",false);

	InputsValidator.removeInlineValidation(code);
	InputsValidator.removeInlineValidation(description);
	InputsValidator.removeInlineValidation(status);

	code.val("");
	description.val("");
	status.val("");

	FormTransition.closeModal('#countryModal');

};


/*-------------------------------- Inline Event  ----------------------*/
var clickAddForCountry = ()=>{
	clearDataForCountry();
	$("#formHeading").html("Add Country");
	FormTransition.openModal('#countryModal');
};

var updateIconClickForCountry = (code)=>{
	console.log(code);
	let _sF = (data)=>{
		$("#btnSave").hide();
		$("#btnUpdate").show();
		$("#btnDelete").hide();
		populateFormForCountry(data);
		$("#code").prop("disabled",true);
		$("#formHeading").html("Update Country");
		FormTransition.openModal('#countryModal');
	};
	clearDataForCountry();
	findDetailByCodeForCountry(code,_sF);
};

var deleteIconClickForCountry = (code)=>{
	let _sF = (data)=>{
		$("#btnSave").hide();
		$("#btnUpdate").hide();
		$("#btnDelete").show();
		populateFormForCountry(data);
		$("#code").prop("disabled",true);
		$("#description").prop("disabled",true);
		$("#status").prop("disabled",true);
		$("#formHeading").html("Delete Country");
		FormTransition.openModal('#countryModal');
	};
	clearDataForCountry();
	findDetailByCodeForCountry(code,_sF);
};


/*-------------------------------- Dynamic Event  ----------------------*/

var evenBinderForCountry = ()=>{
	$("#btnCountryAdd").off().on("click",function(){
		clickAddForCountry();
	});

	$("#btnSave").off().on("click",function(){
		saveForCountry();
	});

	$("#btnUpdate").off().on("click",function(){
		updateForCountry();
	});

	$("#btnDelete").off().on("click",function(){
		deleteForCountry();
	});

	$("#btnCancel").off().on("click",function(){
		clearDataForCountry();
	});

};

/*-------------------------------- Document Ready ----------------------*/


$(document).ready(()=>{
	loadReferenceDataForCountry();
	evenBinderForCountry();
	loadCountryTable();
});