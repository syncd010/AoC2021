(ns aoc2021.day20
  (:require [clojure.string :as str]))

(defn convert-input-line [line]
  (mapv #(Integer/parseInt %) (str/split (str/replace line #".|#" {"." "0" "#" "1"}) #"")))

(defn convert [raw-input]
  (let [img-subs (convert-input-line (first raw-input))
        img (mapv
             (fn [line] (convert-input-line line))
             (drop 2 raw-input))]
    [img-subs img]))

(comment
  "Change the file to load it in the REPL"
  #_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
  (def raw-input (str/split-lines (str/trim (slurp "resources/input20Test")))))

(defn enhanced-value [img img-subs row col outer-val]
  (let [row-idx [(- row 2) (- row 1) row]
        col-idx [(- col 2) (- col 1) col]
        subs-idx (map (fn [[r c]] (get (get img r) c outer-val)) (for [r row-idx c col-idx] [r c]))]
    (get img-subs (Integer/parseInt (str/join subs-idx) 2))))

(defn enhance-once [img img-subs pass]
  (let [outer-val (if (= 0 (first img-subs)) 0 (if (even? pass) 0 1))]
    (vec (pmap (fn [row]
                 (mapv (fn [col] (enhanced-value img img-subs row col outer-val)) (range (+ 2 (count (first img))))))
               (range (+ 2 (count img)))))))

;; (defn enhance-once [img img-subs pass]
;;   (let [outer-val (if (= 0 (first img-subs)) 0 (if (even? pass) 0 1))
;;         height (+ 2 (count img))
;;         width (+ 2 (count (first img)))]
;;     (loop [row 0 new-img (transient [])]
;;       (if (= row height)
;;         (persistent! new-img)
;;         (let [new-row (loop [col 0 new-row (transient [])]
;;                         (if (= col width)
;;                           (persistent! new-row)
;;                           (recur (inc col) (conj! new-row (enhanced-value img img-subs row col outer-val)))))]
;;           (recur (inc row) (conj! new-img new-row)))))))

(defn enhance [img img-subs n]
  (loop [img img img-subs img-subs n n pass 0]
    (if (= n 0)
      img
      (recur (enhance-once img img-subs pass) img-subs (dec n) (rem (inc pass) 2)))))

(defn img-repr [img]
  (str/join "\n" (map (fn [line] (str/replace (str/join line) #"0|1" {"0" "." "1" "#"})) img)))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn solve-part-one [raw-input]
  (let [[img-subs img] (convert raw-input)
        enhanced-img (enhance img img-subs 2)]
    ;; (println (img-repr enhanced-img))))
    (reduce + (map #(reduce + %) enhanced-img))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn solve-part-two [raw-input]
  (let [[img-subs img] (convert raw-input)
        enhanced-img (enhance img img-subs 50)]
    (shutdown-agents)
    (reduce + (map #(reduce + %) enhanced-img))))
