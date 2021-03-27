/**
 * @author: rusiru
 */
var stepper;
/*-------------------------------- Document Ready ----------------------*/


$(document).ready(()=>{

	stepper = new Stepper($('.bs-stepper')[0]);

	$('#questionCategory').select2({
		theme: 'bootstrap4'
	});
});