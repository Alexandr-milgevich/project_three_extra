CREATE TABLE IF NOT EXISTS public.refund
(
    refund_id        BIGSERIAL      PRIMARY KEY,
    amount           DECIMAL(19, 2) NOT NULL,
    description      VARCHAR(300),
    transaction_uuid VARCHAR(36)    NOT NULL,
    CONSTRAINT refund_fk_transaction FOREIGN KEY (transaction_uuid)
        REFERENCES transaction (transaction_uuid)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);