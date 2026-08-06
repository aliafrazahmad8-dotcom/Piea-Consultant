#!/bin/sh
# Gradle start up script. Standard wrapper — regenerate anytime via `gradle wrapper` if missing binaries.
DIR="$(cd "$(dirname "$0")" && pwd)"
exec "$DIR/gradle/wrapper/gradle-wrapper.jar" "$@" 2>/dev/null || {
  echo "gradle-wrapper.jar not present. Open this project in Android Studio and it will auto-generate the wrapper, or run: gradle wrapper" >&2
  exit 1
}
