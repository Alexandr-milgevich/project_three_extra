CREATE TABLE IF NOT EXISTS public.bank_account
(
    number  BIGINT     NOT NULL UNIQUE,
    balance INTEGER    NOT NULL,
    user_id BIGINT     NOT NULL,

    status  VARCHAR(7) NOT NULL,
    CONSTRAINT bank_account_pkey PRIMARY KEY (number),
    CONSTRAINT bank_account_fk_user FOREIGN KEY (user_id)
        REFERENCES "user" (user_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);