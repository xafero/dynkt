# dynkt
This is a JSR-223 scripting engine for the language "Kotlin"

## What is that and this?
- **dynkt**: The core source code of this project
- **dynkt-osgi**: Addon as OSGi bundle with batteries included

## How to build?
`mvn clean package install`

## How to run in OSGi?
> java -jar bin\felix.jar
> g! install file:/./dynkt-jsr223-osgi-1.0.0-SNAPSHOT.jar
> g! start 5

## License
Everything is free to use under the terms of attached LICENSE file.
