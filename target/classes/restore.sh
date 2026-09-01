#!/bin/bash

# Nombre del contenedor
CONTAINER_NAME=postgres

# Credenciales de la base de datos
DB_USER="postgres"
DB_NAME="isw" #Esto se deberá modificar para cada grupo
DB_PASSWORD="postgres"

# Archivo de backup a restaurar
BACKUP_FILE=$1

# Verificar si el archivo de backup se ha pasado como argumento
if [ -z "$BACKUP_FILE" ]; then
  echo "Por favor, proporciona el archivo de backup a restaurar."
  exit 1
fi

# Comandos para dropear y crear la base de datos
DROP_CREATE_DB_COMMANDS="DROP DATABASE IF EXISTS $DB_NAME; CREATE DATABASE $DB_NAME;"

# Ejecutar los comandos de dropeo y creación de la base de datos
echo $DROP_CREATE_DB_COMMANDS | docker exec -i -e PGPASSWORD=$DB_PASSWORD $CONTAINER_NAME psql -U $DB_USER postgres

# Verificar si los comandos de dropeo y creación tuvieron éxito
if [ $? -ne 0 ]; then
  echo "Error al dropear o crear la base de datos"
  exit 1
fi

# Restaurar la base de datos desde el archivo de backup
docker exec -i -e PGPASSWORD=$DB_PASSWORD $CONTAINER_NAME psql -U $DB_USER $DB_NAME < $BACKUP_FILE

# Verificar si el comando de restore tuvo éxito
if [ $? -eq 0 ]; then
  echo "Restauración realizada con éxito desde: $BACKUP_FILE"
else
  echo "Error al realizar la restauración"
  exit 1
fi
