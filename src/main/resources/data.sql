INSERT IGNORE INTO users (user_id, username, password) VALUES ('testUser', '테스트 유저', '1234');

INSERT IGNORE INTO list_tb (content, priority, start_date, end_date, done, user_id)
VALUES ('밥 먹기', 2, '2025-02-09 10:20:20', '2025-02-14 20:20:20', false, 'testUser');

INSERT IGNORE INTO list_tb (content, priority, start_date, end_date, done, user_id)
VALUES ('수달이랑 약속', 1, '2025-02-10 20:20:20', '2025-02-15 20:20:20', false, 'testUser');

INSERT IGNORE INTO list_tb (content, priority, start_date, end_date, done, user_id)
VALUES ('공부 하기', 1, '2025-02-10 10:20:20', '2025-02-16 20:20:20', false, 'testUser');

INSERT IGNORE INTO list_tb (content, priority, start_date, end_date, done, user_id)
VALUES ('유튜브 보기', 3, '2025-02-10 20:20:20', '2025-02-15 20:20:20', false, 'testUser');

INSERT IGNORE INTO list_tb (content, priority, start_date, end_date, done, user_id)
VALUES ('카페 가기', 2, '2025-02-11 20:20:20', '2025-02-16 20:20:20', false, 'testUser');
