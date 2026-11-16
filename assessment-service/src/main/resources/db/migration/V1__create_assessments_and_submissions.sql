CREATE TABLE assessments (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    course_id BIGINT NOT NULL,
    total_questions INT NOT NULL,
    total_marks INT NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE assessment_submissions (
    id BIGINT NOT NULL AUTO_INCREMENT,
    employee_id BIGINT NOT NULL,
    assessment_id BIGINT NOT NULL,
    score INT NOT NULL,
    total_marks INT NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'SUBMITTED',
    submitted_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_submission_assessment
        FOREIGN KEY (assessment_id) REFERENCES assessments(id),
    CONSTRAINT uk_employee_assessment
        UNIQUE (employee_id, assessment_id)
);

CREATE INDEX idx_submissions_employee
ON assessment_submissions(employee_id);
