#!/bin/bash
# Restauration d'une sauvegarde

if [ -z "$1" ]; then
    echo "Usage: $0 <chemin_vers_backup.sql>"
    exit 1
fi

CONTAINER_NAME="fsc_tournament_db"
DB_NAME="${POSTGRES_DB:-fsc_tournament}"
DB_USER="${POSTGRES_USER:-fsc_user}"

cat "$1" | docker exec -i "$CONTAINER_NAME" psql -U "$DB_USER" -d "$DB_NAME"
echo "Restauration terminée depuis $1"
