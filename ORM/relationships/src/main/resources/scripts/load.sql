INSERT INTO PAIS(ISO3, NOMBRE) VALUES ('GER', 'Alemania');
INSERT INTO PAIS(ISO3, NOMBRE) VALUES ('AGO', 'Angola');
INSERT INTO PAIS(ISO3, NOMBRE) VALUES ('ARG', 'Argentina');
INSERT INTO PAIS(ISO3, NOMBRE) VALUES ('BEL', 'Bélgica');
INSERT INTO PAIS(ISO3, NOMBRE) VALUES ('BRA', 'Brasil');
INSERT INTO PAIS(ISO3, NOMBRE) VALUES ('HRV', 'Croacia');
INSERT INTO PAIS(ISO3, NOMBRE) VALUES ('ESP', 'España');
INSERT INTO PAIS(ISO3, NOMBRE) VALUES ('FRA', 'Francia');
INSERT INTO PAIS(ISO3, NOMBRE) VALUES ('ENG', 'Inglaterra');
INSERT INTO PAIS(ISO3, NOMBRE) VALUES ('ITA', 'Italia');
INSERT INTO PAIS(ISO3, NOMBRE) VALUES ('NOR', 'Noruega');
INSERT INTO PAIS(ISO3, NOMBRE) VALUES ('POL', 'Polonia');
INSERT INTO PAIS(ISO3, NOMBRE) VALUES ('GBR', 'Reino Unido');
INSERT INTO PAIS(ISO3, NOMBRE) VALUES ('COD', 'República Democrática del Congo');


INSERT INTO ENTRENADOR(ID, NOMBRE, PAIS_ISO3) VALUES (1,'Carlo Ancelotti','ITA');
INSERT INTO ENTRENADOR(ID, NOMBRE, PAIS_ISO3) VALUES (2,'Xavi Hernandez','ESP');
INSERT INTO ENTRENADOR(ID, NOMBRE, PAIS_ISO3) VALUES (3,'Javier Calleja Revilla','ESP');
INSERT INTO ENTRENADOR(ID, NOMBRE, PAIS_ISO3) VALUES (4,'Thomas Tuchel','GER');
INSERT INTO ENTRENADOR(ID, NOMBRE, PAIS_ISO3) VALUES (5,'Jürgen Klopp','GER');
INSERT INTO ENTRENADOR(ID, NOMBRE, PAIS_ISO3) VALUES (6,'Rubén Baraja','ESP');
INSERT INTO ENTRENADOR(ID, NOMBRE, PAIS_ISO3) VALUES (7,'Pep Guardiola','ESP');
INSERT INTO ENTRENADOR(ID, NOMBRE, PAIS_ISO3) VALUES (10,'''Cholo'' Simeone','ARG');

INSERT INTO EQUIPO(ID, NOMBRE, PAIS_ISO3, ENTRENADOR_ID) VALUES (1, 'Real Madrid C.F.','ESP', 1);
INSERT INTO EQUIPO(ID, NOMBRE, PAIS_ISO3, ENTRENADOR_ID) VALUES (2, 'F.C. Barcelona','ESP', 2);
INSERT INTO EQUIPO(ID, NOMBRE, PAIS_ISO3, ENTRENADOR_ID) VALUES (3, 'Levante U.D.','ESP', 3);
INSERT INTO EQUIPO(ID, NOMBRE, PAIS_ISO3, ENTRENADOR_ID) VALUES (4, 'Bayern Munich','GER', 4);
INSERT INTO EQUIPO(ID, NOMBRE, PAIS_ISO3, ENTRENADOR_ID) VALUES (5, 'Liverpool F.C.','GER', 5);
INSERT INTO EQUIPO(ID, NOMBRE, PAIS_ISO3, ENTRENADOR_ID) VALUES (6, 'Valencia C.F.','ESP', 6);
INSERT INTO EQUIPO(ID, NOMBRE, PAIS_ISO3, ENTRENADOR_ID) VALUES (7, 'Manchester City','ENG', 7);
INSERT INTO EQUIPO(ID, NOMBRE, PAIS_ISO3, ENTRENADOR_ID) VALUES (10, 'Atletico de Madrid','ESP', 10);

INSERT INTO JUGADOR(ID, NOMBRE, NACIMIENTO, DEMARCACION, EQUIPO_ID, DORSAL) VALUES (1,'Luka Modric','1985-09-09', 2, 1, 10);
INSERT INTO JUGADOR(ID, NOMBRE, NACIMIENTO, DEMARCACION, EQUIPO_ID, DORSAL) VALUES (2,'Jude Bellingham','2003-06-29', 2, 1, 5);
INSERT INTO JUGADOR(ID, NOMBRE, NACIMIENTO, DEMARCACION, EQUIPO_ID, DORSAL) VALUES (3,'Pedri','2002-11-25', 2, 2, 8);
INSERT INTO JUGADOR(ID, NOMBRE, NACIMIENTO, DEMARCACION, EQUIPO_ID, DORSAL) VALUES (4,'Robert Lewandowski','1998-08-21', 3, 2, 9);
INSERT INTO JUGADOR(ID, NOMBRE, NACIMIENTO, DEMARCACION, EQUIPO_ID, DORSAL) VALUES (5,'Rodrygo','2001-01-09', 3, 1, 11);
INSERT INTO JUGADOR(ID, NOMBRE, NACIMIENTO, DEMARCACION, EQUIPO_ID, DORSAL) VALUES (6,'Antoine Griezmann','1991-03-21', 3, 10, 7);
INSERT INTO JUGADOR(ID, NOMBRE, NACIMIENTO, DEMARCACION, EQUIPO_ID, DORSAL) VALUES (7,'Eduardo Camavinga','2001-11-10', 2, 1, 12);
INSERT INTO JUGADOR(ID, NOMBRE, NACIMIENTO, DEMARCACION, EQUIPO_ID, DORSAL) VALUES (8,'Erling Haalland','2000-07-21', 3, 7, 9);
INSERT INTO JUGADOR(ID, NOMBRE, NACIMIENTO, DEMARCACION, EQUIPO_ID, DORSAL) VALUES (9,'Thibaut Courtois','1992-05-11', 0, 1, 1);
INSERT INTO JUGADOR(ID, NOMBRE, NACIMIENTO, DEMARCACION, EQUIPO_ID, DORSAL) VALUES (10,'Toni Kroos','1990-01-04', 2, 1, 8);
INSERT INTO JUGADOR(ID, NOMBRE, NACIMIENTO, DEMARCACION, EQUIPO_ID, DORSAL) VALUES (11,'Vinícius Júnior','2000-07-12', 3, 1, 7);
INSERT INTO JUGADOR(ID, NOMBRE, NACIMIENTO, DEMARCACION, EQUIPO_ID, DORSAL) VALUES (12,'Dani Carvajal','1992-01-11', 1, 1, 2);

INSERT INTO NACIONALIDADES(JUGADOR_ID, PAIS_ISO3) VALUES (1,'HRV');
INSERT INTO NACIONALIDADES(JUGADOR_ID, PAIS_ISO3) VALUES (2,'GBR');
INSERT INTO NACIONALIDADES(JUGADOR_ID, PAIS_ISO3) VALUES (3,'ESP');
INSERT INTO NACIONALIDADES(JUGADOR_ID, PAIS_ISO3) VALUES (4,'POL');
INSERT INTO NACIONALIDADES(JUGADOR_ID, PAIS_ISO3) VALUES (5,'BRA');
INSERT INTO NACIONALIDADES(JUGADOR_ID, PAIS_ISO3) VALUES (6,'FRA');
INSERT INTO NACIONALIDADES(JUGADOR_ID, PAIS_ISO3) VALUES (7,'FRA');
INSERT INTO NACIONALIDADES(JUGADOR_ID, PAIS_ISO3) VALUES (7,'AGO');
INSERT INTO NACIONALIDADES(JUGADOR_ID, PAIS_ISO3) VALUES (7,'COD');
INSERT INTO NACIONALIDADES(JUGADOR_ID, PAIS_ISO3) VALUES (8,'NOR');
INSERT INTO NACIONALIDADES(JUGADOR_ID, PAIS_ISO3) VALUES (9,'BEL');
INSERT INTO NACIONALIDADES(JUGADOR_ID, PAIS_ISO3) VALUES (10,'GER');
INSERT INTO NACIONALIDADES(JUGADOR_ID, PAIS_ISO3) VALUES (11,'BRA');
INSERT INTO NACIONALIDADES(JUGADOR_ID, PAIS_ISO3) VALUES (12,'ESP');