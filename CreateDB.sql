DROP IF EXISTS secretroom;
Create DATABASE SecretRoom;
Use secretroom;
CREATE TABLE Room (
    RID int NOT NULL,
    ExpDate DATETIME NOT NULL,
    PRIMARY KEY (RID)
);

CREATE TABLE Files (
    File longblob,
    Name varchar(255),
	RID int Not Null,
    FOREIGN Key (RID) REFERENCES Room(RID) ON DELETE CASCADE
);


CREATE TABLE Messages (
    Message varchar(255),
	RID int Not Null,
	Name varchar(40) not null,
	Date DATETIME NOT NULL,
    FOREIGN Key (RID) REFERENCES Room(RID) ON DELETE CASCADE
);


SET GLOBAL event_scheduler = ON;

CREATE EVENT deleteRooms
  ON SCHEDULE EVERY '1' HOUR
  STARTS now()    
DO 
DELETE FROM room r WHERE r.ExpDate < now();