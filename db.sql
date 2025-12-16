-- créer la base de donneés
CREATE DATABASE videogame CHARSET utf8mb4;

USE videogame;

-- Créer les tables
CREATE TABLE game(
id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
title VARCHAR(50) UNIQUE NOT NULL,
published_at DATE NOT NULL,
`type` VARCHAR(50) NOT NULL
)ENGINE=InnoDB;


CREATE TABLE device(
id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
`name` VARCHAR(50) UNIQUE NOT NULL,
`type` VARCHAR(50) NOT NULL,
manufacturer VARCHAR(50) NOT NULL
)ENGINE=InnoDB;

CREATE TABLE game_device(
id_game INT,
id_device INT,
PRIMARY KEY(id_game, id_device)
)ENGINE=InnoDB;

-- Ajouter les contraintes de clé étrangére
ALTER TABLE game_device
ADD CONSTRAINT fk_assign_game
FOREIGN KEY(id_game)
REFERENCES game(id);

ALTER TABLE game_device
ADD CONSTRAINT fk_assign_device
FOREIGN KEY(id_device)
REFERENCES device(id);