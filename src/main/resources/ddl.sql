
CREATE TABLE `exam`.`status_category` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(10) NOT NULL,
  `description` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `code_UNIQUE` (`code` ASC)
  );
  
 CREATE TABLE `exam`.`status` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(10) NOT NULL,
  `description` VARCHAR(50) NOT NULL,
  `status_category` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `code_UNIQUE` (`code` ASC),
  INDEX `fk_status_status_category_idx` (`status_category` ASC),
  CONSTRAINT `fk_status_status_category`
    FOREIGN KEY (`status_category`)
    REFERENCES `exam`.`status_category` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE);
        
    
CREATE TABLE `exam`.`sys_role` (
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
    REFERENCES `exam`.`status` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE);

CREATE TABLE `exam`.`title` (
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
    REFERENCES `exam`.`status` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE);


CREATE TABLE `exam`.`sys_user` (
  `username` VARCHAR(25) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `title` INT NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `status` INT NOT NULL,
  `reset_password` TINYINT NOT NULL DEFAULT 0,
  `created_by` VARCHAR(25) NOT NULL,
  `created_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` VARCHAR(25) NOT NULL,
  `updated_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`username`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC),
  INDEX `fk_sys_user_status_idx` (`status` ASC),
  CONSTRAINT `fk_sys_user_status`
    FOREIGN KEY (`status`)
    REFERENCES `exam`.`status` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  INDEX `fx_sys_user_title_idx` (`title` ASC),
  CONSTRAINT `fk_sys_user_title`
    FOREIGN KEY (`title`)
    REFERENCES `exam`.`title` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
);
    
CREATE TABLE `exam`.`section` (
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
    REFERENCES `exam`.`status` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE);
 
    
    
CREATE TABLE `exam`.`authority` (
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
    REFERENCES `exam`.`status` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  INDEX `fx_authority_section_idx` (`section` ASC),
  CONSTRAINT `fk_authority_section`
    FOREIGN KEY (`section`)
    REFERENCES `exam`.`section` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
);

CREATE TABLE `exam`.`sys_user_sys_role` (
  `sys_user` VARCHAR(25) NOT NULL,
  `sys_role` INT NOT NULL,
  PRIMARY KEY (`sys_user`, `sys_role`),
  INDEX `fk_sys_user_sys_role_sys_role_idx` (`sys_role` ASC),
  CONSTRAINT `fk_sys_user_sys_role_sys_user`
    FOREIGN KEY (`sys_user`)
    REFERENCES `exam`.`sys_user` (`username`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_sys_user_sys_role_sys_role`
    FOREIGN KEY (`sys_role`)
    REFERENCES `exam`.`sys_role` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE);

CREATE TABLE `exam`.`sys_role_authoriry` (
  `sys_role` INT NOT NULL,
  `authority` INT NOT NULL,
  PRIMARY KEY (`sys_role`, `authority`),
  INDEX `fk_sys_role_authoriry_authority_idx` (`authority` ASC),
  CONSTRAINT `fk_sys_role_authority_sys_role`
    FOREIGN KEY (`sys_role`)
    REFERENCES `exam`.`sys_role` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_sys_role_authority_authority`
    FOREIGN KEY (`authority`)
    REFERENCES `exam`.`authority` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE);
    
    
CREATE TABLE `exam`.`master_data` (
  `code` VARCHAR(150) NOT NULL,
  `value` VARCHAR(255) NULL,
  `created_by` VARCHAR(25) NOT NULL,
  `created_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` VARCHAR(25) NOT NULL,
  `updated_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`code`));

CREATE TABLE `exam`.`sys_user_authority` (
  `sys_user` VARCHAR(25) NOT NULL,
  `authority` INT(11) NOT NULL,
  `is_grant` INT(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`sys_user`, `authority`),
  INDEX `fk_sys_user_authority_authority_idx` (`authority` ASC),
  CONSTRAINT `fk_sys_user_authority_sys_user`
    FOREIGN KEY (`sys_user`)
    REFERENCES `exam`.`sys_user` (`username`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_sys_user_authority_authority`
    FOREIGN KEY (`authority`)
    REFERENCES `exam`.`authority` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE);

CREATE TABLE `exam`.`country` (
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
    REFERENCES `exam`.`status` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE);

CREATE TABLE `exam`.`password_policy` (
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
    REFERENCES `exam`.`status` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE);




CREATE TABLE `exam`.`password_reset_request` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `sys_user` VARCHAR(25) NOT NULL,
  `status` INT NOT NULL,
  `created_by` VARCHAR(25) NOT NULL,
  `created_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` VARCHAR(25) NOT NULL,
  `updated_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `fk_password_reset_request_status_idx` (`status` ASC),
  CONSTRAINT `fk_password_reset_request_status`
    FOREIGN KEY (`status`)
    REFERENCES `exam`.`status` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  INDEX `fx_password_reset_request_sys_user_idx` (`sys_user` ASC),
  CONSTRAINT `fk_password_reset_request_sys_user`
    FOREIGN KEY (`sys_user`)
    REFERENCES `exam`.`sys_user` (`username`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
);



CREATE TABLE `exam`.`question_category` (
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
  INDEX `fk_question_category_status_idx` (`status` ASC),
  CONSTRAINT `fk_question_category_status`
    FOREIGN KEY (`status`)
    REFERENCES `exam`.`status` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE);

CREATE TABLE `exam`.`question` (
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
  INDEX `fk_question_status_idx` (`status` ASC),
  CONSTRAINT `fk_question_status`
    FOREIGN KEY (`status`)
    REFERENCES `exam`.`status` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE);


CREATE TABLE `exam`.`question_question_category` (
  `question` INT NOT NULL,
  `question_category` INT NOT NULL,
  PRIMARY KEY (`question`, `question_category`),
  CONSTRAINT `fk_question_question_question_category`
    FOREIGN KEY (`question`)
    REFERENCES `exam`.`question` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_question_category_question_question_category`
    FOREIGN KEY (`question_category`)
    REFERENCES `exam`.`question_category` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE);

CREATE TABLE `exam`.`question_answer` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(50) NOT NULL,
  `status` INT NOT NULL,
  `question` INT NOT NULL,
  `correct` BOOLEAN DEFAULT FALSE,
  `created_by` VARCHAR(25) NOT NULL,
  `created_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` VARCHAR(25) NOT NULL,
  `updated_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `fk_question_answer_status_idx` (`status` ASC),
  CONSTRAINT `fk_question_answer_status`
    FOREIGN KEY (`status`)
    REFERENCES `exam`.`status` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  INDEX `fk_question_answer_question_idx` (`question` ASC),
  CONSTRAINT `fk_question_answer_question`
    FOREIGN KEY (`question`)
    REFERENCES `exam`.`question` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE);


CREATE TABLE `exam`.`examination` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(10) NOT NULL,
  `description` VARCHAR(50) NOT NULL,
  `question_category` INT NOT NULL,
  `status` INT NOT NULL,
  `effective_on` DATETIME NULL,
  `expier_on` DATETIME  NULL,
  `created_by` VARCHAR(25) NOT NULL,
  `created_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` VARCHAR(25) NOT NULL,
  `updated_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `code_UNIQUE` (`code` ASC),
  INDEX `fk_examination_question_category_idx` (`status` ASC),
  CONSTRAINT `fk_examination_question_category`
    FOREIGN KEY (`question_category`)
    REFERENCES `exam`.`question_category` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  INDEX `fk_examination_status_idx` (`status` ASC),
  CONSTRAINT `fk_examination_status`
    FOREIGN KEY (`status`)
    REFERENCES `exam`.`status` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE);

    
INSERT INTO `exam`.`status_category` (`code`, `description`) VALUES ('DEFAULT', 'Default');
INSERT INTO `exam`.`status_category` (`code`,`description`) VALUES ('DELETE','Delete');
INSERT INTO `exam`.`status_category` (`code`,`description`) VALUES ('PASREREQ','Password Reset Request');

INSERT INTO `exam`.`status` (`code`,`description`,`status_category`) VALUES ('ACTIVE','Active',1);
INSERT INTO `exam`.`status` (`code`,`description`,`status_category`) VALUES ('INACTIVE','Inactive',1);
INSERT INTO `exam`.`status` (`code`,`description`,`status_category`) VALUES ('DELETE','Delete',2);
INSERT INTO `exam`.`status` (`code`,`description`,`status_category`) VALUES ('REQUEST','Requested',3);
INSERT INTO `exam`.`status` (`code`,`description`,`status_category`) VALUES ('PRESET','Password Reset',3);

INSERT INTO  `exam`.`section`(`code`,`description`,`status`,`created_by`,`updated_by`) VALUES ('USER_MGT_S','User Management',1,'SYSTEM','SYSTEM');
INSERT INTO  `exam`.`section`(`code`,`description`,`status`,`created_by`,`updated_by`) VALUES ('REF_MGT_S','Reference Data Management',1,'SYSTEM','SYSTEM');
INSERT INTO  `exam`.`section`(`code`,`description`,`status`,`created_by`,`updated_by`) VALUES ('SYS_CFG_S','System Configuration',1,'SYSTEM','SYSTEM');

INSERT INTO `exam`.`title` (`code`,`description`,`status`,`created_by`,`updated_by`) VALUES ('CODE_NONE','None',1,'SYSTEM','SYSTEM');
INSERT INTO `exam`.`title` (`code`,`description`,`status`,`created_by`,`updated_by`) VALUES ('CODE_MR','Mr.',1,'SYSTEM','SYSTEM');
INSERT INTO `exam`.`title` (`code`,`description`,`status`,`created_by`,`updated_by`) VALUES ('CODE_MRS','Mrs.',1,'SYSTEM','SYSTEM');
INSERT INTO `exam`.`title` (`code`,`description`,`status`,`created_by`,`updated_by`) VALUES ('CODE_MISS','Miss.',1,'SYSTEM','SYSTEM');
INSERT INTO `exam`.`title` (`code`,`description`,`status`,`created_by`,`updated_by`) VALUES ('CODE_MS','Ms.',1,'SYSTEM','SYSTEM');
INSERT INTO `exam`.`title` (`code`,`description`,`status`,`created_by`,`updated_by`) VALUES ('CODE_PROF','Prof.',1,'SYSTEM','SYSTEM');
INSERT INTO `exam`.`title` (`code`,`description`,`status`,`created_by`,`updated_by`) VALUES ('CODE_DR','Dr.',1,'SYSTEM','SYSTEM');




INSERT INTO `exam`.`sys_role` (`code`,`description`,`status`,`created_by`,`updated_by`) VALUES ('SYSTEM','System',1,'SYSTEM','SYSTEM');

INSERT INTO `exam`.`sys_user` (`username`,`password`,`title`,`name`,`status`,`created_by`,`updated_by`) VALUES ('SYSTEM','$2a$10$/9rQ/RW6jv1DS0uXS4FoxeHvzZPiWpgnB6XdIxjymSYE9UFoGKEnq',1,'System',1,'SYSTEM','SYSTEM');

INSERT INTO `exam`.`authority` (`code`,`description`,`auth_code`,`url`,`section`,`status`,`created_by`,`updated_by`) VALUES ('USER','User Management','ROLE_USER','/user/',1,1,'SYSTEM','SYSTEM');
INSERT INTO `exam`.`authority` (`code`,`description`,`auth_code`,`url`,`section`,`status`,`created_by`,`updated_by`) VALUES ('ROLE','User Role Management','ROLE_ROLE','/sysRole/',1,1,'SYSTEM','SYSTEM');
INSERT INTO `exam`.`authority` (`code`,`description`,`auth_code`,`url`,`section`,`status`,`created_by`,`updated_by`) VALUES ('SECTION','Section Management','ROLE_SECTION','/section/',1,1,'SYSTEM','SYSTEM');
INSERT INTO `exam`.`authority` (`code`,`description`,`auth_code`,`url`,`section`,`status`,`created_by`,`updated_by`) VALUES ('ROLEAUTHOR','User Role Authority Management','ROLE_ROLEAUTHORITY','/sysRoleAuthority/',1,1,'SYSTEM','SYSTEM');
INSERT INTO `exam`.`authority` (`code`,`description`,`auth_code`,`url`,`section`,`status`,`created_by`,`updated_by`) VALUES ('AUTHORITY','Authorry Management','ROLE_AUTHORITY','/authority/',1,1,'SYSTEM','SYSTEM');
INSERT INTO `exam`.`authority` (`code`,`description`,`auth_code`,`url`,`section`,`status`,`created_by`,`updated_by`) VALUES ('TITLE','Title','ROLE_TITLE','/title/',1,1,'SYSTEM','SYSTEM');
INSERT INTO `exam`.`authority` (`code`,`description`,`auth_code`,`url`,`section`,`status`,`created_by`,`updated_by`) VALUES ('USERROLE','Assign System Role to Sys User','ROLE_USERROLE','/sysUserSysRole/',1,1,'SYSTEM','SYSTEM');
INSERT INTO `exam`.`authority` (`code`,`description`,`auth_code`,`url`,`section`,`status`,`created_by`,`updated_by`) VALUES ('MASTERDATA','Master Data Management','ROLE_MASTERDATA','/masterData/',1,1,'SYSTEM','SYSTEM');
INSERT INTO `exam`.`authority` (`code`,`description`,`auth_code`,`url`,`section`,`status`,`created_by`,`updated_by`) VALUES ('USERAUTHOR','Sys User Authority','ROLE_USERAUTHORITY','/sysUserAuthority/',1,1,'SYSTEM','SYSTEM');
INSERT INTO `exam`.`authority` (`code`,`description`,`auth_code`,`url`,`section`,`status`,`created_by`,`updated_by`) VALUES ('COUNTRY','Country','ROLE_COUNTRY','/country/',1,1,'SYSTEM','SYSTEM');
INSERT INTO `exam`.`authority` (`code`,`description`,`auth_code`,`url`,`section`,`status`,`created_by`,`updated_by`) VALUES ('PPOLICY','Password Policy','ROLE_PPOLICY','/passwordPolicy/',1,1,'SYSTEM','SYSTEM');


INSERT INTO `exam`.`sys_user_sys_role` (`sys_user`, `sys_role`) VALUES ('SYSTEM', '1');

INSERT INTO `exam`.`sys_role_authoriry` (`sys_role`, `authority`) VALUES ('1', '1');
INSERT INTO `exam`.`sys_role_authoriry` (`sys_role`, `authority`) VALUES ('1', '2');
INSERT INTO `exam`.`sys_role_authoriry` (`sys_role`, `authority`) VALUES ('1', '3');
INSERT INTO `exam`.`sys_role_authoriry` (`sys_role`, `authority`) VALUES ('1', '4');
INSERT INTO `exam`.`sys_role_authoriry` (`sys_role`, `authority`) VALUES ('1', '5');
INSERT INTO `exam`.`sys_role_authoriry` (`sys_role`, `authority`) VALUES ('1', '6');
INSERT INTO `exam`.`sys_role_authoriry` (`sys_role`, `authority`) VALUES ('1', '7');
INSERT INTO `exam`.`sys_role_authoriry` (`sys_role`, `authority`) VALUES ('1', '8');
INSERT INTO `exam`.`sys_role_authoriry` (`sys_role`, `authority`) VALUES ('1', '9');



<!-- link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet"/-->
$("#status").parent().find(".mdl-menu__container").hide();	

    
    
    