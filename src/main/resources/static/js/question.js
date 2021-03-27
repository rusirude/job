/**
 * @author: rusiru
 */
var stepper;
var rowCount = 0;



var rowCreator = ()=>{
	rowCount++;
	return `<div class="row body">
						<div class="form-group col-sm-9">
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

/*-------------------------------- Reference Data , Data Table and Common --------------------*/
;

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

/*-------------------------------- Document Ready ----------------------*/
$(document).ready(()=>{
	let _callback_1 = ()=>{
		stepper = new Stepper($('.bs-stepper')[0]);
		$('#questionCategory').select2();
	};


	loadReferenceDataForQuestion(_callback_1)
});