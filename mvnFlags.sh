#! /bin/bash
SELENIUM_PATH='/path/to/project'
HOME='/Users/jw'
PERSONAL_TAG='@jw'

DEV_URL=''
EST_URL=''
REGRESSION_URL=''

run() {
    tc=$1
    rc=$2
    m=$3
    tag=$4
    echo """mvn -Ddataproviderthreadcount=${tc} -DmaxRetryCount=${rc} -DgenerateToken=true -Dcucumber.filter.tags=\"(${tag}) and not @wip and not @defect and not @api and not @closed\" ${m}"""
    eval """mvn -Ddataproviderthreadcount=${tc} -DmaxRetryCount=${rc} -DgenerateToken=true -Dcucumber.filter.tags=\"(${tag}) and not @wip and not @defect and not @api and not @closed\" ${m}"""
}

#mvn function for custom thread count, retry count, and tag
mvn_function() {

    tc=$1
    rc=$2
    m=$3
    tag=$4

    if [[ $tc != none && $rc != none && $m != none && $tag != none ]]
        then
            run $tc $rc $m $tag
    else
        if [[ $1 == none ]]
            then
                read -r -p 'enter thread count: ' tc
                echo "threads: ${tc}"
        fi
        if [[ $2 == none ]]
            then
                read -r -p 'enter retry count: ' rc
                echo "retry count: ${rc}"
        fi
        if [[ $3 == none ]]
            then
                read -r -p 'maven command (clean, verify, test): ' m
                echo "running the following maven command: ${m}"
        fi
        if [[ $4 == none ]]
            then
                read -r -p 'enter string of tags to prepend to: " and not @wip and not @defect and not @api and not @closed": ' tag
                echo "running the following maven command: ${tag}"
        fi
            run $tc $rc $m $tag
    fi
}

# function for selecting the SPRING_DATASOURCE_URL and calling the mvn_function
environment() {

    e=$1
    t=$2
    r=$3
    m=$4
    tag=$5

    if [[ $e == none ]]; then read -r -p "enter environment" e; fi;

    e=${e:0:1}
    env="$(tr [A-Z] [a-z] <<< "${e}")"

    echo "testing ${env}"

    if [[ $env == d ]]
        then
            SPRING_DATASOURCE_URL=$DEV_URL
            mvn_function $t $r $m $tag
    elif [[ $env == t ]]
        then
            SPRING_DATASOURCE_URL=$TEST_URL
            mvn_function $t $r $m $tag
    elif [[ $env == r ]]
        then
            SPRING_DATASOURCE_URL=$REGRESSION_URL
            mvn_function $t $r $m $tag
    else
            echo "invalid environment. select from the following: dev, test, regression"
            echo "Press 0 to enter env, Press 1 to enter the SPRING_DATASOURCE_URL manually"
            read -r -p "Press any other key to exit" w
            if [[ $w -eq 0 ]]
                then
                    environment $1 $t $r $m $tag
            elif [[ $w -eq 1 ]]
                then
                    SPRING_DATASOURCE_URL=$w
                    mvn_function $t $r $m $tag
            fi
    fi
}

#start
start() {

    tc='none'
    rc='none'
    env="none"
    m='none'
    tag='none'

    print_usage() {
    echo "[options] -env:t:r:m:h [arguments]"
    echo "-h help menu"
    echo "[options], name   -> [arguments]"
    echo "-env, environment -> (d)ev, (t)est, (r)egression"
    echo "-t, thread count  -> number: 1-12"
    echo "-r, retry count   -> number: 1-4"
    echo "-m, maven command -> any mvn option (clean test verify)"
    }

    while test $# -gt 0; do
        echo $1
        echo "testing"
        echo $2
        case $1 in
            -t)
                shift
                tc=$1
                shift ;;
            -r)
                shift
                rc=$1
                shift ;;
            -env)
                shift
                env=$1
                shift ;;
            -m)
                shift
                m=$1
                shift ;;
            -tag)
                shift
                tag=$1
                shift ;;
            -h)
                shift
                print_usage
                exit 1 ;;
            *) echo "invalid flag entered"
                exit 1 ;;
        esac
    done

    dir=$PWD
    echo "$dir"
    echo PROJECT_PATH
    if [ "$dir" != "$PROJECT_PATH" ]; then cd ${HOME}${PROJECT_PATH}; fi;
    environment $env $tc $rc $m $tag
}
