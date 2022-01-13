(ns aoc2021.day23
  (:require [clojure.string :as str])
  (:require [aoc2021.search :as search])
  (:require [aoc2021.common :refer [m-get]]))

(def sz 11)

(defn loc->maze [locs]
  (loop [[l & remaining] locs maze [(vec (repeat sz ".")) (vec (repeat sz "#")) (vec (repeat sz "#"))]]
    (if (nil? l)
      (map #(str/join %) maze)
      (recur remaining (assoc maze (second l) (assoc (get maze (second l)) (nth l 2) (first l)))))))

(defn maze->loc [maze]
  (->> maze
       (map-indexed
        (fn [line-idx line]
          (map-indexed
           (fn [idx c] (when (contains? #{\A \B \C \D} c) [line-idx idx]))
           line)))
       (apply concat)
       (remove nil?)))

(defn convert [raw-input]
  (->> raw-input
       (rest)
       (butlast)
       (map #(vec (str/replace (subs % 1 (dec (count %))) #" " "#")))
       (vec)))

(comment
  "Change the file to load it in the REPL"
  #_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
  (def raw-input (str/split-lines (str/trim (slurp "resources/input23Test")))))

(defn repr [maze]
  (str/join "\n" (map str/join maze)))

(def goal-line (vec "A#B#C#D"))
(def final-positions {\A 2 \B 4 \C 6 \D 8})
(def step-cost {\A 1 \B 10 \C 100 \D 1000})

(defn is-goal [node]
  (every? (fn [line] (= (subvec line 2 9) goal-line)) (rest (:value node))))

(defn is-path-free [maze [from-row from-col] [to-row to-col]]
  (cond
    (and (= 0 from-row) (= 0 to-row)) (every? #{\.} (subvec (first maze) (min from-col to-col) (inc (max from-col to-col))))
    :else (and (= \. (m-get maze to-row to-col))
               (= \. (m-get maze from-row from-col))
               (is-path-free maze [(max 0 (dec from-row)) from-col] [(max 0 (dec to-row)) to-col]))))

(defn swap-elem [maze [from-row from-col] [to-row to-col]]
  (vec (map-indexed (fn [line-idx line]
                      (if (and (not= line-idx from-row) (not= line-idx to-row))
                        line
                        (vec (map-indexed (fn [char-idx char]
                                            (cond
                                              (and (= char-idx from-col) (= line-idx from-row)) (m-get maze to-row to-col)
                                              (and (= char-idx to-col) (= line-idx to-row)) (m-get maze from-row from-col)
                                              :else char))
                                          line))))
                    maze)))

(defn get-goal-row [maze goal-col]
  (reduce (fn [res r]
            (if (nil? res)
              nil
              (let [v (m-get maze r goal-col)]
                (cond
                  (= \. v) r
                  (not= goal-col (final-positions v)) nil
                  :else res))))
          -1 (range 1 (count maze))))

(def hallway-positions [[0 0] [0 1] [0 3] [0 5] [0 7] [0 9] [0 10]])

(defn possible-moves
  [maze [row col]]
  (let [goal-col (final-positions (m-get maze row col))
        goal-row (get-goal-row maze goal-col)
        possible-positions (cond
                             (and (= col goal-col) (not (nil? goal-row))) [] ;; We're already in the correct pos
                             (and (= row 0) (nil? goal-row)) [] ;; Can't move to any room
                             (= row 0) [[goal-row goal-col]] ;; Can move to final room
                             :else (if (nil? goal-row) hallway-positions (conj hallway-positions [goal-row goal-col])))
        start-col (if (not= row 0) col (if (> goal-col col) (inc col) (dec col)))
        start-row (max 0 (dec row))]
    (filter #(is-path-free maze [start-row start-col] %) possible-positions)))

(defn maze-id [maze]
  (str/join (map str/join maze)))

(defn successors [node]
  (let [maze (:value node)]
    (mapcat (fn [loc]
              (map (fn [new-loc]
                     (let [new-maze (swap-elem maze loc new-loc)
                           move-cost (* (+ (first loc) (first new-loc) (Math/abs (- (second new-loc) (second loc))))
                                        (step-cost (m-get maze (first loc) (second loc))))]
                       (search/make-search-node new-maze node nil (maze-id new-maze) move-cost)))
                   (possible-moves maze loc)))
            (maze->loc maze))))

(defn search-solution [input]
  (let [start (search/make-search-node input nil nil (maze-id input))
        finish (search/uniform-cost-search start successors is-goal)]
    (str (-> finish :found :path-cost) " (explored " (:explored-count finish) " nodes)")))

(def part-two-lines (map vec ["##D#C#B#A#" "##D#B#A#C#"]))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn solve [raw-input]
  (let [input (convert raw-input)
        ;; part-one 1
        ;; part-two 2
        part-one (search-solution input)
        part-two (search-solution (vec (concat (subvec input 0 2) part-two-lines (subvec input 2))))]
    [part-one part-two]))
