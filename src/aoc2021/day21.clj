(ns aoc2021.day21
  (:require [clojure.string :as str]))

(defn convert [raw-input]
  (map (fn [line] (Integer/parseInt (second (re-seq #"\d+" line)))) raw-input))

(comment
  "Change the file to load it in the REPL"
  #_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
  (def raw-input (str/split-lines (str/trim (slurp "resources/input21Test"))))
  )

(defn mod-1 
  "Mod changing 0 to div"
  [num div]
  (let [m (mod num div)]
    (if (= m 0) div m)))

(defn deterministic-dice 
  "Value of one round of deterministic dice"
  [start]
  (reduce (fn [acc s] (+ acc (mod-1 s 100))) 
          (range start (+ start 3))))

(defn play-deterministic-dice 
  "Direct implementation of the game, round by round"
  [start-positions]
  (let [num-players (count start-positions)]
    (loop [scores (vec (repeat num-players 0)) positions (vec start-positions) round 0]
      (if (some #(>= % 1000) scores)
        [scores positions round]
        (let [player-idx (mod round num-players)
              roll (deterministic-dice (inc (* round 3)))
              new-player-pos (mod-1 (+ (nth positions player-idx) roll) 10)]
          (recur (assoc scores player-idx (+ (nth scores player-idx) new-player-pos)) 
                 (assoc positions player-idx new-player-pos)
                 (inc round)))))))

(def dice-results 
  "Possible results for a Dirac dice"
  [1 2 3])

(def round-results 
  "Possible universe results for a round of 3 Dirac dice rolls"
  (into {} (frequencies (map #(reduce + %) (for [a dice-results b dice-results c dice-results] [a b c])))))

(defn evolve-universe 
  "Evolves a universe for one round, for the player specified"
  [universe player]
  (reduce
   (fn [acc [[[pos1 pts1] [pos2 pts2]] cnt]]
     (let [[player-pos player-pts] (if (= player 0) [pos1 pts1] [pos2 pts2])]
       (into acc (map
                  (fn [[round-pos round-cnt]]
                    (let [new-pos (mod-1 (+ player-pos round-pos) 10)
                          new-pts (+ player-pts new-pos)
                          key (if (= player 0) [[new-pos new-pts] [pos2 pts2]] [[pos1 pts1] [new-pos new-pts]])]
                      [key (+ (get acc key 0) (* cnt round-cnt))]))
                  round-results))))
   {} universe))

(defn filter-high-scores 
  "Partitions universes that have scores bigger than max-score, for player 1 and 2"
  [universe max-score]
  [(filter (fn [[player cnt]] (>= (second (first player)) max-score)) universe)
   (filter (fn [[player cnt]] (>= (second (second player)) max-score)) universe)])

(defn remove-high-scores 
  "Removes universes that have scores bigger than max-score"
  [universe max-score]
  (remove (fn [[[[pos1 pts1] [pos2 pts2]] cnt]] (or (>= pts1 max-score) (>= pts2 max-score)))
          universe))

(defn play-dirac-dice 
  "Plays the dirac dice game. Of note, a universe is specified as a map with key
   [ [ player1-position player1-points ] [ player2-position player2-points ] ]
   to the number of universes with that have those attributes"
  [universe]
  (loop [universe universe player 0 p1-wins 0 p2-wins 0]
    (if (empty? universe)
      [p1-wins p2-wins]
      (let [new-universe (evolve-universe universe player)
            max-score 21
            [p1-universe-wins p2-universe-wins] (filter-high-scores new-universe max-score)]
        (recur (remove-high-scores new-universe max-score) 
               (mod (inc player) 2)
               (+ p1-wins (reduce #(+ %1 (second %2)) 0 p1-universe-wins))
               (+ p2-wins (reduce #(+ %1 (second %2)) 0 p2-universe-wins)))))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn solve [raw-input]
  (let [input (convert raw-input)
        [scores _ round] (play-deterministic-dice input)
        part-one (* (apply min scores) (* round 3))
        part-two (play-dirac-dice {[[(first input) 0] [(second input) 0]] 1})]
    [part-one (apply max part-two)]))
