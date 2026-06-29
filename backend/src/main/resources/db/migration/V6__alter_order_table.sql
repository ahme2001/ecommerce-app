ALTER TABLE `orders`
    DROP FOREIGN KEY `fk_order_payment`;

ALTER TABLE `orders`
    DROP COLUMN `payment_id`;

ALTER TABLE `orders`
    ADD COLUMN `paid_amount` BIGINT default 0;

DROP TABLE IF EXISTS `payment`;