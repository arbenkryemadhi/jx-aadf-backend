CREATE TABLE IF NOT EXISTS app_user (
    app_user_id TEXT PRIMARY KEY,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    email TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS staff (
    staff_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    staff_email TEXT,
    admin_email TEXT
);

INSERT INTO staff (staff_email)
VALUES ('arbenofc@gmail.com');

CREATE TABLE IF NOT EXISTS tender (
    tender_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title TEXT NOT NULL,
    description TEXT NOT NULL,
    status TEXT NOT NULL,
    author_id TEXT NOT NULL,
    created_date TEXT NOT NULL,
    deadline TEXT NOT NULL,
    budget TEXT NOT NULL,
    winning_proposal_id INT,
    document_links TEXT[] DEFAULT '{}',
    FOREIGN KEY (author_id) REFERENCES app_user(app_user_id)
);

CREATE TABLE IF NOT EXISTS proposal (
    proposal_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    tender_id INT NOT NULL,
    author_id TEXT NOT NULL,
    title TEXT NOT NULL,
    description TEXT NOT NULL,
    price TEXT NOT NULL,
    status TEXT NOT NULL,
    created_date TEXT NOT NULL,
    FOREIGN KEY (tender_id) REFERENCES tender(tender_id),
    FOREIGN KEY (author_id) REFERENCES app_user(app_user_id)
);

CREATE TABLE IF NOT EXISTS proposal_review (
    proposal_review_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    proposal_id INT NOT NULL,
    author_id TEXT NOT NULL,
    title TEXT NOT NULL,
    description TEXT NOT NULL,
    created_date TEXT NOT NULL,
    FOREIGN KEY (proposal_id) REFERENCES proposal(proposal_id),
    FOREIGN KEY (author_id) REFERENCES app_user(app_user_id)
);