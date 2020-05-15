CREATE TABLE Account (
  Account_id SERIAL PRIMARY KEY,
  Name varchar(10) NOT NULL,
  Phone varchar(10) NOT NULL
);

CREATE TABLE Hall (
  Place varchar(10) NOT NULL  PRIMARY KEY,
  Status int,
  Price float,
  Account_id INT REFERENCES Account(Account_id)
);

INSERT INTO Hall(Place, Status, Price, Account_id)
VALUES
('1.1', 1, 500, null ),
('1.2', 1, 500, null ),
('1.3', 1, 500, null ),
('2.1', 1, 500, null ),
('2.2', 1, 500, null ),
('2.3', 1, 500, null ),
('3.1', 1, 500, null ),
('3.2', 1, 500, null ),
('3.3', 1, 500, null );

