(ns aoc2021.day3
  (:require [clojure.string :refer [split join]]))

(defn convert-line
  [line]
  (map #(Integer/parseInt %) (split line #"")))

(defn convert
  [raw-input]
  (map convert-line raw-input))

(defn count-ones
  [matrix]
  (reduce #(map + %1 %2) matrix))

(defn calc-gamma
  [matrix]
  (let [len (count matrix)]
    (map #(if (>= % (- len %)) 1 0 ) (count-ones matrix))))

(defn negate
  "Negates the list, assuming it is composed of 0s and 1s"
  [lst]
  (map #(if (== % 0) 1 0) lst))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn solve-part-one [raw-input]
  (let [input (convert raw-input)
        gamma (calc-gamma input)
        epsilon (negate gamma)
        gamma-dec (Integer/parseInt (join gamma) 2)
        epsilon-dec (Integer/parseInt (join epsilon) 2)]
    (* gamma-dec epsilon-dec)))

(defn filter-on-pos
  [pos lst calc-fn]
  (let [res (calc-fn lst)
       new-lst (filter #(== (nth % pos) (nth res pos)) lst)]
    (cond
      (== (count new-lst) 1) new-lst
      (== (+ pos 1) (count (first lst))) new-lst
      :else (recur (+ pos 1) new-lst calc-fn))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn solve-part-two [raw-input]
  (let [input (convert raw-input)
        o2-rating (first (filter-on-pos 0 input calc-gamma))
        co2-rating (first (filter-on-pos 0 input #(negate (calc-gamma %))))
        o2-rating-dec (Integer/parseInt (join (seq o2-rating)) 2)
        co2-rating-dec (Integer/parseInt (join (seq co2-rating)) 2)]
    (* o2-rating-dec co2-rating-dec)))
