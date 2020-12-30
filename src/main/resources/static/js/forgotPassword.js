/*------------------------------------------- Functions ------------------*/

var clearDataForForgotPassword = ()=>{
    let username = $("#username");
    username[0].parentElement.MaterialTextfield.change("");
    InputsValidator.removeInlineValidation(username);
    FormTransition.closeForm('#countryForm','#countryGrid');
};

var generateFinalObjectForForgotPassword = ()=>{
    return {
        username:$("#username").val()||""
    }
};

var successFunctionForForgotPassword = (data)=>{
    if(data.code === Constant.CODE_SUCCESS){
        DialogBox.openSuccessMsgBox(data.message);
    }
    else{
        alert(data.message);
    }
    clearDataForForgotPassword();
};

var failedFunctionForForgotPassword = (data)=>{
    alert("Server Error");
};


var validatorForForgotPassword = ()=>{


    let isValid = true;

    let username = $("#username");

    if(! username.val()){
        InputsValidator.inlineEmptyValidation(username);
        isValid = false;
    }
    return isValid;
};

var resetPassword = ()=>{
    if(validatorForForgotPassword()){
        let url = "/resetPassword";
        let method = "POST";

        callToserver(url,method,generateFinalObjectForForgotPassword(),successFunctionForForgotPassword,failedFunctionForForgotPassword);
    }

};


/*-------------------------------- Dynamic Event  ----------------------*/

var evenBinderForCountry = ()=>{
    $("#btnForgotPassword").off().on("click",function(){
        resetPassword();
    });


};

/*-------------------------------- Document Ready ----------------------*/


$(document).ready(()=>{

    evenBinderForCountry();


});