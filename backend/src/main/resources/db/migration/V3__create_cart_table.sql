CREATE TABLE IF NOT EXISTS `carts` (
    `cart_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT UNIQUE,
    `total_price` DOUBLE DEFAULT 0.0,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT `fk_user_cart`
        FOREIGN KEY (`user_id`)
            REFERENCES `users`(`user_id`) ON DELETE CASCADE  ON UPDATE CASCADE
) ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS `cart_items` (
    `cart_item_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `cart_id` BIGINT NOT NULL,
    `product_price` DOUBLE DEFAULT 0.0,
    `quantity` INT DEFAULT 0,
    `discount` DOUBLE DEFAULT 0.0,
    `product_id` BIGINT,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT `fk_cart_cartitem`
        FOREIGN KEY (`cart_id`)
            REFERENCES `carts`(`cart_id`) ON DELETE CASCADE  ON UPDATE CASCADE,

    CONSTRAINT `fk_product_cartitem`
        FOREIGN KEY (`product_id`)
            REFERENCES `products`(`product_id`) ON DELETE CASCADE  ON UPDATE CASCADE
) ENGINE=InnoDB;