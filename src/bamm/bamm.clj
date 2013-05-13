(ns bamm.bamm
  (:require [bamm.impl :refer :all]
            [analemma.svg :refer [circle]]))

(def defaults
  {:mag 5
   :emit? true
   :svg-h 50
   :svg-w 75
   :offset 5
   :x 25
   :y 25
   :r 25})

(defn tree [category & children]
  {:category category
   :children children})

(defn draw
  ([tree]
     "TODO: automatic color legend")
  ([tree legend]
     (draw tree legend defaults))
  ([{:keys [category children]} legend options]
     (let [{:keys [x y r] :as options} (merge defaults options)
           [left-child right-child] children]
       (maybe-emit
        legend
        options
        (let [right-adjust (weigh-children left-child right-child)
              left-adjust (* -1 right-adjust)]
          (keep identity
                [(circle (float x) (float y) (float r)
                         :fill (get legend category "#FFFFFF"))
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