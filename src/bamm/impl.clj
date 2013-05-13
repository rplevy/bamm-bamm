(ns bamm.impl
  (:require [analemma.svg :refer [svg group]]
            [analemma.xml :refer [emit]]))

(defn render-legend [legend]
  (cons
   [:text {:x 5 :y 15} "LEGEND:"]
   (reduce-kv
    (fn [r k v] (conj r [:text {:fill v
                                :x 5
                                :y (+ 30 (* 10 (count r)))}
                         (name k)]))
    []
    legend)))

(defn maybe-emit [legend {:keys [mag emit? svg-h svg-w offset]}
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

(defn weigh-children [left right]
  (let [exaggeration 2]
    (* exaggeration (- (count (:children right))
                       (count (:children left))))))

(defn adjust-radius [r adjust]
  (+ (/ r 2) adjust))
