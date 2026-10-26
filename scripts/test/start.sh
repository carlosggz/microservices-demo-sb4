#!/bin/bash

script_dir="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
repo_root="$(cd -- "${script_dir}/../.." && pwd)"

compile_project() {
  local message=$1
  local project=$2

  echo "${message}"
  cd $repo_root
  ./gradlew "${project}:clean" "${project}:assemble"
  mv "${project}"/build/libs/* "${script_dir}/app.jar"
  cd $script_dir
  docker build -t "$project" .
  rm app.jar
}

echo 'Compiling all projects...'
compile_project "Processing eureka server" "eureka-server"
compile_project "Processing config-server" "config-server"
compile_project "Processing actors api" "actors-api"
compile_project "Processing movies api" "movies-api"
compile_project "Processing api gateway" "api-gateway"

echo "Generating test environment...."
docker compose up --build -d
echo "Done"