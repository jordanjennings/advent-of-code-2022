package main

import (
	"fmt"
	"os"
	"sort"
	"strconv"
	"strings"
)

func main() {
	inputBytes, _ := os.ReadFile("input.txt")
	input := string(inputBytes)
	part1(input)
	part2(input)
}

func part1(input string) {
	inputLines := strings.Split(input, "\n\n")

	var orderedPairIndices []int
	for i := range inputLines {
		pairLines := strings.Split(inputLines[i], "\n")
		left := parse(pairLines[0])
		right := parse(pairLines[1])
		result := compareItems(left, right)
		if result == ordered {
			orderedPairIndices = append(orderedPairIndices, i + 1)
		}
	}

	answer := 0
	for _, orderedPairIndex := range orderedPairIndices {
		answer += orderedPairIndex
	}
	fmt.Println("Part 1 Answer:", answer)
}

func part2(input string) {
	inputLines := strings.Split(input, "\n")
	var packets [][]any
	for _, line := range inputLines {
		if line != "" {
			packets = append(packets, parse(line))
		}
	}
	packets = append(packets, parse("[[2]]"), parse("[[6]]"))
	sort.Slice(packets, func(i, j int) bool {
		return compareList(packets[i], packets[j]) == ordered
	})
	var marker1Index int
	var marker2Index int
	for i := range packets {
		if fmt.Sprint(packets[i]) == "[[2]]" {
			marker1Index = i + 1
		}
		if fmt.Sprint(packets[i]) == "[[6]]" {
			marker2Index = i + 1
		}
	}
	fmt.Println("Part 2 Answer:", marker1Index * marker2Index)
}

type outcome int

const (
	ordered outcome = iota
	notOrdered
	unknown
)

func compareItems(left any, right any) outcome {
	leftInt, leftIsInt := left.(int)
	rightInt, rightIsInt := right.(int)

	if leftIsInt && rightIsInt {
		if leftInt < rightInt {
			return ordered
		} else if leftInt > rightInt {
			return notOrdered
		}
		return unknown
	}

	leftList, leftIsList := left.([]any)
	rightList, rightIsList :=right.([]any)

	if leftIsList && rightIsList {
		return compareList(leftList, rightList)
	}

	if leftIsInt {
		return compareList([]any{leftInt}, rightList)
	}

	// right must be an int
	return compareList(leftList, []any{rightInt})
}

func compareList(left []any, right []any) outcome {
	for i := range left {
		if i >= len(right) {
			return notOrdered
		}
		result := compareItems(left[i], right[i])
		if result == notOrdered || result == ordered {
			return result
		}
	}
	if len(left) < len(right) {
		return ordered
	}
	return unknown
}

func parse(input string) []any {

	var listStack [][]any
	var currentList []any
	var numberBuffer string

	for i := range input {
		ch := input[i:i+1]
		switch {
		case ch == "[":
			// start list
			listStack = append(listStack, currentList)
			currentList = make([]any, 0)
		case ch == "]":
			// end list
			if numberBuffer != "" {
				number, _ := strconv.Atoi(numberBuffer)
				currentList = append(currentList, number)
				numberBuffer = ""
			}
			parentList := listStack[len(listStack)-1]
			parentList = append(parentList, currentList)
			currentList = parentList
			listStack = listStack[:len(listStack)-1]
		case ch == ",":
			// next value
			if numberBuffer != "" {
				// previous value was number
				number, _ := strconv.Atoi(numberBuffer)
				currentList = append(currentList, number)
				numberBuffer = ""
			}
		case ch >= "0" && ch <= "9":
			// read number
			numberBuffer += ch
		}
	}
	unwrappedList, _ := currentList[0].([]any)
	return unwrappedList
}
