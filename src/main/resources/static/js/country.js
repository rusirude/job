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
		FormTransition.closeForm('#countryForm','#countryGrid');
		DialogBox.openSuccessMsgBox(data.message);		
		countryTable.ajax.reload();
		clearDataForCountry();
	}
	else{
		alert(data.message);
	}
};

var failedFunctionForCountry = (data)=>{
	alert("Server Error");
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
		InputsValidator.inlineEmptyValidation(status);
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
	let url = "/country/loadCountryByCode";
	let method = "POST";
	callToserver(url,method,{code:code},successFunction,failedFunction);
	
};

/*-------------------------------- Reference Data , Data Table and Common --------------------*/

var populateFormForCountry = (data) => {
	if(data){
		$("#code")[0].parentElement.MaterialTextfield.change(data.code || "");
		$("#description")[0].parentElement.MaterialTextfield.change(data.description || "");
		$("#status")[0].parentElement.MaterialSelectfield.change(data.statusCode || "");
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
        		alert("System Failer Occur....! :-(");
        	}
        	

    	},
        failure: function(errMsg) {
            alert(errMsg);
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
		                            		return `<button onClick="updateIconClickForCountry('${data}')" class="mdl-button mdl-js-button mdl-button--icon mdl-button--colored">
													  <i id="icon-update-${data}" class="material-icons">create</i>
													  <div class="mdl-tooltip" data-mdl-for="icon-update-${data}">
														Update
													  </div>
													</button>
													<button onClick="deleteIconClickForCountry('${data}')" class="mdl-button mdl-js-button mdl-button--icon mdl-button--colored">
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

var clearDataForCountry = ()=>{
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

	FormTransition.closeForm('#countryForm','#countryGrid');
	
};

/*-------------------------------- Inline Event  ----------------------*/

var clickAddForCountry = ()=>{
	clearDataForCountry();
	$("#formHeading").html("Add Country");
	FormTransition.openForm('#countryForm','#countryGrid');
};

var updateIconClickForCountry = (code)=>{
	let _sF = (data)=>{
		$("#btnSave").hide();
		$("#btnUpdate").show();
		$("#btnDelete").hide();
		populateFormForCountry(data);
		$("#code")[0].parentElement.MaterialTextfield.disable();
		$("#formHeading").html("Update Country");
		FormTransition.openForm('#countryForm','#countryGrid');
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
		$("#code")[0].parentElement.MaterialTextfield.disable();
		$("#description")[0].parentElement.MaterialTextfield.disable();
		$("#status")[0].parentElement.MaterialSelectfield.disable();
		$("#formHeading").html("Delete Country");
		FormTransition.openForm('#countryForm','#countryGrid');
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

 	let _callback_1 = ()=>{
 		componentHandler.upgradeDom(); 	 		
	};	
	loadReferenceDataForCountry(_callback_1);
	evenBinderForCountry();
	loadCountryTable();
	 

});