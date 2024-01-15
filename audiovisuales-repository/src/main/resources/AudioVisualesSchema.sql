CREATE TABLE IF NOT EXISTS pelicula (
    peliculaID INTEGER GENERATED ALWAYS AS IDENTITY (START WITH 1) PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    AKA VARCHAR(100),
    año int,
    duracion INTEGER NOT NULL,
    pais CHAR(3)
)