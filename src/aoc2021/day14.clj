(ns aoc2021.day14
  (:require [clojure.string :as str]))

(defn convert [raw-input]
  (let [template (first raw-input)
        rules (into {} (map #(str/split % #" -> ") (rest (rest raw-input))))]
  [rules template]))

(comment
  "Change the file to load it in the REPL"
  (def raw-input (str/split-lines (str/trim (slurp "resources/input14Test"))))
  )

(defn expand-once [rules template]
  (loop [expanded (subs template 0 1) to-expand template]
    (if (< (count to-expand) 2)
      (str expanded)
      (let [expansion (str/join [(rules (subs to-expand 0 2)) (second to-expand)])]
        (recur (str expanded expansion) (subs to-expand 1))))))

(defn expand-n [rules template n]
  (if (= n 0) 
    template
    (recur rules (expand-once rules template) (dec n))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn solve-part-one [raw-input]
  (let [[rules template] (convert raw-input)
        expansion (expand-n rules template 10)
        counts (map second (frequencies expansion))]
    (- (apply max counts) (apply min counts))))

(defn count-pairs [rules template n]
  (let [pairs-count (frequencies (map str/join (partition 2 1 template)))
        rules-expanded (into {} (map (fn [[k v]] [k [(str (first k) v) (str v (second k))]]) rules))]
    (loop [pairs-count pairs-count n n]
      (if (= n 0)
        pairs-count
        (let [new-pairs-count
              (reduce
               (fn [m [pair c]]
                 (let [expanded-pairs (map (fn [new-pair] [new-pair (+ c (get m new-pair 0))]) (rules-expanded pair))]
                   (into m expanded-pairs)))
                   {} pairs-count)]
          (recur new-pairs-count (dec n)))))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn solve-part-two [raw-input]
  (let [[rules template] (convert raw-input)
        pairs-count (count-pairs rules template 40)
        letters-count (map second (reduce (fn [m [pair c]] (assoc m (second pair) (+ c (get m (second pair) 0)))) {(first template) 1} pairs-count))]
    (- (apply max letters-count) (apply min letters-count))))
