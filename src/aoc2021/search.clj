(ns aoc2021.search)

(defn make-search-node
  "Makes a data structure suitable to be used with the search functions. Has the following properties:
   - value of the node
   - parent of the node, optional, nil if not specified in which case, the path cannot be recovered
   - operation that generated the node, optional
   - id, unique id, suitable to be used as the key of a map to save the explored nodes
   - cost, cost of exploring this node, optional for BFS and DFS
   - estimated-cost, estimated cost till end, necessary for A* type search
   Internally, each node also has :path-cost, :total-cost and :depth which are maintained by the search"
  ([value] (make-search-node value nil))
  ([value parent] (make-search-node value parent nil))
  ([value parent op] (make-search-node value parent op (str value)))
  ([value parent op id] (make-search-node value parent op id 0))
  ([value parent op id cost] (make-search-node value parent op id cost 0))
  ([value parent op id cost estimated-cost]
   {:value value :parent parent :op op :id id :cost cost :estimated-cost estimated-cost
    :path-cost (if (nil? parent) cost (+ (:path-cost parent) cost))
    :depth (if (nil? parent) 0 (+ (:depth parent) 1))}))

(defn reconstruct-search-path
  "Get just the path from the search node"
  ([node] (reconstruct-search-path node :value))
  ([node wanted]
   (loop [current node path []]
     (if (nil? current)
       path
       (recur (:parent current) (conj path (wanted current)))))))

(defn general-search
  "Generic search that makes few assumptions on the structure of the problem, therefore care 
   must be taken in specifying its arguments for it to work correctly.
   On each iteration it calls *partition* on the unexplored list to get the next element and the 
   remaining, checks if the goal has been reached with *is-goal* returning if it is (the path 
   taken can be obtained by following :parent on each node).
   If it's not, generate successors with *successors*, passing the current node and a map of already
   explored nodes indexed by the node's ids. It's the caller responsibility to return only successors
   that are relevant, filtering the already explored. Successors must be full nodes, created with 
   *make-search-node*, and specifying its properties. Successors are then added to the unexplored list 
   with *enqueue*, which is called with the current unexplored nodes and the successors, therefore it
   determines the search order in conjunction with *partition*."
  [start-nodes successors is-goal partition enqueue]
  (loop [frontier start-nodes explored (zipmap (map :id start-nodes) start-nodes) n 0]
    (let [[current next] (partition frontier)]
      (if (or (nil? current) (is-goal current))
        {:found current :explored-count (if (nil? current) n (inc n))}
        (let [current-successors (successors current explored)]
          (recur (enqueue next current-successors)
                 (into explored (zipmap (map :id current-successors) current-successors))
                 (inc n)))))))

(defn default-filter-successors-fn
  "Default filtering for successors, check if they have already been seen"
  [successors]
  (fn [node explored] (filter #(not (contains? explored (:id %))) (successors node))))

(defn breadth-first-search
  "Breadth first search"
  [start successors is-goal]
    ;; LIFO, use queues, take from the front, append successors at the back
  (let [start-nodes (conj clojure.lang.PersistentQueue/EMPTY start)
        partition (fn [frontier] [(peek frontier) (if (empty? frontier) frontier (pop frontier))])
        enqueue (fn [frontier successors] (into frontier successors))]
    (general-search start-nodes (default-filter-successors-fn successors) is-goal partition enqueue)))

(defn depth-first-search
  "Depth first search"
  [start successors is-goal]
  ;; FIFO, use lists, take from the front, prepend successors at the front
  (let [start-nodes (list start)
        partition (fn [frontier] [(first frontier) (rest frontier)])
        enqueue (fn [frontier successors] (concat successors frontier))]
    (general-search start-nodes (default-filter-successors-fn successors) is-goal partition enqueue)))

;; (defn min-idx [nodes key]
;;   (second (reduce (fn [[min-val min-idx curr-idx] val]
;;                     (if (< (key val) min-val)
;;                       [(key val) curr-idx (inc curr-idx)]
;;                       [min-val min-idx (inc curr-idx)])) [(key (first nodes)) 0 0]
;;                   nodes)))
;; (defn general-cost-search
;;   "General cost search, fits uniform cost, greedy and A* search"
;;   [start successors is-goal cost-key]
;;   (let [start-nodes (vector start)
;;         partition
;;         (fn [frontier]
;;           (let [min-idx (min-idx frontier cost-key)]
;;             [(nth frontier min-idx) (into (subvec frontier 0 min-idx) (subvec frontier (inc min-idx)))]))
;;         filtered-successors
;;         (fn [node explored]
;;           (filter
;;            #(let [seen (get explored (:id %))] (or (nil? seen) (< (cost-key %) (cost-key seen))))
;;            (successors node)))
;;         enqueue (fn [frontier successors] (into frontier successors))]
;;     (general-search start-nodes filtered-successors is-goal partition enqueue)))

(defn general-cost-search
  "General cost search, fits uniform cost, greedy and A* search"
  [start successors is-goal cost-key]
  ;; Sorting the successors every time we enqueue them. This is very inneficient, but it's the 
  ;; best alternative i found. Not sorting and manually finding the minimum is slower than sorting
  (let [start-nodes (list start)
        partition (fn [frontier] [(first frontier) (rest frontier)])
        filtered-successors (fn [node explored]
                              (filter
                               #(let [seen (get explored (:id %))] (or (nil? seen) (< (cost-key %) (cost-key seen))))
                               (successors node)))
        enqueue (fn [frontier successors] (sort-by cost-key (into frontier successors)))]
    (general-search start-nodes filtered-successors is-goal partition enqueue)))

(defn uniform-cost-search
  "Uniform cost search"
  [start successors is-goal]
  (general-cost-search start successors is-goal :path-cost))

(defn greedy-search
  "Greedy search. Make sure to specify :estimated-cost when building nodes in successors, otherwise
   this will fail spectacularly"
  [start successors is-goal]
  (general-cost-search start successors is-goal :estimated-cost))

(defn a-star-search
  "A* search, using the path cost and estimated search to reach the goal"
  [start successors is-goal]
  (general-cost-search start successors is-goal #(+ (:path-cost %) (:estimated-cost %))))
