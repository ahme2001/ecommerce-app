CREATE TABLE IF NOT EXISTS `users` (
   `user_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   `username` VARCHAR(255) NOT NULL UNIQUE,
   `email` VARCHAR(255) NOT NULL UNIQUE,
   `password` VARCHAR(255) NOT NULL,
    `created_at` timestamp DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `roles` (
   `role_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   `role_name` ENUM ('ROLE_ADMIN', 'ROLE_SELLER', 'ROLE_USER') NOT NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `user_role` (
   `user_id` BIGINT NOT NULL,
   `role_id` INT NOT NULL,
   PRIMARY KEY (`user_id`, `role_id`),
   CONSTRAINT fk_user_role_user
       FOREIGN KEY (`user_id`)
           REFERENCES users(`user_id`)
            ON DELETE CASCADE  ON UPDATE CASCADE,
   CONSTRAINT fk_user_role_role
       FOREIGN KEY (`role_id`)
           REFERENCES roles(`role_id`)
            ON DELETE CASCADE  ON UPDATE CASCADE
) ENGINE=InnoDB;

create table IF NOT EXISTS `address` (
     `address_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
     `user_id` BIGINT,
     `building_name` VARCHAR(255),
     `city` VARCHAR(255),
     `country` VARCHAR(255),
     `postal_code` VARCHAR(255),
     `state` VARCHAR(255),
     `street` VARCHAR(255),
     `created_at` timestamp DEFAULT CURRENT_TIMESTAMP,
     `updated_at` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

      CONSTRAINT fk_user_address
          FOREIGN KEY (user_id)
              REFERENCES users(user_id)
                ON DELETE CASCADE  ON UPDATE CASCADE
) ENGINE=InnoDB;