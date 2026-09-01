#!/usr/bin/env sh
# ----------------------------------------------------------------------------
# maven wrapper
# ----------------------------------------------------------------------------

PRG="${0}"

echo "Using the Maven Wrapper"

DIRNAME=`dirname "$PRG"`
# resolve links - $PRG may be a link
while [ -L "$PRG" ]; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG="`dirname "$PRG"`/$link"
  fi
done

# Get standard environment variables
PRGDIR=`dirname "$PRG"`

# location of maven wrapper files
MAVEN_WRAPPER_DIR="$PRGDIR/.mvn/wrapper"

# Download the maven-wrapper jar if it's missing
if [ ! -f "$MAVEN_WRAPPER_DIR/maven-wrapper.jar" ]; then
  echo "maven-wrapper.jar not found in $MAVEN_WRAPPER_DIR"
  echo "Attempting to download maven-wrapper.jar from Maven Central..."
  mkdir -p "$MAVEN_WRAPPER_DIR"
  PROPERTIES_FILE="$MAVEN_WRAPPER_DIR/maven-wrapper.properties"
  if [ -f "$PROPERTIES_FILE" ]; then
    DISTRIBUTION_URL=`sed -n 's/^distributionUrl=\(.*\)$/\1/p' "$PROPERTIES_FILE"`
  else
    DISTRIBUTION_URL="https://repo1.maven.org/maven2/io/takari/maven-wrapper/0.5.6/maven-wrapper-0.5.6.jar"
  fi
  echo "Downloading $DISTRIBUTION_URL"
  # try curl then wget
  if command -v curl >/dev/null 2>&1; then
    curl -fsSL "$DISTRIBUTION_URL" -o "$MAVEN_WRAPPER_DIR/maven-wrapper.jar" || {
      echo "Failed to download maven-wrapper.jar via curl";
      exit 1;
    }
  elif command -v wget >/dev/null 2>&1; then
    wget -q "$DISTRIBUTION_URL" -O "$MAVEN_WRAPPER_DIR/maven-wrapper.jar" || {
      echo "Failed to download maven-wrapper.jar via wget";
      exit 1;
    }
  else
    echo "Neither curl nor wget found; please run 'mvn -N io.takari:maven:wrapper' locally to generate the wrapper.";
    exit 1;
  fi
fi

# Execute the wrapper
java -jar "$MAVEN_WRAPPER_DIR/maven-wrapper.jar" "$@"
