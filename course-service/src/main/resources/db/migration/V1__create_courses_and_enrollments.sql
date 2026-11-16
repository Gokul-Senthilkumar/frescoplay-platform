CREATE TABLE courses (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    category VARCHAR(100) NOT NULL,
    duration_hours INT NOT NULL,
    level VARCHAR(30) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE enrollments (
    id BIGINT NOT NULL AUTO_INCREMENT,
    employee_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    progress_percentage INT NOT NULL DEFAULT 0,
    status VARCHAR(30) NOT NULL DEFAULT 'ENROLLED',
    enrolled_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_enrollments_course
        FOREIGN KEY (course_id) REFERENCES courses(id),
    CONSTRAINT uk_employee_course
        UNIQUE (employee_id, course_id),
    CONSTRAINT chk_progress_percentage
        CHECK (progress_percentage BETWEEN 0 AND 100)
);

CREATE INDEX idx_enrollments_employee
ON enrollments(employee_id);
