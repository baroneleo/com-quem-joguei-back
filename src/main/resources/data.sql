-- data.sql
-- Dados iniciais para o jogo de futebol

-- Inserir usuários de teste (senha: password123)
INSERT INTO users (id, username, email, password, created_at, updated_at) VALUES 
(gen_random_uuid(), 'jogador1', 'jogador1@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', NOW(), NOW()),
(gen_random_uuid(), 'jogador2', 'jogador2@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', NOW(), NOW()),
(gen_random_uuid(), 'jogador3', 'jogador3@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', NOW(), NOW()),
(gen_random_uuid(), 'admin', 'admin@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', NOW(), NOW());

-- Inserir estatísticas iniciais para os jogadores
WITH user_ids AS (
    SELECT id, username FROM users WHERE username IN ('jogador1', 'jogador2', 'jogador3', 'admin')
)
INSERT INTO player_statistics (id, user_id, matches_played, matches_won, matches_lost, matches_drawn, goals_scored, goals_conceded, points, ranking_position, created_at, updated_at)
SELECT 
    gen_random_uuid(),
    ui.id,
    CASE 
        WHEN ui.username = 'jogador1' THEN 15
        WHEN ui.username = 'jogador2' THEN 12
        WHEN ui.username = 'jogador3' THEN 8
        ELSE 5
    END,
    CASE 
        WHEN ui.username = 'jogador1' THEN 9
        WHEN ui.username = 'jogador2' THEN 7
        WHEN ui.username = 'jogador3' THEN 4
        ELSE 3
    END,
    CASE 
        WHEN ui.username = 'jogador1' THEN 3
        WHEN ui.username = 'jogador2' THEN 3
        WHEN ui.username = 'jogador3' THEN 3
        ELSE 1
    END,
    CASE 
        WHEN ui.username = 'jogador1' THEN 3
        WHEN ui.username = 'jogador2' THEN 2
        WHEN ui.username = 'jogador3' THEN 1
        ELSE 1
    END,
    CASE 
        WHEN ui.username = 'jogador1' THEN 22
        WHEN ui.username = 'jogador2' THEN 18
        WHEN ui.username = 'jogador3' THEN 12
        ELSE 8
    END,
    CASE 
        WHEN ui.username = 'jogador1' THEN 12
        WHEN ui.username = 'jogador2' THEN 15
        WHEN ui.username = 'jogador3' THEN 14
        ELSE 5
    END,
    CASE 
        WHEN ui.username = 'jogador1' THEN 30
        WHEN ui.username = 'jogador2' THEN 23
        WHEN ui.username = 'jogador3' THEN 13
        ELSE 10
    END,
    CASE 
        WHEN ui.username = 'jogador1' THEN 1
        WHEN ui.username =
