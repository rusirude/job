let app;
$(document).ready(()=>{	
	app = application.getInstance();
	app.next('/dashBoard/');
	componentHandler.upgradeDom();
});