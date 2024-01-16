#!/bin/bash

PROJECT_DIR="${PWD}"                           # update if script is not located in root folder of project
TEST_RESOURCES="/src/test/resources/webDriver" # path to WebDriver folder
AP="${PROJECT_DIR}${TEST_RESOURCES}"           # complete path to the projects webDriver folder in src/test/resources
DOWNLOAD_EXTENSION="zip"
poll=$( bc <<< 'scale=1; 1/5' )               # sleep delay used to controlling iterations in while loops
timeOut=30                                    # in seconds
to=0                                          # $to * poll = timeOut value created in timeOut() leave set to 0

# URL for getting the LTS version number.
# The edge and chrome links download a simple file containing only the LTS version id
# The firefox link will download a json file which contains the LTS urls for each OS
EDGE_VERSION_URL="https://msedgedriver.azureedge.net/LATEST_STABLE"
CHROME_VERSION_URL="https://googlechromelabs.github.io/chrome-for-testing/LATEST_RELEASE_STABLE"
FIREFOX_VERSION_URL="https://api.github.com/repos/mozilla/geckodriver/releases/latest"

# These URLS contain special symbols used for sed substitution to generate the correct URL
#EDGE_BASE="https://msedgedriver.azureedge.net/<^${DOWNLOAD_EXTENSION}"
EDGE_URL="https://msedgedriver.azureedge.net/"
#CHROME_BASE="https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/<>^${DOWNLOAD_EXTENSION}"
CHROME_URL="https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/"

exitTO() {
  printf "%s second timeout occurred during %s" "${timeOut}" "${1}"
  exit 1
}

timeOut() {
  if [[ $to == 0 ]]; then to=$( printf "%.0f\n" """$( bc <<< "scale=1; $timeOut / $poll" )""" ); fi
}

getOS() {
  timeOut
  case "$OSTYPE" in
  solaris*) echo "$OSTYPE not supported" ;;
  darwin*) OS='mac' ;;
  linux*) OS='linux64' ;;
  bsd*) echo "$OSTYPE not supported" ;;
  msys*) OS='win64' FILE_EXTENSION=".exe" ;;
  *) echo "unknown $OSTYPE" ;;
  esac
}

downloadVersion() {
  curl -L -k --output "$ls" "${1}" --ssl-no-revoke
  while [[ ! -f "${ls}" ]]; do
    sleep "$poll"
    if [[ $to -gt 0 ]]; then to=$( bc <<< "scale=1; $to - 1" ); else exitTO "${2}"; fi
  done
}

downloadSetup() {
  ls=LATEST_STABLE
  errorMsg="downloadSetup function while attempting to download ${1}"
  case $1 in
  edge)
    VERSION_URL="${EDGE_VERSION_URL}"
    downloadVersion ${VERSION_URL} "${errorMsg}"
    # ms is annoying with the encodings.....
    v=$(iconv -f CP1252 -t UTF8 "$ls" | sed 's/[^0-9.]//g')
    rm "$ls"
    if [[ $OS == mac ]]; then OSD="${OS}64_m1"; else OSD=$OS; fi
    URL="$EDGE_URL${v}/edgedriver_${OSD}.zip"
    WEBDRIVER="msedgedriver"
    ;;
  chrome)
    VERSION_URL="${CHROME_VERSION_URL}"
    downloadVersion "${VERSION_URL}" "${errorMsg}"
    v=$(awk '{print $1}' "$ls")
    rm "$ls"
    if [[ $OS == mac ]]; then OSD="${OS}-arm64"; else OSD=$OS; fi
    URL="$CHROME_URL${v}/${OSD}/chromedriver-${OSD}.zip"
    WEBDRIVER="chromedriver"
    ;;
  firefox)
    VERSION_URL="${FIREFOX_VERSION_URL}"
    downloadVersion "${VERSION_URL}" "${errorMsg}"
#    && mv "file" "file.json"
    if [[ $OS == mac ]]; then
      OSD="${OS}os-aarch64"
    else OSD=$OS; fi
    if [[ $OS == linux64 || $OS == mac ]]; then DOWNLOAD_EXTENSION="tar.gz"; fi
    while [[ ! -f "${ls}.json" ]]; do
      sleep "$poll"
      if [ $to -gt 0 ]; then to=$( bc <<< "scale=1; $to - 1" ); else exitTO "${errorMsg}"; fi
    done
    URL=$(grep "${OSD}.${DOWNLOAD_EXTENSION}\"$" "${ls}.json" | awk '{print $2}' | sed 's:^.\(.*\).$:\1:')
    rm "${ls}.json"
    WEBDRIVER="geckodriver"
    ;;
  *)
    echo invalid
    ;;
  esac
  printf "URL used to get LTS version number: %s\nURL used to download ${WEBDRIVER}: %s" "${VERSION_URL}" "${URL}"

}

downloadWebDriver() {
  if [[ $WEBDRIVER == chromedriver ]]; then
  UNZPPDFILE="${WEBDRIVER}-${OSD}/${WEBDRIVER}${FILE_EXTENSION}"
  else UNZPPDFILE=${WEBDRIVER}${FILE_EXTENSION}; fi
  FILE="driver.${DOWNLOAD_EXTENSION}"
  echo "$URL"
  cd /etc/.. && cd "${AP}" &&
    mkdir -p "${AP}/temp" &&
    cd "temp" &&
    curl -L -k --output "$FILE" "$URL" --ssl-no-revoke
  while [ ! -f "${FILE}" ]; do
    sleep "$poll"
    echo "t"
    if [ $to -gt 0 ]; then to=$( bc <<< "scale=1; $to - 1" ); else exitTO "downloadWebDriver function while waiting for ${WEBDRIVER}.${DOWNLOAD_EXTENSION} to download"; fi
  done
  if [[ $DOWNLOAD_EXTENSION == *zip ]]; then unzip "$FILE"; else tar xzf "$FILE"; fi
}

function moveFile() {
  echo $WEBDRIVER
  while [ ! -f "${UNZPPDFILE}" ]; do
    sleep "$poll"
    if [ $to -gt 0 ]; then to=$( bc <<< "scale=1; $to - 1" ); else exitTO "moveFile function while waiting for ${FILE} to unzip"; fi
  done
  cd /etc/.. && cd "${AP}"
  ls
  mkdir -p "${AP}/${OS}" && cp "temp/${UNZPPDFILE}" "$_" &&
    chmod "u=rwx,go=" "${AP}/${OS}/${WEBDRIVER}${FILE_EXTENSION}" &&
    rm -rf "temp" &&
    cd "$PROJECT_DIR" || exit
}

start() {
  BROWSER=$1
  getOS
  downloadSetup "$BROWSER"

  # checks to see if driver exists in project path or exists as defined in pom.xml property
  if [[ (! -f ${AP}/${OS}/${WEBDRIVER}${FILE_EXTENSION}) && (! -f "${2}") ]]; then
    downloadWebDriver
    moveFile
  fi

  while [ ! -f "${AP}/${OS}/${WEBDRIVER}${FILE_EXTENSION}" ]; do
    sleep "$poll"
    if [ $to -gt 0 ]; then to=$( bc <<< "scale=1; $to - 1" ); else exitTO "start function when checking ${WEBDRIVER} in correct filepath"; fi
  done
  echo "$WEBDRIVER downloaded"
  echo "$WEBDRIVER filepath: ${AP}/${OS}/${WEBDRIVER}${FILE_EXTENSION}"
}
