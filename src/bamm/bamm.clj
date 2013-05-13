(ns bamm.bamm
  (:require [bamm.impl :refer :all]
            [analemma.svg :refer [circle]]))

(def defaults
  {:mag 5
   :emit? true
   :svg-h 50
   :svg-w 75
   :x-offset 5
   :y-offset 0
   :x 25
   :y 25
   :r 25})

(defn tree [category & children]
  {::category category
   ::children children})

(defn gen-legend
  "automatic color legend from tree's categories
   accepts tree (a map), or a list of all the keys"
  [tree-or-keys]
  (let [distinct-cats (if (map? tree-or-keys)
                        (tree->keys tree-or-keys)
                        (distinct tree-or-keys))]
    (zipmap distinct-cats (gen-swatch "#93C572" (count distinct-cats)))))

(defn draw
  ([tree]
     (draw tree (gen-legend tree)))
  ([tree legend]
     (draw tree legend defaults))
  ([tree legend options]
     (let [{:keys [x y r] :as options} (merge defaults options)
           [left-child right-child] (::children tree)]
       (maybe-emit
        legend
        options
        (let [right-adjust (weigh-children left-child right-child)
              left-adjust (* -1 right-adjust)]
          (keep identity
                [(circle (float x) (float y) (float r)
                         :fill (get legend (::category tree) "#FFFFFF"))
                 (when left-child
                   (draw left-child legend
                         (assoc options
                           :x (+ (- x (/ r 2)) left-adjust)
                           :r (adjust-radius r left-adjust)
                           :emit? false)))
                 (when right-child
                   (draw right-child legend
                         (assoc options
                           :x (- (+ x (/ r 2)) right-adjust)
                           :r (adjust-radius r right-adjust)
                           :emit? false)))]))))))