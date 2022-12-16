import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Solution {

  record Point(int x, int y) { }
  record Sensor(Point location, Point closestBeacon) { }

  public static void main(String[] args) throws Exception {
    part1();
    part2();
  }

  private static void part1() throws Exception {
    //    var fileName = "sample.txt"; var yToCheck = 10;
    var fileName = "input.txt"; var yToCheck = 2000000;
    List<Sensor> sensors = parseSensors(fileName);

    var noBeaconSpots = sensors.stream()
        .filter(sensor -> {
          var distance = manhattanDistance(sensor.location(), sensor.closestBeacon());
          return Math.abs(sensor.location().y() - yToCheck) <= distance;
        })
        .map(sensor -> noBeaconZone(sensor, yToCheck))
        .flatMap(Collection::stream)
        .collect(Collectors.toSet())
        .stream().filter(p -> p.y() == yToCheck)
        .count();

    System.out.println("Part 1 Answer: " + noBeaconSpots);
  }

  private static void part2() throws Exception {
//    var fileName = "sample.txt"; var min = 0; var max = 20;
    var fileName = "input.txt"; var min = 0; var max = 4_000_000;
    List<Sensor> sensors = parseSensors(fileName);

    for (int y = min; y < max; y++) {
      for (int x = min; x < max; x++) {
        boolean inRangeOfAtLeastOneSensor = false;
        for (var sensor : sensors) {
          var range = manhattanDistance(sensor.location, sensor.closestBeacon);
          var distance = manhattanDistance(new Point(x, y), sensor.location);
          if (distance <= range) {

            int xToSkip;
            if (x >= sensor.location.x) {
              xToSkip = range - distance;
            } else {
              xToSkip = ((sensor.location.x - x) * 2) + Math.abs(range - distance);
            }
            inRangeOfAtLeastOneSensor = true;
            x += xToSkip;
            break;
          }
        }
        if (!inRangeOfAtLeastOneSensor) {
          long tuningFrequency = (x * 4000000L) + y;
          System.out.println("Part 2 Answer: " + tuningFrequency);
          return;
        }
      }
    }


  }

  private static int manhattanDistance(Point p1, Point p2) {
    return Math.abs(p1.x() - p2.x()) + Math.abs(p1.y() - p2.y());
  }

  private static Set<Point> noBeaconZone(Sensor sensor, int yToKeep) {
    int distance = manhattanDistance(sensor.location(), sensor.closestBeacon());

    var noBeaconPoints = new HashSet<Point>();

    for (int i = 0; i < distance; i++) {
      var y1 = sensor.location().y() + i;
      var y2 = sensor.location().y() - i;
      if (y1 != yToKeep && y2 != yToKeep) {
          continue;
      }
      for (int x = sensor.location().x() - distance + i; x < sensor.location().x() + distance - i + 1; x++) {
        if (y1 == yToKeep) {
          noBeaconPoints.add(new Point(x, y1));
        }
        if (y2 == yToKeep) {
          noBeaconPoints.add(new Point(x, y2));
        }
      }
    }

    noBeaconPoints.remove(sensor.closestBeacon());
    return noBeaconPoints;
  }

  private static List<Sensor> parseSensors(String fileName) throws Exception {

    try (var lines = Files.lines(Paths.get(Solution.class.getResource(fileName).toURI()))) {
      var regex = Pattern.compile("Sensor at x=(?<sensorX>[-0-9]+), y=(?<sensorY>[-0-9]+): closest beacon is at x=(?<beaconX>[-0-9]+), y=(?<beaconY>[-0-9]+)");

      return lines.map(line -> {
        var matcher = regex.matcher(line);
        boolean ignored = matcher.find();
        return new Sensor(
            new Point(Integer.parseInt(matcher.group("sensorX")), Integer.parseInt(matcher.group("sensorY"))),
            new Point(Integer.parseInt(matcher.group("beaconX")), Integer.parseInt(matcher.group("beaconY")))
        );
      }).toList();
    }
  }
}
