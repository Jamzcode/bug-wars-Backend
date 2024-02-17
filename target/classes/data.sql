INSERT INTO roles (name)
VALUES ('ROLE_ADMIN'), ('ROLE_USER')
ON CONFLICT (name) DO NOTHING;

INSERT INTO users (username, password, email)
VALUES
    ('spider_bug' ,'$2a$10$SpiderBugPassword', 'spider_bug@example.com'),
    ('test', '$2a$10$Fj9DarsKJLbbwDPDq6N7puQV2H7s69maXrHATRZVLpAX9gi2c2CpC', 'email@gmail.com')
ON CONFLICT (username) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
VALUES
    ((SELECT id FROM users WHERE username = 'spider_bug'), (SELECT id FROM roles WHERE name = 'ROLE_USER')),
    ((SELECT id FROM users WHERE username = 'test'), (SELECT id FROM roles WHERE name = 'ROLE_USER'))
ON CONFLICT (user_id, role_id) DO NOTHING;

<<<<<<< HEAD
--INSERT INTO scripts (user_id, name, raw, bytecode, is_bytecode_valid)
--VALUES
--    ((SELECT id FROM users WHERE username = 'spider_bug'), 'Web Weaver', ':START att isAttack spin',
--    '3a 53 54 41 52 54 20 61 74 74 20 69 73 46 6f 6f 64 20 65 61 74', true);
--
--INSERT INTO scripts (user_id, name, raw, bytecode, is_bytecode_valid)
--VALUES
--    ((SELECT id FROM users WHERE username = 'spider_bug'), 'Venom Strike', ':START att isPoison inject',
<<<<<<< HEAD
--    '3a 53 54 41 52 54 20 61 74 74 20 69 73 46 6f 6f 64 20 65 62 72', true);
=======
--    '3a 53 54 41 52 54 20 61 74 74 20 69 73 46 6f 6f 64 20 65 62 72', true);
>>>>>>> 4cd38007c9bf72b0f9c322b4146424ab3c3f4ba2
=======
INSERT INTO scripts (user_id, name, raw, bytecode, is_bytecode_valid)
VALUES
    ((SELECT id FROM users WHERE username = 'spider_bug'), 'Web Weaver', 'Script 1',
    '[30, 11, 32, 14, 34, 17, 31, 17, 10, 35, 0, 13, 35, 0, 14, 35, 0, 11, 35, 0]', true),
    ((SELECT id FROM users WHERE username = 'spider_bug'), 'Venom Strike', 'Script 2',
    '[33, 5, 0, 35, 0, 12, 35, 0]', true),
     ((SELECT id FROM users WHERE username = 'test'), 'Spider Strike', 'Script 3',
     '[13, 13, 32, 9, 10, 11, 12, 35, 9, 14, 11, 11, 35, 0]', true);

>>>>>>> a4ad898a2b851e59aa3476aa80c0043e385e42a6
