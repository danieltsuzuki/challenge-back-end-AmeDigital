CREATE TABLE IF NOT EXISTS planetas (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    clima VARCHAR(50),
    terreno VARCHAR(50)
);