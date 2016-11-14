#!/bin/bash
trap ctrl_c INT
function ctrl_c() {
    echo aborted by user
    return 1
}

TESTS_DIR=`pwd`
TESTDATA_DIR="../testdata"
SOURCE_DIR="../src"

function main()
{
    javac "$SOURCE_DIR/Terminal.java"
    java "$SOURCE_DIR/Terminal"
    #cat "$TESTDATA_DIR/Terminal_basic.txt" | java Terminal
}

main 2>&1 | tee TestTerminal.log
