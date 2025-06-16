CREATE TABLE IF NOT EXISTS public.refund
(
    refund_id      BIGSERIAL PRIMARY KEY,
    amount         DECIMAL(19, 2) NOT NULL,
    description    VARCHAR(300),
    transaction_id BIGSERIAL      NOT NULL,
    CONSTRAINT refund_fk_transaction FOREIGN KEY (transaction_id)
        REFERENCES transaction (transaction_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);