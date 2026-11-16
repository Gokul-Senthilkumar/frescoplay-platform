INSERT INTO labs
(id, title, description, category, difficulty, instructions, created_at, updated_at)
VALUES
(
    1,
    'AWS S3 Security Lab',
    'Practice securing an Amazon S3 bucket.',
    'AWS Security',
    'BEGINNER',
    '1. Create an S3 bucket.
2. Block public access.
3. Enable default encryption.
4. Review bucket policy and permissions.',
    NOW(6),
    NOW(6)
);