(ns aoc2021.day15
  (:require [clojure.string :as str])
  (:require [aoc2021.common :refer [parse-int]])
  (:require [aoc2021.search :as search]))

(defn convert [raw-input]
  (mapv (fn [line] (mapv #(parse-int %) (str/split line #""))) raw-input))

(comment
  "Change the file to load it in the REPL"
  #_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
  (def raw-input (str/split-lines (str/trim (slurp "resources/input15Test")))))

(defn is-goal-fn [height width]
  (let [end-node [(- height 1) (- width 1)]]
    (fn [node] (= (:value node) end-node))))

(defn m-get
  "Get the value of input at idx [row col], wrapping around if idx is greater than the size of input"
  [input [row col]]
  (let [width (count (first input))
        height (count input)
        value (get (get input (rem row height)) (rem col width))
        value-adj (+ value (quot row height) (quot col width))]
    (if (= (rem value-adj 9) 0) 9 (rem value-adj 9))))

(defn neighbors-idx
  "Indexes of the neighbors. [height width] is needed to check for borders"
  [[row col] [height width]]
  (filter (fn [[_ [r c]]] (and (>= c 0) (< c width) (>= r 0) (< r height)))
          [["D" [(inc row) col]] ["R" [row (inc col)]] ["U" [(dec row) col]] ["L" [row (dec col)]]]))

(defn successors-fn
  "Returns the successors function"
  [input [height width]]
  (fn [node]
    (map (fn [succ-idx]
           (let [op (first succ-idx) idx (second succ-idx)]
             (search/make-search-node idx node op (str idx) (m-get input idx))))
         (neighbors-idx (:value node) [height width]))))

(defn search-reps [input reps]
  (let [start (search/make-search-node [0 0])
        width (* reps (count (first input)))
        height (* reps (count input))
        finish (search/uniform-cost-search start (successors-fn input [height width]) (is-goal-fn height width))]
    (str (-> finish :found :path-cost) " (explored " (:explored-count finish) " nodes)")))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn solve [raw-input]
  (let [input (convert raw-input)]
    [(search-reps input 1) (search-reps input 5)]))
