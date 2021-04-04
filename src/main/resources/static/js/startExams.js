var total = 0;
var done =0;
var startTime;
var endTime;
var currentTime;
var aan = false;
var interval;



/*--------------- progress bars --------------------------*/
var setNoQuestion = ()=>{
    if(total){
        let percentage = parseFloat(((done/total)*100).toFixed(2));
        $("#noDone").css("width",`${percentage}%`);
        $("#noDone").html(percentage);
        $("#noDoneQnt").html(`${done}/${total} %`);
    }
};

var setTimer = ()=>{

    let t = $("#timer");
    let s = moment(new Date(startTime));
    let e = moment(new Date(endTime));
    let c = moment(new Date(currentTime));
    let percentage = parseFloat(((c.diff(s)/e.diff(s))*100).toFixed(2));
    let clazz = (percentage >= 90)? 'bg-danger' :(percentage >= 5 ?'bg-warning':'bg-primary');
    t.removeClass('bg-primary','bg-warning','bg-danger');
    t.addClass(clazz);
    t.css("width",`${percentage}%`);
    t.html(percentage);

    if(percentage >= 100){
        clearInterval(interval);
        $("#stopAlert").modal({'backdrop':true},'show');

    }
};

var incrementTime = ()=>{

    let c = moment(new Date(currentTime));
    currentTime = c.add(1, 'seconds').toDate();;
    setTimer()

};


var createAnswer = (data)=>{
    aan = false;
    let _a = ``;
    let i = 0;
    for(let a of data.questionAnswers){
        aan = (!aan)?a.mark:aan;
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

var finishExam = ()=>{
    app.next('/studentExams/end/'+$("#studentExam").val());
};

var findQuestionForExam = (questionKey,callback)=>{
    let successFunction = (data)=>{
        if(data.code === Constant.CODE_SUCCESS) {
            if(!data.data.closed){
                setQuestionView(data.data.question);
                total = data.data.total||0;
                startTime = data.data.startTime;
                endTime = data.data.endTime;
                currentTime = data.data.currentTime;
            }
            else{
                $("#stopAlert").modal({'backdrop':true},'show');
            }

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
var changeButton = (seq)=>{
    let previous = $("#previous");
    let savePrevious = $("#savePrevious");
    let saveNext = $("#saveNext");
    let next = $("#next");
    if(seq===1){
        previous.hide();
        savePrevious.hide();

    }
    else if(seq === total){
        saveNext.hide();
        next.hide();
    }
    else{
        previous.show();
        savePrevious.show();
        saveNext.show();
        next.show();
    }
};

var clickNextBtn = () =>{
    let  seq = parseInt($("#current").val()||'1');
    $("#current").val(++seq);
    findQuestionForExam(seq);
    changeButton(seq);
};
var clickNextAndSaveBtn = () =>{
    let _f = ()=>{
        done = (!aan)?++done:done;
        setNoQuestion();
        let  seq = parseInt($("#current").val()||'1');
        $("#current").val(++seq);
        findQuestionForExam(seq);
        changeButton(seq);
    };

    saveQuestionAnswer(_f);
};
var clickPreviousAndSaveBtn = () =>{
    let _f = ()=>{
        done = (!aan)?++done:done;
        setNoQuestion();
        let  seq = parseInt($("#current").val()||'1');
        $("#current").val(--seq);
        findQuestionForExam(seq);
        changeButton(seq);
    };

    saveQuestionAnswer(_f);
};

var clickPreviousBtn = () =>{
    let  seq = parseInt($("#current").val()||'1');
    $("#current").val(--seq);
    findQuestionForExam(seq);
    changeButton(seq);
};
var finalSubmit = () =>{
    $("#stopAlert").modal('hide');
    saveQuestionAnswer(finishExam);
};

/*-------------------------------- Document Ready ----------------------*/

$(document).ready(()=>{
    let seq =  parseInt($("#current").val()||'1')||1;
    let _f = ()=>{
        setTimer();
        interval = setInterval(()=>{
            incrementTime();
        },1000)
    };
    findQuestionForExam(seq,_f);
    changeButton(seq);

});