CREATE TABLE IF NOT EXISTS public."user"
(
    user_id      BIGSERIAL    PRIMARY KEY,
    username     VARCHAR(50)  NOT NULL,
    email        VARCHAR(100) NOT NULL UNIQUE,
    phone_number VARCHAR(15)  NOT NULL UNIQUE,
    status       VARCHAR(7)   NOT NULL DEFAULT 'ACTIVE'
);