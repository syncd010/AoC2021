(ns aoc2021.day10
  (:require [aoc2021.common :refer [median]]))

(defn convert [raw-input]
  raw-input)

(def openers (set "([{<"))
(def closers (set ")]}>"))
(def match (zipmap (into (seq openers) (seq closers)) (into (seq closers) (seq openers))))

(defn parse-line
  ([s] (parse-line s '()))
  ([s stack]
   (cond
     (= 0 (count s)) (list nil stack)
     (contains? closers (first s)) (if (not= (first s) (match (first stack)))
                                     (list (first s) stack)
                                     (parse-line (rest s) (rest stack)))
     :else (parse-line (rest s) (conj stack (first s))))))

(def points-part-one (zipmap closers [3 57 1197 25137]))

(defn complete [seq]
  (map match seq))

(def points-part-two (zipmap closers [1 2 3 4]))

(defn score [seq]
  (reduce (fn [acc c] (+ (* acc 5) (points-part-two c))) 0 seq))
  
#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn solve [raw-input]
  (let [input (map seq (convert raw-input))
        part-one (reduce + (map points-part-one (remove nil? (map first (map parse-line input)))))
        part-two (median (sort (map (comp score complete second) (filter #(nil? (first %)) (map parse-line input)))))]
    [part-one part-two]))
