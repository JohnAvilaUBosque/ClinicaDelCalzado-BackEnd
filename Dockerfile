# Usar una imagen base con Java 17
FROM openjdk:17-slim

# Directorio donde se colocará la aplicación en el contenedor
WORKDIR /app

# Copiar el archivo jar del proyecto al directorio /app en el contenedor
COPY build/libs/ClinicaDelCalzado-BackEnd-0.0.7-SNAPSHOT.jar /app/ClinicaDelCalzado-BackEnd.jar

# Exponer el puerto que usa la aplicación
EXPOSE 3000

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "/app/ClinicaDelCalzado-BackEnd.jar"]