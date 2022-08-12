-- push_location definition

CREATE TABLE push_location
(
    task_key   TEXT,
    start_key  INTEGER,
    "path"     TEXT,
    file_name  TEXT,
    table_name TEXT
);


-- push_sink definition

CREATE TABLE push_sink
(
    id               INTEGER PRIMARY KEY AUTOINCREMENT,
    data_source_name TEXT(64),
    url              TEXT(256),
    user_name        TEXT(64),
    password         TEXT(64),
    topic            TEXT(64),
    type             INTEGER,
    enable           INTEGER
);


-- push_task definition

CREATE TABLE push_task
(
    task_name TEXT(64),
    cron      TEXT(64),
    task_key  TEXT(64),
    enable    INTEGER,
    remark    TEXT(256),
    target_id INTEGER
);


-- push_user definition

CREATE TABLE push_user
(
    user_name TEXT(256),
    password  TEXT(64)
);