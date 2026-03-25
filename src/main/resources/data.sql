-- Estados
INSERT INTO estado (nombre) VALUES ('RECHAZADO');
INSERT INTO estado (nombre) VALUES ('APROBADO');
INSERT INTO estado (nombre) VALUES ('PENDIENTE');

-- Usuarios
INSERT INTO usuario (idUsuario, clave, rol) --clave: '1'
VALUES ('1', '$2a$10$r9b216cEaFbEuKQ6RvK/.eDQksUtq7On/s4fs8N9FJuzk/NbKmyWG', 'ROLE_ADMIN');

INSERT INTO usuario (idUsuario, clave, rol) --clave: '2'
VALUES ('2', '$2a$10$HVfrEeFtZ4n3BT/ErcBFKuhFz1BH1FbAtOLcEeUqFdJKcLM9sdu2S', 'ROLE_ADMIN');

INSERT INTO usuario (idUsuario, clave, rol) --clave: '123456'
VALUES ('208640831', '$2a$10$IPyTU83ZhkohMO/9JoWKKO9t7sBBBXr1L4TY6d08h6cwxSZ5phAm.', 'ROLE_OFERENTE');

-- Administrador
INSERT INTO administrador (idUsuario, nombre)
VALUES ('1', 'Eslyn Jara');

INSERT INTO administrador (idUsuario, nombre)
VALUES ('2', 'Mishelle');

-- Oferente
INSERT INTO oferente
(idUsuario, apellido, correo, nacionalidad, nombre, residencia, rutaCV, telefono, estado)
VALUES
    (
        '208640831',
        'Rojas',
        'mishelle@mail.com',
        'Costarricense',
        'Mishelle',
        'Heredia',
        NULL,
        '84549781',
        (SELECT id FROM estado WHERE nombre = 'APROBADO')
    );
-- Caracteristica
INSERT INTO Caracteristica (nombre, idPadre, activo) VALUES ('Informatica', NULL, 1);

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Lenguajes de Programacion', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Informatica') c;

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Tecnologias Web', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Informatica') c;

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Bases de Datos', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Informatica') c;

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Ciberseguridad', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Informatica') c;

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'DevOps', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Informatica') c;

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Java', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Lenguajes de Programacion') c;

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'C#', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Lenguajes de Programacion') c;

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'JavaScript', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Lenguajes de Programacion') c;

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Python', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Lenguajes de Programacion') c;

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'C++', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Lenguajes de Programacion') c;

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'HTML', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Tecnologias Web') c;

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'CSS', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Tecnologias Web') c;

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'React', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Tecnologias Web') c;

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Angular', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Tecnologias Web') c;

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Vue', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Tecnologias Web') c;

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'MySQL', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Bases de Datos') c;

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'PostgreSQL', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Bases de Datos') c;

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'MongoDB', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Bases de Datos') c;

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Oracle', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Bases de Datos') c;

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'SQL Server', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Bases de Datos') c;

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Pentesting', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Ciberseguridad') c;

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Analisis de vulnerabilidades', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Ciberseguridad') c;

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Criptografia', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Ciberseguridad') c;

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Seguridad en redes', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Ciberseguridad') c;

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Ethical Hacking', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Ciberseguridad') c;

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Docker', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='DevOps') c;

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Kubernetes', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='DevOps') c;

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'CI/CD', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='DevOps') c;

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'AWS', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='DevOps') c;

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Azure', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='DevOps') c;

-- Empresa demo (usuario: empresa@jobconnect.cr · clave: 123456 — mismo hash BCrypt que el oferente seed)
INSERT INTO usuario (idUsuario, clave, rol)
VALUES ('empresa@jobconnect.cr', '$2a$10$IPyTU83ZhkohMO/9JoWKKO9t7sBBBXr1L4TY6d08h6cwxSZ5phAm.', 'ROLE_EMPRESA');

INSERT INTO empresa (idUsuario, nombre, ubicacion, telefono, descripcion, tipo, correo, estado)
VALUES (
    'empresa@jobconnect.cr',
    'TechCorp Costa Rica',
    'San José, Costa Rica',
    '88887777001',
    'Soluciones de software y nube para PYME.',
    'PRIVADO',
    'empresa@jobconnect.cr',
    (SELECT id FROM estado WHERE nombre = 'APROBADO')
);

-- 5 puestos públicos (acceso=1), activos, con características hoja del catálogo
INSERT INTO puesto (id_usuario, descripcion, salario, activo, acceso, id_oferente_asignado)
VALUES
    ('empresa@jobconnect.cr', 'Desarrollador backend Java — microservicios', 2650000, 1, 1, NULL),
    ('empresa@jobconnect.cr', 'Desarrollador frontend React + SPA', 2200000, 1, 1, NULL),
    ('empresa@jobconnect.cr', 'Analista datos Python y NoSQL', 1950000, 1, 1, NULL),
    ('empresa@jobconnect.cr', 'Ingeniero DevOps — contenedores y nube AWS', 2700000, 1, 1, NULL),
    ('empresa@jobconnect.cr', 'Full stack web JavaScript y Vue', 2280000, 1, 1, NULL);

INSERT INTO puesto_caracteristica (idPuesto, idCaracteristica)
SELECT p.idPuesto, c.id FROM puesto p, caracteristica c
WHERE p.descripcion = 'Desarrollador backend Java — microservicios' AND c.nombre = 'Java';
INSERT INTO puesto_caracteristica (idPuesto, idCaracteristica)
SELECT p.idPuesto, c.id FROM puesto p, caracteristica c
WHERE p.descripcion = 'Desarrollador backend Java — microservicios' AND c.nombre = 'PostgreSQL';

INSERT INTO puesto_caracteristica (idPuesto, idCaracteristica)
SELECT p.idPuesto, c.id FROM puesto p, caracteristica c
WHERE p.descripcion = 'Desarrollador frontend React + SPA' AND c.nombre = 'React';
INSERT INTO puesto_caracteristica (idPuesto, idCaracteristica)
SELECT p.idPuesto, c.id FROM puesto p, caracteristica c
WHERE p.descripcion = 'Desarrollador frontend React + SPA' AND c.nombre = 'HTML';

INSERT INTO puesto_caracteristica (idPuesto, idCaracteristica)
SELECT p.idPuesto, c.id FROM puesto p, caracteristica c
WHERE p.descripcion = 'Analista datos Python y NoSQL' AND c.nombre = 'Python';
INSERT INTO puesto_caracteristica (idPuesto, idCaracteristica)
SELECT p.idPuesto, c.id FROM puesto p, caracteristica c
WHERE p.descripcion = 'Analista datos Python y NoSQL' AND c.nombre = 'MongoDB';

INSERT INTO puesto_caracteristica (idPuesto, idCaracteristica)
SELECT p.idPuesto, c.id FROM puesto p, caracteristica c
WHERE p.descripcion = 'Ingeniero DevOps — contenedores y nube AWS' AND c.nombre = 'Docker';
INSERT INTO puesto_caracteristica (idPuesto, idCaracteristica)
SELECT p.idPuesto, c.id FROM puesto p, caracteristica c
WHERE p.descripcion = 'Ingeniero DevOps — contenedores y nube AWS' AND c.nombre = 'AWS';

INSERT INTO puesto_caracteristica (idPuesto, idCaracteristica)
SELECT p.idPuesto, c.id FROM puesto p, caracteristica c
WHERE p.descripcion = 'Full stack web JavaScript y Vue' AND c.nombre = 'JavaScript';
INSERT INTO puesto_caracteristica (idPuesto, idCaracteristica)
SELECT p.idPuesto, c.id FROM puesto p, caracteristica c
WHERE p.descripcion = 'Full stack web JavaScript y Vue' AND c.nombre = 'Vue';
