INSERT INTO users (user_id, email, password, roles)
SELECT UUID_TO_BIN(UUID()), 'laika@yahoo.com', '$2a$10$fF2AQDS3R/33cQeZWCdkduaa4HhDEmmiFLShCjpFA1Amay1Qb2M82', 'ROOT, ADMIN, CLIENT'
    WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'laika@yahoo.com'
);

INSERT INTO users (user_id, email, password, roles)
SELECT UUID_TO_BIN(UUID()), 'user@example.com', '$2a$10$SBTD9.f60hvCPP68047sL.aiRsAlx/hPGnfh2z13VfCOqJlXnEeau', 'CLIENT'
    WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'user@example.com'
);

-- ADMIN, CLIENT user
INSERT INTO users (user_id, email, password, roles)
SELECT UUID_TO_BIN(UUID()), 'adminclient@oms.com', '$2a$10$SBTD9.f60hvCPP68047sL.aiRsAlx/hPGnfh2z13VfCOqJlXnEeau', 'ADMIN, CLIENT'
    WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'adminclient@oms.com'
);

-- Regular users
INSERT INTO users (user_id, email, password, roles)
SELECT UUID_TO_BIN(UUID()), 'alice@oms.com', '$2a$10$SBTD9.f60hvCPP68047sL.aiRsAlx/hPGnfh2z13VfCOqJlXnEeau', 'CLIENT'
    WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'alice@oms.com'
);

INSERT INTO users (user_id, email, password, roles)
SELECT UUID_TO_BIN(UUID()), 'bob@oms.com', '$2a$10$SBTD9.f60hvCPP68047sL.aiRsAlx/hPGnfh2z13VfCOqJlXnEeau', 'CLIENT'
    WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'bob@oms.com'
);

INSERT INTO users (user_id, email, password, roles)
SELECT UUID_TO_BIN(UUID()), 'charlie@oms.com', '$2a$10$SBTD9.f60hvCPP68047sL.aiRsAlx/hPGnfh2z13VfCOqJlXnEeau', 'CLIENT'
    WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'charlie@oms.com'
);

