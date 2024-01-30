INSERT INTO roles (name)
VALUES ('ROLE_ADMIN'), ('ROLE_USER')
ON CONFLICT (name) DO NOTHING;

INSERT INTO users (username, password, email)
VALUES
    ('spider_bug' ,'$2a$10$SpiderBugPassword', 'spider_bug@example.com')
ON CONFLICT (username) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
VALUES
    ((SELECT id FROM users WHERE username = 'spider_bug'), (SELECT id FROM roles WHERE name = 'ROLE_USER'))
ON CONFLICT (user_id, role_id) DO NOTHING;

--INSERT INTO scripts (user_id, name, raw, bytecode, is_bytecode_valid)
--VALUES
--    ((SELECT id FROM users WHERE username = 'spider_bug'), 'Web Weaver', ':START att isAttack spin',
--    '3a 53 54 41 52 54 20 61 74 74 20 69 73 46 6f 6f 64 20 65 61 74', true);
--
--INSERT INTO scripts (user_id, name, raw, bytecode, is_bytecode_valid)
--VALUES
--    ((SELECT id FROM users WHERE username = 'spider_bug'), 'Venom Strike', ':START att isPoison inject',
--    '3a 53 54 41 52 54 20 61 74 74 20 69 73 46 6f 6f 64 20 65 62 72', true);
--    '3a 53 54 41 52 54 20 61 74 74 20 69 73 46 6f 6f 64 20 65 62 72', true);

