var application = (function () {
	
  
	let instance;
 
	function init() {
		let innerContainer = $("#appInnerContainer");
		let page = [];
		let pagePointer = -1;		
		
		
		let loanPage = (pageUrl) => {
			if(pageUrl){
				innerContainer.load(pageUrl,function(){
					alert("ok");
				});
			}			
		};
		
		let setNextPointer = (pageUrl)=>{
			page[++pagePointer] = pageUrl;
		};
 

 
	    return {	 
	      
	      next: function (url) {
	    	  
	    	  loanPage(url);
	      }, 
	
	 
	    };
	 
	  };
 
  return {
 

    getInstance: function () {
 
      if ( !instance ) {
        instance = init();
      }
 
      return instance;
    }
 
  };
 
})();