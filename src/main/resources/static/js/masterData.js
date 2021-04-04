/*------------------------------------------- CRUD Functions ------------------*/

var loadReferenceDataForMasterData = (callback)=>{
	$.ajax({
		type: "POST",
		url: "/masterData/loadRefDataForMasterData",
		contentType: "application/json",
		dataType: "json",
		success: function(data){

			if(data.code === Constant.CODE_SUCCESS){
				for(let c of data.data.sysRole){
					$("#STUDENT_ROLE").append(`<option value="${c.code}">${c.description}</option>`);
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


var findExsistingDataForMasterData = ()=>{
	let successFunction = (data)=>{
		if(data.code === Constant.CODE_SUCCESS){			
			for(let _o of data.data || []){
				$("#"+_o.code).val(_o.value || "");
			}
		}
		else{
			alert(data.message);
		}
	};
	let failedFunction = (data)=>{
		alert("Server Error");
	};
	let url = "/masterData/loadMasterData";
	let method = "POST";
	callToserver(url,method,null,successFunction,failedFunction);
	
};

var saveDataForMasterData = ()=>{
	if(validatorForMasterData()){
		let successFunction = (data)=>{
			if(data.code === Constant.CODE_SUCCESS){			
				DialogBox.openMsgBox(data.message,'success');
			}
			else{
				DialogBox.openMsgBox(data.message,'error');
			}
		};
		let failedFunction = (data)=>{
			DialogBox.openMsgBox("Server Error",'error');
		};
		let data = [
					{code:"DEFAULT_PASSWORD",value:$("#DEFAULT_PASSWORD").val()||""},
					{code:"STUDENT_ROLE",value:$("#STUDENT_ROLE").val()||""}
					];
		let url = "/masterData/save";
		let method = "POST";
		callToserver(url,method,data,successFunction,failedFunction);
	}	
};

var validatorForMasterData = ()=>{
	let isValid = true;

	let DEFAULT_PASSWORD = $("#DEFAULT_PASSWORD");
	let STUDENT_ROLE = $("#STUDENT_ROLE");
	if(! DEFAULT_PASSWORD.val()){
		InputsValidator.inlineEmptyValidation(DEFAULT_PASSWORD);
		isValid = false;
	}
	if(! STUDENT_ROLE.val()){
		InputsValidator.inlineEmptyValidationSelect(STUDENT_ROLE);
		isValid = false;
	}
	return isValid;
};

/*-------------------------------- Dynamic Event  ----------------------*/

var evenBinderForMasterData = ()=>{
	$("#btnSave").off().on("click",function(){
		saveDataForMasterData();
	});
};

/*-------------------------------- Document Ready ----------------------*/


$(document).ready(()=>{
	loadReferenceDataForMasterData(findExsistingDataForMasterData);
	evenBinderForMasterData();
});