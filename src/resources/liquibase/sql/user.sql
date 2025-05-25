CREATE TABLE IF NOT EXISTS public."user"
(
    user_id      VARCHAR(50)  NOT NULL,
    username     VARCHAR(50)  NOT NULL,
    email        VARCHAR(100) NOT NULL UNIQUE,
    phone_number VARCHAR(15) UNIQUE,
    CONSTRAINT user_pkey PRIMARY KEY (user_id)
);