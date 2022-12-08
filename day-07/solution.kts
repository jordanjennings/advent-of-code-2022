import java.io.File

class Node(val parent: Node?, val name: String, private val size: Int) {
  val children: MutableList<Node> = mutableListOf()

  fun getSize(): Int = this.size + children.sumOf { it.getSize() }
}

var root = Node(parent = null, name = "/", size = 0)
var current = root
val directories = mutableListOf(root)

val input = File("input.txt").forEachLine {
  if (it == "$ cd /") {
    return@forEachLine
  }

  if (it == "$ cd ..") {
    current = current.parent!!
  } else if (it.startsWith("$ cd ")) {
    val dirName = it.split(" ")[2]
    current = current.children.find { it.name == dirName }!!
  } else {
    val node = Node(parent = current, size = it.split(" ")[0].toIntOrNull() ?: 0, name = it.split(" ")[1])
    if (node.getSize() == 0) directories.add(node)
    current.children.add(node)
  }
}

val dirSizes = directories.map { it.getSize() }

// part 1
val part1 = dirSizes.filter { it <= 100000 }.sum()
println("Part 1 answer: ${part1}")

// part 2
val totalSpace = 70000000
val requiredSpace = 30000000
val usedSpace = root.getSize()
val availableSpace = totalSpace - usedSpace
val minimumToDelete = requiredSpace - availableSpace

val part2 = dirSizes.filter { it >= minimumToDelete }.min()
println("Part 2 answer: ${part2}")