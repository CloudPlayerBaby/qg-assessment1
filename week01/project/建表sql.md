create DATABASE repair_db;
use repair_db;
create table user(
    id int auto_increment PRIMARY key,
  sid VARCHAR(32) not null UNIQUE,
    password varchar(32) not null,
    identity varchar(32)
);
create table repair_form(
    id int auto_increment PRIMARY KEY,
    user_id int,
        type varchar(32),
    problem varchar(200),
    status int
);
ALTER table repair_form ADD CONSTRAINT fk_repair_user FOREIGN KEY(user_id) REFERENCES user(id);
