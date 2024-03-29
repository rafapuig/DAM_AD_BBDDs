CREATE TABLE equipo (
    equipoID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,
    nombre CHARACTER VARYING(40) NOT NULL,
    pais CHARACTER(3));

CREATE TABLE jugador (
     jugadorID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,
     nombre VARCHAR(40) NOT NULL,
     pais CHAR(3) NOT NULL,
     nacimiento DATE,
     estatura DECIMAL(3,2),
     peso INTEGER,
     dorsal INTEGER,
     equipoID INTEGER REFERENCES equipo,
     capitan BOOLEAN);