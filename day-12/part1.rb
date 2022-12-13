require "set"

input = File.read("input.txt")

$map = input.split.map { |line| line.split('') }

Position = Struct.new(:row, :col) do
  def to_s
    "(#{self.row}, #{self.col})"
  end
  def left
    Position.new(self.row, self.col - 1)
  end
  def right
    Position.new(self.row, self.col + 1)
  end
  def up
    Position.new(self.row - 1, self.col)
  end
  def down
    Position.new(self.row + 1, self.col)
  end
end

(0...$map.size).each do |row|
  if $map[row][0] == "S"
    $start_position = Position.new(row, 0)
  end
end

puts "Starting position: #{$start_position}"

$visited = Set[]

$shortest_path = $map.size * $map[0].size + 100  # number bigger than possible number of steps

def get_height(pos)
  letter = $map[pos.row][pos.col]
  standardized = case letter
                 when "S" then "a"
                 when "E" then "z"
                 else letter
                 end
  standardized.ord
end

def steppable(from, to)
  if to.row < 0 || to.col < 0 || to.row >= $map.size || to.col >= $map[0].size
    return false
  end
  get_height(from) - get_height(to) <= 1
end

$path = $map.map { |row| row.map { |_| $map.size * $map[0].size + 100 } }

def take_step(pos, steps)

  if $visited.include?(pos)
    return
  end

  $visited.add(pos)

  if $map[pos.row][pos.col] == 'E'
    total_steps = $visited.size - 1
    if $shortest_path > total_steps
      puts "Found new shortest path: #{total_steps} steps"
      $shortest_path = total_steps
    end
  else
    if $path[pos.row][pos.col] > steps
      $path[pos.row][pos.col] = steps
      if steppable(pos, pos.left)
        take_step(pos.left, steps + 1)
      end
      if steppable(pos, pos.right)
        take_step(pos.right, steps + 1)
      end
      if steppable(pos, pos.up)
        take_step(pos.up, steps + 1)
      end
      if steppable(pos, pos.down)
        take_step(pos.down, steps + 1)
      end
    end
  end

  $visited.delete(pos)

end

take_step($start_position, 0)

puts "Part 1 shortest path: #{$shortest_path}"
