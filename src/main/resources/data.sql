DROP TABLE IF EXISTS BITCOIN;

CREATE TABLE BITCOIN(
   ID INT AUTO_INCREMENT PRIMARY KEY,
   LPRICE FLOAT NOT NULL,
   CURR1 VARCHAR(20) NOT NULL,
   CURR2 VARCHAR(20) NOT NULL,
   CREATEAT TIMESTAMP,
   CONSTRAINT PK_Bitcoin PRIMARY KEY (ID)
);
