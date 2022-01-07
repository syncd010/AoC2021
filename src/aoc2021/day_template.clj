(ns aoc2021.day-template
  (:require [clojure.string :as str]))

(defn convert [raw-input]
  raw-input)

(comment
  "Change the file to load it in the REPL"
  #_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
  (def raw-input (str/split-lines (str/trim (slurp "resources/input"))))
  )

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn solve [raw-input]
  (let [input (convert raw-input)
        part-one 1
        part-two 2]
    [part-one part-two]))
