project=$1
output=$2
xcodebuild -project $project -scheme "Flo" -configuration Automation clean build CONFIGURATION_BUILD_DIR=$output
