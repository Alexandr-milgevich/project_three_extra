CREATE TABLE IF NOT EXISTS public.account
(
    account_id VARCHAR(50) NOT NULL,
    balance    INTEGER     NOT NULL,
    user_id    VARCHAR(50) NOT NULL,
    CONSTRAINT account_pkey PRIMARY KEY (account_id),
    CONSTRAINT account_fk_user FOREIGN KEY (user_id)
        REFERENCES "user" (user_id) ON DELETE CASCADE
);