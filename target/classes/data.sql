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

INSERT INTO scripts (user_id, name, raw, bytecode, is_bytecode_valid)
VALUES
    ((SELECT id FROM users WHERE username = 'spider_bug'), 'Web Weaver', 'Script 1',
    '[30, 11, 32, 14, 34, 17, 31, 17, 10, 35, 0, 13, 35, 0, 14, 35, 0, 11, 35, 0]', true),
    ((SELECT id FROM users WHERE username = 'spider_bug'), 'Venom Strike', 'Script 2',
    '[33, 5, 0, 35, 0, 12, 35, 0]', true),
     ((SELECT id FROM users WHERE username = 'spider_bug'), 'Spider Strike', 'Script 3',
     '[13, 13, 32, 9, 10, 11, 12, 35, 9, 14, 11, 11, 35, 0]', true);

