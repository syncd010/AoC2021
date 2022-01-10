(ns aoc2021.day22
  (:require [clojure.string :as str])
  (:require [clojure.math.combinatorics :as combo]))

(defn make-cube [action x y z]
  {:action action :x x :y y :z z})

(defn convert [raw-input]
  (map (fn [line]
         (let [vals (rest (re-find #"(on|off) x=(-?\d+)..(-?\d+),y=(-?\d+)..(-?\d+),z=(-?\d+)..(-?\d+)" line))
               parsed (map #(Integer/parseInt %) (rest vals))]
           (make-cube (first vals) 
                      [(nth parsed 0), (nth parsed 1)] 
                      [(nth parsed 2), (nth parsed 3)]
                      [(nth parsed 4), (nth parsed 5)])))
       raw-input))

(comment
  "Change the file to load it in the REPL"
  #_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
  (def raw-input (str/split-lines (str/trim (slurp "resources/input22Test"))))
  )

(defn partition-ranges 
  "Returns target-range, partitioned where it overlaps with base-range.
   If there's no overlap, an empty vector is returned.
   The last element of the result is always the overlap of both ranges (so
   it can be later removed, if one desires only the difference of the ranges)."
  [target-range base-range]
  (remove #(> (first %) (second %))
          (cond
            ;; No overlap: B----B...T----T | T----T...B----B
            (or (< (second base-range) (first target-range))
                (> (first base-range) (second target-range)))
            []
            ;; B----T----T----B
            (and (>= (first target-range) (first base-range))
                 (<= (second target-range) (second base-range)))
            [target-range]
            ;; T----B----B----T
            (and (<= (first target-range) (first base-range))
                 (>= (second target-range) (second base-range)))
            [[(first target-range) (dec (first base-range))] [(inc (second base-range)) (second target-range)] base-range]
            ;; T----B----T----B
            (and (<= (first target-range) (first base-range))
                 (>= (second target-range) (first base-range))
                 (<= (second target-range) (second base-range)))
            [[(first target-range) (dec (first base-range))] [(first base-range) (second target-range)]]
            ;; B----T----B----T
            (and (>= (second target-range) (second base-range))
                 (>= (first target-range) (first base-range))
                 (<= (first target-range) (second base-range)))
            [[(inc (second base-range)) (second target-range)] [(first target-range) (second base-range)]])))

(defn cube-difference 
  "Returns target-cube without base-cube points."
  [target-cube base-cube]
  (let [x-partition (partition-ranges (:x target-cube) (:x base-cube))
        y-partition (partition-ranges (:y target-cube) (:y base-cube))
        z-partition (partition-ranges (:z target-cube) (:z base-cube))]
    (if (some empty? [x-partition y-partition z-partition]) ;; No overlap
      [target-cube]
      (let [cartesian (combo/cartesian-product x-partition y-partition z-partition)
            ;; Remove the overlap with base-cube, which is given in the last position
            ;; of the partitions
            diff (remove #(= % [(last x-partition) (last y-partition) (last z-partition)]) cartesian)]
        (map (fn [[x y z]] (make-cube "on" x y z)) diff)))))

(defn partition-and-add 
  "Partition each cube in partitioned by the new cube. Add the new cube if it is 'on'"
  [partitioned cube]
  (loop [parted [] unparted partitioned]
    (if (= 0 (count unparted))
      (if (= "on" (:action cube))
        (conj parted cube)
        parted)
      (recur (reduce conj parted (cube-difference (peek unparted) cube)) (pop unparted)))))

(defn cube-volume [cube]
  (* (inc (apply - (reverse (:x cube)))) (inc (apply - (reverse (:y cube)))) (inc (apply - (reverse (:z cube))))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn solve [raw-input]
  (let [input (convert raw-input)
        input-filtered (filter (fn [c] (some #(and (>= % -50) (<= % 50)) (flatten [(:x c) (:y c) (:z c)]))) input)
        part-one-cubes (reduce partition-and-add [(first input-filtered)] (rest input-filtered))
        part-one (reduce + (map cube-volume part-one-cubes))
        part-two-cubes (reduce partition-and-add [(first input)] (rest input))
        part-two (reduce + (map cube-volume part-two-cubes))]
    [part-one part-two]))
