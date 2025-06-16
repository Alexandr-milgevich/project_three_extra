CREATE TABLE IF NOT EXISTS public.transaction
(
    transaction_id         BIGSERIAL PRIMARY KEY,
    transaction_uuid       VARCHAR(36)    NOT NULL UNIQUE DEFAULT gen_random_uuid(),
    amount                 DECIMAL(19, 2) NOT NULL,
    type                   VARCHAR(36)    NOT NULL,
    category               VARCHAR(36)    NOT NULL,
    created_date           TIMESTAMP      NOT NULL        DEFAULT CURRENT_TIMESTAMP,
    bank_name              VARCHAR(100),
    card_number            VARCHAR(100),
    merchant_name          VARCHAR(100),
    digital_wallet_id      VARCHAR(100),
    merchant_category_code VARCHAR(100),
    status                 VARCHAR(7)     NOT NULL        DEFAULT 'ACTIVE',
    account_id             BIGSERIAL      NOT NULL,
    CONSTRAINT transaction_fk_account FOREIGN KEY (account_id)
        REFERENCES bank_account (account_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);