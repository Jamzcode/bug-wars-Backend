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

INSERT INTO scripts (user_id, name, raw, bytecode, is_bytecode_valid)
VALUES
    ((SELECT id FROM users WHERE username = 'spider_bug'), 'Web Weaver',
    'ifEnemy mov ifFood eat ifWall rotr ifAlly rotr mov _goto noop mov _goto eat _goto',
    '{30, 10, 32, 14, 34, 11, 31, 11, 10, 35, 0, 10, 35, 0, 14, 35, 0}',
    true),
    ((SELECT id FROM users WHERE username = 'spider_bug'), 'Venom Strike',
    'ifEmpty mov noop _goto noop rotl _goto',
    '{33, 10, 0, 35, 0, 12, 35, 0}',
    true),
    ((SELECT id FROM users WHERE username = 'test'), 'Spider Strike',
    'att att ifFood mov mov rotr _goto ifEnemy mov rotr rotr _goto noop',
    '{13, 13, 32, 10, 10, 11, 12, 35, 10, 14, 11, 11, 35, 0}',
    true);

