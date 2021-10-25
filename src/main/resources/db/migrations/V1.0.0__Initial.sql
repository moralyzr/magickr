CREATE TABLE Users
(
    id        BIGSERIAL PRIMARY KEY,
    name      VARCHAR(25) NOT NULL,
    lastName  VARCHAR(25) NOT NULL,
    email     VARCHAR(50) NOT NULL,
    password  VARCHAR(60) NOT NULL,
    active    BOOLEAN     NOT NULL,
    birthDate TIMESTAMP   NOT NULL
);

CREATE TABLE Adventurers
(
    id                BIGSERIAL PRIMARY KEY,
    userId            BIGINT       NOT NULL,
    avatar            VARCHAR(255) NOT NULL,
    name              VARCHAR(25)  NOT NULL,
    title             VARCHAR(25),
    level             SMALLINT     NOT NULL,
    currentExperience INTEGER      NOT NULL,

    CONSTRAINT fk_user FOREIGN KEY (userId) REFERENCES Users (id) ON DELETE CASCADE
);
