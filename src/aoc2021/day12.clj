(ns aoc2021.day12
  (:require [clojure.string :as str])
  (:require [aoc2021.common :refer [has-duplicates? is-lower-case? coll-contains?]]))

(defn convert
  "Returns a map with the forward and the reverse mappings"
  [raw-input]
  (let [two-way-map (reduce
                     (fn [m line]
                       (let [[orig dest] (str/split line #"-")]
                         (assoc m
                                orig (if (contains? m orig) (conj (m orig) dest) [dest])
                                dest (if (contains? m dest) (conj (m dest) orig) [orig]))))
                     {} raw-input)]
    (into {} (map (fn [[k vs]] (when (not= k "end") [k (filter #(not= % "start") vs)])) two-way-map))))

(defn explore [successors is-complete? from-node]
  (loop [to-explore (list [from-node]) explored-count 0]
    (if (empty? to-explore)
      explored-count
      (let [current-path (first to-explore)
            succ-paths (map #(conj current-path %) (successors current-path))
            complete-paths (filter is-complete? succ-paths)
            incomplete-paths (remove is-complete? succ-paths)]
        (recur (concat incomplete-paths (rest to-explore)) (+ explored-count (count complete-paths)))))))

(defn is-complete? [path]
  (= "end" (last path)))

(defn successors-fn-part-one [cave-map]
  (fn [path]
    (filter
     #(or (not (is-lower-case? %))
          (not (coll-contains? path %)))
     (cave-map (last path)))))

(defn solve-part-one [raw-input]
  (let [input (convert raw-input)]
    (explore (successors-fn-part-one input) is-complete? "start")))

(defn successors-fn-part-two [cave-map]
  (fn [path]
    (filter
     #(or (not (is-lower-case? %))
          (not (coll-contains? path %))
          (not (has-duplicates? (filter is-lower-case? path))))
     (cave-map (last path)))))

(defn solve-part-two [raw-input]
  (let [input (convert raw-input)]
    (explore (successors-fn-part-two input) is-complete? "start")))
