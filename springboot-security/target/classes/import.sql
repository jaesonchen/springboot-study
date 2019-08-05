CREATE TABLE persistent_logins (
    username varchar(64) NOT NULL, 
    series varchar(64) NOT NULL, 
    token varchar(64) NOT NULL, 
    last_used timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
    PRIMARY KEY (series)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
    
CREATE TABLE role (
    id int(10) unsigned NOT NULL AUTO_INCREMENT, 
    role_name varchar(45) NOT NULL, 
    description varchar(45) NOT NULL, 
    PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
    
CREATE TABLE user (
    id int(10) unsigned NOT NULL AUTO_INCREMENT, 
    username varchar(32) NOT NULL, 
    password varchar(128) NOT NULL, 
    PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
    
CREATE TABLE user_role (
    role_id int(10) unsigned NOT NULL, 
    user_id int(10) unsigned NOT NULL, 
    PRIMARY KEY (role_id, user_id), 
    CONSTRAINT FK859n2jvi8ivhui0rl0esws6o FOREIGN KEY (user_id) REFERENCES user (id) , 
    CONSTRAINT FKa68196081fvovjhkek5m97n3y FOREIGN KEY (role_id) REFERENCES role (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
