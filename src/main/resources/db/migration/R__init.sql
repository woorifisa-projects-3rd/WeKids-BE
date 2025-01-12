-- -- `member` 테이블에 대한 더미 데이터
INSERT INTO member (id, `name`, member_type, phone, birthday, profile, email, simple_password, `state`, inactive_date, card_state, bank_member_id, created_at, updated_at, `role`, social)
VALUES
    (1, '강현우', 'PARENT', '010-1234-5678', '1985-01-05', 'DADAPING', 'wekids1@naver.com', '{bcrypt}$2a$10$H4hK3cIdFrHEk7dSiLF/KeBZcp99zsX2qCb6PlfupNcxZUPh3Ev7O', 'ACTIVE', null, 'NONE', 1, '2024-01-01 00:00:00', '2024-01-01 00:00:00', 'ROLE_PARENT', 'naver'),
    (2, '구자빈', 'CHILD', '010-2345-6789', '2018-03-03', 'CHACHAPING', 'wekids2@naver.com', '{bcrypt}$2a$10$A.yrgthceIagp5pCoPNbvug3sSpLBBBZWGZk/A8Wr4EpSO4JDvOr6', 'ACTIVE', null, 'CREATED', 2, '2024-01-01 00:00:00', '2024-01-01 00:00:00', 'ROLE_CHILD', 'naver'),
    (3, '최윤정', 'CHILD', '010-3456-6789', '2019-05-02', 'DADAPING', 'wekids3@naver.com', '{bcrypt}$2a$10$oVd5oA3bSa4zh8JSiCLyeeVaRId17Lm.ay5ALzu9kGJD8wpNtlody', 'ACTIVE', null, 'NONE', null, '2024-01-01 00:00:00', '2024-01-01 00:00:00', 'ROLE_CHILD', 'naver');

INSERT INTO account (id, account_number, balance, password, `state`, inactive_date, member_id, created_at, updated_at)
VALUES
    (1, '1002-913-023908', 2800000, NULL, 'ACTIVE', NULL, 1, '2024-01-01 00:00:00', '2024-12-09 00:00:00'), -- 강현우
    (2, '1002-913-023909', 400000, '5678', 'ACTIVE', NULL, 2, '2024-01-01 00:00:00', '2024-12-09 00:00:00'); -- 구자빈

INSERT INTO card (id, card_number, valid_thru, cvc, member_name, password, card_name, `state`, inactive_date, new_date, account_id, created_at, updated_at)
VALUES
    (1, '5159-5432-1098-7654', '2026-12-31', '456', '구자빈', '5678', '자빈핑', 'ACTIVE', NULL, '2024-01-01 00:00:00', 2, '2024-01-01 00:00:00', '2024-01-01 00:00:00');

INSERT INTO design (member_id, color, `character`, account_id, card_id)
VALUES
    (1, 'BLUE', 'DADAPING', 1, NULL),  -- 강현우
    (2, 'GREEN', 'CHACHAPING', 2, 1);  -- 구자빈

INSERT INTO parent_child (`parent_id`, `child_id`, created_at, updated_at)
VALUES
    (1, 2, '2024-01-01 00:00:00', '2024-01-01 00:00:00'),
    (1, 3, '2024-01-01 00:00:00', '2024-01-01 00:00:00');