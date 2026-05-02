-- Usuários de exemplo (senha = "password" - hash BCrypt)
INSERT INTO users (id, username, password, created_at) VALUES
  (1, 'alice',  '$2a$10$7EqJtq98hPqEX7fNZaFWoOq8b6u/5eKxZ1xQe1yGqz1K8r1Y8u1e', now()),
  (2, 'bob',    '$2a$10$7EqJtq98hPqEX7fNZaFWoOq8b6u/5eKxZ1xQe1yGqz1K8r1Y8u1e', now()),
  (3, 'carol',  '$2a$10$7EqJtq98hPqEX7fNZaFWoOq8b6u/5eKxZ1xQe1yGqz1K8r1Y8u1e', now());

-- Estatísticas globais iniciais
INSERT INTO stats (id, user_id, wins, losses) VALUES
  (1, 1, 3, 0),
  (2, 2, 1, 1),
  (3, 3, 0, 2);

-- Estatísticas por país (exemplos)
INSERT INTO country_stats (id, user_id, country, wins, losses) VALUES
  (1, 1, 'spain', 2, 0),
  (2, 1, 'england', 1, 0),
  (3, 2, 'spain', 1, 1),
  (4, 3, 'brazil', 0, 2);

-- Opcional: algumas partidas de exemplo
INSERT INTO matches (id, player1_id, player2_id, winner_id, country, created_at) VALUES
  (1, 1, 2, 1, 'spain', now()),
  (2, 2, 3, 2, 'brazil', now());
