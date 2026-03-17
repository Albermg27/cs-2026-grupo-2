# Praćtica 1 - Control de calidad de una aplicación web 

**Grupo 2**

## Miembros del Equipo
| Nombre y Apellidos | Correo URJC | Usuario GitHub |
|:--- |:--- |:--- |
| Marcos García García | m.garciaga.2022@alumnos.urjc.es | marcosgrc |
| Adrián Muñoz Serrano | a.munozse.2022@alumnos.urjc.es | adri04ms |
| Jorge Padilla Rodríguez | j.padilla.2021@alumnos.urjc.es | Jorge-PR |
| Naroa Martín Simón | n.martins.2022@alumnos.urjc.es | NaroaMS04 |
| [Nombre 5] | [email5]@alumnos.urjc.es | [User5] |
| [Nombre 6] | [email5]@alumnos.urjc.es | [User5] |
| [Nombre 7] | [email5]@alumnos.urjc.es | [User5] |

---

### **Participación de Miembros en la Práctica 1**

#### **Alumno 1 - Marcos García García**

Para esta práctica he realizado las siguientes tareas: organizar la plantilla inicial del documento ANALISIS_CALIDAD.md, añadir el code smell 1 (Literales duplicados) detectado por SonarCloud y añadir dos code smells detectados manualmente, el 1 (Duplicación de código en métodos deposit) y el 4 (Método demasiado largo).

| Nº    | Commits      |
|:------------: |:------------:|
|1| [Plantilla de la memoria .md](https://github.com/Albermg27/cs-2026-grupo-2/commit/77c3c9600e47dc3e24a7eba3e93b1fbdb354e9d3)  |
|2| [Bad Smell: Duplicación de código en métodos deposit](https://github.com/Albermg27/cs-2026-grupo-2/commit/0735e27565d2932f82a75d56360c51f2d46cfdf6)  |
|3| [Bad Smells: Literales duplicados y Método demasiado largo](https://github.com/Albermg27/cs-2026-grupo-2/commit/ea6557df578fb3ab0acf6d9aa2bb0c1ff5ac259b)  |


---

#### **Alumno 2 - Adrián Muñoz Serrano**

Para esta práctica he realizado las siguientes tareas: he identificado manualmente dos code smells en el proyecto; Data Clumps y Magic Numbers, ambos localizados en la clase AccountService.java. Además, he añadido el code smell 2 (Variable local no utilizada) y el code smell 3 (Comparación de Strings con operador de identidad) detectados por SonarCloud.

| Nº    | Commits      |
|:------------: |:------------:|
|1| [Bad Smeels: Data Clumps y Magic Numbers](https://github.com/Albermg27/cs-2026-grupo-2/commit/2008713329e8d1ad508ef72a494dd66ae1d104eb)  |
|2| [Bad Smells SonarCloud: Variable local no utilizada y Comparación de Strings con operador de identidad](https://github.com/Albermg27/cs-2026-grupo-2/commit/29af02cd1448e647b9d99a9d1e8028cc15ce60b2)  |

---

#### **Alumno 3 - Jorge Padilla Rodríguez**

Durante esta práctica he realizado la detección manual de los siguientes code smells: bad smell 11, referente a la existencia de código muerto dentro del método deposit; bad smell 12, que describe código duplicado o redundante dentro del método deposit; bad smell 13, que muestra cómo aparece código duplicado (la validación de una variable) a lo largo de los métodos deposit, withdraw y transfer; bad smell 14, donde vemos que el método rm tiene un nombre poco comunicativo.

| Nº    | Commits      |
|:------------: |:------------:|
|1| [Bad smell 11: código muerto dentro del método deposit](https://github.com/Albermg27/cs-2026-grupo-2/commit/bb9f0a1d3d3ad7f0617ce1bad77b251c15ba8c37)  |
|2| [Bad smells 12 y 13: código duplicado en deposit y en varios métodos](https://github.com/Albermg27/cs-2026-grupo-2/commit/e8e92c37d5d9b2904fbcb7993ad92e329bebb905)  |
|3| [Bad smell 14: nombre de método poco comunicativo](https://github.com/Albermg27/cs-2026-grupo-2/commit/7807b3b361d84d4ff161600c8df9f82b68016a0f)  |

---

#### **Alumno 4 - Naroa Martín Simón**

En esta práctica me he encargado de revisar a mano la clase AccountService.java para encontrar fallos de diseño que SonarCloud no detecta. He analizado dos problemas: Primitive Obsession, que trata sobre cómo el uso excesivo de tipos básicos (como double o String) para el dinero o las cuentas puede causar errores; y Feature Envy, donde he explicado que el servicio está "cotilleando" demasiado el saldo de las cuentas en lugar de dejar que la propia clase Account gestione su lógica interna.

| Nº    | Commits      |
|:------------: |:------------:|
|1| [Bad smells 8 and 9 added: Primitive Obsession y Feature Envy](https://github.com/Albermg27/cs-2026-grupo-2/commit/caa1cef5baf1f8c89de75bc97b75bcaefff4818a)  |

---

#### **Alumno 5 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      |
|:------------: |:------------:|
|1| [Issue detectada](URL_commit_1)  |
|2| [Prueba unitaria implementada](URL_commit_2)  |
|3| [Refactorización implementada](URL_commit_3)  |
|4| [Caso de TDD implementado](URL_commit_4)  |
|5| [Prueba de sistema implementada](URL_commit_5)  |

---