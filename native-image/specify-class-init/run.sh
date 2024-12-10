#!/usr/bin/env bash
set -ex

javac TalkParser.java
native-image -Ob -o runtime-parser TalkParser
time ./runtime-parser
du -sh runtime-parser
native-image -Ob --initialize-at-build-time=TalkParser,Talk -o buildtime-parser TalkParser
time ./buildtime-parser
du -sh buildtime-parser