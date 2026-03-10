-- ============================================================
--  BOLSA DE EMPLEO  –  Esquema MySQL
--  Proyecto Web Java + MySQL
-- ============================================================

SET FOREIGN_KEY_CHECKS = 0;
DROP DATABASE IF EXISTS bolsa_empleo;
CREATE DATABASE bolsa_empleo CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bolsa_empleo;

-- ============================================================
-- 1. ADMINISTRADORES
-- ============================================================
CREATE TABLE administrador (
                               id            INT UNSIGNED    AUTO_INCREMENT PRIMARY KEY,
                               identificacion VARCHAR(20)    NOT NULL UNIQUE,
                               nombre        VARCHAR(100)    NOT NULL,
                               clave_hash    VARCHAR(255)    NOT NULL,          -- bcrypt / argon2
                               activo        TINYINT(1)      NOT NULL DEFAULT 1,
                               creado_en     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ============================================================
-- 2. CARACTERÍSTICAS (jerarquicas, auto-referenciadas)
-- ============================================================
CREATE TABLE caracteristica (
                                id            INT UNSIGNED    AUTO_INCREMENT PRIMARY KEY,
                                nombre        VARCHAR(100)    NOT NULL,
                                descripcion   TEXT,
                                padre_id      INT UNSIGNED    NULL,              -- NULL = raíz
                                activa        TINYINT(1)      NOT NULL DEFAULT 1,
                                creado_en     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                CONSTRAINT fk_caract_padre
                                    FOREIGN KEY (padre_id) REFERENCES caracteristica(id)
                                        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
-- 3. EMPRESAS
-- ============================================================
CREATE TABLE empresa (
                         id            INT UNSIGNED    AUTO_INCREMENT PRIMARY KEY,
                         nombre        VARCHAR(150)    NOT NULL,
                         localizacion  VARCHAR(200)    NOT NULL,
                         correo        VARCHAR(150)    NOT NULL UNIQUE,
                         telefono      VARCHAR(30),
                         descripcion   TEXT,
                         clave_hash    VARCHAR(255)    NOT NULL,
    -- Estados: PENDIENTE | APROBADA | RECHAZADA | SUSPENDIDA
                         estado        ENUM('PENDIENTE','APROBADA','RECHAZADA','SUSPENDIDA')
                                  NOT NULL DEFAULT 'PENDIENTE',
                         aprobado_por  INT UNSIGNED    NULL,              -- FK → administrador
                         aprobado_en   DATETIME        NULL,
                         creado_en     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         CONSTRAINT fk_empresa_admin
                             FOREIGN KEY (aprobado_por) REFERENCES administrador(id)
                                 ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
-- 4. OFERENTES (personas)
-- ============================================================
CREATE TABLE oferente (
                          id              INT UNSIGNED  AUTO_INCREMENT PRIMARY KEY,
                          identificacion  VARCHAR(20)   NOT NULL UNIQUE,
                          nombre          VARCHAR(100)  NOT NULL,
                          primer_apellido VARCHAR(100)  NOT NULL,
                          nacionalidad    VARCHAR(80)   NOT NULL,
                          telefono        VARCHAR(30),
                          correo          VARCHAR(150)  NOT NULL UNIQUE,
                          lugar_residencia VARCHAR(200) NOT NULL,
                          clave_hash      VARCHAR(255)  NOT NULL,
                          curriculum_pdf  VARCHAR(500)  NULL,             -- ruta / URL al PDF
    -- Estados: PENDIENTE | APROBADO | RECHAZADO | SUSPENDIDO
                          estado          ENUM('PENDIENTE','APROBADO','RECHAZADO','SUSPENDIDO')
                                  NOT NULL DEFAULT 'PENDIENTE',
                          aprobado_por    INT UNSIGNED  NULL,
                          aprobado_en     DATETIME      NULL,
                          creado_en       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT fk_oferente_admin
                              FOREIGN KEY (aprobado_por) REFERENCES administrador(id)
                                  ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
-- 5. HABILIDADES DEL OFERENTE
--    (oferente ↔ característica con nivel 1-5)
-- ============================================================
CREATE TABLE oferente_habilidad (
                                    oferente_id      INT UNSIGNED NOT NULL,
                                    caracteristica_id INT UNSIGNED NOT NULL,
                                    nivel            TINYINT UNSIGNED NOT NULL DEFAULT 1
                     CHECK (nivel BETWEEN 1 AND 5),
                                    actualizado_en   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                        ON UPDATE CURRENT_TIMESTAMP,
                                    PRIMARY KEY (oferente_id, caracteristica_id),
                                    CONSTRAINT fk_oh_oferente
                                        FOREIGN KEY (oferente_id) REFERENCES oferente(id)
                                            ON DELETE CASCADE ON UPDATE CASCADE,
                                    CONSTRAINT fk_oh_caract
                                        FOREIGN KEY (caracteristica_id) REFERENCES caracteristica(id)
                                            ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

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
    -- Estados: ACTIVO | INACTIVO
                        estado          ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
                        creado_en       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        actualizado_en  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                            ON UPDATE CURRENT_TIMESTAMP,
                        CONSTRAINT fk_puesto_empresa
                            FOREIGN KEY (empresa_id) REFERENCES empresa(id)
                                ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
-- 7. REQUISITOS DEL PUESTO
--    (puesto ↔ característica con nivel mínimo requerido)
-- ============================================================
CREATE TABLE puesto_requisito (
                                  puesto_id        INT UNSIGNED NOT NULL,
                                  caracteristica_id INT UNSIGNED NOT NULL,
                                  nivel_requerido  TINYINT UNSIGNED NOT NULL DEFAULT 1
                     CHECK (nivel_requerido BETWEEN 1 AND 5),
                                  PRIMARY KEY (puesto_id, caracteristica_id),
                                  CONSTRAINT fk_pr_puesto
                                      FOREIGN KEY (puesto_id) REFERENCES puesto(id)
                                          ON DELETE CASCADE ON UPDATE CASCADE,
                                  CONSTRAINT fk_pr_caract
                                      FOREIGN KEY (caracteristica_id) REFERENCES caracteristica(id)
                                          ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- ÍNDICES ADICIONALES (rendimiento)
-- ============================================================
CREATE INDEX idx_puesto_tipo_estado    ON puesto(tipo_publicacion, estado, creado_en DESC);
CREATE INDEX idx_puesto_empresa        ON puesto(empresa_id, estado);
CREATE INDEX idx_oferente_habilidad    ON oferente_habilidad(caracteristica_id, nivel);
CREATE INDEX idx_puesto_requisito_car  ON puesto_requisito(caracteristica_id, nivel_requerido);
CREATE INDEX idx_empresa_estado        ON empresa(estado);
CREATE INDEX idx_oferente_estado       ON oferente(estado);

-- ============================================================
-- VISTAS ÚTILES
-- ============================================================

-- 5 puestos públicos más recientes (para la portada)
CREATE OR REPLACE VIEW v_puestos_publicos_recientes AS
SELECT p.id, p.titulo, p.descripcion, p.salario_ofrecido,
       p.creado_en, e.nombre AS empresa_nombre, e.localizacion
FROM   puesto p
           JOIN   empresa e ON e.id = p.empresa_id
WHERE  p.tipo_publicacion = 'PUBLICO'
  AND  p.estado           = 'ACTIVO'
  AND  e.estado           = 'APROBADA'
ORDER  BY p.creado_en DESC
    LIMIT  5;

-- Grado de coincidencia puesto ↔ oferente
-- Número de requisitos del puesto que el oferente cubre con nivel >=  requerido
CREATE OR REPLACE VIEW v_coincidencia AS
SELECT
    pr.puesto_id,
    oh.oferente_id,
    COUNT(*)                                          AS requisitos_totales,
    SUM(oh.nivel >= pr.nivel_requerido)               AS requisitos_cubiertos,
    ROUND(
            100.0 * SUM(oh.nivel >= pr.nivel_requerido)
                / COUNT(*), 1)                          AS porcentaje_coincidencia
FROM  puesto_requisito pr
          JOIN  oferente_habilidad oh
                ON  oh.caracteristica_id = pr.caracteristica_id
GROUP BY pr.puesto_id, oh.oferente_id;

-- ============================================================
-- DATOS INICIALES
-- ============================================================

-- Administrador por defecto  (clave: 'admin123' — cambiar en producción)
INSERT INTO administrador (identificacion, nombre, clave_hash) VALUES
    ('00000001', 'Administrador Principal',
     '$2a$12$exampleHashChangeInProduction111111111111111111111111111');

-- Árbol de características de ejemplo
INSERT INTO caracteristica (id, nombre, padre_id) VALUES
                                                      (1,  'Lenguajes de Programación', NULL),
                                                      (2,  'C#',                         1),
                                                      (3,  'Java',                        1),
                                                      (4,  'Python',                      1),
                                                      (5,  'C++',                         1),
                                                      (6,  'Tecnologías Web',             NULL),
                                                      (7,  'HTML',                        6),
                                                      (8,  'CSS',                         6),
                                                      (9,  'JavaScript',                  6),
                                                      (10, 'TypeScript',                  6),
                                                      (11, 'Bases de Datos',              NULL),
                                                      (12, 'MySQL',                       11),
                                                      (13, 'PostgreSQL',                  11),
                                                      (14, 'MongoDB',                     11),
                                                      (15, 'Frameworks Backend',          NULL),
                                                      (16, 'Spring Boot',                 15),
                                                      (17, 'Django',                      15),
                                                      (18, 'Node.js',                     15),
                                                      (19, 'Habilidades Blandas',         NULL),
                                                      (20, 'Trabajo en equipo',           19),
                                                      (21, 'Comunicación',                19),
                                                      (22, 'Liderazgo',                   19);
