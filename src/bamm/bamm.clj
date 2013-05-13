(ns bamm.bamm
  (:require [analemma
             [svg :refer [svg group circle]]
             [xml :refer [emit]]]))

(def defaults
  {:mag 10
   :emit? true
   :svg-h 100
   :svg-w 100
   :x 50
   :y 50
   :r 50})

(defn ^:private maybe-emit [{:keys [mag emit? svg-h svg-w]} svg-content]
  (if-not emit?
    svg-content
    (do (emit
         (svg {:height (* mag svg-h)
               :width (* mag svg-w)}
              svg-content)))))

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
        options
        (apply
         group
         (keep identity
               [(circle (* mag x) (* mag y) (* mag r)
                        :fill (get legend category "#FFFFFF"))
                (when left-daughter
                  (draw left-daughter legend
                        (assoc options :x (- x (/ r 2)) :r (/ r 2)
                               :emit? false)))
                (when right-daughter
                  (draw right-daughter legend
                        (assoc options :x (+ x (/ r 2)) :r (/ r 2)
                               :emit? false)))]))))))