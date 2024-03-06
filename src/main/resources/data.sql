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
    'mov eat eat  rotl :ifWall mov mov :ifAlly rotr mov :goto eat mov',
    '{10, 14, 14, 12, 4, 10, 6, 10, 8, 10}',
    true),
    ((SELECT id FROM users WHERE username = 'spider_bug'), 'Venom Strike',
    'ifEmpty mov noop _goto noop rotl _goto',
    '{33, 10, 0, 35, 0, 12, 35, 0}',
    true),
    ((SELECT id FROM users WHERE username = 'test'), 'Spider Strike',
    'mov eat eat  rotl :ifWall mov mov :ifAlly rotr mov :goto eat mov',
    '{10, 14, 14, 12, 4, 10, 6, 10, 8, 10}',
    true);

