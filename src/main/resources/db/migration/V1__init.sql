
DROP TABLE IF EXISTS users;

create table users
(
    id            bigserial not null,
    apartment     varchar(255),
    city          varchar(255),
    country       varchar(255),
    street        varchar(255),
    street_number varchar(255),
    zip           varchar(255),
    birth_date    date not null,
    email         varchar(255) not null unique ,
    first_name    varchar(255) not null,
    last_name     varchar(255) not null,
    phone_number varchar(255),
    primary key (id)
);

INSERT INTO users (email, first_name, birth_date, last_name, country, city, street, street_number, apartment, zip, phone_number)
VALUES
    ('user1@example.com', 'John', '1990-01-01', 'Doe', 'Country1', 'City1', 'Street1', '1A', 'Apt1', '12345', '123-123-123'),
    ('user2@example.com', 'Jane', '1995-02-15', 'Smith', 'Country2', 'City2', 'Street2', '2B', 'Apt2', '54321', '123-456-789'),
    ('user3@example.com', 'Alice', '1988-03-20', 'Johnson', 'Country3', 'City3', 'Street3', '3C', 'Apt3', '98765', '111-222-333'),
    ('user4@example.com', 'Michael', '1992-04-10', 'Brown', 'Country4', 'City4', 'Street4', '4D', 'Apt4', '11111', '222-333-444'),
    ('user5@example.com', 'Emily', '1998-05-05', 'Williams', 'Country5', 'City5', 'Street5', '5E', 'Apt5', '55555', '333-444-555'),
    ('user6@example.com', 'David', '1993-06-25', 'Jones', 'Country6', 'City6', 'Street6', '6F', 'Apt6', '66666', '444-555-666'),
    ('user7@example.com', 'Sophia', '1987-07-15', 'Davis', 'Country7', 'City7', 'Street7', '7G', 'Apt7', '77777', '555-666-777'),
    ('user8@example.com', 'Matthew', '1990-08-12', 'Miller', 'Country8', 'City8', 'Street8', '8H', 'Apt8', '88888', '666-777-888'),
    ('user9@example.com', 'Olivia', '1994-09-30', 'Wilson', 'Country9', 'City9', 'Street9', '9I', 'Apt9', '99999', '777-888-999'),
    ('user10@example.com', 'James', '1985-10-22', 'Moore', 'Country10', 'City10', 'Street10', '10J', 'Apt10', '10101', '888-999-000'),
    ('user11@example.com', 'Emma', '1996-11-18', 'Anderson', 'Country11', 'City11', 'Street11', '11K', 'Apt11', '11111', '999-000-111'),
    ('user12@example.com', 'Daniel', '1989-12-05', 'Martinez', 'Country12', 'City12', 'Street12', '12L', 'Apt12', '12121', '000-111-222'),
    ('user13@example.com', 'Ava', '1991-01-14', 'Lee', 'Country13', 'City13', 'Street13', '13M', 'Apt13', '13131', '111-222-333'),
    ('user14@example.com', 'William', '1997-02-28', 'Clark', 'Country14', 'City14', 'Street14', '14N', 'Apt14', '14141', '222-333-444'),
    ('user15@example.com', 'Mia', '1986-03-07', 'Taylor', 'Country15', 'City15', 'Street15', '15O', 'Apt15', '15151', '333-444-555'),
    ('user16@example.com', 'Alexander', '1992-04-09', 'White', 'Country16', 'City16', 'Street16', '16P', 'Apt16', '16161', '444-555-666'),
    ('user17@example.com', 'Sophia', '1993-05-20', 'Hall', 'Country17', 'City17', 'Street17', '17Q', 'Apt17', '17171', '555-666-777'),
    ('user18@example.com', 'Ethan', '1984-06-11', 'Garcia', 'Country18', 'City18', 'Street18', '18R', 'Apt18', '18181', '666-777-888'),
    ('user19@example.com', 'Olivia', '1995-07-24', 'Harris', 'Country19', 'City19', 'Street19', '19S', 'Apt19', '19191', '777-888-999'),
    ('user20@example.com', 'Liam', '1988-08-16', 'Brown', 'Country20', 'City20', 'Street20', '20T', 'Apt20', '20202', '888-999-000');

