
var loadStudentExamsTable = ()=>{
    sectionTable = $('#studentExamsTable').DataTable( {
        ajax: {
            url : "/studentExams/pendingExamsForStudent",
            contentType:"application/json",
            type:"POST",
            data:function(d){
                return JSON.stringify(createCommonDataTableRequset(d));
            }
        },
        paging: true,
        lengthChange: false,
        searching: false,
        ordering: true,
        info: true,
        autoWidth: false,
        responsive: true,
        processing: true,
        serverSide: true,
        columns: [
            { data: "examinationDescription"         ,name:"examination"         },
            { data: "duration"                       ,name:"duration"            },
            { data: "noQuestion"                     ,name:"noQuestion"          },
            { data: "statusDescription"              ,name:"status"              },
            {
                data: "id",
                render: function (data, type, full) {
                    return `<button onclick="clickStartExamBtn('${data}')" type="button" class="btn btn-outline-primary btn-sm" data-toggle="tooltip" data-placement="bottom" title="Start">
														<i class="fa fa-pencil-alt"></i>
													</button>`;
                }
            }
        ]
    } );
};

var clickStartExamBtn = (examCode)=>{
    app.next(`/studentExams/start/${examCode}`);
};
/*-------------------------------- Document Ready ----------------------*/


$(document).ready(()=>{
    loadStudentExamsTable();
});