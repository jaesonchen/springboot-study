CREATE TABLE permission (
    id int(10) unsigned NOT NULL AUTO_INCREMENT, 
    perm_name varchar(64) NOT NULL, 
    perm_type varchar(64) NOT NULL, 
    description varchar(128), 
    PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
    
CREATE TABLE role (
    id int(10) unsigned NOT NULL AUTO_INCREMENT, 
    role_name varchar(32) NOT NULL, 
    description varchar(128), 
    PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
    
CREATE TABLE role_permission (
    role_id int(10) unsigned NOT NULL, 
    permission_id int(10) unsigned NOT NULL, 
    PRIMARY KEY (role_id, permission_id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
    
CREATE TABLE user (
    id int(10) unsigned NOT NULL AUTO_INCREMENT, 
    username varchar(64) NOT NULL, 
    password varchar(64) NOT NULL, 
    salt varchar(64) NOT NULL, 
    status smallint(5) unsigned DEFAULT '0' NOT NULL, 
    PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE user_role (
    user_id int(10) unsigned NOT NULL, 
    role_id int(10) unsigned NOT NULL, 
    PRIMARY KEY (user_id, role_id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
