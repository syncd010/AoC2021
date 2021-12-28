#!/bin/sh

if [ -z "$1" ]
then
    echo "Please provide a day number"
    exit
fi

DAY="day$1"

if [ -f "./src/aoc2021/day$1.clj" ]
then
    echo "Day $1 already exists"
else
    cp "./src/aoc2021/day_template.clj" "./src/aoc2021/day$1.clj"
    sed -i "s/(ns aoc2021.day-template/(ns aoc2021.day$1/" "./src/aoc2021/day$1.clj"
    touch "./resources/input$1"
    touch "./resources/input$1Test"
    echo "Day $1 created"
fi

