#Borro la base de datos asi no genera errores en caso de ya existir una copia.
DROP DATABASE universidad;

# Creo de la Base de Datos
CREATE DATABASE universidad;

# selecciono la base de datos sobre la cual voy a hacer modificaciones
USE universidad;

#------------------------------------------------------------------------------------
# Creacion de tablas para las entidades

CREATE TABLE alumnos(
	LU INT unsigned NOT NULL,
	doc_tipo VARCHAR(45) NOT NULL,
	doc_nro INT unsigned NOT NULL,
	apellido VARCHAR(45) NOT NULL,
	nombre VARCHAR(45) NOT NULL,
	genero VARCHAR(45) NOT NULL,

    
	CONSTRAINT pk_alumnos
	PRIMARY KEY(LU)
    
) ENGINE=InnoDB;

CREATE TABLE datos_de_contactos(
	direccion VARCHAR(45) NOT NULL,
	numero INT unsigned NOT NULL,
	piso INT unsigned NOT NULL,
	departamento VARCHAR(45) NOT NULL,
	telefono VARCHAR(45) NOT NULL,
	mail VARCHAR(45) NOT NULL,

    
	CONSTRAINT pk_datos_de_contactos
	PRIMARY KEY()
    
) ENGINE=InnoDB;

CREATE TABLE carreras(
	duracion INT unsigned NOT NULL,
	nombre VARCHAR(45) NOT NULL,
    
	CONSTRAINT pk_carreras
	PRIMARY KEY(nombre)
    
) ENGINE=InnoDB;

CREATE TABLE planes(
	nombre_carrera VARCHAR(45) NOT NULL,
	id_plan INT unsigned NOT NULL,

	PRIMARY KEY (nombre_carrera,id_plan),

	CONSTRAINT fk_planes_carreras
	FOREIGN KEY (nombre) REFERENCES carreras (nombre)
	ON DELETE RESTRICT ON UPDATE CASCADE,
    
) ENGINE=InnoDB;

CREATE TABLE profesores(
	matricula INT unsigned NOT NULL,
	doc_tipo VARCHAR(45) NOT NULL,
	doc_nro INT unsigned NOT NULL,
	apellido VARCHAR(45) NOT NULL,
	nombre VARCHAR(45) NOT NULL,
	genero VARCHAR(45) NOT NULL,
	cuil INT unsigned NOT NULL,

    
	CONSTRAINT pk_profesores
	PRIMARY KEY(matricula)

    
) ENGINE=InnoDB;

CREATE TABLE materias(
	id INT unsigned NOT NULL,
	carga_horaria INT unsigned NOT NULL,
	nombre VARCHAR(45) NOT NULL,
    
	CONSTRAINT pk_materias
	PRIMARY KEY(id),
	
	CONSTRAINT fk_materias_profesores
	FOREIGN KEY (matricula) REFERENCES profesores(matricula)
	ON DELETE RESTRICT ON UPDATE CASCADE
    
) ENGINE=InnoDB;


#-------------------------------------------------------------------------
# Creación Tablas para las relaciones

CREATE TABLE inscripciones(
	codigo_inscripcion VARCHAR(45) NOT NULL,
	fecha DATE NOT NULL DEFAULT '2020-01-01',
	LU INT unsigned NOT NULL,
	nombre VARCHAR(45) NOT NULL,
    
    
    CONSTRAINT pk_inscripciones
    PRIMARY KEY (codigo_inscripcion),
	
	CONSTRAINT fk_inscripciones_alumnos
	FOREIGN KEY (LU) REFERENCES alumnos(LU)
	ON DELETE RESTRICT ON UPDATE CASCADE,
	
	CONSTRAINT fk_inscripciones_carreras
	FOREIGN KEY (nombre) REFERENCES carreras (nombre)
	ON DELETE RESTRICT ON UPDATE CASCADE
	
    
) ENGINE=InnoDB;

CREATE TABLE correlativas(
	id_correlativas INT unsigned NOT NULL,
	id_materias INT unsigned NOT NULL,
	
	PRIMARY KEY (id_materias,id_correlativas),

	CONSTRAINT fk_correlativas_materias
	FOREIGN KEY (id_materias) REFERENCES materias (id)
	ON DELETE RESTRICT ON UPDATE CASCADE
	
	
	#es una relacion reflexiva de correlativas y materias ya q son lo mismo
	
) ENGINE=InnoDB;

CREATE TABLE notas(
	puntaje INT unsigned NOT NULL,
	estado VARCHAR(45) NOT NULL,
	id_materias INT unsigned NOT NULL,
	LU INT unsigned NOT NULL,
    
	CONSTRAINT pk_notas
	PRIMARY KEY(LU,id),
	
	CONSTRAINT fk_inscripciones_alumnos
	FOREIGN KEY (LU) REFERENCES alumnos(LU)
	ON DELETE RESTRICT ON UPDATE CASCADE,
	
	CONSTRAINT fk_correlativas_materias
	FOREIGN KEY (id_materias) REFERENCES materias (id)
	ON DELETE RESTRICT ON UPDATE CASCADE
    
) ENGINE=InnoDB;
	

#------------------------------------------------------------------------------------

# Creacion de usuarios

#admin
	CREATE USER 'admin'@'localhost' IDENTIFIED BY 'admin';
    GRANT ALL PRIVILEGES ON vuelos.* TO 'admin'@localhost;