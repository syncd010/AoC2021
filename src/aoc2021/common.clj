(ns aoc2021.common
  (:require [clojure.string :as str]))

(defn zip
  "Returns a lazy sequence of lists with interleaved elements from the input collections"
  [& colls]
  (partition (count colls) (apply interleave colls)))

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

(defn vec-remove
  "Remove elem in coll"
  [pos coll]
  (into (subvec coll 0 pos) (subvec coll (inc pos))))

(defn parse-int
  "Helper wrapper for Integer/parseInt"
  ([n] (parse-int n 10))
  ([n base]
   (Integer/parseInt n base)))

(defn dist
  ([to] (dist [0 0] to))
  ([from to] (Math/sqrt (+ (Math/pow (- (first to) (first from)) 2) (Math/pow (- (second to) (second from)) 2)))))

(defn m-dist
  ([to] (m-dist [0 0] to))
  ([from to] (+ (Math/abs (- (first to) (first from))) (Math/abs (- (second to) (second from))))))

(defn sum
  "Sums a sequence"
  [seq]
  (reduce + seq))

(defn mean
  "Mean of a sequence"
  [coll]
  (let [sz (count coll)]
    (if (pos? sz)
      (/ (sum coll) sz)
      0)))

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

(defn get-first
  "Filters seq using pred and returns the first element"
  [pred seq]
  (first (filter pred seq)))

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
