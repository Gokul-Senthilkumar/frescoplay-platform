
CREATE TABLE employees (
    id BIGINT NOT NULL AUTO_INCREMENT,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(30) NOT NULL DEFAULT 'EMPLOYEE',
    created_at DATETIME(6) NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT uk_employees_email UNIQUE (email)
);
