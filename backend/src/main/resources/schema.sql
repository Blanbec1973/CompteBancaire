
CREATE TABLE IF NOT EXISTS nature (
    code VARCHAR(10) PRIMARY KEY,
    label VARCHAR(100) NOT NULL,
    requires_cheque_number BOOLEAN NOT NULL
);
