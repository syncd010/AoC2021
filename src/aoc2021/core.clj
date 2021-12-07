(ns aoc2021.core
  (:require [clojure.tools.cli :refer [parse-opts]])
  (:require [clojure.string :refer [trim split-lines]])
  (:gen-class))

(def cli-options
  [["-d" "--day DAY" "Day to run"
    :default 1
    :validate [#(<= % 25) "Day must be 1-25"]
    :parse-fn #(Integer/parseInt %)]
   ["-f" "--file FILE" "File to run"
    :default-fn #(str "./resources/input" (:day %) (if (:test %) "Test" ""))]
   ["-t" "--test" "Use test file if none specified"]])

(defn solve [ns-name input]
  (require (symbol ns-name))
  (let [ns-day (find-ns (symbol ns-name))
        part-one ((ns-resolve ns-day 'solve-part-one) input)
        part-two ((ns-resolve ns-day 'solve-part-two) input)]
    [part-one part-two]))

(defn -main
  [& args]
  (let [parsed-args (:options (parse-opts args cli-options))
        ns-name (str "aoc2021.day" (:day parsed-args))
        contents (split-lines (trim (slurp (:file parsed-args))))
        res (solve ns-name contents)]
    (println (str "Running day " (:day parsed-args) " with file " (:file parsed-args) "\n"
                  "Part one solution: " (get res 0) "\nPart two solution: " (get res 1)))))

