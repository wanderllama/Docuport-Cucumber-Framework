#!/bin/bash
# URLS and cases need to be added to enable more options

# converting the commands used in the if statement to aliases
# that would be assigned during the first switch statement if
# the OS commands are different

# assign alias here
function downloadWebDriver() {
    case $1 in
      edge)
        echo edge
        export url="https://msedgedriver.azureedge.net/120.0.2210.91/edgedriver_mac64_m1.zip"
        export driver="msedgedriver" ;;
      chrome)
        echo chrome
        export driver="chromedriver" ;;
      firefox)
        echo firefox
        export driver="firefoxdriver" ;;
      *)
        echo invalid ;;
    esac

    TEST_RESOURCES="common/src/main/resources"
    export absPath=${PWD}/${TEST_RESOURCES}/drivers
    FILE_EXTENSION=""

    case "$OSTYPE" in
    solaris*) echo "$OSTYPE not supported" ;;
    darwin*) OS='mac' ;;
    linux*) OS='linux64' ;;
    bsd*) echo "$OSTYPE not supported" ;;
    msys*) OS='win32' FILE_EXTENSION=".exe" ;;
    *) echo "unknown $OSTYPE" ;;
    esac

    # update to use aliases so OS with different commands can use same if block
    if [ ! -f "${absPath}/${OS}/${driver}${FILE_EXTENSION}" ]; then
      cd /etc/.. && cd "${absPath}" &&
        mkdir "temp" &&
        cd "temp" &&
        curl -L -k --output driver.zip $url --ssl-no-revoke &&
        unzip driver.zip && echo "unzip complete"
        cp "${driver}${FILE_EXTENSION}" "${absPath}/${OS}" &&
        chmod "u=rx,go=rx" "${absPath}/${OS}/${driver}${FILE_EXTENSION}" &&
        cd /etc/.. &&
        cd "${absPath}" &&
        rm -rf temp &&
        cd "$ROOT_DIR" || exit
    fi
}
#}
#set -x
#const WebdDriverType
#
## shellcheck disable=SC2154
#const SCRIPT_DIR="${realpath}"
#const TEST_RESOURCES="/common/src/test/resources"
#export absPath="${SCRIPT_DIR}""${TEST_RESOURCES}"
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
#if [ ! -f "$absPath/drivers/$OSTYPE/msedgedriver"$FILE_EXTENSION ]; then
#  cd "$TEST_RESOURCES/drivers" &&
#    mkdir "temp" &&
#    cd "temp" &&
#    curl -L -k --output driver.zip https://msedgedriver.azureedge.net/120.0.2210.91/edgedriver_mac64_m1.zip --ssl-no-revoke &&
#    unzip driver.zip
#    sleep 5
#    cp "msedgedriver$FILE_EXTENSION" "$TEST_RESOURCES/drivers/$OSTYPE"
#    chmod +700 "$TEST_RESOURCES/$OSTYPE/msedgedriver"$FILE_EXTENSION
#    cd "$absPath/drivers"
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
