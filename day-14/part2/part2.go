package main

import (
	"fmt"
	"os"
	"strconv"
	"strings"
)

const (
	rock = "#"
	sand = "o"
)

func main() {
	inputBytes, _ := os.ReadFile("input.txt")
	input := string(inputBytes)
	rockLocations, maxY := parse(input)
	sandCount := dropSand(rockLocations, maxY + 2)
	fmt.Println("Part 2 Answer:", sandCount)
}

func dropSand(rockOrSandLocations map[point]string, floorY int) int {
	start := point{500, 0}
	sandCount := 0
	startBlocked := false

	for !startBlocked {
		current := start
		sandCount += 1

		for true {
			if current.y + 1 == floorY {
				rockOrSandLocations[current] = sand
				break
			}

			down := point{current.x, current.y + 1}
			if _, occupied := rockOrSandLocations[down]; !occupied {
				current = down
				continue
			}

			// try down-left
			downLeft := point{current.x - 1, current.y + 1}
			if _, occupied := rockOrSandLocations[downLeft]; !occupied {
				current = downLeft
				continue
			}

			// try down-right
			downRight := point{current.x + 1, current.y + 1}
			if _, occupied := rockOrSandLocations[downRight]; !occupied {
				current = downRight
				continue
			}

			// couldn't slide down anywhere, now at rest
			rockOrSandLocations[current] = sand
			break
		}
		_, ok := rockOrSandLocations[point{500, 0}]
		startBlocked = ok
	}

	return sandCount
}

type point struct { x, y int }
func parse(input string) (map[point]string, int){
	maxY := -1
	inputLines := strings.Split(input, "\n")
	rockLocations := make(map[point]string)

	for _, line := range inputLines {
		if line == "" { continue }
		points := strings.Split(line, " -> ")
		var start *point
		var end *point
		for _, pointStr := range points {
			coords := strings.Split(pointStr, ",")
			x, _ := strconv.Atoi(coords[0])
			y, _ := strconv.Atoi(coords[1])
			if start == nil {
				start = &point{x, y}
			} else {
				end = &point{x, y}

				xIncrement := 0
				yIncrement := 0
				if start.x == end.x {
					yIncrement = direction(start.y, end.y)
				} else if start.y == end.y {
					xIncrement = direction(start.x, end.x)
				}

				current := start
				for *current != *end {
					rockLocations[*current] = rock
					current = &point{ current.x + xIncrement, current.y + yIncrement }
				}

				start = end
			}

			if y > maxY { maxY = y }
		}
		rockLocations[*end] = rock
	}

	return rockLocations, maxY

}

func direction(x1 int, x2 int) int {
	increment := 0
	if x1 < x2 {
		increment = 1
	} else if x1 > x2 {
		increment = -1
	}
	return increment
}
