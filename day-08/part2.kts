import java.io.File

val input = File("input.txt").readLines(Charsets.UTF_8).map { it.toCharArray().map { it.digitToInt() } }

data class Position(val row: Int, val col: Int)

fun left(p: Position) = Position(p.row, p.col - 1)
fun right(p: Position) = Position(p.row, p.col + 1)
fun up(p: Position) = Position(p.row - 1, p.col)
fun down(p: Position) = Position(p.row + 1, p.col)

fun visibleDistance(startingHeight: Int, pos: Position, move: ((Position) -> Position)): Int {
  val newPos = move(pos)
  if (newPos.col < 0 || newPos.row < 0 || newPos.row >= input.size || newPos.col >= input[newPos.row].size) {
    return 0
  }
  val height = input[newPos.row][newPos.col]
  if (height < startingHeight) {
    return visibleDistance(startingHeight, newPos, move) + 1
  } else {
    return 1
  }
}

var maxScenicScore = 0

for (row in 0 until input.size) {
  for (col in 0 until input[row].size) {
    val height = input[row][col]
    val pos = Position(row, col)

    val scenicScore = visibleDistance(height, pos, ::left) *
        visibleDistance(height, pos, ::right) *
        visibleDistance(height, pos, ::up) *
        visibleDistance(height, pos, ::down)

    if (scenicScore > maxScenicScore) maxScenicScore = scenicScore
  }
}

println("Max scenic score: ${maxScenicScore}")

