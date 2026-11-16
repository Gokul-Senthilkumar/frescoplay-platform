CREATE TABLE labs (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    category VARCHAR(100) NOT NULL,
    difficulty VARCHAR(30) NOT NULL,
    instructions TEXT NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE lab_completions (
    id BIGINT NOT NULL AUTO_INCREMENT,
    employee_id BIGINT NOT NULL,
    lab_id BIGINT NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'COMPLETED',
    completed_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_lab_completion
        FOREIGN KEY (lab_id) REFERENCES labs(id),
    CONSTRAINT uk_employee_lab
        UNIQUE (employee_id, lab_id)
);

CREATE INDEX idx_lab_completions_employee
ON lab_completions(employee_id);
