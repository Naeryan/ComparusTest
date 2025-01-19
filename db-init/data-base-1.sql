CREATE DATABASE "data-base-1";
\connect "data-base-1"

CREATE TABLE users (
    user_id     VARCHAR PRIMARY KEY,
    login       VARCHAR,
    first_name  VARCHAR,
    last_name   VARCHAR
);

INSERT INTO users VALUES
    ('user-id-1', 'user-1', 'Ervin', 'Smit'),
    ('user-id-2', 'user-2', 'Kit', 'Sadis'),
    ('user-id-3', 'user-3', 'Levi', 'Ackerman');