USE `universidad` ; 

/* Alumnos */
	INSERT INTO alumnos (dni, nombre, apellido, genero) VALUES (1, "Juan", "Perez", "Masculino");
	INSERT INTO alumnos (dni, nombre, apellido, genero) VALUES (2, "Carlos", "Martinez", "Masculino");
	INSERT INTO alumnos (dni, nombre, apellido, genero) VALUES (3, "Maria", "Gonzalez", "Femenino");
	INSERT INTO alumnos (dni, nombre, apellido, genero) VALUES (4, "Carla", "Gomez", "Femenino");

/* Carreras */
	INSERT INTO carreras (nombre, duracion) VALUES ("Licenciatura en computacion", 5);
	INSERT INTO carreras (nombre, duracion) VALUES ("Ingenieria civil", 6);
	
/* Planes */
	INSERT INTO planes (id_carrera, version) VALUES (1, 2007);
	INSERT INTO planes (id_carrera, version) VALUES (1, 2012);
	INSERT INTO planes (id_carrera, version) VALUES (2, 2009);
	INSERT INTO planes (id_carrera, version) VALUES (2, 2015);

/* Materias */	
	/*-----------------------------------*/
	/* Licencuatura en computación */
	/*-----------------------------------*/
	INSERT INTO materias (nombre, carga_horaria) VALUES ("RPA", 128); /* 1 */
	INSERT INTO materias (nombre, carga_horaria) VALUES ("Algebra", 128); /* 2 */
	INSERT INTO materias (nombre, carga_horaria) VALUES ("Analisis 1", 128); /* 3 */
	INSERT INTO materias (nombre, carga_horaria) VALUES ("IPOO", 128); /* 4 */
	INSERT INTO materias (nombre, carga_horaria) VALUES ("Estructuras de datos", 128); /* 5 */
	INSERT INTO materias (nombre, carga_horaria) VALUES ("TDP", 128); /* 6 */
	INSERT INTO planes_materias (id_plan, id_materia) VALUES (1, 1);
	INSERT INTO planes_materias (id_plan, id_materia) VALUES (1, 2);
	INSERT INTO planes_materias (id_plan, id_materia) VALUES (1, 3);
	INSERT INTO planes_materias (id_plan, id_materia) VALUES (1, 4);
	INSERT INTO planes_materias (id_plan, id_materia) VALUES (1, 5);
	INSERT INTO planes_materias (id_plan, id_materia) VALUES (1, 6);
	INSERT INTO planes_materias (id_plan, id_materia) VALUES (2, 1);
	INSERT INTO planes_materias (id_plan, id_materia) VALUES (2, 2);
	INSERT INTO planes_materias (id_plan, id_materia) VALUES (2, 3);
	INSERT INTO planes_materias (id_plan, id_materia) VALUES (2, 4);
	INSERT INTO planes_materias (id_plan, id_materia) VALUES (2, 5);
	INSERT INTO planes_materias (id_plan, id_materia) VALUES (2, 6);
	/*-----------------------------------*/
	/* Ingenieria civil */
	/*-----------------------------------*/
	INSERT INTO materias (nombre, carga_horaria) VALUES ("Dibujo tecnico", 128); /* 7 */
	INSERT INTO materias (nombre, carga_horaria) VALUES ("Analisis 2", 128); /* 8 */
	INSERT INTO materias (nombre, carga_horaria) VALUES ("Fisica 1", 128); /* 9 */
	INSERT INTO materias (nombre, carga_horaria) VALUES ("Fisica 2", 128); /* 10 */
	INSERT INTO materias (nombre, carga_horaria) VALUES ("Resistencia de materiales", 128); /* 11 */
	INSERT INTO materias (nombre, carga_horaria) VALUES ("Construccion 1", 128); /* 12 */
	INSERT INTO materias (nombre, carga_horaria) VALUES ("Construccion 2", 128);	/* 13 */
	INSERT INTO planes_materias (id_plan, id_materia) VALUES (3, 2);
	INSERT INTO planes_materias (id_plan, id_materia) VALUES (3, 3);
	INSERT INTO planes_materias (id_plan, id_materia) VALUES (3, 7);
	INSERT INTO planes_materias (id_plan, id_materia) VALUES (3, 8);
	INSERT INTO planes_materias (id_plan, id_materia) VALUES (3, 9);
	INSERT INTO planes_materias (id_plan, id_materia) VALUES (3, 10);
	INSERT INTO planes_materias (id_plan, id_materia) VALUES (3, 11);
	INSERT INTO planes_materias (id_plan, id_materia) VALUES (3, 12);
	INSERT INTO planes_materias (id_plan, id_materia) VALUES (3, 13);
	INSERT INTO planes_materias (id_plan, id_materia) VALUES (4, 2);
	INSERT INTO planes_materias (id_plan, id_materia) VALUES (4, 3);
	INSERT INTO planes_materias (id_plan, id_materia) VALUES (4, 7);
	INSERT INTO planes_materias (id_plan, id_materia) VALUES (4, 8);
	INSERT INTO planes_materias (id_plan, id_materia) VALUES (4, 9);
	INSERT INTO planes_materias (id_plan, id_materia) VALUES (4, 10);
	INSERT INTO planes_materias (id_plan, id_materia) VALUES (4, 11);
	INSERT INTO planes_materias (id_plan, id_materia) VALUES (4, 12);
	INSERT INTO planes_materias (id_plan, id_materia) VALUES (4, 13);
	/*-----------------------------------*/
	
	/* Inscripciones en una carrera */
	INSERT INTO inscripciones_carreras (LU_alumno, id_plan, fecha) VALUES (1, 2, str_to_date('4/12/2012', '%d/%m/%Y'));
	INSERT INTO inscripciones_carreras (LU_alumno, id_plan, fecha) VALUES (2, 1, str_to_date('5/12/2009', '%d/%m/%Y'));
	INSERT INTO inscripciones_carreras (LU_alumno, id_plan, fecha) VALUES (2, 4, str_to_date('2/12/2016', '%d/%m/%Y'));
	
	
	
	/* Profesores */
	INSERT INTO profesores (matricula, dni, cuil, nombre, apellido, genero) VALUES (1111, 10, 101111, "Jorge", "Lombardi", "Masculino");
	INSERT INTO profesores (matricula, dni, cuil, nombre, apellido, genero) VALUES (2222, 20, 202222, "Damian", "Fernandez", "Masculino");
	INSERT INTO profesores (matricula, dni, cuil, nombre, apellido, genero) VALUES (3333, 30, 303333, "Julia", "Roca", "Femenino");
	INSERT INTO profesores (matricula, dni, cuil, nombre, apellido, genero) VALUES (4444, 40, 404444, "Ana", "Diaz", "Femenino");
	
	
