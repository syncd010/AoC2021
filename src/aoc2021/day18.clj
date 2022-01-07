(ns aoc2021.day18
  (:require [clojure.string :as str]))

(comment
  "Change the file to load it in the REPL"
  #_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
  (def raw-input (str/split-lines (str/trim (slurp "resources/input18Test")))))

(defn parse-number [number pairs]
  ;; More suited where mutable state exists, but done in one pass
  (let [curr (subs number 0 1)]
    (cond
      (= 1 (count number)) (first pairs)
      (= curr "[") (parse-number (subs number 1) (conj pairs []))
      (= curr ",") (parse-number (subs number 1) pairs)
      (= curr "]") (let [remaining (pop pairs)]
                     (parse-number (subs number 1) (conj (pop remaining) (conj (peek remaining) (peek pairs)))))
      :else (let [n (Integer/parseInt curr)]
              (parse-number (subs number 1) (conj (pop pairs) (conj (peek pairs) n)))))))

(defn convert [raw-input]
  (map #(parse-number % []) raw-input))

(defn split 
  "Split op"
  [n]
  [(int (Math/floor (/ n 2))) (int (Math/ceil (/ n 2)))])

(defn propagate-explosion 
  "Propagate value in the direction specified"
  [node direction value]
  (cond
    (number? node) (+ node value)
    (= direction :left) [(propagate-explosion (first node) direction value) (second node)]
    (= direction :right) [(first node) (propagate-explosion (second node) direction value)]))

(defn reduce-node-step 
  "Performs one step of the reduction algorithm. 
   Returns the reduced node and a map specifying if and explosion or split occurred"
  [node level]
  (let [left (first node)
        right (second node)
        [reduced-left res-left] (when (not (number? left)) (reduce-node-step left (inc level)))
        [reduced-right res-right] (when (not (number? right)) (reduce-node-step right (inc level)))]
    (cond
      (and (number? left) (number? right) (>= level 4)) [0 {:exploded true :propagate-left left :propagate-right right :which node}]
      (:exploded res-left) (if (:propagate-right res-left)
                             [[reduced-left (propagate-explosion right :left (:propagate-right res-left))] (dissoc res-left :propagate-right)]
                             [[reduced-left right] res-left])
      (:exploded res-right) (if (:propagate-left res-right)
                              [[(propagate-explosion left :right (:propagate-left res-right)) reduced-right] (dissoc res-right :propagate-left)]
                              [[left reduced-right] res-right])
      (and (number? left) (>= left 10)) [[(split left) right] {:split true :which node}]
      (:split res-left) [[reduced-left right] res-left]
      (and (number? right) (>= right 10)) [[left (split right)] {:split true :which node}]
      (:split res-right) [[left reduced-right] res-right]
      :else [node {}])))

(defn reduce-node 
  "Completly reduces the node"
  [node]
  (let [[reduced res] (reduce-node-step node 0)]
    ;; (println node res)
    (if (or (:exploded res) (:split res))
      (reduce-node reduced)
      reduced)))

(defn magnitude [node]
  (if (number? node)
    node
    (+ (* 3 (magnitude (first node))) (* 2 (magnitude (second node))))))

(defn solve-part-one [input]
  (let [reduced (reduce #(reduce-node [%1 %2]) input)]
    (magnitude reduced)))

(defn solve-part-two [input]
  (let [all-pairs (remove nil? (for [fst input snd input] (when (not= fst snd) [fst snd])))]
  (reduce max (map (comp magnitude reduce-node) all-pairs))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn solve [raw-input]
  (let [input (convert raw-input)
        part-one (solve-part-one input)
        part-two (solve-part-two input)]
    [part-one part-two]))
