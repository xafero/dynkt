# dynkt
This is a JSR-223 scripting engine for the language "Kotlin"

[![Build Status](https://travis-ci.org/xafero/dynkt.svg?branch=master)](https://travis-ci.org/xafero/dynkt)

## What is that and this?
- **dynkt**: The core source code of this project
- **dynkt-osgi**: Addon as OSGi bundle with batteries included

## How to build?
`mvn clean package install`

## How to run in OSGi?
1. Run your container: `java -jar bin\felix.jar`
2. Install the bundle: `g! install file:/./dynkt-jsr223-osgi-1.0.0-SNAPSHOT.jar`
3. Start it: `g! start 5`

## License
Everything is free to use under the terms of attached LICENSE file.
