USE moviedb;

DROP TABLE IF EXISTS employees;

CREATE TABLE employees (
    email varchar(50) PRIMARY KEY,
    password varchar(20) NOT NULL,
    fullname varchar(100)
);

INSERT INTO employees VALUES('classta@email.edu','classta','TA CS122B');

