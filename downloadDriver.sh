#!/bin/bash

EDGE_VERSION_URL="https://msedgedriver.azureedge.net/LATEST_STABLE"
CHROME_VERSION_URL="https://googlechromelabs.github.io/chrome-for-testing/LATEST_RELEASE_STABLE"
FIREFOX_VERSION_URL="https://api.github.com/repos/mozilla/geckodriver/releases/latest"

PROJECT_DIR="${PWD}"                           # update if script is not located in root folder of project
TEST_RESOURCES="/src/test/resources/webDriver" # path to WebDriver folder
AP="${PROJECT_DIR}${TEST_RESOURCES}"

DOWNLOAD_EXTENSION=".zip"
FILE_EXTENSION=""

exitTO() {
  printf "20 second timeout occurred on line: %d in function: %s" "${1}" "${funcstack[2]}"
  exit 1
}

getOS() {
  case "$OSTYPE" in
  solaris*) echo "$OSTYPE not supported" ;;
  darwin*) OS='mac' ;;
  linux*) OS='linux64' ;;
  bsd*) echo "$OSTYPE not supported" ;;
  msys*) OS='win64' FILE_EXTENSION=".exe" ;;
  *) echo "unknown $OSTYPE" ;;
  esac
}

download() {
  ls=LATEST_STABLE
  to=100
  poll=0.2
  case $1 in
  edge)
    curl -L -k --output "$ls" $EDGE_VERSION_URL --ssl-no-revoke
    echo "$EDGE_VERSION_URL"
    sleep 1
    while [[ ! -f "${ls}" ]]; do
      sleep "$poll"
      if [ $to -gt 0 ]; then to=$((to - 1)); else exitTO "${LINENO}"; fi
    done
    # ms is annoying with the encodings.....
    v=$(iconv -f CP1252 -t UTF8 "$ls" | sed 's/[^0-9.]//g')
    rm "$ls"
    if [[ $OS == mac ]]; then OSD="${OS}64_m1"; else OSD=$OS; fi
    URL="https://msedgedriver.azureedge.net/${v}/edgedriver_${OSD}.zip"
    WEBDRIVER="msedgedriver"
    ;;
  chrome)
    curl -L -k --output "$ls" "$CHROME_VERSION_URL" --ssl-no-revoke
    while [[ ! -f "${ls}" ]]; do
      sleep "$poll"
      if [ $to -gt 0 ]; then to=$((to - 1)); else exitTO "${LINENO}"; fi
    done
    v=$(awk '{print $1}' "$ls")
    rm "$ls"
    if [[ $OS == mac ]]; then OSD="${OS}-arm64"; else OSD=$OS; fi
    URL="https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/${v}/${OSD}/chromedriver-${OSD}.zip"
    WEBDRIVER="chromedriver"
    ;;
  firefox)
    curl -L -k --output "${ls}.json" "$FIREFOX_VERSION_URL" --ssl-no-revoke
    if [[ $OS == mac ]]; then
      OSD="${OS}os-aarch64"
    else OSD=$OS; fi
    if [[ $OS == linux64 || $OS == mac ]]; then DOWNLOAD_EXTENSION="tar.gz"; fi
    while [[ ! -f "${ls}.json" ]]; do
      sleep "$poll"
      if [ $to -gt 0 ]; then to=$((to - 1)); else exitTO "${LINENO}"; fi
    done
    URL=$(grep "${OSD}.${DOWNLOAD_EXTENSION}\"$" "${ls}.json" | awk '{print $2}' | sed 's:^.\(.*\).$:\1:')
    rm "${ls}.json"
    WEBDRIVER="geckodriver"
    ;;
  *)
    echo invalid
    ;;
  esac
}

downloadWebDriver() {
  if [[ $WEBDRIVER == chromedriver ]]; then
    UNZPPDFILE="${WEBDRIVER}-${OSD}/${WEBDRIVER}${FILE_EXTENSION}"
  else UNZPPDFILE=${WEBDRIVER}${FILE_EXTENSION}; fi
  FILE="driver.${DOWNLOAD_EXTENSION}"

  cd /etc/.. && cd "${AP}" &&
    mkdir -p "${AP}/temp" &&
    cd "temp" &&
    curl -L -k --output "$FILE" "$URL" --ssl-no-revoke
  if [[ $DOWNLOAD_EXTENSION == *zip ]]; then unzip "$FILE"; else tar xzf "$FILE"; fi
}

function moveFile() {
  mkdir -p "${AP}/${OS}" && cp "${UNZPPDFILE}" "$_" && cd "${AP}/${OS}"
  chmod "u=rwx,go=" "${AP}/${OS}/${WEBDRIVER}${FILE_EXTENSION}" &&
    cd /etc/.. && cd "${AP}" &&
    rm -rf "temp" &&
    cd "$PROJECT_DIR" || exit
}

start() {
  BROWSER=$1
  getOS
  download "$BROWSER"

  # checks to see if driver exists in project path or exists as defined in pom.xml property
  if [[ (! -f ${AP}/${OS}/${WEBDRIVER}${FILE_EXTENSION}) && (! -f "${2}") ]]; then
    downloadWebDriver
    moveFile
  fi

  echo "$WEBDRIVER downloaded"
  echo "$WEBDRIVER filepath: ${AP}/${OS}/${WEBDRIVER}${FILE_EXTENSION}"
  while [ ! -f "${AP}/${OS}/${WEBDRIVER}${FILE_EXTENSION}" ]; do
    sleep "$poll"
    if [ $to -gt 0 ]; then to=$((to - 1)); else exitTO "${LINENO}"; fi
  done
}
