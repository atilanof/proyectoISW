#!/bin/bash

# Nombre del contenedor
CONTAINER_NAME=postgres

# Credenciales de la base de datos
DB_USER="postgres"
DB_NAME="isw"
DB_PASSWORD="postgres"

# Archivo de backup a restaurar
BACKUP_FILE=$1

# Comandos para dropear y crear la base de datos
DROP_CREATE_DB_COMMANDS="DROP DATABASE IF EXISTS $DB_NAME; CREATE DATABASE $DB_NAME;"

# Ejecutar los comandos de dropeo y creación de la base de datos
echo $DROP_CREATE_DB_COMMANDS | docker exec -i -e PGPASSWORD=$DB_PASSWORD $CONTAINER_NAME psql -U $DB_USER

# Verifica si el comando de restore tuvo éxito
if [ $? -eq 0 ]; then
  echo "Restauración realizada con éxito desde: $BACKUP_FILE"
else
  echo "Error al realizar la restauración"
  exit 1
fi

