let app;
$(document).ready(()=>{	
	app = application.getInstance();
	app.next('/dashBoard/');
	loadMainMenu();
	componentHandler.upgradeDom();
});

var loadMainMenu = (callback)=>{
	$.ajax({
		type: "POST",
		url: "/dashBoard/loadMainMenu",
		contentType: "application/json",
		dataType: "json",
		success: function(data){

			let menuList = '';

			if(data.menuSectionDTOs && (data.menuSectionDTOs).length){
				for(let section of data.menuSectionDTOs){
					menuList += `<a class="mdl-navigation__link" style="color: #0091ea" href="#">${section.description}</a>`;
					if(section.menuDTOs && (section.menuDTOs).length){
						for(let menu of section.menuDTOs){
							menuList += `<a class="mdl-navigation__link" href="Javascript:loadPage('${menu.url}')">${menu.description}</a>`;
						}
					}
				}
			}

			$("#mainMenu").append(menuList);

		},
		failure: function(errMsg) {
			alert(errMsg);
		}
	});
};