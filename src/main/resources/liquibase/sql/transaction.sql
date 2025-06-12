CREATE TABLE IF NOT EXISTS public.transaction
(
    transaction_id         BIGSERIAL      NOT NULL UNIQUE,
    transaction_uuid       VARCHAR(36) PRIMARY KEY DEFAULT gen_random_uuid(),
    amount                 DECIMAL(19, 2) NOT NULL,
    type                   VARCHAR(36)    NOT NULL,
    category               VARCHAR(36)    NOT NULL,
    created_date           TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    bank_name              VARCHAR(100),
    card_number            VARCHAR(100),
    merchant_name          VARCHAR(100),
    digital_wallet_id      VARCHAR(100),
    merchant_category_code VARCHAR(100),
    status                 VARCHAR(7)     NOT NULL DEFAULT 'ACTIVE',
    number_account         VARCHAR(36)    NOT NULL,
    CONSTRAINT transaction_fk_account FOREIGN KEY (number_account)
        REFERENCES bank_account (number_account)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);