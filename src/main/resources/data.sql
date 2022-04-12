DROP TABLE IF EXISTS t_user_authority;
DROP TABLE IF EXISTS t_timezone;
DROP TABLE IF EXISTS t_authority;
DROP TABLE IF EXISTS t_user;


create table t_user (id serial PRIMARY KEY, email varchar(255) NOT NULL UNIQUE,sysadmin varchar(255), password varchar(255));
create table t_authority (name VARCHAR(255) PRIMARY KEY);
create table t_user_authority (email varchar(255) NOT NULL, authority_name VARCHAR(255) NOT NULL, PRIMARY KEY (email, authority_name),FOREIGN KEY (authority_name) REFERENCES t_authority (name),FOREIGN KEY (email) REFERENCES t_user (email));
create table t_timezone (id serial PRIMARY KEY, name varchar(255),city varchar(255),hourdiff INT,mindiff INT, user_id INT NOT NULL,FOREIGN KEY (user_id) REFERENCES t_user (id));

INSERT INTO t_authority (name) VALUES ('ROLE_ADMIN');
INSERT INTO t_authority (name) VALUES ('ROLE_USER');

INSERT INTO t_user (id, email, sysadmin, password) VALUES (500, 'admin@gmail.com',true, '$2a$10$JquPqGKGKSplSc/lvHpsJedbK/xqe2Vghqw.nE17tNdB.UWcIVQ7K');

INSERT INTO t_user_authority (email, authority_name) VALUES ('admin@gmail.com', 'ROLE_ADMIN');