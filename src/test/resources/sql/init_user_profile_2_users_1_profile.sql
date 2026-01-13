INSERT INTO "User" ("id", "email", "firstName", "lastName") VALUES (1, 'rimuru@gmail.com', 'Rimuru', 'Tempest');
INSERT INTO "User" ("id", "email", "firstName", "lastName") VALUES (2, 'veldora@gmail.com', 'Veldora', 'Tempest');
INSERT INTO "Profile" ("id", "description", "name") VALUES (1, 'Administrator', 'Admin');
INSERT INTO "UserProfile" ("id", "user_id", "profile_id") VALUES (1, 1, 1);
INSERT INTO "UserProfile" ("id", "user_id", "profile_id") VALUES (2, 2, 1);