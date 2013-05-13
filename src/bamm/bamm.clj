(ns bamm.bamm
  (:require [analemma
             [svg :refer [svg group circle]]
             [xml :refer [emit]]]))

(def defaults
  {:mag 5
   :emit? true
   :svg-h 50
   :svg-w 75
   :offset 5
   :x 25
   :y 25
   :r 25})

(defn ^:private render-legend [legend]
  (cons
   [:text {:x 5 :y 15} "LEGEND:"]
   (reduce-kv
    (fn [r k v] (conj r [:text {:fill v
                                :x 5
                                :y (+ 30 (* 10 (count r)))}
                         (name k)]))
    []
    legend)))

(defn ^:private maybe-emit [legend
                            {:keys [mag emit? svg-h svg-w offset]}
                            svg-content]
  (if-not emit?
    (apply group svg-content)
    (emit
     (svg {:height (* mag (float svg-h))
           :width (* mag (float svg-w))}
       (group
        (apply group (render-legend legend))
        [:g {:transform (format "translate(%s)" (* mag offset))}
         (concat [:g {:transform (format "scale(%s)" offset)}]
                 svg-content)])))))

(defn tree [category & children]
  {:category category
   :children children})

(defn ^:private weigh-children [left right]
  (let [exaggeration 1.5]
    (* exaggeration (- (count (:children right))
                       (count (:children left))))))

(defn ^:private adjust-radius [r adjust]
  (+ (/ r 2) adjust))

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
        (keep identity
              [(circle (float x) (float y) (float r)
                       :fill (get legend category "#FFFFFF"))
               (when left-child
                 (draw left-child legend
                       (let [adjust (weigh-children right-child left-child)]
                         (assoc options
                           :x (+ (- x (/ r 2)) adjust)
                           :r (adjust-radius r adjust)
                           :emit? false))))
               (when right-child
                 (draw right-child legend
                       (let [adjust (weigh-children left-child right-child)]
                         (assoc options
                           :x (- (+ x (/ r 2)) adjust)
                           :r (adjust-radius r adjust)
                           :emit? false))))])))))