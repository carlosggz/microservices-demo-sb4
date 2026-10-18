CREATE TABLE properties (
    id          SERIAL PRIMARY KEY,
    application VARCHAR(255) NOT NULL,
    profile     VARCHAR(255) NOT NULL,
    label       VARCHAR(255) NOT NULL,
    "key"       VARCHAR(255) NOT NULL,
    "value"     TEXT
);
