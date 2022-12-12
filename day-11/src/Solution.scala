import java.io.File
import scala.collection.mutable
import scala.io.Source
import scala.util.Using

case class Monkey(items: mutable.Queue[Long],
                  operation: Long => Long,
                  testDivisor: Int,
                  trueMonkey: Int,
                  falseMonkey: Int,
                  var inspectionCount: Long = 0)

object Solution {
  def main(args: Array[String]): Unit = {
    val part1 = runRounds(20, reduceWorry = true)
    println(s"Part 1 Answer: ${part1}")

    val part2 = runRounds(10000, reduceWorry = false)
    println(s"Part 2 Answer: ${part2}")
  }

  def runRounds(rounds: Int, reduceWorry: Boolean): Long = {
    val monkeys = parseInput()
    val megaMod = monkeys.map(_.testDivisor).product
    for (_ <- 1 to rounds) {
      runRound(monkeys, megaMod, reduceWorry)
    }
    monkeys.map(_.inspectionCount).sorted.takeRight(2).reduce(_.*(_))
  }

  def parseInput(): Array[Monkey] = {
    val fileName = "input.txt"
    val input = Using.resource(getClass.getResourceAsStream(fileName)) { stream =>
      Source.fromInputStream(stream).mkString
    }

    val pattern =
      """Monkey [0-9+]:
        |  Starting items:(.*)
        |  Operation: new = old ([+\-\*\/]) ([0-9]+|old)
        |  Test: divisible by ([0-9]+)
        |    If true: throw to monkey ([0-9]+)
        |    If false: throw to monkey ([0-9]+)""".stripMargin.r

    input
      .split("\n\n")
      .map(monkeyInput => {
        val pattern(startingItems, operator, operand, divisor, trueMonkeyNum, falseMonkeyNum) = monkeyInput
        val operation: Long => Long =
          operator match {
            case "+" => _ + operand.toInt
            case "-" => _ - operand.toInt
            case "*" =>
              if (operand == "old")
                (old: Long) => old * old
              else
                _ * operand.toInt
            case "/" => _ - operand.toInt
          }

        Monkey(
          items = startingItems.split(",").map(_.trim.toLong).to(mutable.Queue),
          operation = operation,
          testDivisor = divisor.toInt,
          trueMonkey = trueMonkeyNum.toInt,
          falseMonkey = falseMonkeyNum.toInt
        )
      })
  }

  def runRound(monkeys: Array[Monkey], megaMod: Int, reduceWorry: Boolean = true): Unit = {

    monkeys.foreach(monkey => {
      while (monkey.items.nonEmpty) {
        val item = monkey.items.dequeue()
        monkey.inspectionCount += 1

        val worryWhileInspecting = monkey.operation(item)
        val worryAfterInspecting = if reduceWorry then worryWhileInspecting / 3 else worryWhileInspecting

        val megaModRemainder = worryAfterInspecting % megaMod
        val worryRemainder = megaModRemainder % monkey.testDivisor

        val recipient = if (worryRemainder == 0) then monkey.trueMonkey else monkey.falseMonkey

        monkeys(recipient).items.enqueue(megaModRemainder)
      }
    })
  }

}

