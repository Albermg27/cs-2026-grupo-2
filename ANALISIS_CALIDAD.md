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

### Issue X: [Nombre de la issue, p.e. Duplicación de código en X método]
* **Ubicación:** `AccountService.java` - Línea [Indicar línea/s]
* **Reporte de la issue:**
    > **Nota:** Si se detectó con SonarCloud, adjuntar captura de la issue en la plataforma. Si fue manual, adjuntar captura del código resaltado.
    ![Captura Issue 1](ruta/a/tu/imagen_issue1.png)

* **Explicación del mal olor:**
    [Escribir aquí la explicación basada en los conceptos de la asignatura] Descripción y problema

    * Si fue detectado por SonarCloud, **¿Problema real o Falso positivo?**

### Issue 1: [Nombre de la issue, p.e. Duplicación de código en X método]
* **Ubicación:** `AccountService.java` - métodos `deposit(String, double, String) y deposit(String, double)`
* **Reporte de la issue:**
     <img width="3312" height="471" alt="image" src="https://github.com/user-attachments/assets/c2bd40f6-a42b-4b35-a050-afc8fed03d77" />
     <img width="2888" height="540" alt="image" src="https://github.com/user-attachments/assets/6c62166b-64a7-49cb-958d-8bb09a1dc7aa" />


* **Explicación del mal olor:**
   * Descripción: Ambos métodos repiten exactamente la misma lógica de validación (cuatro bloques if para la  cantidad) y la misma lógica para registrar el depósito y enviar notificaciones.
   * Problema: Si las reglas de negocio para los depósitos cambian (por ejemplo, el límite máximo), hay que modificarlo en varios lugares, lo que aumenta el riesgo de inconsistencias y errores.

---


## **Refactorización**

NO REALIZAR EN ESTA PRÁCTICA
