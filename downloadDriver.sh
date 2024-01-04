#!/bin/bash

# converting the commands used to aliases if the macos commands are different

# edge
# https://msedgedriver.azureedge.net/120.0.2210.91/edgedriver_linux64.zip
# https://msedgedriver.azureedge.net/120.0.2210.91/edgedriver_win64.zip
# https://msedgedriver.azureedge.net/120.0.2210.91/edgedriver_mac64_m1.zip

# chrome
# https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/120.0.6099.109/linux64/chromedriver-linux64.zip
# https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/120.0.6099.109/win64/chromedriver-win64.zip
# https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/120.0.6099.109/mac-arm64/chromedriver-mac-arm64.zip

# gecko (firefox)
# https://github.com/mozilla/geckodriver/releases/download/v0.34.0/geckodriver-v0.34.0-win32.zip
# https://github.com/mozilla/geckodriver/releases/download/v0.34.0/geckodriver-v0.34.0-macos-aarch64.tar.gz
# https://github.com/mozilla/geckodriver/releases/download/v0.34.0/geckodriver-v0.34.0-linux64.tar.gz


# assign alias here
function downloadWebDriver() {

    DOWNLOAD_EXTENSION="zip"
    PROJECT_DIR=${PWD}
    TEST_RESOURCES="src/test/resources"
    ABSPATH="${PROJECT_DIR}/${TEST_RESOURCES}/webDriver"
    FILE_EXTENSION=""

    case "$OSTYPE" in
    solaris*) echo "$OSTYPE not supported" ;;
    darwin*) OS='mac' ;;
    linux*) OS='linux64' ;;
    bsd*) echo "$OSTYPE not supported" ;;
    msys*) OS='win64' FILE_EXTENSION=".exe" ;;
    *) echo "unknown $OSTYPE" ;;
    esac

    case $1 in
      edge)
        echo edge
        if [ $OS == 'mac' ]; then OSD="${OS}64_m1"; else OSD=$OS; fi
        URL="https://msedgedriver.azureedge.net/120.0.2210.91/edgedriver_${OSD}.zip"
        WEBDRIVER="msedgedriver" ;;
      chrome)
        echo chrome
        if [ $OS == 'mac' ]; then OSD="${OS}-arm64"; else OSD=$OS; fi
        URL="https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/120.0.6099.109/mac-arm64/chromedriver-${OSD}.zip"
        WEBDRIVER="chromedriver" ;;
      firefox)
        echo firefox
        if [ $OS == 'mac' ]; then OSD="${OS}os-aarch64"; DOWNLOAD_EXTENSION="tar.gz";
        elif [ $OS == 'win64' ]; then OSD="win32"; DOWNLOAD_EXTENSION="tar.gz";
        else OSD=$OS; fi
        URL="https://github.com/mozilla/geckodriver/releases/download/v0.34.0/geckodriver-v0.34.0-${OSD}.${DOWNLOAD_EXTENSION}"
        WEBDRIVER="geckodriver" ;;
      *)
        echo invalid ;;
    esac

    if [ $WEBDRIVER == 'chromedriver' ];
    then UNZPPDFILE="${WEBDRIVER}-${OSD}/${WEBDRIVER}${FILE_EXTENSION}";
    else UNZPPDFILE=${WEBDRIVER}${FILE_EXTENSION}; fi

    FILE="driver.${DOWNLOAD_EXTENSION}"

# checks to see if driver exists in project path or exists as defined in pom.xml property
    if [[ (! -f ${ABSPATH}/${OS}/${WEBDRIVER}${FILE_EXTENSION}) && (! -f "${2}") ]]; then
        cd /etc/.. && cd "${ABSPATH}" &&
        mkdir "temp" &&
        cd "temp" && echo $URL
        curl -L -k --output $FILE $URL --ssl-no-revoke
        if [[ $DOWNLOAD_EXTENSION == *zip ]]; then unzip $FILE; else tar xzf $FILE; fi;
        cp "${UNZPPDFILE}" "${ABSPATH}/${OS}" &&
        chmod "u=rwx,go=rx" "${ABSPATH}/${OS}/${WEBDRIVER}${FILE_EXTENSION}" &&
        cd /etc/.. &&
        cd "${ABSPATH}" &&
        rm -rf temp &&
        cd "$PROJECT_DIR" || exit
    fi

    echo $WEBDRIVER
    echo "${ABSPATH}/${OS}/${WEBDRIVER}${FILE_EXTENSION}"
    while [ ! -f "${ABSPATH}/${OS}/${WEBDRIVER}${FILE_EXTENSION}" ]; do sleep .2; done;
}

# downloadWebDriver edge


#}
#set -x
#const WebdDriverType
#
## shellcheck disable=SC2154
#const SCRIPT_DIR="${realpath}"
#const TEST_RESOURCES="/common/src/test/resources"
#export ABSPATH="${SCRIPT_DIR}""${TEST_RESOURCES}"
#FILE_EXTENSION=""
#
#case "$OSTYPE" in
#solaris*) echo "$OSTYPE not supported" ;;
#darwin*) OS='mac' ;;
#linux*) OS='linux64' ;;
#bsd*) echo "$OSTYPE not supported" ;;
#msys*) OS='win32' FILE_EXTENSION=".exe" ;;
#*) echo "unknown $OSTYPE" ;;
#esac
#
#if [ ! -f "$ABSPATH/drivers/$OSTYPE/msedgedriver"$FILE_EXTENSION ]; then
#  cd "$TEST_RESOURCES/drivers" &&
#    mkdir "temp" &&
#    cd "temp" &&
#    curl -L -k --output driver.zip https://msedgedriver.azureedge.net/120.0.2210.91/edgedriver_mac64_m1.zip --ssl-no-revoke &&
#    unzip driver.zip
#    sleep 5
#    cp "msedgedriver$FILE_EXTENSION" "$TEST_RESOURCES/drivers/$OSTYPE"
#    chmod +700 "$TEST_RESOURCES/$OSTYPE/msedgedriver"$FILE_EXTENSION
#    cd "$ABSPATH/drivers"
#    rm -rf temp &&
#    cd "$ROOT_DIR" || exit
#fi
#
#if [ ! -f "$TEST_RESOURCES/drivers/geckodriver"$FILE_EXTENSION ]; then
#  cd "$TEST_RESOURCES/drivers" &&
#    mkdir "temp" &&
#    cd "temp" &&
#    curl -L -k --output driver.zip https://www.nuget.org/api/v2/package/Selenium.WebDriver.GeckoDriver/ --ssl-no-revoke &&
#    unzip driver.zip &&
#    cd driver/$OS &&
#    cp "geckodriver$FILE_EXTENSION" "$TEST_RESOURCES/drivers" &&
#    chmod +700 "$TEST_RESOURCES/drivers/geckodriver"$FILE_EXTENSION &&
#    cd ../../../ &&
#    rm -rf temp &&
#    cd $ROOT_DIR
#fi

#uncomment to keep bash open
#! /bin/bash
