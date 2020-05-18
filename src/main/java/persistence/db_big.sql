DROP TABLE IF EXISTS hall;
DROP TABLE IF EXISTS Account;

CREATE TABLE Account (
  Account_id SERIAL PRIMARY KEY,
  Name varchar(100) NOT NULL,
  Phone varchar(100) NOT NULL
);

CREATE TABLE Hall (
  Place varchar(10) NOT NULL  PRIMARY KEY,
  State int,
  Price float,
  Account_id INT REFERENCES Account(Account_id)
);

INSERT INTO Hall(Place, State, Price, Account_id)
VALUES
('1.1', 1, 500, null ),
('1.2', 1, 500, null ),
('1.3', 1, 500, null ),
('1.4', 1, 500, null ),
('1.5', 1, 500, null ),
('1.6', 1, 500, null ),
('2.1', 1, 500, null ),
('2.2', 1, 500, null ),
('2.3', 1, 500, null ),
('2.4', 1, 500, null ),
('2.5', 1, 500, null ),
('2.6', 1, 500, null ),
('3.1', 1, 500, null ),
('3.2', 1, 500, null ),
('3.3', 1, 500, null ),
('3.4', 1, 500, null ),
('3.5', 1, 500, null ),
('3.6', 1, 500, null ),
('4.1', 1, 500, null ),
('4.2', 1, 500, null ),
('4.3', 1, 500, null ),
('4.4', 1, 500, null ),
('4.5', 1, 500, null ),
('4.6', 1, 500, null ),
('4.7', 1, 500, null );

