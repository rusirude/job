/**
 * @author: rusiru
 */

var passwordResetRequestTable;

/*------------------------------------------- CRUD Functions ------------------*/

var generateFinalObjectForPasswordResetRequest = (id)=> {
    return {
        id: id
    }
};

var successFunctionForPasswordResetRequest = (data)=> {
    if (data.code === Constant.CODE_SUCCESS) {
        DialogBox.openSuccessMsgBox(data.message);
        passwordResetRequestTable.ajax.reload();
    }
    else {
        alert(data.message);
    }
};

var failedFunctionForPasswordResetRequest = (data)=> {
    alert("Server Error");
};



var confirmForPasswordResetRequest = (id)=> {
    let url = "/passwordResetRequest/resetPassword";
    let method = "POST";

    callToserver(url, method, generateFinalObjectForPasswordResetRequest(id), successFunctionForPasswordResetRequest, failedFunctionForPasswordResetRequest);


};



/*-------------------------------- Reference Data , Data Table and Common --------------------*/

var loadPasswordResetRequestTable = ()=> {
    passwordResetRequestTable = $('#passwordResetRequestTable').DataTable({
        ajax: {
            url: "/passwordResetRequest/loadPasswordResetRequests",
            contentType: "application/json",
            type: "POST",
            data: function (d) {
                return JSON.stringify(createCommonDataTableRequset(d));
            }
        },
        processing: true,
        serverSide: true,
        drawCallback: function (settings) {
            componentHandler.upgradeDom();
        },
        scrollY: true,
        scrollX: true,
        scrollCollapse: true,
        paging: true,
        pagingType: "full_numbers",
        columns: [
            {data: "username", name: "username", class: "mdl-data-table__cell--non-numeric"},
            {data: "titleDescription", name: "title", class: "mdl-data-table__cell--non-numeric"},
            {data: "name", name: "name", class: "mdl-data-table__cell--non-numeric"},
            {data: "statusDescription", name: "status", class: "mdl-data-table__cell--non-numeric"},
            {
                data: "id",
                class: "mdl-data-table__cell--non-numeric",
                render: function (data, type, full) {
                    return `<button onClick="confirmIconClickForPasswordResetRequest('${data}')" class="mdl-button mdl-js-button mdl-button--icon mdl-button--colored">
													  <i id="icon-confirm-${data}" class="material-icons">thumb_up</i>
													  <div class="mdl-tooltip" data-mdl-for="icon-confirm-${data}">
														Reset Password
													  </div>
													</button>`;
                }
            }
        ]
    });
};



/*-------------------------------- Inline Event  ----------------------*/

var confirmIconClickForPasswordResetRequest = (id)=> {
    confirmForPasswordResetRequest(id);
};



/*-------------------------------- Document Ready ----------------------*/


$(document).ready(()=> {
    loadPasswordResetRequestTable();


});