import java.io.File

val input = File("input.txt").readLines(Charsets.UTF_8).map { it.toCharArray().map { it.digitToInt() } }

data class Position(val row: Int, val col: Int)

fun left(p: Position) = Position(p.row, p.col - 1)
fun right(p: Position) = Position(p.row, p.col + 1)
fun up(p: Position) = Position(p.row - 1, p.col)
fun down(p: Position) = Position(p.row + 1, p.col)

fun visible(startingHeight: Int, pos: Position, move: ((Position) -> Position)): Boolean {
  val newPos = move(pos)
  if (newPos.col < 0 || newPos.row < 0 || newPos.row >= input.size || newPos.col >= input[newPos.row].size) {
    return true
  }
  val height = input[newPos.row][newPos.col]
  return height < startingHeight && visible(startingHeight, newPos, move)
}

var visibleTrees = 0

for (row in 0 until input.size) {
  for (col in 0 until input[row].size) {
    val height = input[row][col]
    val pos = Position(row, col)
    if (visible(height, pos, ::left)
        || visible(height, pos, ::right)
        || visible(height, pos, ::up)
        || visible(height, pos, ::down)) {
      visibleTrees++
    }
  }
}

println("Visible trees: ${visibleTrees}")

