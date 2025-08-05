# 📚 LiterAlura

Literalura es una aplicación de consola desarrollada en Java que interactúa con la [API de Gutendex](https://gutendex.com/) para buscar, registrar y gestionar libros en una base de datos PostgreSQL. También permite filtrar y mostrar los libros y autores registrados.

---

## 🚀 Funcionalidades

- 🔍 Buscar libros usando la API de Gutendex.
- 📥 Guardar libros seleccionados en una base de datos local.
- 👨‍💻 Registrar autores y su información (año de nacimiento y fallecimiento).
- 🌐 Guardar y listar los idiomas asociados a cada libro.
- 📊 Mostrar libros filtrados por:
  - Autores vivos durante un rango de años determinado.
  - Idioma.
  - Los 10 libros más descargados.
- 🧾 Listar todos los autores o libros registrados.

---

## 🛠️ Tecnologías utilizadas

- Java 17+
- Spring Boot 3.5+
- Spring Data JPA (Hibernate)
- PostgreSQL
- Maven
- API de Gutendex (para obtener datos de libros)

---


## ⚙️ Configuración

### 1. Configurar base de datos

Asegúrate de tener PostgreSQL corriendo. Ejemplo de configuración (`src/main/resources/application.properties`):

```properties
spring.datasource.url=jdbc:postgresql://localhost:puerto/db_name
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true
```

## 💡 Cómo usar
### 🖥️ Opciones del menú
Al ejecutar la aplicación, verás un menú como este:
```1 - Buscar y registrar un libro
2 - Listar libros registrados
3 - Listar autores vivos en un año determinado
4 - Listar libros por idioma
5 - Listar autores registrados
6 - Mostrar los 10 libros más descargados
0 - Salir
```
#### Ejemplo de uso:
1. Elige la opción 1 para buscar un libro.

2. Escribe un título (por ejemplo, Pride and Prejudice).

3. Selecciona uno de los resultados para registrarlo.

4. Luego puedes usar las opciones 2 a 6 para ver y filtrar la información.

## 📌 Requisitos
- Java 17 o superior
- PostgreSQL en ejecución
- Maven instalado

## 📥 Instalación
```
git clone https://github.com/JJGG1/literalura.git
cd literalura
mvn spring-boot:run
```
