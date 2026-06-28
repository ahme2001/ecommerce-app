CREATE TABLE IF NOT EXISTS `categories` (
    `category_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `category_name` VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `products` (
    `product_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `product_name` VARCHAR(255) NOT NULL,
    `description` VARCHAR(1000) NOT NULL,
    `image` VARCHAR(300) NOT NULL,
    `quantity` INT DEFAULT 0,
    `price` DOUBLE DEFAULT 0.0,
    `discount` DOUBLE DEFAULT 0.0,
    `special_price` DOUBLE DEFAULT 0.0,
    `category_id` BIGINT,
    `seller_id` BIGINT,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT `fk_product_seller`
        FOREIGN KEY (`seller_id`)
            REFERENCES `users`(`user_id`),

    CONSTRAINT `fk_product_category`
        FOREIGN KEY (`category_id`)
            REFERENCES `categories`(`category_id`)
) ENGINE=InnoDB;

