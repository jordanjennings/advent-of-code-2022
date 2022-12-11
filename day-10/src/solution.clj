(ns solution
  (:require
    [clojure.java.io :as io]
    [clojure.string :as str]))

(def lines (str/split-lines (slurp (io/resource "input.txt"))))

(defn noop [register] [register])
(defn addx [register increment]
  [register (+ register increment)])

(defn process-line [line register]
  (let [split (str/split line #" ")]
    (case (first split)
      "noop" (noop register)
      "addx" (addx register (Long/parseLong (nth split 1))))))

(def ticks
  (reduce
    (fn [accum line] (concat accum (process-line line (last accum))))
    [1]
    lines))

(def samples
  (keep-indexed
    (fn [index item]
      (if
        (= (mod (- (inc index) 20) 40) 0)
        (* (inc index) item)
        nil))
    ticks))

(println "Part 1 Answer: " (reduce + samples))

(def screen
  (partition
    40
    (map-indexed
      (fn [index register]
        (let [pixel (mod index 40)]
          (if (<= (dec pixel) register (inc pixel)) "#" ".")))
      ticks)))

(println "Part 2 Answer:")
(doseq [line screen] (println (apply str line)))
