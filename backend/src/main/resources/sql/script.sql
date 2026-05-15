-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION';

DROP SCHEMA IF EXISTS `bolsa_empleo` ;
CREATE SCHEMA IF NOT EXISTS `bolsa_empleo` DEFAULT CHARACTER SET utf8 ;
USE `bolsa_empleo` ;

-- -----------------------------------------------------
-- Estado
-- -----------------------------------------------------
CREATE TABLE Estado (
    id INT AUTO_INCREMENT,
    nombre ENUM('PENDIENTE', 'ACEPTADO', 'RECHAZADO') NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- Usuario (PADRE)
-- -----------------------------------------------------
CREATE TABLE Usuario (
     idUsuario VARCHAR(9) NOT NULL,
     clave VARCHAR(300) NOT NULL,
     rol ENUM('ADMIN', 'OFERENTE', 'EMPRESA') NOT NULL,
     PRIMARY KEY (idUsuario)
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- Oferente (HIJA)
-- -----------------------------------------------------
CREATE TABLE Oferente (
      idUsuario VARCHAR(9) NOT NULL,
      nombre VARCHAR(45) NOT NULL,
      apellidos VARCHAR(45) NOT NULL,
      nacionalidad VARCHAR(40) NOT NULL,
      telefono VARCHAR(11) NOT NULL,
      correo VARCHAR(50) NOT NULL,
      residencia VARCHAR(50) NOT NULL,
      rutaCV VARCHAR(200),
      estado INT NOT NULL,

      PRIMARY KEY (idUsuario),
      FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario) ON DELETE CASCADE,
      FOREIGN KEY (estado) REFERENCES Estado(id)
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- Administrador (HIJA)
-- -----------------------------------------------------
CREATE TABLE Administrador (
       idUsuario VARCHAR(9) NOT NULL,
       nombre VARCHAR(60) NOT NULL,

       PRIMARY KEY (idUsuario),
       FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario) ON DELETE CASCADE
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- Empresa (HIJA)
-- -----------------------------------------------------
CREATE TABLE Empresa (
     idUsuario VARCHAR(50) NOT NULL,
     nombre VARCHAR(45) NOT NULL,
     ubicacion VARCHAR(45) NOT NULL,
     telefono VARCHAR(11) NOT NULL,
     descripcion VARCHAR(100),
     tipo ENUM('PUBLICO', 'PRIVADO'),
     estado INT NOT NULL,

     PRIMARY KEY (idUsuario),
     UNIQUE (telefono),
     FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario) ON DELETE CASCADE,
     FOREIGN KEY (estado) REFERENCES Estado(id)
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- Caracteristica (JERÁRQUICA)
-- -----------------------------------------------------
CREATE TABLE Caracteristica (
    id INT AUTO_INCREMENT,
    nombre VARCHAR(45) NOT NULL,
    idPadre INT NULL,
    activo TINYINT,

    PRIMARY KEY (id),
    UNIQUE (nombre),
    FOREIGN KEY (idPadre) REFERENCES Caracteristica(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- Oferente - Caracteristica (N:M)
-- -----------------------------------------------------
CREATE TABLE OferenteHabilidad (
       idUsuario VARCHAR(9),
       idCaracteristica INT,
       nivel INT NOT NULL,

       PRIMARY KEY (idUsuario, idCaracteristica),
       FOREIGN KEY (idUsuario) REFERENCES Oferente(idUsuario) ON DELETE CASCADE,
       FOREIGN KEY (idCaracteristica) REFERENCES Caracteristica(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- Puesto (1:N con Empresa)
-- -----------------------------------------------------
CREATE TABLE Puesto (
    idPuesto INT AUTO_INCREMENT,
    idUsuario VARCHAR(9) NOT NULL,
    descripcion VARCHAR(100) NOT NULL,
    salario DOUBLE NOT NULL,
    activo TINYINT NULL,

    PRIMARY KEY (idPuesto),
    FOREIGN KEY (idUsuario) REFERENCES Empresa(idUsuario) ON DELETE CASCADE
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- INSERTS
-- -----------------------------------------------------

-- INSERT INTO Estado (nombre) VALUES ('PENDIENTE'), ('ACEPTADO'), ('RECHAZADO');

-- Admin 1
INSERT INTO Usuario VALUES ('208380902', '111', 'ADMIN');

INSERT INTO Administrador (idUsuario, nombre)
SELECT idUsuario, 'Eslyn Jara'
FROM Usuario
WHERE idUsuario = '208380902' AND rol = 'ADMIN';

-- Admin 2
INSERT INTO Usuario VALUES ('208640831', '111', 'ADMIN');

INSERT INTO Administrador (idUsuario, nombre)
SELECT idUsuario, 'Mishelle Rojas'
FROM Usuario
WHERE idUsuario = '208640831' AND rol = 'ADMIN';

INSERT INTO Caracteristica (nombre, idPadre, activo)
VALUES ('Informatica', NULL, 1);

-- TIPOS DE ESTADO
INSERT INTO Estado (nombre) VALUES ('PENDIENTE'), ('ACEPTADO'), ('RECHAZADO');

-- (raiz) = Informatica #1
-- Lenguajes de programacion #2
-- Tecnologias Web #3
-- Bases de datos #4
-- Ciberserguridad #5
-- DevOps #6

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Lenguajes de Programacion', id, 1 FROM Caracteristica WHERE nombre='Informatica';

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Tecnologias Web', id, 1 FROM Caracteristica WHERE nombre='Informatica';

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Bases de Datos', id, 1 FROM Caracteristica WHERE nombre='Informatica';

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Ciberseguridad', id, 1 FROM Caracteristica WHERE nombre='Informatica';

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'DevOps', id, 1 FROM Caracteristica WHERE nombre='Informatica';

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Java', id, 1 FROM Caracteristica WHERE nombre='Lenguajes de Programacion';

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'C#', id, 1 FROM Caracteristica WHERE nombre='Lenguajes de Programacion';

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'JavaScript', id, 1 FROM Caracteristica WHERE nombre='Lenguajes de Programacion';

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Python', id, 1 FROM Caracteristica WHERE nombre='Lenguajes de Programacion';

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'C++', id, 1 FROM Caracteristica WHERE nombre='Lenguajes de Programacion';

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'HTML', id, 1 FROM Caracteristica WHERE nombre='Tecnologias Web';

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'CSS', id, 1 FROM Caracteristica WHERE nombre='Tecnologias Web';

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'React', id, 1 FROM Caracteristica WHERE nombre='Tecnologias Web';

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Angular', id, 1 FROM Caracteristica WHERE nombre='Tecnologias Web';

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Vue', id, 1 FROM Caracteristica WHERE nombre='Tecnologias Web';

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'MySQL', id, 1 FROM Caracteristica WHERE nombre='Bases de Datos';

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'PostgreSQL', id, 1 FROM Caracteristica WHERE nombre='Bases de Datos';

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'MongoDB', id, 1 FROM Caracteristica WHERE nombre='Bases de Datos';

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Oracle', id, 1 FROM Caracteristica WHERE nombre='Bases de Datos';

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'SQL Server', id, 1 FROM Caracteristica WHERE nombre='Bases de Datos';

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Pentesting', id, 1 FROM Caracteristica WHERE nombre='Ciberseguridad';

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Analisis de vulnerabilidades', id, 1 FROM Caracteristica WHERE nombre='Ciberseguridad';

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Criptografia', id, 1 FROM Caracteristica WHERE nombre='Ciberseguridad';

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Seguridad en redes', id, 1 FROM Caracteristica WHERE nombre='Ciberseguridad';

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Ethical Hacking', id, 1 FROM Caracteristica WHERE nombre='Ciberseguridad';

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Docker', id, 1 FROM Caracteristica WHERE nombre='DevOps';

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Kubernetes', id, 1 FROM Caracteristica WHERE nombre='DevOps';

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'CI/CD', id, 1 FROM Caracteristica WHERE nombre='DevOps';

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'AWS', id, 1 FROM Caracteristica WHERE nombre='DevOps';

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Azure', id, 1 FROM Caracteristica WHERE nombre='DevOps';

-- -----------------------------------------------------
-- Puesto - Caracteristica (N:M)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS puesto_caracteristica (
    idPuesto INT NOT NULL,
    idCaracteristica INT NOT NULL,
    PRIMARY KEY (idPuesto, idCaracteristica),
    FOREIGN KEY (idPuesto) REFERENCES Puesto(idPuesto) ON DELETE CASCADE,
    FOREIGN KEY (idCaracteristica) REFERENCES Caracteristica(id) ON DELETE CASCADE
) ENGINE=InnoDB;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;