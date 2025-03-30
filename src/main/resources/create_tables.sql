/* Delete old */
DROP TABLE IF EXISTS Weather;
DROP TABLE IF EXISTS City;
DROP TABLE IF EXISTS State;

/* Create new */
CREATE TABLE State
(
    id   VARCHAR(16) PRIMARY KEY NOT NULL,
    name VARCHAR(128)            NOT NULL
);

CREATE TABLE City
(
    id      INT PRIMARY KEY NOT NULL,
    stateId VARCHAR(16)     NOT NULL,
    name    VARCHAR(128)    NOT NULL
);

ALTER TABLE City
    ADD FOREIGN KEY (stateId) REFERENCES State (id);

CREATE TABLE Weather
(
    id          INT PRIMARY KEY NOT NULL,
    cityId      INT             NOT NULL,
    temperature INT             NOT NULL,
    feelsLike   INT             NOT NULL,
    pressure    FLOAT           NOT NULL,
    humidity    FLOAT           NOT NULL,
    description VARCHAR(128)    NOT NULL
);

ALTER TABLE Weather
    ADD FOREIGN KEY (cityId) REFERENCES City (id);
