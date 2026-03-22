-- Ejecutar en MySQL si la tabla de unión no existe (guarda la lista de características por puesto).
-- Hibernate con ddl-auto=update suele crearla sola; si falla el guardado, crea esto y ajusta Puesto.idUsuario.

CREATE TABLE IF NOT EXISTS puesto_caracteristica (
    idPuesto INT NOT NULL,
    idCaracteristica INT NOT NULL,
    PRIMARY KEY (idPuesto, idCaracteristica),
    CONSTRAINT fk_pc_puesto
        FOREIGN KEY (idPuesto) REFERENCES Puesto(idPuesto) ON DELETE CASCADE,
    CONSTRAINT fk_pc_caracteristica
        FOREIGN KEY (idCaracteristica) REFERENCES Caracteristica(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Si Puesto.idUsuario es VARCHAR(9) y la empresa usa correo como id (más largo):
-- ALTER TABLE Puesto MODIFY COLUMN idUsuario VARCHAR(50) NOT NULL;
