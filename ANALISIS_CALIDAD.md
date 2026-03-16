# Memoria de Análisis de Calidad de Código (Práctica 2)

## 1. Información del Grupo
* **Nombre del Grupo:** Grupo 2
* **Miembros:**
    * Marcos García García
    * Narao Martín Simón
    * Alberto Mayoral Gómez
    * Icíar Moreno López
    * Adrián Muñoz Serrano
    * Jorge Padilla Rodríguez
    * Laura Pineda Ballesteros


---

## 2. Dashboard de SonarCloud (Análisis Inicial)
Captura de pantalla del **dashboard** (Overview) de SonarCloud donde se visualizan las métricas principales tras el primer análisis.

![Overview SonarQube](img/dashboard_sonarcloud.png)

---

## 3. Detección y Análisis de Bad Smells
A continuación, se detallan los problemas detectados en la clase `AccountService.java` mediante el uso de SonarCloud e inspección manual.

## 3.1 Bad Smells detectados por SonarCloud
### Bad Smell 1: Literales duplicados
* **Ubicación:** `AccountService.java` - Líneas `107`, `114`, `156` y `163`.
* **Reporte de la issue:**

    ![Captura SonarCloud](img/duplicate-literals-sonarcloud.png)

    ![Strings Duplicados 1](img/duplicate-literals.png)

    ![Strings Duplicados 2](img/duplicate-literals2.png)

* **Explicación del bad smell:**
  * Descripción: Encontramos cuatro Strings literales idénticos hardcodeados en la clase. Esto efectivamente es así, no se trata de un falso positivo.
  
  * Problema: En caso de querer cambiar el String, el proceso de refactor podría generar inconsistencias si no se modifica el String en todos los lugares.

  * Como solucionarlo: El problema se solucionaría creando una constante `DEPOSIT_CONFIRMATION` cuyo valor sea el String, y usándola en todos los lugares donde se necesita ese String. De esta manera al cambiar el valor de la constante se refleja en todos los demás lugares y se mantiene consistente.


## 3.2 Bad Smells detectados manualmente

### Bad Smell 1: Duplicación de código en métodos deposit
* **Ubicación:** `AccountService.java` - Métodos `deposit(String, double, String) y deposit(String, double)`
* **Reporte de la issue:**

    ![Codigo Duplicado 1](img/cod-duplicado1.png)

    ![Codigo Duplicado 2](img/cod-duplicado2.png)


* **Explicación del bad smell:**
   * Descripción: Ambos métodos repiten exactamente la misma lógica de validación (cuatro bloques if para la  cantidad) y la misma lógica para registrar el depósito y enviar notificaciones.

   * Problema: Si las reglas de negocio para los depósitos cambian (por ejemplo, el límite máximo), hay que modificarlo en varios lugares, lo que aumenta el riesgo de inconsistencias y errores.

   * Como solucionarlo: El problema se podría solucionar unificando ambos métodos en uno solo, ya que el único cambio en el código es que como descripción del depósito se usa "Quick deposit" de forma predeterminada en el método que no recibe descripción.



### Bad Smell 2: Mysterious Names / Non-Descriptive Names (Nombres poco claros)
* **Ubicación:** `AccountService.java` - Método `transfer(String fromAccountNumber, String toAccountNumber, double amount)` Líneas `231-232`
* **Reporte de la issue:**

  ![Mysterious Names](img/MysteriousNames.png)
* **Explicación del bad smell:**
    * Descripción: En el método transfer se utilizan variables con nombres muy cortos y poco descriptivos (m y o). Estos nombres no reflejan claramente qué representan dentro de la lógica del método.

   * Problema: El uso de nombres crípticos reduce la legibilidad del código y dificulta su comprensión, especialmente para otros desarrolladores o incluso para el propio autor cuando vuelva a revisar el código en el futuro. Esto obliga a analizar el contexto del método para entender qué representan las variables, lo que aumenta el tiempo necesario para mantener o modificar el código.

   * Cómo solucionarlo: Este problema se puede solucionar utilizando nombres de variables más descriptivos que reflejen claramente su función dentro del método. Por ejemplo, sustituir m por sourceAccount o fromAccount, y o por destinationAccount o toAccount. De esta forma, la intención del código se entiende de manera inmediata y se mejora la claridad y mantenibilidad del sistema.
 
### Bad Smell 3: Large Class (Clase Grande)
* **Ubicación:** `AccountService.java` - Toda la clase `AccountService` Líneas `1-326`
* **Reporte de la issue:**

  ![Large Class](img/LargeClass.png)
* **Explicación del bad smell:**
   * **Descripción:** La clase `AccountService` concentra demasiadas responsabilidades dentro de una única clase. Entre sus funciones se encuentran la creación de cuentas, la gestión de depósitos y transferencias, el registro de transacciones, la lógica de generación de números de cuenta y la gestión de notificaciones a los usuarios.

   * **Problema:** Este diseño viola el **Principio de Responsabilidad Única (Single Responsibility Principle, SRP)**, que establece que una clase debería tener una única razón para cambiar. Al encargarse de múltiples tareas diferentes, la clase se vuelve más difícil de entender, mantener y probar. Además, cualquier modificación en una de estas funcionalidades puede afectar a otras partes del sistema de forma inesperada.

   * **Cómo solucionarlo:**  
   Este problema se puede solucionar dividiendo la clase en varios servicios más especializados, cada uno encargado de una responsabilidad concreta. Por ejemplo:
   - Un `AccountService` encargado únicamente de la gestión de cuentas.
   - Un `TransactionService` para manejar depósitos, retiradas y transferencias.
   - Un `NotificationService` para gestionar el envío de notificaciones (email o SMS).
   
   De esta forma se mejora la modularidad del sistema, se facilita el mantenimiento del código y se respeta el principio de responsabilidad única.
     
### Bad Smell 4: Método demasiado largo (transfer)
* **Ubicación:** `AccountService.java` - Método `transfer(String, String, double)`
* **Reporte de la issue:**

    ![Codigo Duplicado 1](img/long-method.png)


* **Explicación del bad smell:**
  * Descripción: El método realiza múltiples tareas: valida cantidades, busca cuentas, verifica saldo, realiza dos operaciones financieras, crea dos registros de transacciones y envía dos notificaciones distintas.
  
  * Problema: Un método que hace demasiado obliga al lector a mantener demasiado contexto en memoria y dificulta las pruebas unitarias aisladas de cada paso.

  * Como solucionarlo: El problema se podría solucionar fragmentando la lógica de este método en métodos más pequeños que se encarguen de realizar una única tarea cada uno, en vez de tener todo el código concentrado en un único método.

### Bad Smell 5: Data Clumps (Racimos de Datos)
* **Ubicación:** `AccountService.java` - Métodos `deposit`, `withdraw` y `transfer`.
* **Reporte de la issue:**

    ![Data Clumps](img/data-clumps.png)

* **Explicación del bad smell:**
    * **Descripción:** En varios métodos se pasan constantemente los mismos conjuntos de parámetros juntos. Por ejemplo, siempre que se envía una notificación, se pasan `User user`, `NotificationType`, `String subject` y `String message`.
    
    * **Problema:** Existe un grupo de datos que "siempre van juntos" pero no han sido encapsulados en un objeto propio. Esto aumenta la complejidad de las firmas de los métodos y dificulta la reutilización de la lógica de notificación. Es un indicador de que falta una abstracción (como una clase `Notification Request`).

    * **Cómo solucionarlo:** El problema se puede solucionar creando un objeto de datos (Data Object) o un *Record* que agrupe estos campos. De esta manera, en lugar de pasar cuatro parámetros independientes, los métodos recibirían un único objeto de configuración de notificación.

### Bad Smell 6: Magic Numbers (Números Mágicos)
* **Ubicación:** `AccountService.java` - Métodos `deposit` (Líneas 77-89) y `withdraw` (Líneas 176-182).
* **Reporte de la issue:**

    ![Magic Numbers](img/magic-numbers1.png)
    ![Magic Numbers](img/magic-numbers2.png)

* **Explicación del bad smell:**
    * **Descripción:** Se utilizan valores numéricos literales como `10000`, `50000`, `5000` o `20000` directamente en la lógica de control sin ninguna explicación sobre su origen o significado.

    * **Problema:** Un desarrollador externo no puede saber si estos límites responden a una lógica de negocio, a una restricción técnica o a una normativa legal. Al estar "hard-coded", si el banco decide cambiar estos límites, hay que buscarlos y reemplazarlos manualmente en todo el código, lo que facilita la aparición de errores.

    * **Cómo solucionarlo:** La solución consiste en extraer estos valores a constantes con nombres descriptivos (por ejemplo, `MAX_DEPOSIT_THRESHOLD` o `DAILY_WITHDRAWAL_LIMIT`). Esto centraliza la configuración en un solo lugar y hace que el código sea autodocumentado.


### Bad Smell 7: Switch Statement (Sentencias Condicionales Complejas)
* **Ubicación:** `AccountService.java` - Dentro de los métodos `deposit`, `withdraw` y `transfer` - Líneas 103, 110, 152, 159, 202, 208, 267 y 273

* **Reporte de la issue:**
  
<img width="600" height="807" alt="image" src="https://github.com/user-attachments/assets/4b360d4f-529d-4901-ab9f-5d958ecf35c2">
<img width="600" height="836" alt="image" src="https://github.com/user-attachments/assets/79de44b7-513e-41ee-b823-0f6af2430458">
<img width="600" height="715" alt="image" src="https://github.com/user-attachments/assets/4b55ab82-f548-46d5-9a30-f9011db17b59">
<img width="600" height="678" alt="image" src="https://github.com/user-attachments/assets/bd310429-d3eb-4758-b3b2-1d94e43d9dd6">

* **Explicación del mal olor:**

    * **Descripción:** Se repite constantemente la estructura if (notifType == User.NotificationType.EMAIL) { … } else if (notifType == User.NotificationType.SMS) { … }. Cada vez que se añada un nuevo canal de notificación (como notificaciones push) habrá que buscar y modificar todos estos bloques en toda la clase, aumentando el riesgo de introducir errores y dificultando el mantenimiento de la aplicación.
    * **Problema:** Es una violación del polimorfismo, el servicio asume la responsabilidad de cómo notificar basándose en el tipo de usuario.
    * **Cómo solucionarlo:** Eliminar la responsabilidad de decidir cómo enviar del AccountService y delegarla a los propios servicios de notificación mediante una interfaz común.
  
### Bad Smell 8: Primitive Obsession (Obsesión por los Primitivos) - NAROA
* **Ubicación:** `AccountService.java` - Uso general de `double amount` y `String accountNumber`.
* **Reporte de la issue:**

    ![Primitive Obsession - Validación en Deposit](img/Primitive Obsession-1.png)
    ![Primitive Obsession - Validación en Withdraw](img/Primitive Obsession-2.png)
    ![Primitive Obsession - Firma del método Transfer](img/Primitive Obsession-3.png)

* **Explicación del bad smell:**
    * **Descripción:** Se utilizan tipos primitivos (double, String) para representar conceptos de dominio complejos como montos de dinero y números de cuenta bancaria. Específicamente, el uso de double para transacciones financieras es un error de calidad crítico, ya que los tipos de punto flotante no garantizan la precisión decimal necesaria en banca, pudiendo generar errores de redondeo acumulativos (por ejemplo, en el cálculo de intereses o saldos). Asimismo, el número de cuenta se trata como una simple cadena de texto, perdiendo su identidad como entidad con reglas propias.
    * **Problema:** Se pierden oportunidades para encapsular lógica de validación y formateo. Como se observa en las tres capturas, la validación para asegurar que un monto sea positivo se repite manualmente en `deposit` y `withdraw`, y el formato de la cuenta no está garantizado en `transfer`. Esto deja la lógica de validación dispersa por todo el servicio, aumentando el riesgo de inconsistencias.
    * **Cómo solucionarlo:** Crear *Value Objects* como una clase o record `Money` y `AccountNumber`. De esta forma, la validación ocurre una sola vez en el constructor del objeto y el servicio recibe datos cuya integridad ya está garantizada por el tipo.

### Bad Smell 9: Feature Envy (Envidia de Funciones) - NAROA
* **Ubicación:** `AccountService.java` - Métodos `withdraw` y `transfer`.
* **Reporte de la issue:**

    ![Feature Envy - Lógica de saldo en Withdraw](img/Feature Envy-1.png)
    ![Feature Envy - Lógica de saldo en Transfer](img/Feature Envy-2.png)

* **Explicación del bad smell:**
    * **Descripción:** El servicio `AccountService` accede al saldo de la cuenta mediante `account.getBalance()` para realizar una validación lógica (`< amount`) y decidir si lanza una excepción de "Fondos insuficientes".
    * **Problema:** El servicio está asumiendo una responsabilidad que debería ser de la entidad `Account`. La lógica de determinar si una cuenta es capaz de afrontar un cargo según su estado interno es lógica de negocio pura. Al realizarla en el servicio, este demuestra "envidia" por el comportamiento que debería estar encapsulado en la clase de dominio, obligando al servicio a conocer innecesariamente las reglas de gestión de saldo.
    * **Cómo solucionarlo:** Aplicar el principio **"Tell, Don't Ask"** (Dile, no preguntes). Se debe mover la lógica de validación a la clase `Account`. El servicio simplemente debe invocar el método de retirada en la entidad, y que sea la propia cuenta la que valide su capacidad de pago y lance la excepción si no puede cumplir la operación.
