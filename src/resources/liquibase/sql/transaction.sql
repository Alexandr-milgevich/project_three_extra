CREATE TABLE IF NOT EXISTS public.transaction
(
    transaction_id         VARCHAR(50) NOT NULL,
    amount                 INTEGER     NOT NULL,
    type                   VARCHAR(36) NOT NULL,
    category               VARCHAR(36) NOT NULL,
    created_date           VARCHAR(10) NOT NULL,

    bank_name              VARCHAR(100),
    card_number            VARCHAR(100),
    merchant_name          INTEGER,
    digital_wallet_id      VARCHAR(100),
    merchant_category_code VARCHAR(100),

    account_id             VARCHAR(36) NOT NULL,
    CONSTRAINT transaction_pkey PRIMARY KEY (transaction_id),
    CONSTRAINT transaction_fk_account FOREIGN KEY (account_id)
        REFERENCES account (account_id) ON DELETE CASCADE
);