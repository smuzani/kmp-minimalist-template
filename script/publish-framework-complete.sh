#!/bin/bash

# Get the script directory and project root directory
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT_ROOT="$( cd "$SCRIPT_DIR/.." && pwd )"

# Configuration
GITHUB_REPO="ahmadAlfhajri/kmp-minimalist-template_test_script" #TODO: yg betul
GITHUB_TOKEN="your_github_token"
FRAMEWORK_NAME="shared" #TODO: kasi nama yg betul
# Update path to be relative to project root
XCFRAMEWORK_PATH="${PROJECT_ROOT}/shared/build/cocoapods/publish/release/${FRAMEWORK_NAME}.xcframework"
RELEASE_DIR="${PROJECT_ROOT}/shared/build/cocoapods/publish/release"

# Function to format version tag to version number
format_version() {
    local version_tag=$1
    echo "${version_tag#v}"
}

# Function to create zip name
get_zip_name() {
    local version_tag=$1
    local version_number=$(format_version "$version_tag")
    echo "XenditFingerprintSDK-${version_number}.zip" #TODO: Ubah nama
}

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print step information
print_step() {
    echo -e "\n${BLUE}=== $1 ===${NC}"
}

# Function to check if command succeeded
check_status() {
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ $1 completed successfully${NC}"
    else
        echo -e "${RED}✗ $1 failed${NC}"
        exit 1
    fi
}

# Function to check if file exists
check_file_exists() {
    if [ ! -d "$1" ]; then
        echo -e "${RED}Error: $2 not found at: $1${NC}"
        exit 1
    fi
}

# Function to check if release exists
check_release() {
    local version_tag=$1
    
    print_step "Checking if release $version_tag exists"
    
    response=$(curl -s \
        -H "Authorization: token $GITHUB_TOKEN" \
        -H "Accept: application/vnd.github.v3+json" \
        "https://api.github.com/repos/$GITHUB_REPO/releases/tags/$version_tag")
    
    # Check if release exists
    if [ "$(echo "$response" | jq -r '.message')" = "Not Found" ]; then
        echo -e "${BLUE}Release not found - will create new release${NC}"
        return 1
    else
        # Extract and return the release ID
        release_id=$(echo "$response" | jq -r '.id')
        echo -e "${BLUE}Found existing release with ID: $release_id${NC}"
        return 0
    fi
}

# Function to get existing release upload URL
get_existing_release_url() {
    local version_tag=$1
    
    response=$(curl -s \
        -H "Authorization: token $GITHUB_TOKEN" \
        -H "Accept: application/vnd.github.v3+json" \
        "https://api.github.com/repos/$GITHUB_REPO/releases/tags/$version_tag")
    
    upload_url=$(echo "$response" | jq -r .upload_url | sed 's/{?name,label}//g')
    echo "$upload_url"
}

# Function to delete existing asset if it exists
delete_existing_asset() {
    local version_tag=$1
    
    print_step "Checking for existing assets"
    
    assets_response=$(curl -s \
        -H "Authorization: token $GITHUB_TOKEN" \
        -H "Accept: application/vnd.github.v3+json" \
        "https://api.github.com/repos/$GITHUB_REPO/releases/tags/$version_tag")
    
    # Find asset with matching name
    asset_id=$(echo "$assets_response" | jq -r ".assets[] | select(.name == \"$ZIP_NAME\") | .id")
    
    if [ ! -z "$asset_id" ] && [ "$asset_id" != "null" ]; then
        echo -e "${BLUE}Deleting existing asset with ID: $asset_id${NC}"
        curl -s -X DELETE \
            -H "Authorization: token $GITHUB_TOKEN" \
            -H "Accept: application/vnd.github.v3+json" \
            "https://api.github.com/repos/$GITHUB_REPO/releases/assets/$asset_id"
        check_status "Asset deletion"
    fi
}

# Function to create a new release
create_release() {
    local version_tag=$1
    
    print_step "Creating new release $version_tag"
    
    echo "Debug - Upload URL: ${upload_url}"

    response=$(curl -s -X POST \
        -H "Authorization: token $GITHUB_TOKEN" \
        -H "Accept: application/vnd.github.v3+json" \
        "https://api.github.com/repos/$GITHUB_REPO/releases" \
        -d "{
            \"tag_name\": \"$version_tag\",
            \"name\": \"Release $version_tag\",
            \"body\": \"Release $version_tag of $FRAMEWORK_NAME\",
            \"draft\": false,
            \"prerelease\": false
        }")
    
    upload_url=$(echo "$response" | jq -r .upload_url | sed 's/{?name,label}//g')
    
    if [ -z "$upload_url" ] || [ "$upload_url" = "null" ]; then
        echo -e "${RED}Error creating release. Response:${NC}"
        echo "$response"
        exit 1
    fi
    
    echo "$upload_url"
}

# Function to upload asset to release
upload_asset() {
    local upload_url=$1
    local zip_path="${RELEASE_DIR}/${ZIP_NAME}"
    
    print_step "Uploading XCFramework"
    
    # Check if zip file exists in release directory
    if [ ! -f "$zip_path" ]; then
        echo -e "${RED}Error: Zip file not found: $zip_path${NC}"
        echo -e "${BLUE}Release directory contents:${NC}"
        ls -la "$RELEASE_DIR"
        exit 1
    fi
    
    echo -e "${BLUE}Uploading file: $zip_path${NC}"
    echo -e "${BLUE}File size: $(ls -lh "$zip_path" | awk '{print $5}')${NC}"
    
    cd "$RELEASE_DIR"
    
    curl -v \
        -H "Authorization: token $GITHUB_TOKEN" \
        -H "Content-Type: application/zip" \
        -H "Accept: application/vnd.github.v3+json" \
        --data-binary "@${ZIP_NAME}" \
        "${upload_url}?name=${ZIP_NAME}" 2>&1
    
    check_status "Release upload"
}

# Function to build XCFramework using Gradle
build_framework() {
    print_step "Building XCFramework"
    # Change to project root directory before running gradlew
    cd "$PROJECT_ROOT"
    ./gradlew podPublishReleaseXCFramework
    check_status "Gradle build"
    
    # Check if XCFramework was generated
    check_file_exists "$XCFRAMEWORK_PATH" "XCFramework"
}

# Function to create zip of XCFramework
create_zip() {
    print_step "Creating zip file"
    # Make sure we're in the project root
    cd "$PROJECT_ROOT"
    (cd "$(dirname "$XCFRAMEWORK_PATH")" && zip -r "$(pwd)/$ZIP_NAME" "$(basename "$XCFRAMEWORK_PATH")")
    check_status "Zip creation"
}

# Main execution
main() {
    local version_tag=$1
    local upload_url
    
    if [ -z "$version_tag" ]; then
        echo -e "${RED}Error: Version tag is required${NC}"
        echo "Usage: $0 <version_tag>"
        echo "Example: $0 v1.0.0"
        exit 1
    fi

    # Set ZIP_NAME using the new format
    ZIP_NAME=$(get_zip_name "$version_tag")
    
    # Check if required commands exist
    if [ ! -f "${PROJECT_ROOT}/gradlew" ]; then
        echo -e "${RED}Error: gradlew not found in project root directory${NC}" >&2
        exit 1
    fi
    command -v jq >/dev/null 2>&1 || { echo -e "${RED}Error: jq is required but not installed${NC}" >&2; exit 1; }
    
    # Build framework
    build_framework
    
    # Create zip
    create_zip
    
    # Handle GitHub release
    if check_release "$version_tag"; then
        echo -e "${BLUE}Using existing release...${NC}"
        delete_existing_asset "$version_tag"
        upload_url=$(get_existing_release_url "$version_tag")
    else
        echo -e "${BLUE}Creating new release...${NC}"
        upload_url=$(create_release "$version_tag")
    fi

    # echo "Debug - Upload URL: ${upload_url}"
    
    # Upload to GitHub
    upload_asset "$upload_url"
    
    # Cleanup
    print_step "Cleaning up"
    rm "${RELEASE_DIR}/${ZIP_NAME}" 2>/dev/null
    
    echo -e "\n${GREEN}🎉 Release process completed successfully!${NC}"
}

# Run the script with version tag argument
main "$1"
