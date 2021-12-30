(ns aoc2021.day17
  (:require [clojure.string :as str])
  (:require [aoc2021.common :refer [quad-formula]]))

(defn convert [raw-input]
  (->> raw-input 
       first
       (re-seq #"-?\d+")
       (map #(Integer/parseInt %))
       (partition 2)))

(comment
  "Change the file to load it in the REPL"
  #_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
  (def raw-input (str/split-lines (str/trim (slurp "resources/input17Test"))))
  )

(defn s
  "Position function s(t) (in discrete time)"
  ([v0 a t] (s 0 v0 a t))
  ([s0 v0 a t]
   (+ s0 (* (- v0 (/ a 2)) t) (* (/ a 2) t t))))

(defn t-zero
  "Time at which s(t)=val"
  ([v0 a val] (t-zero 0 v0 a val))
  ([s0 v0 a val]
   (let [a-quad (/ a 2)
         b-quad (- v0 (/ a 2))
         c-quad (- s0 val)]
     (quad-formula a-quad b-quad c-quad))))

(defn hits?
  "Whether the bounding box defined by [xm, xM][ym, yM] is hit with the initial conditions"
  ([[v0x v0y] [ax ay] [xm xM] [ym yM]] (hits? [0 0] [v0x v0y] [ax ay] [xm xM] [ym yM]))
  ([[s0x s0y] [v0x v0y] [ax ay] [xm xM] [ym yM]]
   (let [tx-max (/ v0x (Math/abs ax))
         ty-min (int (Math/ceil (first (t-zero s0y v0y ay yM))))
         ty-max (int (Math/floor (first (t-zero s0y v0y ay ym))))
         s-xs (map #(s s0x v0x ax %) (range (min ty-min tx-max) (inc (min ty-max tx-max))))]
     (and (>= ty-max ty-min) (some #(and (>= % xm) (<= % xM)) s-xs)))))

(defn all-hits [[xm xM] [ym yM]]
  (let [min-v0x (int (Math/ceil (+ -0.5 (Math/sqrt (- 0.25 (* 2 (- 0 xm)))))))
        max-v0x (inc xM)
        min-v0y (dec ym)
        max-v0y (inc (Math/abs ym))]
    (filter #(hits? % [-1 -1] [xm xM] [ym yM]) (for [vx (range min-v0x max-v0x) vy (range min-v0y max-v0y)] [vx vy]))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn solve-part-one [raw-input]
  (let [input (convert raw-input)
        max-vy (apply max (map second (all-hits (first input) (second input))))]
  (int (Math/floor (s max-vy -1 (+ max-vy 0.5))))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn solve-part-two [raw-input]
  (let [input (convert raw-input)]
  (count (all-hits (first input) (second input)))))
