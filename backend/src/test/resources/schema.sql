-- équivalent H2 de la vue v_soldefinmoiscourant
CREATE TABLE v_soldefinmoiscourant (
    datesolde DATE PRIMARY KEY,
    soldefinmoiscourant DECIMAL(19,2)
);

-- équivalent H2 de v_soldepecbanque
CREATE TABLE v_soldepecbanque (
    soldepecbanque DECIMAL(19,2) PRIMARY KEY
);