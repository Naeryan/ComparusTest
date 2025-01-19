CREATE TABLE user_table (
    ldap_login  VARCHAR PRIMARY KEY,
    name        VARCHAR,
    surname     VARCHAR
);

INSERT INTO user_table VALUES
    ('user-ldap-1', 'Eren', 'Jeger'),
    ('user-ldap-2', 'Mikasa', 'Ackerman');