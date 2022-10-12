-- application.yml 에서 jpa ddal-auto 는 create-drop은 서버가 시작될때 마다 테이블을 새로 만들기 때문에
--편의상 초기데이터들을 자동으로 db에 넣어주도록 함

insert into users (username, password, nickname, activated) values ('admin', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 'admin', 1);

insert into authority (authority_name) values ('ROLE_USER');
insert into authority (authority_name) values ('ROLE_ADMIN');

insert into user_authority (user_id, authority_name) values (1, 'ROLE_USER');
insert into user_authority (user_id, authority_name) values (1, 'ROLE_ADMIN');