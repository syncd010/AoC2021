(ns aoc2021.day6
  (:require [clojure.string :refer [split]])
  (:require [aoc2021.common :refer [parse-int sum]]))

(def part-one-days 80)
(def part-two-days 256)

(defn convert [raw-input]
  (mapv parse-int (split (first raw-input) #",")))

(defn evolve-gens
  "Straigthforwad simulation of evolving the generations"
  [n gens]
  (if (= n 0)
    gens
    (let [gens-dec (mapv dec gens)
          new-gen-sz (count (filter #(= % -1) gens-dec))]
      (recur (dec n) (into (replace {-1 6} gens-dec) (repeat new-gen-sz 8))))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn solve-part-one [raw-input]
  (let [input (convert raw-input)]
  (count (evolve-gens part-one-days input))))

(defn evolve-age-freqs
  "Evolve generations which are grouped in age groups"
  [n age-freqs]
  (if (= n 0)
    age-freqs
    (let [next-age-freqs (conj (subvec age-freqs 1) (first age-freqs))]
      (recur (dec n) (assoc next-age-freqs 6 (+ (get age-freqs 0) (get age-freqs 7)))))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn solve-part-two [raw-input]
  (let [input (convert raw-input)
        age-freqs (vals (into (sorted-map)
                              (merge (zipmap (range 9) (repeat 0))
                                     (frequencies input))))]
    (sum (evolve-age-freqs part-two-days (vec age-freqs)))))
