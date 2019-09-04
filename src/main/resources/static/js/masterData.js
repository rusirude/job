/*------------------------------------------- CRUD Functions ------------------*/

var loadReferenceDataForMasterData = (callback)=>{
	$.ajax({
		type: "POST",
		url: "/masterData/loadRefDataForMasterData",
		contentType: "application/json",
		dataType: "json",
		success: function(data){

			if(data.code === Constant.CODE_SUCCESS){
				for(let c of data.data.countries){
					$("#DEFAULT_COUNTRY").append(`<option value="${c.code}">${c.description}</option>`);
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
			componentHandler.upgradeDom(); 
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
				DialogBox.openSuccessMsgBox(data.message);
			}
			else{
				alert(data.message);
			}
		};
		let failedFunction = (data)=>{
			alert("Server Error");
		};
		let data = [
					{code:"DEFAULT_PASSWORD",value:$("#DEFAULT_PASSWORD").val()||""},
					{code:"COMPANY_NAME",value:$("#COMPANY_NAME").val()||""},
					{code:"DEFAULT_COUNTRY",value:$("#DEFAULT_COUNTRY").val()||""}
					];
		let url = "/masterData/save";
		let method = "POST";
		callToserver(url,method,data,successFunction,failedFunction);
	}	
};

var validatorForMasterData = ()=>{
	let isValid = true;
//	
//	let code = $("#code");
//	let description = $("#description");
//	let status = $("#status");
//	
//	if(! code.val()){		
//		//code.prop("required",true);		
//		return isValid = false;
//	}
//	if(! description.val()){
//		//description.prop("required",true);		
//		return isValid = false;
//	}
//	if(! status.val()){
//		//status.prop("required",true);
//		return isValid = false;
//	}
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