const fs = require('fs')

const input = fs.readFileSync("./input.txt", {encoding:'utf8', flag:'r'})

const markerLength = 4

for (let i = markerLength; i < input.length; i++) {

  const buffer = input.substring(i - markerLength, i)
  const unique = buffer.split('').reduce((set, letter) => set.add(letter), new Set())
  if (unique.size === markerLength) {
    console.log(`Found marker ${buffer} at position ${i}`)
    break
  }
}
