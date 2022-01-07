(ns aoc2021.day5
  (:require [clojure.string :as str]))

(defn convert-line [line]
  (let [line-split (str/split line #" -> ")]
    (mapv (fn [p] (mapv #(Integer/parseInt %) (str/split p #","))) line-split)))

(defn convert [raw-input]
  (map convert-line raw-input))

(defn calc-step [from to]
  (cond (== from to) 0
        (> from to) -1
        (< from to) 1))

(defn fill-line
  "Fills a line from the first to the second element in line-spec. Assumes that the line is strictly horizontal, vertical or diagonal."
  [[from to]]
  (let [steps (inc (Math/max (Math/abs (- (first from) (first to))) (Math/abs (- (second from) (second to)))))
        x-step (calc-step (first from) (first to))
        y-step (calc-step (second from) (second to))]
    (map (fn [i] [(+ (first from) (* i x-step)) (+ (second from) (* i y-step))]) (range steps))))

(defn straight-line?
  "Whether the line specified is strictly straight"
  [[from to]]
  (or (== (first from) (first to)) (== (second from) (second to))))
  
(defn solve-part-one [input]
  (let [straight-lines (map fill-line (filter straight-line? input))
        pos-frequencies (frequencies (apply concat straight-lines))]
    (count (filter #(> % 1) (vals pos-frequencies)))))

(defn solve-part-two [input]
  (let [lines (map fill-line input)
        pos-frequencies (frequencies (apply concat lines))]
    (count (filter #(> % 1) (vals pos-frequencies)))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn solve [raw-input]
  (let [input (convert raw-input)
        part-one (solve-part-one input)
        part-two (solve-part-two input)]
    [part-one part-two]))
