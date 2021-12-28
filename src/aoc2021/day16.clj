(ns aoc2021.day16
  (:require [clojure.string :as str]))

(defn convert [raw-input]
  (->> raw-input
       first
       (mapv (fn [c]
               (let [repr (str "0000" (Integer/toBinaryString (Integer/parseInt (str c) 16)))]
                 (subs repr (- (count repr) 4)))))
       str/join))

(comment
  "Change the file to load it in the REPL"
  #_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
  (def raw-input (str/split-lines (str/trim (slurp "resources/input16Test")))))

(declare parse-packet)

(defn parse-literal [packet]
  (let [version (Integer/parseInt (subs packet 0 3) 2)
        type (Integer/parseInt (subs packet 3 6) 2)]
    (loop [remaining (subs packet 6) groups [] consumed 6]
      (let [new-groups (conj groups (subs remaining 1 5))]
        (if (= \0 (first remaining))
          (let [val (reduce + (map-indexed (fn [idx e] (* (Integer/parseInt e 2) (Math/pow 2 (* 4 idx)))) (reverse new-groups)))]
            [{:version version :type type :value val} (+ consumed 5)])
          (recur (subs remaining 5) new-groups (+ consumed 5)))))))

(defn parse-operator [packet]
  (let [version (Integer/parseInt (subs packet 0 3) 2)
        type (Integer/parseInt (subs packet 3 6) 2)
        length-type (Integer/parseInt (subs packet 6 7) 2)
        header-end (if (= 0 length-type) (+ 7 15) (+ 7 11))
        op-num (Integer/parseInt (subs packet 7 header-end) 2)
        stop-fn (if (= 0 length-type)
                  (fn [sub-packets consumed] (= consumed op-num))
                  (fn [sub-packets consumed] (= (count sub-packets) op-num)))]
    (loop [sub-packets [] consumed 0]
      (if (stop-fn sub-packets consumed)
        [{:version version :type type :length-type length-type :sub-packets sub-packets} (+ header-end consumed)]
        (let [[subp n] (parse-packet (subs packet (+ header-end consumed)))]
          (recur (conj sub-packets subp) (+ consumed n)))))))

(defn parse-packet [packet]
  (let [type (Integer/parseInt (subs packet 3 6) 2)]
    (cond
      (= type 4) (parse-literal packet)
      :else (parse-operator packet))))

(defn sum-versions [packet]
  (+ (:version packet) (apply + (map sum-versions (:sub-packets packet)))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn solve-part-one [raw-input]
  (-> raw-input
      convert
      parse-packet
      first
      sum-versions))

(defn eval-packet [packet]
  (let [type (:type packet)
        evaluated-sub-packets (map eval-packet (:sub-packets packet))]
    (cond
      (= type 0) (apply + evaluated-sub-packets)
      (= type 1) (apply * evaluated-sub-packets)
      (= type 2) (apply min evaluated-sub-packets)
      (= type 3) (apply max evaluated-sub-packets)
      (= type 4) (:value packet)
      (= type 5) (if (> (first evaluated-sub-packets) (second evaluated-sub-packets)) 1 0)
      (= type 6) (if (< (first evaluated-sub-packets) (second evaluated-sub-packets)) 1 0)
      (= type 7) (if (= (first evaluated-sub-packets) (second evaluated-sub-packets)) 1 0))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn solve-part-two [raw-input]
  (-> raw-input
      convert
      parse-packet
      first
      eval-packet))
