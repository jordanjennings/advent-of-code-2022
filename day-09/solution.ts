const fs = require('fs')

const input = fs.readFileSync('input.txt', 'utf8')

class Position {
  constructor(readonly row: number, readonly col: number) {}
  adjacent(other: Position) {
    return Math.abs(this.row - other.row) <= 1 && Math.abs(this.col - other.col) <= 1
  }
  toString() {
    return `(${this.col}, ${this.row})`
  }
}

function left(pos: Position) { return new Position(pos.row, pos.col - 1) }
function right(pos: Position) { return new Position(pos.row, pos.col + 1) }
function up(pos: Position) { return new Position(pos.row + 1, pos.col) }
function down(pos: Position) { return new Position(pos.row - 1, pos.col) }

const KNOT_COUNT = 10

let knots = new Array(KNOT_COUNT).fill(new Position(0, 0));

const part1TailPositions = new Set([knots[0].toString()])
const part2TailPositions = new Set([knots[0].toString()])

function move(distance: number, moveFunction: (_: Position) => (Position)) {

  for (let i = 0; i < distance; i++) {
    knots[0] = moveFunction(knots[0])

    // this is gross
    for (let j = 1; j < KNOT_COUNT; j++) {
      if (!knots[j].adjacent(knots[j - 1])) {
        if (knots[j].col === knots[j - 1].col) {
          const rowDistance = knots[j].row - knots[j - 1].row
          if (rowDistance === 2) {
            knots[j] = down(knots[j])
          } else if (rowDistance == -2) {
            knots[j] = up(knots[j])
          }
        } else if (knots[j].row === knots[j - 1].row) {
          const colDistance = knots[j].col - knots[j - 1].col
          if (colDistance === 2) {
            knots[j] = left(knots[j])
          } else if (colDistance == -2) {
            knots[j] = right(knots[j])
          }
        } else {
          if (knots[j].row > knots[j - 1].row) {
            knots[j] = down(knots[j])
          } else {
            knots[j] = up(knots[j])
          }
          if (knots[j].col > knots[j - 1].col) {
            knots[j] = left(knots[j])
          } else {
            knots[j] = right(knots[j])
          }
        }
      }
      part1TailPositions.add(knots[1].toString())
      part2TailPositions.add(knots[KNOT_COUNT - 1].toString())
    }
  }
}

input.split('\n').forEach((line: string) => {
  const split = line.split(' ')
  const direction = split[0]
  const distance = Number(split[1])

  if (direction === 'L') {
    move(distance, left)
  } else if (direction === 'R') {
    move(distance, right)
  } else if (direction === 'U') {
    move(distance, up)
  } else if (direction === 'D') {
    move(distance, down)
  }
})

console.log(`Part 1 Answer: ${part1TailPositions.size}`)
console.log(`Part 2 Answer: ${part2TailPositions.size}`)
