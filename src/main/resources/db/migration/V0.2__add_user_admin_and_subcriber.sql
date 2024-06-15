insert into users(id, nick_name, role, email)
values ('a2cd8169-8c7d-4425-8f6a-992415b30bf3'::uuid, 'advanced_user_1', 'USER', 'neogenez42@gmail.com'),
       ('b146431b-d977-47d0-b5d5-740ac0f186ad'::uuid, 'advanced_user_2', 'USER', 'neogenez42@gmail.com'),
       ('0b532d97-d57e-4f4a-8764-f3a98146421f'::uuid, 'advanced_user_3', 'USER', 'neogenez42@gmail.com'),
       ('1d671027-ff14-414a-a3e2-1dbfcbda1425'::uuid, 'honest_admin', 'ADMIN', 'neogenez42@gmail.com'),
       ('ce54f352-e289-40b6-8b86-36cb20e737ee'::uuid, 'giga_chad_super_admin', 'SUPER_ADMIN', 'neogenez42@gmail.com');


insert into rutube_account(id, user_id, value)
values ('5e54f352-e289-40b6-8b86-36cb20e737ee'::uuid, (select id from users where nick_name = 'advanced_user_1'), 0),
       ('4e54f352-e289-40b6-8b86-36cb20e737ee'::uuid, (select id from users where nick_name = 'advanced_user_2'), 0),
       ('3e54f352-e289-40b6-8b86-36cb20e737ee'::uuid, (select id from users where nick_name = 'advanced_user_3'), 0),
       ('2e54f352-e289-40b6-8b86-36cb20e737ee'::uuid, (select id from users where nick_name = 'honest_admin'), 0),
       ('1e54f352-e289-40b6-8b86-36cb20e737ee'::uuid, (select id from users where nick_name = 'giga_chad_super_admin'), 0);


insert into bank_account(id, user_id, value)
values ('9e54f352-e289-40b6-8b86-36cb20e737ee'::uuid, (select id from users where nick_name = 'advanced_user_1'), 0),
       ('8e54f352-e289-40b6-8b86-36cb20e737ee'::uuid, (select id from users where nick_name = 'advanced_user_2'), 0),
       ('7e54f352-e289-40b6-8b86-36cb20e737ee'::uuid, (select id from users where nick_name = 'advanced_user_3'), 0),
       ('6e54f352-e289-40b6-8b86-36cb20e737ee'::uuid, (select id from users where nick_name = 'honest_admin'), 0),
       ('de54f352-e289-40b6-8b86-36cb20e737ee'::uuid, (select id from users where nick_name = 'giga_chad_super_admin'), 0);

insert into subscription_type(name, monthly_pay_rub)
values ('RUTUBE_NIGHT', 199),
       ('RUTUBE_SPORT', 299);
