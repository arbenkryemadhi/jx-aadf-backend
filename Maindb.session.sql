CREATE TABLE IF NOT EXISTS app_user (
    app_user_id TEXT PRIMARY KEY,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    email TEXT NOT NULL
);
CREATE TABLE IF NOT EXISTS staff (
    staff_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email TEXT NOT NULL
);
INSERT INTO staff (email)
VALUES ('');