let app;
$(document).ready(()=>{
	$('.sidenav').sidenav();
	app = application.getInstance();
	app.next('/dashBoard/');
});