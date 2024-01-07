#!/bin/bash

PROJECT_DIR="${PWD}"                           # update if script is not located in root folder of project
TEST_RESOURCES="/src/test/resources/webDriver" # path to WebDriver folder
AP="${PROJECT_DIR}${TEST_RESOURCES}"           # complete path to the projects webDriver folder in src/test/resources
DOWNLOAD_EXTENSION=".zip"
FILE_EXTENSION=""
UNZPPDFILE=""

# URL for getting the LTS version number.
# The edge and chrome links download a simple file containing only the LTS version id
# The firefox link will download a json file which contains the LTS urls for each OS
EDGE_VERSION_URL="https://msedgedriver.azureedge.net/LATEST_STABLE"
CHROME_VERSION_URL="https://googlechromelabs.github.io/chrome-for-testing/LATEST_RELEASE_STABLE"
FIREFOX_VERSION_URL="https://api.github.com/repos/mozilla/geckodriver/releases/latest"

# These URLS contain special symbols used for sed substitution to generate the correct URL
EDGE_BASE="https://msedgedriver.azureedge.net/<^${DOWNLOAD_EXTENSION}"
CHROME_BASE="https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/<>^${DOWNLOAD_EXTENSION}"

exitTO() {
  ls=LATEST_STABLE
  cd /etc/.. && cd "${PROJECT_DIR}"
  if [[ -f $ls ]]; then rm "${ls}"; fi
  if [[ -f "${ls}.json" ]]; then rm "${ls}.json"; fi
  if [[ -d "${TEST_RESOURCES}temp" ]];  then rm "${TEST_RESOURCES}temp"; fi
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

getVersion() {
  ls=LATEST_STABLE
  to=125
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
    WEBDRIVER="edgedriver_"
#    base="${EDGE_BASE}"
    #    URL="https://msedgedriver.azureedge.net/${v}/edgedriver_${OSD}.zip"
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
    WEBDRIVER="chromedriver-"
    #    URL="https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/${v}/${OSD}/chromedriver-${OSD}.zip"
    ;;
  firefox)
    curl -L -k --output "${ls}.json" "$FIREFOX_VERSION_URL" --ssl-no-revoke
    if [[ $OS == mac ]]; then
      OSD="${OS}os-aarch64"
    else OSD=$OS; fi
    if [[ $OS == linux64 || $OS == mac ]]; then DOWNLOAD_EXTENSION=".tar.gz"; fi
    while [[ ! -f "${ls}.json" ]]; do
      sleep "$poll"
      if [ $to -gt 0 ]; then to=$((to - 1)); else exitTO "${LINENO}"; fi
    done
    URL=$(grep "${OSD}${DOWNLOAD_EXTENSION}\"$" "${ls}.json" | awk '{print $2}' | sed 's:^.\(.*\).$:\1:')
    rm "${ls}.json"
    WEBDRIVER="geckodriver"
    ;;
  *)
    echo invalid
    ;;
  esac

}

chromeEdgeExtraSetup() {
  WDOS=${WEBDRIVER}${OSD}
  UNZPPDFILE=${WEBDRIVER}${FILE_EXTENSION}
  if [[ $WEBDRIVER == chromedriver- ]]; then
    base="${CHROME_BASE}"
    UNZPPDFILE="${WEBDRIVER}${OSD}/${UNZPPDFILE}"
    WEBDRIVER=chromedriver
  elif [ $WEBDRIVER == edgedriver_ ]; then
    base="${EDGE_BASE}"
    WEBDRIVER=msedgedriver
    UNZPPDFILE=${WEBDRIVER}${FILE_EXTENSION}
  fi
  URL=$(echo $base | sed "s/</${v}\//" | sed "s/\^/${WDOS}/" | sed "s/>/${OSD}\//")
}

downloadWebDriver() {
  UNZPPDFILE=${WEBDRIVER}${FILE_EXTENSION}
  FILE="driver${DOWNLOAD_EXTENSION}"

  cd /etc/.. && cd "${AP}" &&
    mkdir -p "${AP}/temp" &&
    cd "temp" &&
    curl -L -k --output "$FILE" "$URL" --ssl-no-revoke
  if [[ $DOWNLOAD_EXTENSION == *.zip ]]; then unzip "$FILE"; else tar xzf "$FILE"; fi
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
  getVersion "$BROWSER"

  # checks to see if driver exists in project path or exists as defined in pom.xml property
  if [[ (! -f ${AP}/${OS}/${WEBDRIVER}${FILE_EXTENSION}) && (! -f "${2}") ]]; then
    if [[ $BROWSER != firefox ]]; then chromeEdgeExtraSetup; fi
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
