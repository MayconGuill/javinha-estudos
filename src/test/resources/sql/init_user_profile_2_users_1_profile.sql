INSERT INTO user (id, email, first_name, last_name) VALUES (1, 'rimuru@gmail.com', 'Rimuru', 'Tempest');
INSERT INTO user (id, email, first_name, last_name) VALUES (2, 'veldora@gmail.com', 'Veldora', 'Tempest');
INSERT INTO profile (id, description, name) VALUES (1, 'Administrator', 'Admin');
INSERT INTO user_profile (id, user_id, profile_id) VALUES (1, 1, 1);
INSERT INTO user_profile (id, user_id, profile_id) VALUES (2, 2, 1);