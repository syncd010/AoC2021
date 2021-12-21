(ns aoc2021.day8
  (:require [clojure.string :as str])
  (:require [clojure.set :as set])
  (:require [aoc2021.common :refer [parse-int sum]]))

(defn convert [raw-input]
  (map (fn [line]
         (map (fn [side]
                (map (comp str/join sort) (str/split (str/trim side) #" ")))
              (str/split line #"\|")))
       raw-input))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn solve-part-one [raw-input]
  (let [input (convert raw-input)
        counts (map #(count %) (flatten (map second input)))
        relevant (filter #(or (= % 7) (= % 4) (= % 2) (= % 3)) counts)]
    (count relevant)))

(defn str-difference
  "Return a string that is the first string without characters of the second"
  [s1 s2]
  (str/join "" (set/difference (set (seq s1)) (set (seq s2)))))

(defn find-example
  "Finds the first example that conforms to the spec in mask"
  [mask examples]
  (first (filter #(and (= (count %) (:len mask)) ((:filter-fn mask) %)) examples)))

(defn build-digits-map
  "Build a map that decodes the digits in example"
  [examples]
  (let [base-digits-map {1 (first (filter #(= (count %) 2) examples))
                         4 (first (filter #(= (count %) 4) examples))
                         7 (first (filter #(= (count %) 3) examples))
                         8 (first (filter #(= (count %) 7) examples))}
        diff-4-and-1 (str-difference (base-digits-map 4) (base-digits-map 1))
        masks {3 {:len 5 :filter-fn (fn [s] (= "" (str-difference (base-digits-map 7) s)))}
               5 {:len 5 :filter-fn (fn [s] (= "" (str-difference diff-4-and-1 s)))}
               2 {:len 5 :filter-fn (fn [s] (and (not= "" (str-difference (base-digits-map 7) s))
                                          (not= "" (str-difference diff-4-and-1 s))))}
               6 {:len 6 :filter-fn (fn [s] (not= "" (str-difference (base-digits-map 1) s)))}
               9 {:len 6 :filter-fn (fn [s] (= "" (str-difference (base-digits-map 4) s)))}
               0 {:len 6 :filter-fn (fn [s] (not= "" (str-difference diff-4-and-1 s)))}}]
    (into base-digits-map
          (map (fn [[k mask]] [k (find-example mask examples)]) masks))))

(defn decode-output-digits [line]
  (let [digits-map (set/map-invert (build-digits-map (flatten line)))
        output-digits (map #(digits-map %) (second line))]
    (parse-int (str/join output-digits))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn solve-part-two [raw-input]
  (let [input (convert raw-input)]
    (sum (map decode-output-digits input))))
