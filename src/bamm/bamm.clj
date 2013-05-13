(ns bamm.bamm
  (:require [analemma
             [svg :refer [svg group circle]]
             [xml :refer [emit]]]))

(def defaults
  {:mag 5
   :emit? true
   :svg-h 50
   :svg-w 50
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

(defn ^:private maybe-emit [legend {:keys [mag emit? svg-h svg-w]} svg-content]
  (if-not emit?
    (apply group svg-content)
    (emit
     (svg {:height (* mag (float svg-h))
           :width (* mag (float svg-w))}
          (group
           (apply group (render-legend legend))
           (apply group svg-content))))))

(defn tree [category & children]
  {:category category
   :children children})

(defn draw
  ([tree]
     "TODO: automatic color legend")
  ([tree legend]
     (draw tree legend defaults))
  ([{:keys [category children]} legend options]
     (let [{:keys [mag x y r] :as options} (merge defaults options)
           [left-daughter right-daughter] children]
       (maybe-emit
        legend
        options
        (keep identity
              [(circle (* mag (float x))
                       (* mag (float y))
                       (* mag (float r))
                       :fill (get legend category "#FFFFFF"))
               (when left-daughter
                 (draw left-daughter legend
                       (assoc options :x (- x (/ r 2)) :r (/ r 2)
                              :emit? false)))
               (when right-daughter
                 (draw right-daughter legend
                       (assoc options :x (+ x (/ r 2)) :r (/ r 2)
                              :emit? false)))])))))