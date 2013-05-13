(ns bamm.impl
  (:require [analemma.svg :refer [svg group]]
            [analemma.xml :refer [emit]]))

(defn hex->rgb [rgb-str]
  (->> rgb-str
       rest
       (partition 2)
       (map (partial apply str))
       (map #(Integer/parseInt %, 16))))

(defn rgb->hex [[red green blue]]
  (format "#%02X%02X%02X" red green blue))

(defn avg-with-rand-256 [x]
  (int (/ (+ (.nextInt (java.util.Random.) 256) x) 2)))

(defn gen-swatch [mix-color n-colors]
  (map (fn [_] (rgb->hex (map avg-with-rand-256 (hex->rgb mix-color))))
       (range n-colors)))

(defn tree->keys [tree]
  (-> (set (flatten (clojure.walk/prewalk #(if (coll? %) (map identity %) %)
                                          tree)))
      (disj :bamm.bamm/category :bamm.bamm/children nil)))

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

(defn emit-with-legend [legend
                        {:keys [mag emit? svg-h svg-w x-offset y-offset]}
                        svg-content]
  (emit
   (svg {:height (* mag (float svg-h))
         :width (* mag (float svg-w))}
        (group
         (apply group (render-legend legend))
         [:g {:transform (format "translate(%s,%s)"
                                 (* mag x-offset)
                                 (* mag y-offset))}
          (concat [:g {:transform (format "scale(%s)" x-offset)}]
                  svg-content)]))))

(defn maybe-emit [legend {:keys [emit?] :as options} svg-content]
  (if-not emit?
    (apply group svg-content)
    (emit-with-legend legend options svg-content)))

(defn weigh-children [left right]
  (let [exaggeration 2]
    (* exaggeration (- (count (:bamm.bamm/children right))
                       (count (:bamm.bamm/children left))))))

(defn adjust-radius [r adjust]
  (+ (/ r 2) adjust))
