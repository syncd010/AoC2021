(ns aoc2021.day25
  (:require [clojure.string :as str]))

(defn convert [raw-input]
  (mapv (fn [line] (vec line)) raw-input))

(comment
  "Change the file to load it in the REPL"
  #_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
  (def raw-input (str/split-lines (str/trim (slurp "resources/input25Test")))))

(defn repr [matrix width]
  (str/join "\n" (map (fn [line] (str/join line)) (partition width matrix))))

(defn evolve-direction [matrix width heigth dir]
  (let [[new-matrix _ moves]
        (reduce
         (fn [[new-matrix idx moves] c]
           (if (not= c dir)
             [new-matrix (inc idx) moves]
             (let [next-idx (if (= c \>)
                              (+ (* (quot idx width) width) (mod (inc idx) width))
                              (mod (+ idx width) (* width heigth)))]
               (if (= \. (matrix next-idx)) ;; Next space is free
                 [(assoc! (assoc! new-matrix idx \.) next-idx c) (inc idx) (inc moves)]
                 [new-matrix (inc idx) moves]))))
         [(transient (vec matrix)) 0 0]
         matrix)]
    [(persistent! new-matrix) moves]))

(defn evolve [input width heigth]
  (loop [matrix input steps 0 moves 0]
    (let [[east-matrix east-moves] (evolve-direction matrix width heigth \>)
          [south-matrix south-moves] (evolve-direction east-matrix width heigth \v)]
      (if (= 0 (+ east-moves south-moves))
        [south-matrix steps]
        (recur south-matrix (inc steps) (+ moves east-moves south-moves))))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn solve [raw-input]
  (let [input (convert raw-input)
        part-one (evolve (vec (apply concat input)) (count (first input)) (count input))
        part-two 2]
    [(inc (second part-one)) part-two]))
