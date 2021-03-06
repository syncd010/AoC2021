(ns aoc2021.day2
  (:require [clojure.string :refer [split]]))

(defn convert
  [day-input]
  (map #(split % #" ") day-input))

(defn move-part-one
  [pos move]
  (let [steps (Integer/parseInt (get move 1))]
    (case (first move)
      "forward" [(+ (first pos) steps) (second pos)]
      "down" [(first pos) (+ (second pos) steps)]
      "up" [(first pos) (- (second pos) steps)])))

(defn solve-part-one [input]
  (let [pos (reduce move-part-one [0 0] input)]
    (* (first pos) (second pos))))

(defn move-part-two
  [pos move]
  (let [steps (Integer/parseInt (second move))]
    (case (move 0)
      "forward" [(+ (pos 0) steps) (+ (pos 1) (* (pos 2) steps)) (pos 2)]
      "down"  [(pos 0) (pos 1) (+ (pos 2) steps)]
      "up"  [(pos 0) (pos 1) (- (pos 2) steps)])))

(defn solve-part-two [input]
  (let [pos (reduce move-part-two [0 0 0] input)]
    (* (first pos) (second pos))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn solve [raw-input]
  (let [input (convert raw-input)
        part-one (solve-part-one input)
        part-two (solve-part-two input)]
    [part-one part-two]))
