let app;
$(document).ready(()=>{	
	app = application.getInstance();
	app.next('/dashBoard/');
	loadMainMenu();
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


					menuList += `<li class="nav-item">
									<a href="#" class="nav-link">
									  <i class="nav-icon fas fa-edit"></i>
									  <p>
										${section.description}
										<i class="fas fa-angle-left right"></i>
									  </p>
									</a>`;

					if(section.menuDTOs && (section.menuDTOs).length){
						menuList += '<ul class="nav nav-treeview">';
						for(let menu of section.menuDTOs){
							menuList += `<li class="nav-item">
											<a href="Javascript:loadPage('${menu.url}')" class="nav-link">
												<i class="fa fa-chevron-right nav-icon"></i>
												<p>${menu.description}</p>
											</a>
										</li>`;
						}
						menuList += '</ul>';
					}

					menuList += `</li>`;
				}
			}

			$("#mainMenu").append(menuList);

		},
		failure: function(errMsg) {
			alert(errMsg);
		}
	});
};