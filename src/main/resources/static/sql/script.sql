-- ============================================================
--  BOLSA DE EMPLEO  –  Esquema MySQL
--  Proyecto Web Java + MySQL
-- ============================================================

SET FOREIGN_KEY_CHECKS = 0;
DROP DATABASE IF EXISTS bolsa_empleo;
CREATE DATABASE bolsa_empleo;
USE bolsa_empleo;

-- ============================================================
-- 1. ADMINISTRADORES
-- ============================================================
CREATE TABLE Administrador (
   id            INT UNSIGNED    AUTO_INCREMENT PRIMARY KEY,
   identificacion VARCHAR(20)    NOT NULL UNIQUE,
   nombre        VARCHAR(100)    NOT NULL,
   clave_hash    VARCHAR(255)    NOT NULL,
   activo        TINYINT(1)      NOT NULL DEFAULT 1,
   creado_en     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- 2. EMPRESAS
-- ============================================================
CREATE TABLE Empresa (
     id            INT UNSIGNED    AUTO_INCREMENT PRIMARY KEY,
     nombre        VARCHAR(150)    NOT NULL,
     localizacion  VARCHAR(200)    NOT NULL,
     correo        VARCHAR(150)    NOT NULL UNIQUE,
     telefono      VARCHAR(30),
     descripcion   TEXT,

     clave_hash    VARCHAR(255)    NOT NULL,
     estado        ENUM('PENDIENTE','APROBADA','RECHAZADA','SUSPENDIDA') NOT NULL DEFAULT 'PENDIENTE',
     aprobado_por  INT UNSIGNED    NULL, -- Fk → administrador
     aprobado_en   DATETIME        NULL,
     creado_en     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,

     CONSTRAINT fk_empresa_admin
         FOREIGN KEY (aprobado_por) REFERENCES administrador(id)
             ON DELETE SET NULL ON UPDATE CASCADE
);

-- ============================================================
-- 3. OFERENTES (personas)
-- ============================================================
CREATE TABLE Oferente (
      id              INT UNSIGNED  AUTO_INCREMENT PRIMARY KEY,
      identificacion  VARCHAR(20)   NOT NULL UNIQUE,
      nombre          VARCHAR(100)  NOT NULL,
      primer_apellido VARCHAR(100)  NOT NULL,
      nacionalidad    VARCHAR(80)   NOT NULL,
      telefono        VARCHAR(30),
      correo          VARCHAR(150)  NOT NULL UNIQUE,
      lugar_residencia VARCHAR(200) NOT NULL,
      clave_hash      VARCHAR(255)  NOT NULL,
      curriculum_pdf  VARCHAR(500)  NULL, -- ruta -> URL al PDF
      estado          ENUM('PENDIENTE','APROBADO','RECHAZADO','SUSPENDIDO') NOT NULL DEFAULT 'PENDIENTE',
      aprobado_por    INT UNSIGNED  NULL,
      aprobado_en     DATETIME      NULL,
      creado_en       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,

      CONSTRAINT fk_oferente_admin
          FOREIGN KEY (aprobado_por) REFERENCES administrador(id)
              ON DELETE SET NULL ON UPDATE CASCADE
);

-- ============================================================
-- 4. Destreza
-- ============================================================
CREATE TABLE destreza (
    id            INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    nombre        VARCHAR(100) NOT NULL,
    descripcion   TEXT,
    padre_id      INT UNSIGNED NULL, -- referencia a la destreza padre
    activa        TINYINT(1) NOT NULL DEFAULT 1,
    creado_en     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_destreza_padre
        FOREIGN KEY (padre_id) REFERENCES destreza(id)
            ON DELETE RESTRICT ON UPDATE CASCADE
);


-- ============================================================
-- 5. HABILIDADES DEL OFERENTE
--    (oferente ↔ característica con nivel 1-5)
-- ============================================================
CREATE TABLE oferente_habilidad (
    oferente_id      INT UNSIGNED NOT NULL,
    destreza_id INT UNSIGNED NOT NULL,
    nivel            TINYINT UNSIGNED NOT NULL DEFAULT 1 CHECK (nivel BETWEEN 1 AND 5),
    actualizado_en   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (oferente_id, destreza_id),

    CONSTRAINT fk_oh_oferente
        FOREIGN KEY (oferente_id) REFERENCES oferente(id)
            ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT fk_oh_caract
        FOREIGN KEY (destreza_id) REFERENCES destreza(id)
            ON DELETE RESTRICT ON UPDATE CASCADE
);

-- ============================================================
-- 6. PUESTOS DE TRABAJO
-- ============================================================
CREATE TABLE puesto (
    id              INT UNSIGNED  AUTO_INCREMENT PRIMARY KEY,
    empresa_id      INT UNSIGNED  NOT NULL,
    titulo          VARCHAR(200)  NOT NULL,
    descripcion     TEXT          NOT NULL,
    salario_ofrecido DECIMAL(12,2) NULL,
    tipo_publicacion ENUM('PUBLICO','PRIVADO') NOT NULL DEFAULT 'PUBLICO',
    estado          ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
    creado_en       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_puesto_empresa
        FOREIGN KEY (empresa_id) REFERENCES empresa(id)
            ON DELETE CASCADE ON UPDATE CASCADE
);

-- ============================================================
-- 7. REQUISITOS DEL PUESTO
--    (puesto ↔ característica con nivel mínimo requerido)
-- ============================================================
CREATE TABLE puesto_requisito (
    puesto_id         INT UNSIGNED NOT NULL,
    destreza_id INT UNSIGNED NOT NULL,
    nivel_requerido   TINYINT UNSIGNED NOT NULL DEFAULT 1
    CHECK (nivel_requerido BETWEEN 1 AND 5),

    PRIMARY KEY (puesto_id, destreza_id),

    CONSTRAINT fk_pr_puesto
      FOREIGN KEY (puesto_id) REFERENCES puesto(id)
          ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT fk_pr_caract
      FOREIGN KEY (destreza_id) REFERENCES destreza(id)
          ON DELETE RESTRICT ON UPDATE CASCADE
);

SET FOREIGN_KEY_CHECKS = 1;