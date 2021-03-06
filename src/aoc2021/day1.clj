(ns aoc2021.day1
  (:require [aoc2021.common :refer [zip]]))

(defn convert [day-input]
  (map #(Integer/parseInt %) day-input))

(defn count-increasing-in-seq [lst]
 (let [pairs (zip (drop-last 1 lst) (drop 1 lst))]
  (count (filter #(> (second %) (first %)) pairs))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn solve [day-input]
  (let [input (convert day-input)
        part-one (count-increasing-in-seq input)
        input-grouped  (zip (drop-last 2 input) (drop 1 (drop-last 1 input)) (drop 2 input))
        input-summed  (map #(reduce + %) input-grouped)
        part-two (count-increasing-in-seq input-summed)]
    [part-one part-two]))
