CREATE DATABASE population;

USE population;

SET default_storage_engine=INNODB;

CREATE TABLE users (
	id INT(10) AUTO_INCREMENT,
	login VARCHAR(20),
	pwd VARCHAR(40),
	typus VARCHAR(6),
	PRIMARY KEY(id)
);

INSERT INTO users (login, pwd, typus) VALUES ("admin", "21232f297a57a5a743894a0e4a801fc3", "admin");


usersCREATE USER 'admin' IDENTIFIED BY '21232f297a57a5a743894a0e4a801fc3';*/

GRANT ALL PRIVILEGES ON *.* TO 'admin' WITH GRANT OPTION;

FLUSH PRIVILEGES;