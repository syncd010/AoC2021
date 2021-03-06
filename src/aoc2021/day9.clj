(ns aoc2021.day9
  (:require [clojure.string :as str])
  (:require [aoc2021.common :refer [m-get]]))

(defn convert [raw-input]
  (mapv (fn [line] (mapv #(Integer/parseInt %) (str/split line #""))) raw-input))

(defn neighbors-idx
  "Indexes of the neighbors"
  [row col]
  [[(dec row) col] [(inc row) col] [row (dec col)] [row (inc col)]])

(defn neighbors
  "Values of the neighbors"
  [matrix row col]
  (remove nil? (map (fn [[r c]] (m-get matrix r c)) (neighbors-idx row col))))
  
(defn is-low-point?
  "Is this a low point"
  [matrix row col]
  (< (m-get matrix row col) (apply min (neighbors matrix row col))))

(defn flood-basin
  "A kind of DFS to get all the low points. Note that flooded should be a set,
  as contains? is used to check membership"
  [matrix from flooded]
  (let [row (first from)
        col (second from)
        value (m-get matrix row col)]
    (if (or (= value nil) (= value 9) (contains? flooded from))
      flooded
      (reduce #(flood-basin matrix %2 %1) (conj flooded from) (neighbors-idx row col)))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn solve [raw-input]
  (let [input (convert raw-input)
        width (count (first input))
        heigth (count input)
        low-points (remove nil? (for [row (range heigth) col (range width)]
                                  (when (is-low-point? input row col) [row col])))
        part-one (reduce + (map (fn [[r c]] (inc (m-get input r c))) low-points))
        basins (map #(flood-basin input % #{}) low-points)
        part-two (apply * (take 3 (sort > (map count basins))))]
    [part-one part-two]))
