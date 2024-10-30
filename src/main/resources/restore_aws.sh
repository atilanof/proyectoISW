#!/bin/bash

# Verificar si los parámetros necesarios se han pasado como argumentos
if [ $# -lt 2 ]; then
  echo "Uso: $0 <archivo_de_backup> <ip_o_dns_de_postgresql_aws>"
  exit 1
fi

# Credenciales de la base de datos (modificar según sea necesario)
DB_USER="postgres"
DB_NAME="isw" # Esto se deberá modificar para cada grupo
DB_PASSWORD="postgres"

# Archivo de backup a restaurar y dirección del servidor PostgreSQL en AWS
BACKUP_FILE=$1
PG_HOST=$2

# Verificar si el archivo de backup existe
if [ ! -f "$BACKUP_FILE" ]; then
  echo "El archivo de backup no existe: $BACKUP_FILE"
  exit 1
fi

# Comandos para dropear y crear la base de datos
DROP_CREATE_DB_COMMANDS="DROP DATABASE IF EXISTS $DB_NAME; CREATE DATABASE $DB_NAME;"

# Ejecutar los comandos de dropeo y creación de la base de datos
PGPASSWORD=$DB_PASSWORD psql -h $PG_HOST -U $DB_USER -d postgres -c "$DROP_CREATE_DB_COMMANDS"

# Verificar si los comandos de dropeo y creación tuvieron éxito
if [ $? -ne 0 ]; then
  echo "Error al dropear o crear la base de datos"
  exit 1
fi

# Restaurar la base de datos desde el archivo de backup
PGPASSWORD=$DB_PASSWORD psql -h $PG_HOST -U $DB_USER -d $DB_NAME < $BACKUP_FILE

# Verificar si el comando de restore tuvo éxito
if [ $? -eq 0 ]; then
  echo "Restauración realizada con éxito desde: $BACKUP_FILE"
else
  echo "Error al realizar la restauración"
  exit 1
fi
