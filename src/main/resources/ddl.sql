
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


CREATE TABLE `job`.`sys_user` (
  `username` VARCHAR(25) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
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
    ON UPDATE CASCADE);
    
    
CREATE TABLE `job`.`authority` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(10) NOT NULL,
  `description` VARCHAR(50) NOT NULL,
  `auth_code` VARCHAR(20) NOT NULL,
  `url` VARCHAR(80) NULL,
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
    ON UPDATE CASCADE);

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
  `authoriry` INT NOT NULL,
  PRIMARY KEY (`sys_role`, `authoriry`),
  INDEX `fk_sys_role_authoriry_authority_idx` (`authoriry` ASC),
  CONSTRAINT `fk_sys_role_authoriry_sys_role`
    FOREIGN KEY (`sys_role`)
    REFERENCES `job`.`sys_role` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_sys_role_authoriry_authority`
    FOREIGN KEY (`authoriry`)
    REFERENCES `job`.`authority` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE);

    
INSERT INTO `job`.`status_category` (`code`, `description`) VALUES ('DEFAULT', 'Default');

INSERT INTO `job`.`sys_role` (`code`, `description`, `status`, `created_by`, `updated_by`) VALUES ('SYSTEM', 'System', '1', 'SYSTEM', 'SYSTEM');

INSERT INTO `job`.`sys_user` (`username`, `password`, `name`, `status`, `created_by`, `updated_by`) VALUES ('SYSTEM', 'SYSTEM', 'SYSEM', '1', 'SYSTEM', 'SYSTEM');

INSERT INTO `job`.`authority` (`code`, `description`, `auth_code`, `url`, `status`, `created_by`, `updated_by`) VALUES ('USER', 'User Management', 'ROLE_USER', 'viewUser', '1', 'SYSTEM', 'SYSTEM');





    
    
    