#! /bin/bash
PROJECT_PATH=''
PERSONAL_TAG='@jw'

DEV_URL=''
TEST_URL=''
STAGING_URL=''

#mvn function for custom thread count, retry count, and tag
mvn_function() {
read -r -p "custom run? default is 12 threads count, 1 retry count and ${PERSONAL_TAG} tag. y(es) or n(o)" r
response=${r:0:1}

if [ "${response,,}" == 'y' ]
	then
		read -r -p "enter thread count" tc
		read -r -p "enter retry count" rc
    read -r -p 'enter tag, including "@", to append to the beginning of the String: " and not @wip and not @defect and not @api and not @closed"' tag
    read -r -p 'maven command (clean, verify, test): ' cm
    response='n'
    echo """mvn -Ddataproviderthreadcount=${tc} -DmaxRetryCount=${rc} -DgenerateToken=true -Dcucumber.filter.tags=\"(${tag}) and not @wip and not @defect and not @api and not @closed\" ${cm}"""
		eval """mvn -Ddataproviderthreadcount=${tc} -DmaxRetryCount=${rc} -DgenerateToken=true -Dcucumber.filter.tags=\"(${tag}) and not @wip and not @defect and not @api and not @closed\" ${cm}"""
else
        eval """mvn -Ddataproviderthreadcount=12 -DmaxRetryCount=1 -DgenerateToken=true -Dcucumber.filter.tags=\"(${PERSONAL_TAG}) and not @wip and not @defect and not @api and not @closed\" test"""
fi
}

# function for selecting the SPRING_DATASOURCE_URL and calling the mvn_function
environment() {
env=$1
if [[ $env == none ]]; then read -r -p "enter environment:" env; fi;
env=${env:0:1}
e="$(tr [A-Z] [a-z] <<< "${env}")"

if [[ $e == d || $e == D ]]
	then
		export SPRING_DATASOURCE_URL=$DEV_URL
		mvn_function
elif [[ $e == t ]]
	then
		export SPRING_DATASOURCE_URL=$TEST_URL
		mvn_function
elif [[ $e == s ]]
	then
		export SPRING_DATASOURCE_URL=$STAGING_URL
		mvn_function
else
		read -r "invalid environment. select from the following: dev, test, regression\nPress 0 to enter env\nPress 1 to enter the SPRING_DATASOURCE_URL manually\nPress any other key to exit" c
		if [[ $c -eq 0 ]]
			then
				environment
		elif [[ $c -eq 1 ]]
			then
			  read -r -p "enter URL" u
				export SPRING_DATASOURCE_URL="${u}"
				mvn_function
		else
				echo "exiting"
		fi
fi

}
start() {
env=${1:-none}
environment "${env}"
}