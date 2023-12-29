-- Eliminar la tabla persona
DROP TABLE IF EXISTS persona;

-- Crear la tabla persona
CREATE TABLE IF NOT EXISTS persona(
    personaID INTEGER GENERATED ALWAYS AS IDENTITY(START WITH 1) PRIMARY KEY,
    nombre VARCHAR(20) NOT NULL,
    apellidos VARCHAR(30) NOT NULL,
    sexo CHAR(1) NOT NULL,
    nacimiento DATE,
    ingresos REAL);

-- Rellenarla de personas
INSERT INTO persona VALUES (DEFAULT, 'Armando', 'Bronca Segura', 'H', '1970-08-03', 2500);
INSERT INTO persona VALUES (DEFAULT, 'Belen', 'Tilla', 'M', '1970-08-03', 2500);
INSERT INTO persona VALUES (DEFAULT, 'Esther', 'Malgin', 'M', '1983-12-06', 2100);
INSERT INTO persona VALUES (DEFAULT, 'Amador', 'Denador', 'H', '1994-12-24', 1200);
INSERT INTO persona VALUES (DEFAULT, 'Aitor', 'Tilla', 'H', '2001-01-07', 1300);
INSERT INTO persona VALUES (DEFAULT, 'Sandra', 'Matica', 'M', '1977-02-19', 1500);
INSERT INTO persona VALUES (DEFAULT, 'Victor', 'Nado', 'H', '1998-06-30', 2400);
INSERT INTO persona VALUES (DEFAULT, 'Pedro', 'Gado', 'H', '2002-04-23', 1100);
INSERT INTO persona VALUES (DEFAULT, 'Vanesa', 'Tanica', 'M', '2000-01-06', 1200);
INSERT INTO persona VALUES (DEFAULT, 'Marta', 'Baco', 'M', '1970-08-03', 1700);
INSERT INTO persona VALUES (DEFAULT, 'Consuelo', 'Tería', 'M', '1992-07-08', 1900);
INSERT INTO persona VALUES (DEFAULT, 'Mercedes', 'Pacio', 'M', '1970-08-03', 2400);
INSERT INTO persona VALUES (DEFAULT, 'Lorenzo', 'Penco', 'H', '1968-03-12', 1900);