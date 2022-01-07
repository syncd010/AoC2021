(ns aoc2021.day13
  (:require [clojure.string :as str])
  (:require [aoc2021.common :refer [parse-int vec2D->1D str-partition]]))

(defn parse-fold-line [line]
  (let [fld (str/split (str/replace line "fold along " "") #"=")]
    [(first fld) (parse-int (second fld))]))

(defn convert [raw-input]
  (let [pos-lines (filter #(and (> (count %) 0) (not (str/starts-with? % "fold"))) raw-input)
        fold-lines (filter #(str/starts-with? % "fold") raw-input)]
    [(map (fn [line] (mapv #(parse-int %) (str/split line #","))) pos-lines)
     (map (fn [line] (parse-fold-line line)) fold-lines)]))

(defn translate [[x y] [axis v]]
  (let [discriminant (if (= axis "x") x y)
        pos-translated (if (= axis "x") [(- (* 2 v) x) y] [x (- (* 2 v) y)])]
    (cond
      (> discriminant v) pos-translated
      (< discriminant v) [x y]
      :else nil)))

(defn fold-paper [markings fold]
    (remove nil? (map #(translate % fold) markings)))

(defn repr [markings]
  (let [width (+ 1 (apply max (map first markings)))
        board (reduce
               (fn [s pos] (str/join [s (str/join (repeat (- pos (count s)) ".")) "#"]))
               "" (sort (map #(vec2D->1D width %) markings)))]
    (str/join "\n" (str-partition width board))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn solve [raw-input]
  (let [[markings folds] (convert raw-input)
        part-one (count (distinct (fold-paper markings (first folds))))
        code-marks (reduce #(distinct (fold-paper %1 %2)) markings folds)
        part-two (str "\n" (repr code-marks))]
    [part-one part-two]))
