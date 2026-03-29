# Bolsa de empleo (P1BolsaEmpleo)

Aplicación web para publicar puestos, postularse y administrar la bolsa. Incluye perfiles para **oferentes** (candidatos), **empresas** y **administradores**.

## Tecnologías

- Java 24  
- Spring Boot 4 (Web, Thymeleaf, Data JPA, Security)  
- MySQL  
- Maven

## Requisitos

- JDK 24  
- MySQL en ejecución  
- Maven (o usar el wrapper si lo añades al repo)

## Configuración

1. Crea la base de datos (o deja que la URL la cree si usas `createDatabaseIfNotExist=true`).
2. Ajusta en `src/main/resources/application.properties`:
  - `spring.datasource.url` (host, puerto y nombre de la base, por defecto `bolsa_empleo`)
  - `spring.datasource.username` y `spring.datasource.password`

## Cómo ejecutar

Desde la raíz del proyecto:

```bash
mvn spring-boot:run
```

La aplicación queda disponible en **[http://localhost:8080](http://localhost:8080)** (puerto definido en `application.properties`).

Al arrancar, JPA actualiza el esquema y se pueden cargar datos iniciales según la configuración de `spring.sql.init` y los scripts en `src/main/resources`.

## Estructura principal

- `src/main/java/una/ac/cr/p1bolsaempleo/` — controladores, servicios, modelos y seguridad  
- `src/main/resources/templates/` — vistas Thymeleaf  
- `src/main/resources/static/` — CSS y recursos estáticos

## Pruebas

```bash
mvn test
```

