#!/usr/bin/env bash
# Minimal (offline) Gradle wrapper script.
# Descarga la distribución de Gradle si no existe y la ejecuta.
# Útil cuando no puedes generar el wrapper oficial.

set -euo pipefail

GRADLE_VERSION="8.5"
DIST_URL="https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip"
# Usamos un directorio local al proyecto para no depender de permisos globales.
BASE_DIR="$(cd -- "$(dirname "$0")" && pwd)"
WRAPPER_DIR="${BASE_DIR}/.gradle/wrapper/dists"
DIST_DIR="${WRAPPER_DIR}/gradle-${GRADLE_VERSION}-bin"
ZIP_PATH="${DIST_DIR}/gradle-${GRADLE_VERSION}-bin.zip"
UNZIPPED_BIN="${DIST_DIR}/gradle-${GRADLE_VERSION}/bin/gradle"

download_gradle() {
  mkdir -p "${DIST_DIR}"
  echo "Descargando Gradle ${GRADLE_VERSION} desde ${DIST_URL}..."
  if command -v curl >/dev/null 2>&1; then
    curl -fL "${DIST_URL}" -o "${ZIP_PATH}"
  elif command -v wget >/dev/null 2>&1; then
    wget -O "${ZIP_PATH}" "${DIST_URL}"
  else
    echo "Error: se necesita curl o wget para descargar Gradle." >&2
    exit 1
  fi
}

unzip_gradle() {
  echo "Descomprimiendo Gradle en ${DIST_DIR}..."
  unzip -q "${ZIP_PATH}" -d "${DIST_DIR}"
}

# Paso 1: asegurarnos de tener el zip.
if [ ! -f "${ZIP_PATH}" ]; then
  download_gradle
fi

# Paso 2: descomprimir si no está el binario.
if [ ! -x "${UNZIPPED_BIN}" ]; then
  unzip_gradle
fi

# Paso 3: ejecutar gradle con los argumentos que lleguen.
exec "${UNZIPPED_BIN}" "$@"
