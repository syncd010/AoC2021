(ns aoc2021.day11
  (:require [clojure.string :as str])
  (:require [aoc2021.common :refer [parse-int sum]]))

(defn convert [raw-input]
  (let [parsed (mapv (fn [line] (mapv #(parse-int %) (str/split line #""))) raw-input)]
    {:width (count (first parsed))
     :height (count parsed)
     :values (flatten parsed)}))

(defn neighbors-idx
  "Indexes of the neighbors"
  [idx width height]
  (let [row (quot idx width)
        col (rem idx width)
        potential-neighbors (for [i (range -1 2) j (range -1 2)] [(+ row i) (+ col j)])
        filtered-neighbors (filter
                            (fn [[r c]] (and (or (not= r row) (not= c col)) (>= r 0) (< r width) (>= c 0) (< c height)))
                            potential-neighbors)]
    (map (fn [[r c]] (+ (* r width) c)) filtered-neighbors)))

(defn update-board [board flashed neighbors]
  (assoc board :values
         (keep-indexed (fn [i val]
                         (cond
                           (some #{i} flashed) 0
                           (and (some #{i} neighbors) (not= val 0)) (+ val (count (filter #{i} neighbors)))
                           :else val)) (board :values))))

(defn flash
  [board flashed-acc]
  (let [flashed (keep-indexed (fn [i v] (when (> v 9) i)) (board :values))
        neighbors (flatten (map #(neighbors-idx % (board :width) (board :height)) flashed))]
    (if (empty? flashed)
      [flashed-acc board]
      (recur (update-board board flashed neighbors) (+ flashed-acc (count flashed))))))

(defn step [board]
   (flash (assoc board :values (map inc (board :values))) 0))

(defn solve-part-one [input]
  (loop [n 100 flashed-acc 0 board input]
    (if (= n 0)
      flashed-acc
      (let [res (step board)]
        (recur (dec n) (+ flashed-acc (first res)) (second res))))))

(defn solve-part-two [input]
  (loop [n 0 flashed-acc 0 board input]
    (if (= (sum (board :values)) 0)
      n
      (let [res (step board)]
        (recur (inc n) (+ flashed-acc (first res)) (second res))))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn solve [raw-input]
  (let [input (convert raw-input)
        part-one (solve-part-one input)
        part-two (solve-part-two input)]
    [part-one part-two]))
