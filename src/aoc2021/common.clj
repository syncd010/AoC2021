(ns aoc2021.common
  (:require [clojure.string :as str]))

(defmacro timed
  "Evaluates expr and the time it took.  Returns the a pair of time and value of expr."
  [expr]
  `(let [start# (. System (nanoTime))
         ret# ~expr]
     [(/ (double (- (. System (nanoTime)) start#)) 1000000.0) ret#]))

(defn zip
  "Returns a lazy sequence of lists with interleaved elements from the input collections"
  [& colls]
  (partition (count colls) (apply interleave colls)))

(defn min-index 
  "Minimum index of the collection"
  [coll]
  (first (apply min-key second (map-indexed vector coll))))

(defn max-index
  "Maximum index of the collection"
  [coll]
  (first (apply max-key second (map-indexed vector coll))))

(defn is-lower-case?
  "Whether the input string is all in lower case"
  [s]
  (= (str/lower-case s) s))

(defn has-duplicates? [xs]
  (not= (count (distinct xs)) (count xs)))

(defn transpose
  "Transpose a matrix"
  [m]
  (apply mapv vector m))

(defn dot
  "Poor man's dot product"
  [m1 m2]
  (cond
    (and (vector? (first m1)) (vector? (first m2))) (transpose (mapv #(dot m1 %) (transpose m2))) ;; Both matrixes
    (vector? (first m1)) (mapv #(dot % m2) m1) ;; m2 is guaranteed vector
    (vector? (first m2)) (mapv #(dot % m1) (transpose m2)) ;; m1 is guaranteed vector
    :else (reduce + (map * m1 m2)))) ;; m1 and m2 are vectors

(defn parse-int
  "Helper wrapper for Integer/parseInt"
  ([n] (parse-int n 10))
  ([n base]
   (Integer/parseInt n base)))

(defn m-dist
  "Manhattan distance"
  ([to] (m-dist (repeat (count to) 0) to))
  ([from to] 
   (reduce + (map #(Math/abs %) (map - to from)))))

(defn sum
  "Sums a sequence"
  [seq]
  (reduce + seq))

(defn mean
  "Mean of a sequence"
  [coll]
  (let [sz (count coll)]
    (if (pos? sz) (/ (reduce + coll) sz) 0)))

(defn median
  "Median of a sequence"
  [coll]
  (let [sorted (sort coll)
        sz (count sorted)
        halfway (quot sz 2)]
    (if (odd? sz)
      (nth sorted halfway)
      (let [bottom-val (nth sorted (dec halfway))
            top-val (nth sorted halfway)]
        (/ (+ bottom-val top-val) 2)))))

(defn coll-contains?
  "Returns whether the collection contains the given value"
  [coll value]
  (some #(= % value) coll))

(defn call
  "Allows to call a function by name"
  [fn & args]
  (apply (resolve (symbol fn)) args))

(defn vec2D->1D
  "Transforms a 2D position into 1D"
  [width [x y]]
  (+ x (* y width)))

(defn vec1D->2D
  "Transforms 1D position into 2D"
  [width x]
  [(rem x width) (quot x width)])

(defn str-partition
  "Partitions a string into a vector of n characters"
  [n s]
  (re-seq (re-pattern (str ".{1," n "}")) s))

(defn quad-formula
  "Quadratic formula"
  [a b c]
  (let [disc (- (* b b) (* 4 a c))]
    (cond
      (< disc 0) []
      (= disc 0) [(/ (- b) (* 2 a))]
      (> disc 0) [(/ (- (- b) (Math/sqrt disc)) (* 2 a)) (/ (+ (- b) (Math/sqrt disc)) (* 2 a))])))

(defn rotation-matrix 
  "Returns the rotation matrix in 3D for the angles around z, y and x axes respectively
   (yaw, pitch and roll angles)"
  [alpha beta gamma]
  (let [cos-a (Math/cos alpha)
        sin-a (Math/sin alpha)
        cos-b (Math/cos beta)
        sin-b (Math/sin beta)
        cos-g (Math/cos gamma)
        sin-g (Math/sin gamma)]
    [(mapv int [(* cos-a cos-b) (- (* cos-a sin-b sin-g) (* sin-a cos-g)) (+ (* cos-a sin-b cos-g) (* sin-a sin-g))])
     (mapv int [(* sin-a cos-b) (+ (* sin-a sin-b sin-g) (* cos-a cos-g)) (- (* sin-a sin-b cos-g) (* cos-a sin-g))])
     (mapv int [(- sin-b) (* cos-b sin-g) (* cos-b cos-g)])]))

