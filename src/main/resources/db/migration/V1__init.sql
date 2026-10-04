CREATE TABLE IF NOT EXISTS persons (
                                       id      SERIAL PRIMARY KEY,
                                       name    VARCHAR(80) NOT NULL,
    age     INTEGER,
    address VARCHAR(255),
    work    VARCHAR(255)
    );

CREATE INDEX IF NOT EXISTS idx_persons_name ON persons (name);