-- Estados
INSERT INTO estado (nombre) SELECT 'RECHAZADO' WHERE NOT EXISTS (SELECT 1 FROM estado WHERE nombre='RECHAZADO');
INSERT INTO estado (nombre) SELECT 'APROBADO'  WHERE NOT EXISTS (SELECT 1 FROM estado WHERE nombre='APROBADO');
INSERT INTO estado (nombre) SELECT 'PENDIENTE' WHERE NOT EXISTS (SELECT 1 FROM estado WHERE nombre='PENDIENTE');

-- Usuarios
INSERT IGNORE INTO usuario (idUsuario, clave, rol) --clave: '1'
VALUES ('1', '$2a$10$r9b216cEaFbEuKQ6RvK/.eDQksUtq7On/s4fs8N9FJuzk/NbKmyWG', 'ROLE_ADMIN');

INSERT IGNORE INTO usuario (idUsuario, clave, rol) --clave: '2'
VALUES ('2', '$2a$10$HVfrEeFtZ4n3BT/ErcBFKuhFz1BH1FbAtOLcEeUqFdJKcLM9sdu2S', 'ROLE_ADMIN');

INSERT IGNORE INTO usuario (idUsuario, clave, rol) --clave: '123456'
VALUES ('208640831', '$2a$10$IPyTU83ZhkohMO/9JoWKKO9t7sBBBXr1L4TY6d08h6cwxSZ5phAm.', 'ROLE_OFERENTE');

-- Administrador
INSERT IGNORE INTO administrador (idUsuario, nombre)
VALUES ('1', 'Eslyn Jara');

INSERT IGNORE INTO administrador (idUsuario, nombre)
VALUES ('2', 'Mishelle');

-- Oferente
INSERT IGNORE INTO oferente
(idUsuario, apellido, correo, nacionalidad, nombre, residencia, rutaCV, telefono, estado)
SELECT '208640831', 'Rojas', 'mishelle@mail.com', 'Costarricense', 'Mishelle', 'Heredia', NULL, '84549781',
       (SELECT id FROM estado WHERE nombre = 'APROBADO' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM oferente WHERE idUsuario = '208640831');

-- Caracteristica
INSERT IGNORE INTO Caracteristica (nombre, idPadre, activo) VALUES ('Informatica', NULL, 1);

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Lenguajes de Programacion', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Informatica') c
WHERE NOT EXISTS (SELECT 1 FROM Caracteristica WHERE nombre='Lenguajes de Programacion');

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Tecnologias Web', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Informatica') c
WHERE NOT EXISTS (SELECT 1 FROM Caracteristica WHERE nombre='Tecnologias Web');

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Bases de Datos', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Informatica') c
WHERE NOT EXISTS (SELECT 1 FROM Caracteristica WHERE nombre='Bases de Datos');

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Ciberseguridad', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Informatica') c
WHERE NOT EXISTS (SELECT 1 FROM Caracteristica WHERE nombre='Ciberseguridad');

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'DevOps', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Informatica') c
WHERE NOT EXISTS (SELECT 1 FROM Caracteristica WHERE nombre='DevOps');

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Java', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Lenguajes de Programacion') c
WHERE NOT EXISTS (SELECT 1 FROM Caracteristica WHERE nombre='Java');

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'C#', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Lenguajes de Programacion') c
WHERE NOT EXISTS (SELECT 1 FROM Caracteristica WHERE nombre='C#');

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'JavaScript', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Lenguajes de Programacion') c
WHERE NOT EXISTS (SELECT 1 FROM Caracteristica WHERE nombre='JavaScript');

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Python', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Lenguajes de Programacion') c
WHERE NOT EXISTS (SELECT 1 FROM Caracteristica WHERE nombre='Python');

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'C++', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Lenguajes de Programacion') c
WHERE NOT EXISTS (SELECT 1 FROM Caracteristica WHERE nombre='C++');

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'HTML', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Tecnologias Web') c
WHERE NOT EXISTS (SELECT 1 FROM Caracteristica WHERE nombre='HTML');

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'CSS', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Tecnologias Web') c
WHERE NOT EXISTS (SELECT 1 FROM Caracteristica WHERE nombre='CSS');

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'React', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Tecnologias Web') c
WHERE NOT EXISTS (SELECT 1 FROM Caracteristica WHERE nombre='React');

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Angular', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Tecnologias Web') c
WHERE NOT EXISTS (SELECT 1 FROM Caracteristica WHERE nombre='Angular');

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Vue', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Tecnologias Web') c
WHERE NOT EXISTS (SELECT 1 FROM Caracteristica WHERE nombre='Vue');

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'MySQL', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Bases de Datos') c
WHERE NOT EXISTS (SELECT 1 FROM Caracteristica WHERE nombre='MySQL');

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'PostgreSQL', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Bases de Datos') c
WHERE NOT EXISTS (SELECT 1 FROM Caracteristica WHERE nombre='PostgreSQL');

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'MongoDB', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Bases de Datos') c
WHERE NOT EXISTS (SELECT 1 FROM Caracteristica WHERE nombre='MongoDB');

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Oracle', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Bases de Datos') c
WHERE NOT EXISTS (SELECT 1 FROM Caracteristica WHERE nombre='Oracle');

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'SQL Server', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Bases de Datos') c
WHERE NOT EXISTS (SELECT 1 FROM Caracteristica WHERE nombre='SQL Server');

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Pentesting', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Ciberseguridad') c
WHERE NOT EXISTS (SELECT 1 FROM Caracteristica WHERE nombre='Pentesting');

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Analisis de vulnerabilidades', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Ciberseguridad') c
WHERE NOT EXISTS (SELECT 1 FROM Caracteristica WHERE nombre='Analisis de vulnerabilidades');

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Criptografia', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Ciberseguridad') c
WHERE NOT EXISTS (SELECT 1 FROM Caracteristica WHERE nombre='Criptografia');

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Seguridad en redes', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Ciberseguridad') c
WHERE NOT EXISTS (SELECT 1 FROM Caracteristica WHERE nombre='Seguridad en redes');

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Ethical Hacking', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='Ciberseguridad') c
WHERE NOT EXISTS (SELECT 1 FROM Caracteristica WHERE nombre='Ethical Hacking');

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Docker', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='DevOps') c
WHERE NOT EXISTS (SELECT 1 FROM Caracteristica WHERE nombre='Docker');

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Kubernetes', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='DevOps') c
WHERE NOT EXISTS (SELECT 1 FROM Caracteristica WHERE nombre='Kubernetes');

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'CI/CD', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='DevOps') c
WHERE NOT EXISTS (SELECT 1 FROM Caracteristica WHERE nombre='CI/CD');

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'AWS', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='DevOps') c
WHERE NOT EXISTS (SELECT 1 FROM Caracteristica WHERE nombre='AWS');

INSERT INTO Caracteristica (nombre, idPadre, activo)
SELECT 'Azure', c.id, 1
FROM (SELECT id FROM Caracteristica WHERE nombre='DevOps') c
WHERE NOT EXISTS (SELECT 1 FROM Caracteristica WHERE nombre='Azure');

-- Empresa demo (usuario: empresa@jobconnect.cr · clave: 123456 — mismo hash BCrypt que el oferente seed)
INSERT IGNORE INTO usuario (idUsuario, clave, rol)
VALUES ('empresa@jobconnect.cr', '$2a$10$IPyTU83ZhkohMO/9JoWKKO9t7sBBBXr1L4TY6d08h6cwxSZ5phAm.', 'ROLE_EMPRESA');

INSERT IGNORE INTO empresa (idUsuario, nombre, ubicacion, telefono, descripcion, tipo, correo, estado)
SELECT 'empresa@jobconnect.cr', 'TechCorp Costa Rica', 'San José, Costa Rica', '88887777001',
       'Soluciones de software y nube para PYME.', 'PRIVADO', 'empresa@jobconnect.cr',
       (SELECT id FROM estado WHERE nombre = 'APROBADO' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM empresa WHERE idUsuario = 'empresa@jobconnect.cr');

-- 5 puestos públicos (acceso=1), activos, con características hoja del catálogo
INSERT INTO puesto (id_usuario, descripcion, salario, activo, acceso, id_oferente_asignado)
SELECT 'empresa@jobconnect.cr', 'Desarrollador backend Java — microservicios', 2650000, 1, 1, NULL
WHERE NOT EXISTS (SELECT 1 FROM puesto WHERE descripcion = 'Desarrollador backend Java — microservicios');

INSERT INTO puesto (id_usuario, descripcion, salario, activo, acceso, id_oferente_asignado)
SELECT 'empresa@jobconnect.cr', 'Desarrollador frontend React + SPA', 2200000, 1, 1, NULL
WHERE NOT EXISTS (SELECT 1 FROM puesto WHERE descripcion = 'Desarrollador frontend React + SPA');

INSERT INTO puesto (id_usuario, descripcion, salario, activo, acceso, id_oferente_asignado)
SELECT 'empresa@jobconnect.cr', 'Analista datos Python y NoSQL', 1950000, 1, 1, NULL
WHERE NOT EXISTS (SELECT 1 FROM puesto WHERE descripcion = 'Analista datos Python y NoSQL');

INSERT INTO puesto (id_usuario, descripcion, salario, activo, acceso, id_oferente_asignado)
SELECT 'empresa@jobconnect.cr', 'Ingeniero DevOps — contenedores y nube AWS', 2700000, 1, 1, NULL
WHERE NOT EXISTS (SELECT 1 FROM puesto WHERE descripcion = 'Ingeniero DevOps — contenedores y nube AWS');

INSERT INTO puesto (id_usuario, descripcion, salario, activo, acceso, id_oferente_asignado)
SELECT 'empresa@jobconnect.cr', 'Full stack web JavaScript y Vue', 2280000, 1, 1, NULL
WHERE NOT EXISTS (SELECT 1 FROM puesto WHERE descripcion = 'Full stack web JavaScript y Vue');

INSERT INTO puesto_caracteristica (idPuesto, idCaracteristica)
SELECT p.idPuesto, c.id FROM puesto p, caracteristica c
WHERE p.descripcion = 'Desarrollador backend Java — microservicios' AND c.nombre = 'Java'
AND NOT EXISTS (SELECT 1 FROM puesto_caracteristica pc WHERE pc.idPuesto = p.idPuesto AND pc.idCaracteristica = c.id);

INSERT INTO puesto_caracteristica (idPuesto, idCaracteristica)
SELECT p.idPuesto, c.id FROM puesto p, caracteristica c
WHERE p.descripcion = 'Desarrollador backend Java — microservicios' AND c.nombre = 'PostgreSQL'
AND NOT EXISTS (SELECT 1 FROM puesto_caracteristica pc WHERE pc.idPuesto = p.idPuesto AND pc.idCaracteristica = c.id);

INSERT INTO puesto_caracteristica (idPuesto, idCaracteristica)
SELECT p.idPuesto, c.id FROM puesto p, caracteristica c
WHERE p.descripcion = 'Desarrollador frontend React + SPA' AND c.nombre = 'React'
AND NOT EXISTS (SELECT 1 FROM puesto_caracteristica pc WHERE pc.idPuesto = p.idPuesto AND pc.idCaracteristica = c.id);

INSERT INTO puesto_caracteristica (idPuesto, idCaracteristica)
SELECT p.idPuesto, c.id FROM puesto p, caracteristica c
WHERE p.descripcion = 'Desarrollador frontend React + SPA' AND c.nombre = 'HTML'
AND NOT EXISTS (SELECT 1 FROM puesto_caracteristica pc WHERE pc.idPuesto = p.idPuesto AND pc.idCaracteristica = c.id);

INSERT INTO puesto_caracteristica (idPuesto, idCaracteristica)
SELECT p.idPuesto, c.id FROM puesto p, caracteristica c
WHERE p.descripcion = 'Analista datos Python y NoSQL' AND c.nombre = 'Python'
AND NOT EXISTS (SELECT 1 FROM puesto_caracteristica pc WHERE pc.idPuesto = p.idPuesto AND pc.idCaracteristica = c.id);

INSERT INTO puesto_caracteristica (idPuesto, idCaracteristica)
SELECT p.idPuesto, c.id FROM puesto p, caracteristica c
WHERE p.descripcion = 'Analista datos Python y NoSQL' AND c.nombre = 'MongoDB'
AND NOT EXISTS (SELECT 1 FROM puesto_caracteristica pc WHERE pc.idPuesto = p.idPuesto AND pc.idCaracteristica = c.id);

INSERT INTO puesto_caracteristica (idPuesto, idCaracteristica)
SELECT p.idPuesto, c.id FROM puesto p, caracteristica c
WHERE p.descripcion = 'Ingeniero DevOps — contenedores y nube AWS' AND c.nombre = 'Docker'
AND NOT EXISTS (SELECT 1 FROM puesto_caracteristica pc WHERE pc.idPuesto = p.idPuesto AND pc.idCaracteristica = c.id);

INSERT INTO puesto_caracteristica (idPuesto, idCaracteristica)
SELECT p.idPuesto, c.id FROM puesto p, caracteristica c
WHERE p.descripcion = 'Ingeniero DevOps — contenedores y nube AWS' AND c.nombre = 'AWS'
AND NOT EXISTS (SELECT 1 FROM puesto_caracteristica pc WHERE pc.idPuesto = p.idPuesto AND pc.idCaracteristica = c.id);

INSERT INTO puesto_caracteristica (idPuesto, idCaracteristica)
SELECT p.idPuesto, c.id FROM puesto p, caracteristica c
WHERE p.descripcion = 'Full stack web JavaScript y Vue' AND c.nombre = 'JavaScript'
AND NOT EXISTS (SELECT 1 FROM puesto_caracteristica pc WHERE pc.idPuesto = p.idPuesto AND pc.idCaracteristica = c.id);

INSERT INTO puesto_caracteristica (idPuesto, idCaracteristica)
SELECT p.idPuesto, c.id FROM puesto p, caracteristica c
WHERE p.descripcion = 'Full stack web JavaScript y Vue' AND c.nombre = 'Vue'
AND NOT EXISTS (SELECT 1 FROM puesto_caracteristica pc WHERE pc.idPuesto = p.idPuesto AND pc.idCaracteristica = c.id);
