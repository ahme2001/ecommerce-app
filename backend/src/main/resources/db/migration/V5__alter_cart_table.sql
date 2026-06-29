ALTER TABLE `carts`
    ADD COLUMN `address_id` BIGINT,
    ADD COLUMN `payment_intent_id` VARCHAR(300);

ALTER TABLE `carts`
    ADD CONSTRAINT `fk_cart_address`
    FOREIGN KEY (`address_id`) REFERENCES `address`(`address_id`)
    ON DELETE SET NULL ON UPDATE CASCADE;