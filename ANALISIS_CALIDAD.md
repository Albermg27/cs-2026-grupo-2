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

### Bad Smell X: [Nombre de la issue, p.e. Duplicación de código en X método]
* **Ubicación:** `AccountService.java` - Línea [Indicar línea/s]
* **Reporte de la issue:**
    > **Nota:** Si se detectó con SonarCloud, adjuntar captura de la issue en la plataforma. Si fue manual, adjuntar captura del código resaltado.
    ![Captura Issue 1](ruta/a/tu/imagen_issue1.png)

* **Explicación del bad smell:**
    [Escribir aquí la explicación basada en los conceptos de la asignatura] Descripción y problema

    * Si fue detectado por SonarCloud, **¿Problema real o Falso positivo?**

### Bad Smell 1: [Nombre de la issue, p.e. Duplicación de código en X método]
* **Ubicación:** `AccountService.java` - Métodos `deposit(String, double, String) y deposit(String, double)`
* **Reporte de la issue:**
     <img width="3312" height="471" alt="image" src="https://github.com/user-attachments/assets/c2bd40f6-a42b-4b35-a050-afc8fed03d77" />
     <img width="2888" height="540" alt="image" src="https://github.com/user-attachments/assets/6c62166b-64a7-49cb-958d-8bb09a1dc7aa" />


* **Explicación del bad smell:**
   * Descripción: Ambos métodos repiten exactamente la misma lógica de validación (cuatro bloques if para la  cantidad) y la misma lógica para registrar el depósito y enviar notificaciones.
   * Problema: Si las reglas de negocio para los depósitos cambian (por ejemplo, el límite máximo), hay que modificarlo en varios lugares, lo que aumenta el riesgo de inconsistencias y errores.



### Bad Semll 2: [Mysterious Names / Non-Descriptive Names (Nombres poco claros)]
* **Ubicación:** `AccountService.java` - Método `transfer(String fromAccountNumber, String toAccountNumber, double amount)` Líneas `231-232`
* **Reporte de la issue:**
  
* **Explicación del bad smell:**
    * Descripción: En el método transfer se utilizan variables con nombres muy cortos y poco descriptivos (m y o). Estos nombres no reflejan claramente qué representan dentro de la lógica del método.

   * Problema: El uso de nombres crípticos reduce la legibilidad del código y dificulta su comprensión, especialmente para otros desarrolladores o incluso para el propio autor cuando vuelva a revisar el código en el futuro. Esto obliga a analizar el contexto del método para entender qué representan las variables, lo que aumenta el tiempo necesario para mantener o modificar el código.

   * Cómo solucionarlo: Este problema se puede solucionar utilizando nombres de variables más descriptivos que reflejen claramente su función dentro del método. Por ejemplo, sustituir m por sourceAccount o fromAccount, y o por destinationAccount o toAccount. De esta forma, la intención del código se entiende de manera inmediata y se mejora la claridad y mantenibilidad del sistema.
 
### Bad Smell 3: [Large Class (Clase Grande)]
* **Ubicación:** `AccountService.java` - Toda la clase `AccountService` Líneas `1-326`
* **Reporte de la issue:**  

* **Explicación del bad smell:**
   * **Descripción:** La clase `AccountService` concentra demasiadas responsabilidades dentro de una única clase. Entre sus funciones se encuentran la creación de cuentas, la gestión de depósitos y transferencias, el registro de transacciones, la lógica de generación de números de cuenta y la gestión de notificaciones a los usuarios.

   * **Problema:** Este diseño viola el **Principio de Responsabilidad Única (Single Responsibility Principle, SRP)**, que establece que una clase debería tener una única razón para cambiar. Al encargarse de múltiples tareas diferentes, la clase se vuelve más difícil de entender, mantener y probar. Además, cualquier modificación en una de estas funcionalidades puede afectar a otras partes del sistema de forma inesperada.

   * **Cómo solucionarlo:**  
   Este problema se puede solucionar dividiendo la clase en varios servicios más especializados, cada uno encargado de una responsabilidad concreta. Por ejemplo:
   - Un `AccountService` encargado únicamente de la gestión de cuentas.
   - Un `TransactionService` para manejar depósitos, retiradas y transferencias.
   - Un `NotificationService` para gestionar el envío de notificaciones (email o SMS).
   
   De esta forma se mejora la modularidad del sistema, se facilita el mantenimiento del código y se respeta el principio de responsabilidad única.
     
---
