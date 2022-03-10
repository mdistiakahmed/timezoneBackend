DROP TABLE IF EXISTS t_user_authority;
DROP TABLE IF EXISTS t_timezone;
DROP TABLE IF EXISTS t_authority;
DROP TABLE IF EXISTS t_user;


create table t_user (id serial PRIMARY KEY, email varchar(255),firstname varchar(255),lastname varchar(255),sysadmin varchar(255), password varchar(255));
create table t_authority (name VARCHAR(255) PRIMARY KEY);
create table t_user_authority (user_id INT NOT NULL, authority_name VARCHAR(255) NOT NULL, PRIMARY KEY (user_id, authority_name),FOREIGN KEY (authority_name) REFERENCES t_authority (name),FOREIGN KEY (user_id) REFERENCES t_user (id));
create table t_timezone (id serial PRIMARY KEY, name varchar(255),city varchar(255),hourdiff INT,mindiff INT, user_id INT NOT NULL,FOREIGN KEY (user_id) REFERENCES t_user (id));

INSERT INTO t_authority (name) VALUES ('ROLE_ADMIN');
INSERT INTO t_authority (name) VALUES ('ROLE_USER');

INSERT INTO t_user (id, email,firstname, lastname, sysadmin, password) VALUES (500, 'admin@gmail.com','istiak','ahmed',true, '$2a$10$JquPqGKGKSplSc/lvHpsJedbK/xqe2Vghqw.nE17tNdB.UWcIVQ7K');
INSERT INTO t_user (id, email,firstname, lastname, sysadmin, password) VALUES (501, 'user', 'suraiya','begum',false,'$2a$12$TIRkqFy3oTmmVMYVqKa.yeRnmhTOmejLW9UdjmWs7LWTzaRq/E6JK');
INSERT INTO t_user (id, email,firstname, lastname, sysadmin, password) VALUES (502, 'user502','istiak','ahmed',true, '$2a$10$JquPqGKGKSplSc/lvHpsJedbK/xqe2Vghqw.nE17tNdB.UWcIVQ7K');
INSERT INTO t_user (id, email,firstname, lastname, sysadmin, password) VALUES (503, 'user503', 'suraiya','begum',false,'$2a$12$TIRkqFy3oTmmVMYVqKa.yeRnmhTOmejLW9UdjmWs7LWTzaRq/E6JK');
INSERT INTO t_user (id, email,firstname, lastname, sysadmin, password) VALUES (504, 'user504','istiak','ahmed',true, '$2a$10$JquPqGKGKSplSc/lvHpsJedbK/xqe2Vghqw.nE17tNdB.UWcIVQ7K');
INSERT INTO t_user (id, email,firstname, lastname, sysadmin, password) VALUES (505, 'user505', 'suraiya','begum',false,'$2a$12$TIRkqFy3oTmmVMYVqKa.yeRnmhTOmejLW9UdjmWs7LWTzaRq/E6JK');
INSERT INTO t_user (id, email,firstname, lastname, sysadmin, password) VALUES (506, 'user506','istiak','ahmed',true, '$2a$10$JquPqGKGKSplSc/lvHpsJedbK/xqe2Vghqw.nE17tNdB.UWcIVQ7K');
INSERT INTO t_user (id, email,firstname, lastname, sysadmin, password) VALUES (507, 'user507', 'suraiya','begum',false,'$2a$12$TIRkqFy3oTmmVMYVqKa.yeRnmhTOmejLW9UdjmWs7LWTzaRq/E6JK');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (500, 'ROLE_ADMIN');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (501, 'ROLE_USER');