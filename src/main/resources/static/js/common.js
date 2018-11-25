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
	
}