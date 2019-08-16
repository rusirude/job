/**
 * Common Back End Ajax Call * 
 */
var callToserver = (url,method,data,successFunction,errorFunction)=>{
	$.ajax({
        type: method,
        url: url,        
        contentType: "application/json",
        dataType: "json",
        data:JSON.stringify(data),
        success: function(data) {
			if(successFunction){
				successFunction(data);
			}
		},
        failure: function(errMsg) {
        	if(errorFunction){
        		errorFunction(errMsg);
			} 
        }
  });
};



var createCommonDataTableRequset = (inputObj)=>{
    return {
            draw:inputObj.draw,
            start:inputObj.start,
            length:inputObj.length,
            sortColumnName:inputObj.columns[inputObj.order[0].column].name ||"",
            sortOrder:inputObj.order[0].dir || "desc",
            search:inputObj.search.value || ""
         }
	
};


var FormTransition = (function () {
	function openForm(formId,tableId){
		$(formId).show('fast');
		$(tableId).hide('fast');

	};
	function closeForm(formId,tableId){
		$(formId).hide('fast');
		$(tableId).show('fast');
	};
	return {
		openForm: function(formId,tableId){
			openForm(formId,tableId);
		},
		closeForm:function(formId,tableId){
			closeForm(formId,tableId);
		}

	}
})();


/* Dialog Box Functionality ********/

var DialogBox = (function () {
	function openSuccessMessage(message){
		let msg = `<i class="material-icons">done</i> ${message}`
		let s = document.querySelector('dialog');
		
		$(".mdl-dialog .mdl-dialog__content").html(msg);		
		$("#mdl-dialog-failed").html("").hide().off("click");		
		$("#mdl-dialog-success").html("OK").on("click",()=>{
			s.close();
		});
		
		s.showModal();

	}
	return {
		openSuccessMsgBox: function(message){
			openSuccessMessage(message);
		}
	}
})();

/*  Inputs Validations    */
var InputsValidator = (function(){
	function inlineValidation(element,message){
		let _element = $(element);
		_element.parent().find("span.mdl-textfield__error, span.mdl-selectfield__error").html(message||"");
		_element.parent().addClass('is-invalid');

	}
	function removeInlineValidation(element){
		let _element = $(element);
		_element.parent().find("span.mdl-textfield__error, span.mdl-selectfield__error").html("");
		_element.parent().removeClass('is-invalid');
	}
	function inlineEmptyValidation(element){
		let _element = $(element);
		_element.parent().find("span.mdl-textfield__error, span.mdl-selectfield__error").html("Can't be Empty");
		_element.parent().addClass('is-invalid');
	}
	return{
		inlineValidation: function(element,message){
			inlineValidation(element,message)
		},
		removeInlineValidation: function(element){
			removeInlineValidation(element);
		},
		inlineEmptyValidation: function(element){
			inlineEmptyValidation(element);
		}
	}
})();



