CREATE TABLE Users
(
    id        BIGSERIAL   PRIMARY KEY,
    name      VARCHAR(25) NOT NULL,
    lastName  VARCHAR(25) NOT NULL,
    email     VARCHAR(25) NOT NULL,
    password  VARCHAR(60) NOT NULL,
    active    BOOLEAN     NOT NULL,
    birthDate TIMESTAMP   NOT NULL
);
