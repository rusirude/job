
CREATE TABLE `job`.`status_category` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(10) NOT NULL,
  `description` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `code_UNIQUE` (`code` ASC)
  );
  
 CREATE TABLE `job`.`status` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(10) NOT NULL,
  `description` VARCHAR(50) NOT NULL,
  `status_category` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `code_UNIQUE` (`code` ASC),
  INDEX `fk_status_status_category_idx` (`status_category` ASC),
  CONSTRAINT `fk_status_status_category`
    FOREIGN KEY (`status_category`)
    REFERENCES `job`.`status_category` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE);
        
    
CREATE TABLE `job`.`sys_role` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(10) NOT NULL,
  `description` VARCHAR(50) NOT NULL,
  `status` INT NOT NULL,
  `created_by` VARCHAR(25) NOT NULL,
  `created_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` VARCHAR(25) NOT NULL,
  `updated_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `code_UNIQUE` (`code` ASC),
  INDEX `fk_sys_role_status_idx` (`status` ASC),
  CONSTRAINT `fk_sys_role_status`
    FOREIGN KEY (`status`)
    REFERENCES `job`.`status` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE);

CREATE TABLE `job`.`title` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(10) NOT NULL,
  `description` VARCHAR(50) NOT NULL,
  `status` INT NOT NULL,
  `created_by` VARCHAR(25) NOT NULL,
  `created_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` VARCHAR(25) NOT NULL,
  `updated_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `code_UNIQUE` (`code` ASC),
  INDEX `fk_title_status_idx` (`status` ASC),
  CONSTRAINT `fk_title_status`
    FOREIGN KEY (`status`)
    REFERENCES `job`.`status` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE);


CREATE TABLE `job`.`sys_user` (
  `username` VARCHAR(25) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `title` INT NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `status` INT NOT NULL,
  `created_by` VARCHAR(25) NOT NULL,
  `created_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` VARCHAR(25) NOT NULL,
  `updated_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`username`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC),
  INDEX `fk_sys_user_status_idx` (`status` ASC),
  CONSTRAINT `fk_sys_user_status`
    FOREIGN KEY (`status`)
    REFERENCES `job`.`status` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  INDEX `fx_sys_user_title_idx` (`title` ASC),
  CONSTRAINT `fk_sys_user_title`
    FOREIGN KEY (`title`)
    REFERENCES `job`.`title` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
);
    
CREATE TABLE `job`.`section` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(10) NOT NULL,
  `description` VARCHAR(50) NOT NULL,
  `status` INT NOT NULL,
  `created_by` VARCHAR(25) NOT NULL,
  `created_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` VARCHAR(25) NOT NULL,
  `updated_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `sectioncol_UNIQUE` (`code` ASC),
  INDEX `fk_section_status_idx` (`status` ASC),
  CONSTRAINT `fk_section_status`
    FOREIGN KEY (`status`)
    REFERENCES `job`.`status` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE);
 
    
    
CREATE TABLE `job`.`authority` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(10) NOT NULL,
  `description` VARCHAR(50) NOT NULL,
  `auth_code` VARCHAR(20) NOT NULL,
  `url` VARCHAR(80) NULL,
  `section` INT NOT NULL,
  `status` INT NOT NULL,
  `created_by` VARCHAR(25) NOT NULL,
  `created_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` VARCHAR(25) NOT NULL,
  `updated_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `code_UNIQUE` (`code` ASC),
  UNIQUE INDEX `auth_code_UNIQUE` (`auth_code` ASC),
  INDEX `fk_authority_status_idx` (`status` ASC),
  CONSTRAINT `fk_authority_status`
    FOREIGN KEY (`status`)
    REFERENCES `job`.`status` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  INDEX `fx_authority_section_idx` (`section` ASC),
  CONSTRAINT `fk_authority_section`
    FOREIGN KEY (`section`)
    REFERENCES `job`.`section` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
);

CREATE TABLE `job`.`sys_user_sys_role` (
  `sys_user` VARCHAR(25) NOT NULL,
  `sys_role` INT NOT NULL,
  PRIMARY KEY (`sys_user`, `sys_role`),
  INDEX `fk_sys_user_sys_role_sys_role_idx` (`sys_role` ASC),
  CONSTRAINT `fk_sys_user_sys_role_sys_user`
    FOREIGN KEY (`sys_user`)
    REFERENCES `job`.`sys_user` (`username`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_sys_user_sys_role_sys_role`
    FOREIGN KEY (`sys_role`)
    REFERENCES `job`.`sys_role` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE);

CREATE TABLE `job`.`sys_role_authoriry` (
  `sys_role` INT NOT NULL,
  `authority` INT NOT NULL,
  PRIMARY KEY (`sys_role`, `authority`),
  INDEX `fk_sys_role_authoriry_authority_idx` (`authority` ASC),
  CONSTRAINT `fk_sys_role_authority_sys_role`
    FOREIGN KEY (`sys_role`)
    REFERENCES `job`.`sys_role` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_sys_role_authority_authority`
    FOREIGN KEY (`authority`)
    REFERENCES `job`.`authority` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE);
    
    
CREATE TABLE `job`.`master_data` (
  `code` VARCHAR(150) NOT NULL,
  `value` VARCHAR(255) NULL,
  `created_by` VARCHAR(25) NOT NULL,
  `created_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` VARCHAR(25) NOT NULL,
  `updated_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`code`));

CREATE TABLE `job`.`sys_user_authority` (
  `sys_user` VARCHAR(25) NOT NULL,
  `authority` INT(11) NOT NULL,
  `is_grant` INT(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`sys_user`, `authority`),
  INDEX `fk_sys_user_authority_authority_idx` (`authority` ASC),
  CONSTRAINT `fk_sys_user_authority_sys_user`
    FOREIGN KEY (`sys_user`)
    REFERENCES `job`.`sys_user` (`username`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_sys_user_authority_authority`
    FOREIGN KEY (`authority`)
    REFERENCES `job`.`authority` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE);

CREATE TABLE `job`.`country` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(10) NOT NULL,
  `description` VARCHAR(50) NOT NULL,
  `status` INT NOT NULL,
  `created_by` VARCHAR(25) NOT NULL,
  `created_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` VARCHAR(25) NOT NULL,
  `updated_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `code_UNIQUE` (`code` ASC),
  INDEX `fk_country_status_idx` (`status` ASC),
  CONSTRAINT `fk_country_status`
    FOREIGN KEY (`status`)
    REFERENCES `job`.`status` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE);

CREATE TABLE `job`.`password_policy` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `min_length` INT NOT NULL DEFAULT 0,
  `max_length` INT NOT NULL DEFAULT 0,
  `min_char` INT NOT NULL DEFAULT 0,
  `min_num` INT NOT NULL DEFAULT 0,
  `min_spe_char` INT NOT NULL DEFAULT 0,
  `no_of_invalid_attempt` INT NOT NULL DEFAULT 0,
  `no_of_history_password` INT NOT NULL DEFAULT 0,
  `status` INT NOT NULL,
  `created_by` VARCHAR(25) NOT NULL,
  `created_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` VARCHAR(25) NOT NULL,
  `updated_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `fk_password_policy_status_idx` (`status` ASC),
  CONSTRAINT `fk_password_policy_status`
    FOREIGN KEY (`status`)
    REFERENCES `job`.`status` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE);
    
INSERT INTO `job`.`status_category` (`code`, `description`) VALUES ('DEFAULT', 'Default');
INSERT INTO `job`.`status_category` (`code`,`description`) VALUES ('DELETE','Delete');

INSERT INTO `job`.`status` (`code`,`description`,`status_category`) VALUES ('ACTIVE','Active',1);
INSERT INTO `job`.`status` (`code`,`description`,`status_category`) VALUES ('INACTIVE','Inactive',1);
INSERT INTO `job`.`status` (`code`,`description`,`status_category`) VALUES ('DELETE','Delete',2);

INSERT INTO  `job`.`section`(`code`,`description`,`status`,`created_by`,`updated_by`) VALUES ('USER_MGT_S','User Management',1,'SYSTEM','SYSTEM');
INSERT INTO  `job`.`section`(`code`,`description`,`status`,`created_by`,`updated_by`) VALUES ('REF_MGT_S','Reference Data Management',1,'SYSTEM','SYSTEM');
INSERT INTO  `job`.`section`(`code`,`description`,`status`,`created_by`,`updated_by`) VALUES ('SYS_CFG_S','System Configuration',1,'SYSTEM','SYSTEM');

INSERT INTO `job`.`title` (`code`,`description`,`status`,`created_by`,`updated_by`) VALUES ('CODE_NONE','None',1,'SYSTEM','SYSTEM');
INSERT INTO `job`.`title` (`code`,`description`,`status`,`created_by`,`updated_by`) VALUES ('CODE_MR','Mr.',1,'SYSTEM','SYSTEM');
INSERT INTO `job`.`title` (`code`,`description`,`status`,`created_by`,`updated_by`) VALUES ('CODE_MRS','Mrs.',1,'SYSTEM','SYSTEM');
INSERT INTO `job`.`title` (`code`,`description`,`status`,`created_by`,`updated_by`) VALUES ('CODE_MISS','Miss.',1,'SYSTEM','SYSTEM');
INSERT INTO `job`.`title` (`code`,`description`,`status`,`created_by`,`updated_by`) VALUES ('CODE_MS','Ms.',1,'SYSTEM','SYSTEM');
INSERT INTO `job`.`title` (`code`,`description`,`status`,`created_by`,`updated_by`) VALUES ('CODE_PROF','Prof.',1,'SYSTEM','SYSTEM');
INSERT INTO `job`.`title` (`code`,`description`,`status`,`created_by`,`updated_by`) VALUES ('CODE_DR','Dr.',1,'SYSTEM','SYSTEM');




INSERT INTO `job`.`sys_role` (`code`,`description`,`status`,`created_by`,`updated_by`) VALUES ('SYSTEM','System',1,'SYSTEM','SYSTEM');

INSERT INTO `job`.`sys_user` (`username`,`password`,`title`,`name`,`status`,`created_by`,`updated_by`) VALUES ('SYSTEM','$2a$10$/9rQ/RW6jv1DS0uXS4FoxeHvzZPiWpgnB6XdIxjymSYE9UFoGKEnq',1,'System',1,'SYSTEM','SYSTEM');

INSERT INTO `job`.`authority` (`code`,`description`,`auth_code`,`url`,`section`,`status`,`created_by`,`updated_by`) VALUES ('USER','User Management','ROLE_USER','/user/',1,1,'SYSTEM','SYSTEM');
INSERT INTO `job`.`authority` (`code`,`description`,`auth_code`,`url`,`section`,`status`,`created_by`,`updated_by`) VALUES ('ROLE','User Role Management','ROLE_ROLE','/sysRole/',1,1,'SYSTEM','SYSTEM');
INSERT INTO `job`.`authority` (`code`,`description`,`auth_code`,`url`,`section`,`status`,`created_by`,`updated_by`) VALUES ('SECTION','Section Management','ROLE_SECTION','/section/',1,1,'SYSTEM','SYSTEM');
INSERT INTO `job`.`authority` (`code`,`description`,`auth_code`,`url`,`section`,`status`,`created_by`,`updated_by`) VALUES ('ROLEAUTHOR','User Role Authority Management','ROLE_ROLEAUTHORITY','/sysRoleAuthority/',1,1,'SYSTEM','SYSTEM');
INSERT INTO `job`.`authority` (`code`,`description`,`auth_code`,`url`,`section`,`status`,`created_by`,`updated_by`) VALUES ('AUTHORITY','Authorry Management','ROLE_AUTHORITY','/authority/',1,1,'SYSTEM','SYSTEM');
INSERT INTO `job`.`authority` (`code`,`description`,`auth_code`,`url`,`section`,`status`,`created_by`,`updated_by`) VALUES ('TITLE','Title','ROLE_TITLE','/title/',1,1,'SYSTEM','SYSTEM');
INSERT INTO `job`.`authority` (`code`,`description`,`auth_code`,`url`,`section`,`status`,`created_by`,`updated_by`) VALUES ('USERROLE','Assign System Role to Sys User','ROLE_USERROLE','/sysUserSysRole/',1,1,'SYSTEM','SYSTEM');
INSERT INTO `job`.`authority` (`code`,`description`,`auth_code`,`url`,`section`,`status`,`created_by`,`updated_by`) VALUES ('MASTERDATA','Master Data Management','ROLE_MASTERDATA','/masterData/',1,1,'SYSTEM','SYSTEM');
INSERT INTO `job`.`authority` (`code`,`description`,`auth_code`,`url`,`section`,`status`,`created_by`,`updated_by`) VALUES ('USERAUTHOR','Sys User Authority','ROLE_USERAUTHORITY','/sysUserAuthority/',1,1,'SYSTEM','SYSTEM');
INSERT INTO `job`.`authority` (`code`,`description`,`auth_code`,`url`,`section`,`status`,`created_by`,`updated_by`) VALUES ('COUNTRY','Country','ROLE_COUNTRY','/country/',1,1,'SYSTEM','SYSTEM');
INSERT INTO `job`.`authority` (`code`,`description`,`auth_code`,`url`,`section`,`status`,`created_by`,`updated_by`) VALUES ('PPOLICY','Password Policy','ROLE_PPOLICY','/passwordPolicy/',1,1,'SYSTEM','SYSTEM');


INSERT INTO `job`.`sys_user_sys_role` (`sys_user`, `sys_role`) VALUES ('SYSTEM', '1');

INSERT INTO `job`.`sys_role_authoriry` (`sys_role`, `authority`) VALUES ('1', '1');
INSERT INTO `job`.`sys_role_authoriry` (`sys_role`, `authority`) VALUES ('1', '2');
INSERT INTO `job`.`sys_role_authoriry` (`sys_role`, `authority`) VALUES ('1', '3');
INSERT INTO `job`.`sys_role_authoriry` (`sys_role`, `authority`) VALUES ('1', '4');
INSERT INTO `job`.`sys_role_authoriry` (`sys_role`, `authority`) VALUES ('1', '5');
INSERT INTO `job`.`sys_role_authoriry` (`sys_role`, `authority`) VALUES ('1', '6');
INSERT INTO `job`.`sys_role_authoriry` (`sys_role`, `authority`) VALUES ('1', '7');
INSERT INTO `job`.`sys_role_authoriry` (`sys_role`, `authority`) VALUES ('1', '8');
INSERT INTO `job`.`sys_role_authoriry` (`sys_role`, `authority`) VALUES ('1', '9');



<!-- link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet"/-->
$("#status").parent().find(".mdl-menu__container").hide();	

    
    
    