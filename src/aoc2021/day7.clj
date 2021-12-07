(ns aoc2021.day7
  (:require [clojure.string :refer [split trim]])
  (:require [aoc2021.common :refer [parse-int median mean sum]]))

(defn convert [raw-input]
  (mapv parse-int (split (first raw-input) #",")))

(defn solve-part-one [raw-input]
  (let [input (convert raw-input)
        ;; The mean minimizes the sum of absolute differences (l1-norm in 1D)
        min-pos (median input)]
    (sum (map #(Math/abs (- % min-pos)) input))))

(defn triangular-number
  "Returns the nth triangular number: n * (n + 1) / 2"
  [n]
  (/ (* n (+ n 1)) 2))

(defn cost-to
  "Return the cost of moving all points to pos"
  [pos points]
  (sum (map #(triangular-number (Math/abs (- pos %))) points)))

(defn find-min-cost-towards
  "Find the minimum cost in a given direction (+1 or -1). Returns the min cost"
  [from-pos dir points]
  (let [this-cost (cost-to from-pos points)
        next-cost (cost-to (+ from-pos dir) points)]
    (if (> next-cost this-cost)
      this-cost
      (recur (+ from-pos dir) dir points))))

(defn find-min-cost
  "Finds the minimum cost for all positions.
  Assumes points is sorted, otherwise it won't work"
  [points]
  (let [start-pos (int (mean points)) ;; The mean is the answer
        left-costs (find-min-cost-towards start-pos -1 points)
        right-costs (find-min-cost-towards start-pos 1 points)]
    (min left-costs right-costs)))

(defn solve-part-two [raw-input]
  (let [input (convert raw-input)]
    (find-min-cost (sort input))))
