var total = 0;
var done =0;

/*--------------- progress bars --------------------------*/
var setNoQuestion = ()=>{
    if(total){
        let percentage = parseFloat(((done/total)*100).toFixed(2));
        $("#noDone").css("width",`${percentage}%`);
        $("#noDone").html(percentage);
        $("#noDoneQnt").html(`${done}/${total}`);
    }
};

var createAnswer = (data)=>{
    let _a = ``;
    let i = 0;
    for(let a of data.questionAnswers){
        let _id = `ans_${data.code}_${++i}`;
        _a += `<div class="custom-control custom-radio">
                    <input class="custom-control-input" value="${a.id}" type="radio" id="${_id}" name="answer" ${a.mark?"checked":""}/>
                    <label for="${_id}" class="custom-control-label">${a.description}</label>
                </div>`
    }
    return _a;
};

var setQuestionView = (data)=>{
    let _q = `<div class="card">
                    <div class="card-body">
                        <h5 data-code="${data.code}">${data.description}</h5>
                    </div>
                </div>
                <div class="card">
                    <div class="card-body">
                        <div class="col-sm-12">
                            <!-- radio -->
                            <div class="form-group">
                                ${createAnswer(data)}
                            </div>
                        </div>
                    </div>
                </div>`;
    $("#questionSection").html(_q);
};

var findQuestionForExam = (questionKey)=>{
    let successFunction = (data)=>{
        if(data.code === Constant.CODE_SUCCESS) {
            setQuestionView(data.data.question);
            total = data.data.total||0;
        }
        else{
            DialogBox.openMsgBox(data.message,'error');
        }
    };
    let failedFunction = (data)=>{
        DialogBox.openMsgBox("Server Error",'error');
    };
    let studentExam  = $("#studentExam").val();
    let url = `/studentExams/question/${studentExam}/${questionKey}`;
    let method = "POST";
    callToserver(url,method,null,successFunction,failedFunction);

};

/*--------------------------- Save Question --------------------------*/

var saveQuestionAnswer = (callback)=>{
    let successFunction = (data)=>{
        if(data.code === Constant.CODE_SUCCESS) {
            if(callback){
                callback();
            }
        }
        else{
            DialogBox.openMsgBox(data.message,'error');
        }
    };
    let failedFunction = (data)=>{
        DialogBox.openMsgBox("Server Error",'error');
    };
    let obj = {
        studentExamination: $("#studentExam").val(),
        seq: $("#current").val(),
        answer:$("input[name='answer']:checked").val()
    };
    let url = `/studentExams/saveAnswer`;
    let method = "POST";
    callToserver(url,method,obj,successFunction,failedFunction);

};

/*-------------------------------- Dynamic Event  ----------------------*/

var clickNextBtn = () =>{
    let  seq = parseInt($("#current").val()||'1');
    $("#current").val(++seq);
    findQuestionForExam(seq);
};
var clickNextAndSaveBtn = () =>{
    let _f = ()=>{
        done++;
        setNoQuestion();
        let  seq = parseInt($("#current").val()||'1');
        $("#current").val(++seq);
        findQuestionForExam(seq);
    };

    saveQuestionAnswer(_f);
};

var clickPreviousBtn = () =>{
    let  seq = parseInt($("#current").val()||'1');
    $("#current").val(--seq);
    findQuestionForExam(seq);
};

/*-------------------------------- Document Ready ----------------------*/

$(document).ready(()=>{
    findQuestionForExam(parseInt($("#current").val()||'1')||1);

});