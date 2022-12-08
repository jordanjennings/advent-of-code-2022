#!/usr/bin/env bash

set -e

function contains() {
  start1=$1
  end1=$2
  start2=$3
  end2=$4

  if [[ $start1 -ge $start2 && $end1 -le $end2 ]] ||
     [[ $start2 -ge $start1 && $end2 -le $end1 ]]; then
    true
  else
    false
  fi
}

num_fully_contained=0

for line in $(cat input.txt); do

  elf1=$(echo "$line" | cut -d',' -f1)
  elf2=$(echo "$line" | cut -d',' -f2)

  elf1_start=$(echo $elf1 | cut -d'-' -f1)
  elf1_end=$(echo $elf1 | cut -d'-' -f2)

  elf2_start=$(echo $elf2 | cut -d'-' -f1)
  elf2_end=$(echo $elf2 | cut -d'-' -f2)

  if contains $elf1_start $elf1_end $elf2_start $elf2_end; then
    num_fully_contained=$((num_fully_contained + 1))
  fi

done

echo "Assignments pairs where one is fully contained by the other: $num_fully_contained"