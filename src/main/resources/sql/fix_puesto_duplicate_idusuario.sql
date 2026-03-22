-- =============================================================================
-- Quitar columna duplicada idUsuario dejando solo id_usuario (FK a empresa).
-- Error 1828: hay que borrar primero la FOREIGN KEY que usa esa columna.
-- =============================================================================

USE bolsa_empleo;

-- Ver restricciones y columnas (opcional):
-- SHOW CREATE TABLE puesto;
-- SELECT * FROM information_schema.KEY_COLUMN_USAGE WHERE TABLE_SCHEMA = 'bolsa_empleo' AND TABLE_NAME = 'puesto';

-- 0) Copiar datos si hace falta (solo si id_usuario está vacía y idUsuario tiene valor):
-- UPDATE puesto SET id_usuario = idUsuario WHERE id_usuario IS NULL AND idUsuario IS NOT NULL;

-- 1) Eliminar la FK que apunta a empresa usando la columna idUsuario
--    (el nombre puede ser puesto_ibfk_1 u otro; confírmalo con SHOW CREATE TABLE puesto;)
ALTER TABLE puesto DROP FOREIGN KEY puesto_ibfk_1;

-- 2) Ahora sí se puede borrar la columna duplicada
ALTER TABLE puesto DROP COLUMN idUsuario;

-- 3) Crear de nuevo el FK sobre la columna que usa Hibernate (id_usuario -> empresa)
--    Ajusta el nombre de la columna PK en empresa si en tu BD no es 'idUsuario':
--    SHOW CREATE TABLE empresa;
ALTER TABLE puesto
    ADD CONSTRAINT fk_puesto_empresa_usuario
    FOREIGN KEY (id_usuario) REFERENCES empresa(idUsuario) ON DELETE CASCADE;

-- Si falla el paso 3 porque ya existía otro FK en id_usuario, revisa:
-- SHOW CREATE TABLE puesto;
