#!/bin/bash
# Sauvegarde de la base de données locale Docker

BACKUP_DIR="./backups"
DATE=$(date +%Y%m%d_%H%M%S)
CONTAINER_NAME="fsc_tournament_db"
DB_NAME="${POSTGRES_DB:-fsc_tournament}"
DB_USER="${POSTGRES_USER:-fsc_user}"

mkdir -p "$BACKUP_DIR"

docker exec "$CONTAINER_NAME" pg_dump -U "$DB_USER" -d "$DB_NAME" \
    > "$BACKUP_DIR/backup_${DATE}.sql"

echo "Sauvegarde créée : $BACKUP_DIR/backup_${DATE}.sql"
