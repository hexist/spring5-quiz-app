insert into usr (id, username, password, active)
  values (1, 'admin', '666', true);

insert into user_role (user_id, roles)
  values (1, 'ADMIN'), (1, 'USER');