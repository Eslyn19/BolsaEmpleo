CREATE DATABASE IF NOT EXISTS bolsa_empleo;
USE bolsa_empleo;

CREATE TABLE estado (
                        id_estado INT NOT NULL AUTO_INCREMENT,
                        nombre    ENUM('PENDIENTE','APROBADO','RECHAZADO') NOT NULL,
                        PRIMARY KEY (id_estado),
                        UNIQUE (nombre)
);

CREATE TABLE usuario (
                         id_usuario     INT          NOT NULL AUTO_INCREMENT,
                         correo         VARCHAR(120) NOT NULL,
                         clave_hash     VARCHAR(255) NOT NULL,
                         tipo           ENUM('ADMIN','EMPRESA','OFERENTE') NOT NULL,
                         activo         TINYINT(1)   NOT NULL DEFAULT 1,
                         fecha_registro DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         PRIMARY KEY (id_usuario),
                         UNIQUE (correo)
);

CREATE TABLE administrador (
                               id_admin       INT          NOT NULL AUTO_INCREMENT,
                               id_usuario     INT          NOT NULL,
                               identificacion VARCHAR(20)  NOT NULL,
                               nombre         VARCHAR(100) NOT NULL,
                               PRIMARY KEY (id_admin),
                               UNIQUE (id_usuario),
                               UNIQUE (identificacion),
                               FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE
);

CREATE TABLE empresa (
                         id_empresa       INT          NOT NULL AUTO_INCREMENT,
                         id_usuario       INT          NOT NULL,
                         nombre           VARCHAR(150) NOT NULL,
                         localizacion     VARCHAR(200),
                         telefono         VARCHAR(20),
                         descripcion      TEXT,
                         id_estado        INT          NOT NULL,
                         aprobado_por     INT,
                         fecha_aprobacion DATETIME,
                         PRIMARY KEY (id_empresa),
                         UNIQUE (id_usuario),
                         FOREIGN KEY (id_usuario)   REFERENCES usuario(id_usuario)     ON DELETE CASCADE,
                         FOREIGN KEY (id_estado)    REFERENCES estado(id_estado),
                         FOREIGN KEY (aprobado_por) REFERENCES administrador(id_admin) ON DELETE SET NULL
);

CREATE TABLE oferente (
                          id_oferente      INT          NOT NULL AUTO_INCREMENT,
                          id_usuario       INT          NOT NULL,
                          identificacion   VARCHAR(20)  NOT NULL,
                          nombre           VARCHAR(100) NOT NULL,
                          primer_apellido  VARCHAR(100) NOT NULL,
                          nacionalidad     VARCHAR(80),
                          telefono         VARCHAR(20),
                          lugar_residencia VARCHAR(200),
                          curriculum_pdf   VARCHAR(255),
                          id_estado        INT          NOT NULL,
                          aprobado_por     INT,
                          fecha_aprobacion DATETIME,
                          PRIMARY KEY (id_oferente),
                          UNIQUE (id_usuario),
                          UNIQUE (identificacion),
                          FOREIGN KEY (id_usuario)   REFERENCES usuario(id_usuario)     ON DELETE CASCADE,
                          FOREIGN KEY (id_estado)    REFERENCES estado(id_estado),
                          FOREIGN KEY (aprobado_por) REFERENCES administrador(id_admin) ON DELETE SET NULL
);

CREATE TABLE caracteristica (
                                id_caracteristica INT          NOT NULL AUTO_INCREMENT,
                                id_padre          INT,
                                nombre            VARCHAR(100) NOT NULL,
                                PRIMARY KEY (id_caracteristica),
                                FOREIGN KEY (id_padre) REFERENCES caracteristica(id_caracteristica) ON DELETE SET NULL
);

CREATE TABLE puesto (
                        id_puesto         INT          NOT NULL AUTO_INCREMENT,
                        id_empresa        INT          NOT NULL,
                        descripcion       TEXT         NOT NULL,
                        salario           DECIMAL(10,2),
                        tipo_publicacion  ENUM('PUBLICO','PRIVADO') NOT NULL DEFAULT 'PUBLICO',
                        activo            TINYINT(1)   NOT NULL DEFAULT 1,
                        fecha_publicacion DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        PRIMARY KEY (id_puesto),
                        FOREIGN KEY (id_empresa) REFERENCES empresa(id_empresa) ON DELETE CASCADE
);

CREATE TABLE puesto_caracteristica (
                                       id_puesto         INT     NOT NULL,
                                       id_caracteristica INT     NOT NULL,
                                       nivel_requerido   TINYINT NOT NULL CHECK (nivel_requerido BETWEEN 1 AND 5),
                                       PRIMARY KEY (id_puesto, id_caracteristica),
                                       FOREIGN KEY (id_puesto)         REFERENCES puesto(id_puesto)                 ON DELETE CASCADE,
                                       FOREIGN KEY (id_caracteristica) REFERENCES caracteristica(id_caracteristica) ON DELETE CASCADE
);

CREATE TABLE oferente_habilidad (
                                    id_oferente       INT     NOT NULL,
                                    id_caracteristica INT     NOT NULL,
                                    nivel             TINYINT NOT NULL CHECK (nivel BETWEEN 1 AND 5),
                                    PRIMARY KEY (id_oferente, id_caracteristica),
                                    FOREIGN KEY (id_oferente)       REFERENCES oferente(id_oferente)             ON DELETE CASCADE,
                                    FOREIGN KEY (id_caracteristica) REFERENCES caracteristica(id_caracteristica) ON DELETE CASCADE
);