(ns aoc2021.day24
  (:require [clojure.string :as str]))

(defn convert [raw-input]
  raw-input)

(comment
  "Change the file to load it in the REPL"
  #_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
  (def raw-input (str/split-lines (str/trim (slurp "resources/input24Test"))))
  )

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn solve [raw-input]
  (let [input (convert raw-input)
        part-one 99394899891971
        part-two 92171126131911]
    [part-one part-two]))

;; --------------------------------------------------------
;; Part One:
;; Cond:
;;   x = 1 (always)
;;   1st = 9

;; w = 1th
;; x = w != (z % 26 + 11)
;; z = z / 1
;; z = z * (25 * x + 1)
;; z = z + (w + 6) * x

;; Cond:
;;   z in [7 15] (1st + 6) -> 15
;;   x = 1 (always)
;;   2nd = 9

;; w = 2th
;; x = w != (z % 26 + 13)
;; z = z / 1
;; z = z * (25 * x + 1)
;; z = z + (w + 14) * x

;; Cond:
;;   z in [7*26 15*26] -> 15*26 + 23
;;   x = 1 (always)
;;   3rd = 3

;; w = 3th
;; x = w != (z % 26 + 15)
;; z = z / 1
;; z = z * (25 * x + 1)
;; z = z + (w + 14) * x

;; Cond:
;;   z in [7*26*26 15*26*26] -> (15*26 + 23)*26 + 17
;;   x = 0 (4th = 3rd + 6) -> 9
;;   4th = 9

;; w = 4th
;; x = w != (z % 26 - 8)
;; z = z / 26
;; z = z * (25 * x + 1)
;; z = z + (w + 10) * x

;; Cond:
;;   z in [7*26 15*26] -> 15*26 + 23
;;   x = 1 (always)
;;   5th = 4?

;; w = 5th
;; x = w != (z % 26 + 13)
;; z = z / 1
;; z = z * (25 * x + 1)
;; z = z + (w + 9) * x

;; Cond:
;;   z in [7*26*26 15*26*26] -> (15*26 + 23)*26 + 13?
;;   x = 1 (always)
;;   6th = 8?

;; w = 6th
;; x = w != (z % 26 + 15)
;; z = z / 1
;; z = z * (25 * x + 1)
;; z = z + (w + 12) * x

;; Cond:
;;   z in [7*26*26*26 15*26*26*26] -> ((15*26 + 23)*26 + 13?)*26 + 20?
;;   x = 0  (7th == 6th + 1)
;;   7th = 9?

;; w = 7th
;; x = w != (z % 26 - 11)
;; z = z / 26
;; z = z * (25 * x + 1)
;; z = z + (w + 8) * x

;; Cond:
;;   z in [7*26*26 15*26*26] -> (15*26 + 23)*26 + 13?
;;   x = 0  (8th = 5th + 9 - 4)
;;   8th = 9?

;; w = 8th
;; x = w != (z % 26 - 4)
;; z = z / 26
;; z = z * (25 * x + 1)
;; z = z + (w + 13) * x

;; Cond:
;;   z in [7*26 15*26] -> 15*26 + 23
;;   x = 0 (9th = 2nd + 14 - 15)
;;   9th = 8

;; w = 9th
;; x = w != (z % 26 - 15)
;; z = z / 26
;; z = z * (25 * x + 1)
;; z = z + (w + 12) * x

;; Cond:
;;   z in [7 15] -> 15
;;   x = 1 (always)
;;   10Th = 9?

;; w = 10th
;; x = w != (z % 26 + 14)
;; z = z / 1
;; z = z * (25 * x + 1)
;; z = z + (w + 6) * x

;; Cond:
;;   z in [7*26 15*26] -> 15*26 + 15?
;;   x = 1 (always)
;;   11th == 1

;; w = 11th
;; x = w != (z % 26 + 14)
;; z = z / 1
;; z = z * (25 * x + 1)
;; z = z + (w + 9) * x

;; Cond:
;;   z in [7*26*26 15*26*26] -> (15*26 + 15?)*26 + 10
;;   x = 0  (12th = 11th + 8)
;;   12th = 9

;; w = 12th
;; x = w != (z % 26 - 1)
;; z = z / 26
;; z = z * (25 * x + 1)
;; z = z + (w + 15) * x

;; Cond:
;;   z in [7*26 15*26] -> 15*26 + 15?
;;   x = 0  (13th = 10th - 2)
;;   13th = 7?

;; w = 13th
;; x = w != (z % 26 - 8)
;; z = z / 26
;; z = z * (25 * x + 1)
;; z = z + (w + 4) * x

;; Cond:
;;   z in [7 15] -> 15
;;   x = 0
;;   14th = 1

;; w = 14th
;; x = w != (z % 26 - 14)
;; z = z / 26
;; z = z * (25 * x + 1)
;; z = z + (w + 10) * x


;; --------------------------------------------------------
;; Part Two:
;; Cond:
;;   x = 1 (always)
;;   1st = 9

;; w = 1st
;; x = w != (z % 26 + 11)
;; z = z / 1
;; z = z * (25 * x + 1)
;; z = z + (w + 6) * x

;; Cond:
;;   z in [7 15] (1st + 6) -> 15
;;   x = 1 (always)
;;   2nd = 2

;; w = 2nd
;; x = w != (z % 26 + 13)
;; z = z / 1
;; z = z * (25 * x + 1)
;; z = z + (w + 14) * x

;; Cond:
;;   z in [7*26 15*26] -> 15*26 + 16
;;   x = 1 (always)
;;   3rd = 1

;; w = 3rd
;; x = w != (z % 26 + 15)
;; z = z / 1
;; z = z * (25 * x + 1)
;; z = z + (w + 14) * x

;; Cond:
;;   z in [7*26*26 15*26*26] -> (15*26 + 16)*26 + 15
;;   x = 0 (4th = 3rd + 6) -> 9
;;   4th = 7

;; w = 4th
;; x = w != (z % 26 - 8)
;; z = z / 26
;; z = z * (25 * x + 1)
;; z = z + (w + 10) * x

;; Cond:
;;   z in [7*26 15*26] -> 15*26 + 16
;;   x = 1 (always)
;;   5th = 1?

;; w = 5th
;; x = w != (z % 26 + 13)
;; z = z / 1
;; z = z * (25 * x + 1)
;; z = z + (w + 9) * x

;; Cond:
;;   z in [7*26*26 15*26*26] -> (15*26 + 16)*26 + 10?
;;   x = 1 (always)
;;   6th = 1?

;; w = 6th
;; x = w != (z % 26 + 15)
;; z = z / 1
;; z = z * (25 * x + 1)
;; z = z + (w + 12) * x

;; Cond:
;;   z in [7*26*26*26 15*26*26*26] -> ((15*26 + 16)*26 + 10?)*26 + 13?
;;   x = 0  (7th == 6th + 1)
;;   7th = 2?

;; w = 7th
;; x = w != (z % 26 - 11)
;; z = z / 26
;; z = z * (25 * x + 1)
;; z = z + (w + 8) * x

;; Cond:
;;   z in [7*26*26 15*26*26] -> (15*26 + 16)*26 + 10?
;;   x = 0  (8th = 5th + 9 - 4)
;;   8th = 6?

;; w = 8th
;; x = w != (z % 26 - 4)
;; z = z / 26
;; z = z * (25 * x + 1)
;; z = z + (w + 13) * x

;; Cond:
;;   z in [7*26 15*26] -> 15*26 + 16
;;   x = 0 (9th = 2nd + 14 - 15)
;;   9th = 1

;; w = 9th
;; x = w != (z % 26 - 15)
;; z = z / 26
;; z = z * (25 * x + 1)
;; z = z + (w + 12) * x

;; Cond:
;;   z in [7 15] -> 15
;;   x = 1 (always)
;;   10th = 3?

;; w = 10th
;; x = w != (z % 26 + 14)
;; z = z / 1
;; z = z * (25 * x + 1)
;; z = z + (w + 6) * x

;; Cond:
;;   z in [7*26 15*26] -> 15*26 + 9?
;;   x = 1 (always)
;;   11th == 1

;; w = 11th
;; x = w != (z % 26 + 14)
;; z = z / 1
;; z = z * (25 * x + 1)
;; z = z + (w + 9) * x

;; Cond:
;;   z in [7*26*26 15*26*26] -> (15*26 + 9?)*26 + 10
;;   x = 0  (12th = 11th + 8)
;;   12th = 9

;; w = 12th
;; x = w != (z % 26 - 1)
;; z = z / 26
;; z = z * (25 * x + 1)
;; z = z + (w + 15) * x

;; Cond:
;;   z in [7*26 15*26] -> 15*26 + 9?
;;   x = 0  (13th = 10th - 2)
;;   13th = 1?

;; w = 13th
;; x = w != (z % 26 - 8)
;; z = z / 26
;; z = z * (25 * x + 1)
;; z = z + (w + 4) * x

;; Cond:
;;   z in [7 15] -> 15
;;   x = 0
;;   14th = 1

;; w = 14th
;; x = w != (z % 26 - 14)
;; z = z / 26
;; z = z * (25 * x + 1)
;; z = z + (w + 10) * x
