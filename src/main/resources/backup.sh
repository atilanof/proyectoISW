#!/bin/bash

# Nombre del contenedor
CONTAINER_NAME=postgres

# Credenciales de la base de datos
DB_USER="postgres"
DB_NAME="isw" #Esto se deberá modificar para cada grupo de clase
DB_PASSWORD="postgres"

# Directorio donde se guardará el backup
BACKUP_DIR=/tmp

# Nombre del archivo de respaldo
BACKUP_FILE=$BACKUP_DIR/isw_backup_$(date +%Y%m%d%H%M%S).sql

# Realizar el backup
docker exec -e PGPASSWORD=$DB_PASSWORD $CONTAINER_NAME pg_dump -U $DB_USER $DB_NAME > $BACKUP_FILE

# Verifica si el comando de backup tuvo éxito
if [ $? -eq 0 ]; then
  echo "Backup realizado con éxito: $BACKUP_FILE"
else
  echo "Error al realizar el backup"
  exit 1
fi

