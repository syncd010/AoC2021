(ns aoc2021.day4
  (:require [clojure.string :refer [split trim]])
  (:require [aoc2021.common :refer [transpose]]))

(defn process-board-line [boards input-line]
  (let [input-vec (split (trim input-line) #" +")]
    (if (== (count input-line) 0)
      (conj boards []) ;; new board
      (conj (pop boards) (conj (last boards) (mapv #(Integer/parseInt %) input-vec))))))

(defn convert [raw-input]
  (let [nums (mapv #(Integer/parseInt %) (split (first raw-input) #","))
        boards (reduce process-board-line [] (rest raw-input))]
    (list nums boards)))

(defn won?
  "Returns whether a board is a winner"
  [board]
  (let [row-win (some (fn [row] (every? (fn [val] (== val 0)) row)) board)
        col-win (some (fn [row] (every? (fn [val] (== val 0)) row)) (transpose board))]
    (or row-win col-win)))

(defn mark-board-number
  "Marks a number in a board"
  [board num]
  (mapv (fn [row] (replace {num 0} row)) board))

(defn play-bingo-round
  "Plays a bingo round, marking the corresponding number and returning a list of winner
  boards (if any) and the remaining boards"
  [boards called-n]
  (let [new-boards (map #(mark-board-number % called-n) boards)
        winner-boards (filter won? new-boards)
        looser-boards (remove won? new-boards)]
    (list winner-boards looser-boards)))

(defn play-bingo-rounds
  "Plays consecutive bingo rounds, until there are no remaining numbers to call.
  Returns a list of results from each round:
  nil if there are no winners on the round or a list of the called-nums and winner boards
  on that round"
  [boards remaining-nums called-nums results]
  (let [round-result (play-bingo-round boards (first remaining-nums))
        winner-boards (first round-result)
        new-called-nums (conj called-nums (first remaining-nums))
        new-results (conj results
                          (when (seq winner-boards)
                            (list new-called-nums winner-boards)))]
    (if (= (count remaining-nums) 1) ;; Reched the end, return
      new-results
      (recur (second round-result) (rest remaining-nums) new-called-nums new-results))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn solve-part-one [raw-input]
  (let [input (convert raw-input)
        bingo-rounds (play-bingo-rounds (second input) (first input) [] [])
        winner-round (first (remove nil? bingo-rounds))]
    (* (reduce + (flatten (second winner-round))) (last (first winner-round)))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn solve-part-two [raw-input]
  (let [input (convert raw-input)
        bingo-rounds (play-bingo-rounds (second input) (first input) [] [])
        winner-round (last (remove nil? bingo-rounds))]
    (* (reduce + (flatten (second winner-round))) (last (first winner-round)))))
