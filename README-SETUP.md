# Banco - Instrucciones rápidas de setup (Windows)

Este archivo te guía para arrancar el backend (Spring Boot) y solucionar el problema de `mvn` no encontrado o bloqueo de la base H2.

Requisitos mínimos
- Java 21 instalado (parece que ya lo tienes: `C:\Program Files\Eclipse Adoptium\jdk-21...`).
- Maven en PATH o usar el Maven Wrapper (`mvnw`).

Pasos recomendados

1) Verifica si `mvn` está instalado:

```powershell
mvn -v
```

Si obtienes "El término 'mvn' no se reconoce...", instala Maven o usa la opción con wrapper.

2) Instalar Maven en Windows (opciones):
- Con Chocolatey (si lo tienes):

```powershell
choco install maven -y
```

- Manual: descarga Apache Maven desde https://maven.apache.org/download.cgi, descomprime y añade la carpeta `bin` a la variable de entorno PATH.

3) Cerrar procesos Java que puedan bloquear la DB H2 (si aparece "file is locked"):

```powershell
# lista procesos java
Get-Process java -ErrorAction SilentlyContinue | Format-Table Id,ProcessName,Path -AutoSize

# detén por Id (reemplaza 13704 por el PID que veas)
Stop-Process -Id 13704 -Force
```

4) Ejecutar el backend:

Si tienes `mvn` instalado:

```powershell
cd 'C:\Users\user\Documents\JavaProyectos\bank'
# este script también realiza la verificación
.\start-backend.ps1
```

Si prefieres que no instales Maven globalmente, puedo añadir el Maven Wrapper (`mvnw`) al repo (pídemelo). Con `mvnw` no hace falta instalar Maven globalmente.

5) Ejecutar el frontend (desde otra terminal):

```powershell
cd 'C:\Users\user\Documents\frontend\fidexa-web'
.\start-frontend.ps1
```

Si algo falla, copia aquí las líneas de error del terminal (las últimas 50–100 líneas) y lo reviso.

Notas:
- Ya configuré H2 con `AUTO_SERVER=TRUE` en `application.properties` para reducir los problemas de bloqueo, pero si la DB quedó abierta por otro proceso, debes cerrarlo.
- Si quieres que automáticamente genere y añada el `mvnw` (Maven Wrapper) al repositorio para evitar instalar Maven, dime y lo añado.

---
Archivo creado automáticamente para ayudarte a arrancar el proyecto.
