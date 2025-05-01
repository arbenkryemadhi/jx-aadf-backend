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
VALUES ('arbenofc@gmail.com');

CREATE TABLE IF NOT EXISTS tender (
    tender_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title TEXT NOT NULL,
    description TEXT NOT NULL,
    status TEXT NOT NULL,
    author TEXT NOT NULL,
    created_date TEXT NOT NULL,
    deadline TEXT NOT NULL,
    budget TEXT NOT NULL,
    winning_proposal_id INT
);