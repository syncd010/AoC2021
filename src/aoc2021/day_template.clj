(ns aoc2021.day-template
  (:require [clojure.string :as str]))

(defn convert [raw-input]
  raw-input)

(comment
  "Change the file to load it in the REPL"
  (def raw-input (str/split-lines (str/trim (slurp "resources/input"))))
  )

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn solve-part-one [raw-input]
  (let [input (convert raw-input)]
  1))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn solve-part-two [raw-input]
  (let [input (convert raw-input)]
  2))
