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
  (loop [new-matrix (transient (vec (repeat (* width heigth) \.))) idx 0 moves 0]
    (if (= idx (* width heigth))
      [(persistent! new-matrix) moves]
      (let [[step-matrix step-moves]
            (cond
              (= \. (matrix idx))
              [new-matrix 0] ;; Free space, do nothing
              (= dir (matrix idx))
              (let [next-idx (if (= dir \>)
                               (+ (* (quot idx width) width) (mod (inc idx) width))
                               (mod (+ idx width) (* width heigth)))]
                (if (= \. (matrix next-idx)) ;; Next space is free
                  [(assoc! (assoc! new-matrix idx \.) next-idx dir) 1]
                  [(assoc! new-matrix idx dir) 0]))
              :else
              [(assoc! new-matrix idx (matrix idx)) 0])] ;; Other dir
        (recur step-matrix (inc idx) (+ moves step-moves))))))

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
