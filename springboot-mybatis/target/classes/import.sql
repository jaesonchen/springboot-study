DROP TABLE IF EXISTS contact;
CREATE TABLE contact (
    id int(10) NOT NULL AUTO_INCREMENT, 
    user_id int(10) NOT NULL, 
    address varchar(45) NOT NULL, 
    zip_code int(10) NOT NULL, 
    PRIMARY KEY (id)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS department;
CREATE TABLE department (
    id int(10) NOT NULL AUTO_INCREMENT, 
    parent_id int(10) DEFAULT '0' NOT NULL, 
    dept_name varchar(45) NOT NULL, 
    PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS user;
CREATE TABLE user (
    id int(10) NOT NULL AUTO_INCREMENT, 
    user_name varchar(45) NOT NULL, 
    password varchar(45) NOT NULL, 
    dept_id int(10) DEFAULT '0' NOT NULL, 
    PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS role;
CREATE TABLE role (
    id int(10) NOT NULL AUTO_INCREMENT, 
    role_name varchar(45) NOT NULL, 
    role_type varchar(45) NOT NULL, 
    PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS user_role;
CREATE TABLE user_role (
    user_id int(10) NOT NULL, 
    role_id int(10) NOT NULL, 
    PRIMARY KEY (user_id, role_id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
