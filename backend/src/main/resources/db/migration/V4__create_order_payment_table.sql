CREATE TABLE IF NOT EXISTS `payment` (
   `payment_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   `payment_method` VARCHAR(255) NOT NULL,
   `pg_name` VARCHAR(255) NOT NULL,
   `pg_payment_id` VARCHAR(255) NOT NULL,
   `pg_response_message` VARCHAR(255) NOT NULL,
   `pg_status` VARCHAR(255) NOT NULL,
   `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `orders` (
    `order_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `payment_id` BIGINT NOT NULL,
    `address_id` BIGINT NOT NULL,
    `total_price` DOUBLE DEFAULT 0.0,
    `email` VARCHAR(255) NOT NULL,
    `order_status` VARCHAR(255) NOT NULL,
    `order_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT `fk_order_payment`
    FOREIGN KEY (`payment_id`)
    REFERENCES `payment`(`payment_id`) ON DELETE CASCADE  ON UPDATE CASCADE,

    CONSTRAINT `fk_order_address`
    FOREIGN KEY (`address_id`)
    REFERENCES `address`(`address_id`) ON DELETE CASCADE  ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `order_item` (
    `order_item_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `order_id` BIGINT NOT NULL,
    `order_product_price` DOUBLE DEFAULT 0.0,
    `quantity` INT DEFAULT 0,
    `discount` DOUBLE DEFAULT 0.0,
    `product_id` BIGINT,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT `fk_order_orderitem`
    FOREIGN KEY (`order_id`)
    REFERENCES `orders`(`order_id`) ON DELETE CASCADE  ON UPDATE CASCADE,

    CONSTRAINT `fk_product_orderitem`
    FOREIGN KEY (`product_id`)
    REFERENCES `products`(`product_id`) ON DELETE CASCADE  ON UPDATE CASCADE
    ) ENGINE=InnoDB;