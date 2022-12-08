def parseStacks(String stackInput) {
  def stackInputArray = stackInput.split('\n').collect { it.toCharArray() }
  def labelRow = stackInputArray.last()
  def stacks = []
  for (int i = 1; i < labelRow.length; i += 4) {
    def cols = stackInputArray.findResults {
      if (it.length >= i && it[i] != ' ') it[i]
    }
    stacks << cols
  }
  return stacks
}

def processMoves(String moveInput, List<char[]> stacks) {
  for (String line : moveInput.split('\n')) {
    def splitLine = line.split(' ')
    int move = Integer.parseInt(splitLine[1])
    int from = Integer.parseInt(splitLine[3]) - 1
    int to = Integer.parseInt(splitLine[5]) - 1

    def cratesToMove = []
    move.times {
      cratesToMove << stacks[from].pop()
    }
    stacks[to].addAll(0, cratesToMove)
  }
}

def input = new File('input.txt').text
def (stackInput, moveInput) = input.split('\n\n')

def stacks = parseStacks(stackInput)
processMoves(moveInput, stacks)

println("Answer: ${stacks.collect { it[0] }.join()}")
