CREATE TABLE IF NOT EXISTS public.bank_account
(
    account_id     BIGSERIAL PRIMARY KEY,
    number_account VARCHAR(36)    NOT NULL UNIQUE DEFAULT gen_random_uuid(),
    balance        DECIMAL(19, 2) NOT NULL        DEFAULT 0,
    user_id        BIGINT         NOT NULL,
    status         VARCHAR(7)     NOT NULL        DEFAULT 'ACTIVE',
    version        BIGINT         NOT NULL        DEFAULT 0,
    CONSTRAINT bank_account_fk_user FOREIGN KEY (user_id)
        REFERENCES "user" (user_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);