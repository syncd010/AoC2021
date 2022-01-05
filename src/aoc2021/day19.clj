(ns aoc2021.day19
  (:require [clojure.string :as str])
  (:require [clojure.set :refer [intersection]])
  (:require [clojure.math.combinatorics :as combo])
  (:require [aoc2021.common :refer [dot rotation-matrix m-dist]]))

(def min-beacons-count 12)

(defn convert [raw-input]
  (loop [scanners [] current nil remaining raw-input]
    (cond
      (empty? remaining) (conj scanners current)
      (empty? (str/trim (first remaining))) (recur scanners current (rest remaining))
      (str/starts-with? (first remaining) "--- scanner") (let [new {:id (if (nil? current) 0 (inc (:id current)))}
                                                               new-scanners (if (nil? current) scanners (conj scanners current))]
                                                           (recur new-scanners new (rest remaining)))
      :else (recur scanners
                   (assoc current :beacons (conj (:beacons current) (map #(Integer/parseInt %) (str/split (first remaining) #","))))
                   (rest remaining)))))

(comment
  "Change the file to load it in the REPL"
  #_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
  (def raw-input (str/split-lines (str/trim (slurp "resources/input19Test")))))

(def rotations
  "All rotation matrices"
  (set (map #(apply rotation-matrix %)
            (for [i (range 4) j (range 4) k (range 4)]
              [(* (/ Math/PI 2) i) (* (/ Math/PI 2) j) (* (/ Math/PI 2) k)]))))

(defn rotate 
  "Rotates all the beacons with the specified rotation"
  [beacons rot]
  (map #(dot rot %) beacons))

(defn diff-from-base 
  "Returns all the beacons in beacons diffed with base"
  [beacons base]
  (map #(mapv - base %) beacons))

(defn overlap-beacons-rotated 
  "Tries all rotations and checks if the beacons overlap with any of them"
  [base-beacons target-beacons]
  (->> rotations
       (filter (fn [rot] (>= (count (intersection (set base-beacons) (set (rotate target-beacons rot))))
                             min-beacons-count)))
       (first)))

(defn abs-values [beacons]
  (map (fn [b] (reduce + (map #(Math/abs %) b))) beacons))

(defn can-overlap? 
  "Quick and dirty test to check if the beacons can even be overlapped. Doesn't guarantee
   that they do, this is just a quick check that it is possible."
  [base-beacons-set target-beacons-set]
  (>= (count (intersection base-beacons-set target-beacons-set))
      min-beacons-count))

(defn overlap-beacons 
  "Tries to overlap beacons over the base beacons diffed"
  [base-beacons beacons]
  (let [base-beacons-set (set (abs-values base-beacons))]
    (->> beacons
         (map (fn [b] (let [target-beacons (diff-from-base beacons b)
                            target-beacons-set (set (abs-values target-beacons))]
                        ;; Hack: fail fast by checking if it is even possible to overlap
                        (when (can-overlap? base-beacons-set target-beacons-set)
                          (if-let [rot (overlap-beacons-rotated base-beacons target-beacons)]
                            {:target-beacon b :rotation rot}
                            nil)))))
         (remove nil?)
         (first))))

(defn overlap-scanners 
  "Tries to overlap the beacons of the scanners and, if successful, returns the transformation that 
   manages to overlap them"
  [s1 s2]
  ;; Hack! we only need to overlap on one beacon, so droping 11 here helps the runtime,
  ;; as in the common case where they don't overlap, we need to check 11 less
  ;; (->> (:beacons s1)
  (->> (drop (dec min-beacons-count) (:beacons s1))
       (map (fn [b] (if-let [res (overlap-beacons (diff-from-base (:beacons s1) b) (:beacons s2))]
                      {:base-scanner-id (:id s1)
                       :base-beacon b
                       :target-scanner-id (:id s2)
                       :target-beacon (:target-beacon res)
                       :rotation (:rotation res)
                       :location (map - b (dot (:rotation res) (:target-beacon res)))}
                      nil)))
       (remove nil?)
       (first)))

(defn all-overlapped-scanners 
  "Tries to overlap the scanners, one by one, and returns the transform that allows the 
   overlaping, relative to the first scanner"
  [scanners]
  (loop [base (first scanners) 
         discovered [] 
         undiscovered (rest scanners) 
         transforms {0 {:rotation (rotation-matrix 0 0 0) :location [0 0 0]}}]
    (if (empty? undiscovered)
      transforms
      ;; Hack: we're using pmap here to speed up the process. Make sure to call (shutdown-agents)
      (let [overlapped (remove nil? (pmap #(overlap-scanners base %) undiscovered))
            overlapped-id (set (map :target-scanner-id overlapped))
            next-discovered (apply conj discovered (filter #(contains? overlapped-id (:id %)) undiscovered))
            next-undiscovered (remove #(contains? overlapped-id (:id %)) undiscovered)
            next-transforms (into transforms
                                    (map (fn [o]
                                           (let [base-transform (get transforms (:base-scanner-id o))]
                                             [(:target-scanner-id o)
                                              {:rotation (dot (:rotation base-transform) (:rotation o))
                                               :location (mapv + (:location base-transform) (dot (:rotation base-transform) (:location o)))}]))
                                         overlapped))]
        (recur (first next-discovered)
               (rest next-discovered)
               next-undiscovered
               next-transforms)))))

(defn correct-beacon-location 
  "Corrects the beacons locations of one scanner based on the given transformation"
  [scanner transforms]
  (let [params (get transforms (:id scanner))]
    (map (fn [b] (mapv + (:location params) (dot (:rotation params) b))) (:beacons scanner))))

(def scanners-transforms (atom {}))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn solve-part-one [raw-input]
  (let [input (convert raw-input)
        transforms (all-overlapped-scanners input)
        corrected-beacons (mapcat #(correct-beacon-location % transforms) input)]
    (swap! scanners-transforms into transforms) ;; Reuse for part two
    (shutdown-agents) ;; Because of pmap
    (count (set corrected-beacons))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn solve-part-two [raw-input]
  (let [locations (map :location (vals @scanners-transforms))]
    (apply max (map #(m-dist (first %) (second %)) (combo/combinations locations 2)) )))
