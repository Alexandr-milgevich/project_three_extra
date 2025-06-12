CREATE TABLE IF NOT EXISTS public.user_status_audit
(
    audit_id   BIGSERIAL PRIMARY KEY,
    user_id    BIGINT     NOT NULL,
    old_status VARCHAR(7) NOT NULL,
    new_status VARCHAR(7) NOT NULL,
    changed_at TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    reason     VARCHAR(300),
    CONSTRAINT user_status_audit_fk_user FOREIGN KEY (user_id)
        REFERENCES "user" (user_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);