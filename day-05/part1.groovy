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
    int from = Integer.parseInt(splitLine[3])
    int to = Integer.parseInt(splitLine[5])

    move.times {
      def top = stacks[from - 1].pop()
      stacks[to - 1].push(top)
    }
  }
}

def input = new File('input.txt').text
def (stackInput, moveInput) = input.split('\n\n')

def stacks = parseStacks(stackInput)
processMoves(moveInput, stacks)

println("Answer: ${stacks.collect { it[0] }.join()}")
