CREATE TABLE IF NOT EXISTS public.refund
(
    refund_id      VARCHAR(50)  NOT NULL,
    amount         INTEGER      NOT NULL,
    description    VARCHAR(300) NOT NULL,
    transaction_id VARCHAR(50)  NOT NULL,
    CONSTRAINT refund_pkey PRIMARY KEY (refund_id),
    CONSTRAINT refund_fk_transaction FOREIGN KEY (transaction_id)
        REFERENCES transaction (transaction_id) ON DELETE CASCADE
);