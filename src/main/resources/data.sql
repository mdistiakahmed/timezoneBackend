DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS user_roles;


create table users (id serial PRIMARY KEY, email varchar(255), password varchar(255));
create table role (id serial PRIMARY KEY, description varchar(255), name varchar(255));
create table user_roles (user_id INT NOT NULL, role_id INT NOT NULL, PRIMARY KEY (user_id, role_id),FOREIGN KEY (role_id) REFERENCES role (id),FOREIGN KEY (user_id) REFERENCES users (id));

INSERT INTO role (id, description, name) VALUES (4, 'Admin role', 'ADMIN');
INSERT INTO role (id, description, name) VALUES (5, 'User role', 'USER');

INSERT INTO users (id, email, password) VALUES (500, 'admin@gmail.com', '$2a$10$JquPqGKGKSplSc/lvHpsJedbK/xqe2Vghqw.nE17tNdB.UWcIVQ7K');
INSERT INTO user_roles (user_id, role_id) VALUES (500, 4);