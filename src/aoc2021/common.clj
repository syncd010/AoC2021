(ns aoc2021.common)

(defn zip 
  "Returns a lazy sequence of lists with interleaved elements from the input collections"
  [& colls]
  (partition (count colls) (apply interleave colls)))

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
  [n]
  (Integer/parseInt n))

(defn parse-bin-int [n]
  (Integer/parseInt n 2))

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

(defn call
  "Allows to call a function by name"
  [fn & args]
  (apply (resolve (symbol fn)) args))
