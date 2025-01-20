CREATE TABLE IF NOT EXISTS `member` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `birthday` DATE NOT NULL,
    `bank_member_id` BIGINT,
    `created_at` DATETIME(6) NOT NULL,
    `inactive_date` DATETIME(6),
    `updated_at` DATETIME(6) NOT NULL,
    `name` VARCHAR(20) NOT NULL,
    `phone` VARCHAR(20) NOT NULL,
    `member_type` VARCHAR(31) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `role` VARCHAR(255),
    `simple_password` VARCHAR(255),
    `social` VARCHAR(255) NOT NULL,
    `card_state` ENUM('CREATED', 'NONE', 'READY') NOT NULL,
    `profile` ENUM('CHACHAPING', 'DADAPING', 'GOGOPING', 'HAPPYING', 'HEARTSPRING', 'LALAPING'),
    `state` ENUM('ACTIVE', 'LEAVE', 'SLEEP') NOT NULL,
    PRIMARY KEY (`id`)
)
ENGINE=InnoDB
AUTO_INCREMENT=1
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `mission` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `amount` DECIMAL(38,2) NOT NULL,
    `deadline` DATE NOT NULL,
    `child_id` BIGINT NOT NULL,
    `created_at` DATETIME(6) NOT NULL,
    `parent_id` BIGINT NOT NULL,
    `updated_at` DATETIME(6) NOT NULL,
    `image` VARCHAR(255),
    `memo` VARCHAR(255),
    `title` VARCHAR(255) NOT NULL,
    `category` ENUM ('ETC','HOUSE_WORK','LIFESTYLE_HABITS','SELF_DEVELOPMENT') NOT NULL,
    `content` TEXT,
    `state` ENUM ('ACCEPT','CANCEL','NEW','OUTDATED','SUBMIT') NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_mission_child` FOREIGN KEY (`child_id`) REFERENCES `member` (`id`),
    CONSTRAINT `FK_mission_parent` FOREIGN KEY (`parent_id`) REFERENCES `member` (`id`)
) ENGINE=InnoDB
AUTO_INCREMENT=1
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `parent_child` (
    `child_id` BIGINT NOT NULL,
    `created_at` DATETIME(6) NOT NULL,
    `parent_id` BIGINT NOT NULL,
    `updated_at` DATETIME(6) NOT NULL,
    PRIMARY KEY (`child_id`, `parent_id`),
    CONSTRAINT `FK_parent_child_child` FOREIGN KEY (`child_id`) REFERENCES `member` (`id`),
    CONSTRAINT `FK_parent_child_parent` FOREIGN KEY (`parent_id`) REFERENCES `member` (`id`)
) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `account` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `balance` DECIMAL(20,2) NOT NULL,
    `created_at` DATETIME(6) NOT NULL,
    `inactive_date` DATETIME(6),
    `member_id` BIGINT NOT NULL,
    `updated_at` DATETIME(6) NOT NULL,
    `account_number` VARCHAR(50) NOT NULL,
    `password` VARCHAR(255),
    `state` ENUM ('ACTIVE','INACTIVE') NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_account_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`)
) ENGINE=InnoDB
AUTO_INCREMENT=1
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `account_transaction` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `amount` DECIMAL(20,2) NOT NULL,
    `balance` DECIMAL(20,2) NOT NULL,
    `account_id` BIGINT NOT NULL,
    `created_at` DATETIME(6) NOT NULL,
    `memo` VARCHAR(255),
    `receiver` VARCHAR(255) NOT NULL,
    `sender` VARCHAR(255) NOT NULL,
    `title` VARCHAR(255) NOT NULL,
    `type` ENUM ('DEPOSIT','WITHDRAWAL') NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_transaction_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB
AUTO_INCREMENT=1
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `alarm` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `is_checked` BIT NOT NULL,
    `created_at` DATETIME(6) NOT NULL,
    `member_id` BIGINT NOT NULL,
    `target_id` BIGINT,
    `updated_at` DATETIME(6) NOT NULL,
    `target_state` VARCHAR(255),
    `type` ENUM ('CARD','MISSION') NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_alarm_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`)
) ENGINE=InnoDB
AUTO_INCREMENT=1
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `card` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `valid_thru` DATE NOT NULL,
    `account_id` BIGINT NOT NULL,
    `created_at` DATETIME(6) NOT NULL,
    `inactive_date` DATETIME(6),
    `new_date` DATETIME(6) NOT NULL,
    `updated_at` DATETIME(6) NOT NULL,
    `card_name` VARCHAR(255) NOT NULL,
    `card_number` VARCHAR(255) NOT NULL,
    `cvc` VARCHAR(255) NOT NULL,
    `member_name` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `state` ENUM ('ACTIVE','INACTIVE') NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_card_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
    CONSTRAINT `UK_card_account` UNIQUE (`account_id`)
) ENGINE=InnoDB
AUTO_INCREMENT=1
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `design` (
    `account_id` BIGINT,
    `card_id` BIGINT,
    `member_id` BIGINT NOT NULL,
    `character` ENUM ('CHACHAPING','DADAPING','GOGOPING','HAPPYING','HEARTSPRING','LALAPING') NOT NULL,
    `color` ENUM ('BLUE','GREEN','PINK1','PINK2','PURPLE','YELLOW') NOT NULL,
    PRIMARY KEY (`member_id`),
    CONSTRAINT `FK_design_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
    CONSTRAINT `FK_design_card` FOREIGN KEY (`card_id`) REFERENCES `card` (`id`),
    CONSTRAINT `FK_design_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`),
    CONSTRAINT `UK_design_account` UNIQUE (`account_id`),
    CONSTRAINT `UK_design_card` UNIQUE (`card_id`)
) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `refresh_token`
(
    `id`              BIGINT       NOT NULL auto_increment,
    `member_id`       BIGINT       NOT NULL,
    `token`           VARCHAR(255) NOT NULL,
    `expiration_time` TIMESTAMP    NOT NULL,
    PRIMARY KEY (`id`)
)
engine = innodb
auto_increment = 1
DEFAULT charset = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;