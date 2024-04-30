drop table phones if exists;

create table Phones(
    Id integer not null primary key AUTO_INCREMENT,
    Available BOOLEAN,
    Name varchar(255),
    BookedBy VARCHAR(255),
    DateBooked TIMESTAMP
);