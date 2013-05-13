(ns bamm.impl
  (:require [analemma.svg :refer [svg group circle]]
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

(defn weigh-children [left right]
  (let [exaggeration 2]
    (* exaggeration (- (count (:bamm.bamm/children right))
                       (count (:bamm.bamm/children left))))))

(defn adjust-radius [r adjust]
  (+ (/ r 2) adjust))

(defn draw-tree* [tree legend {:keys [x y r] :as options}]
  (let [[left-child right-child] (:bamm.bamm/children tree)
        right-adjust (weigh-children left-child right-child)
        left-adjust (* -1 right-adjust)]
    (apply group
           (keep identity
                 [(circle (float x) (float y) (float r)
                          :fill (get legend (:bamm.bamm/category tree)
                                     "#FFFFFF"))
                  (when left-child
                    (draw-tree* left-child legend
                               (assoc options
                                 :x (+ (- x (/ r 2)) left-adjust)
                                 :r (adjust-radius r left-adjust)
                                 :emit? false)))
                  (when right-child
                    (draw-tree* right-child legend
                               (assoc options
                                 :x (- (+ x (/ r 2)) right-adjust)
                                 :r (adjust-radius r right-adjust)
                                 :emit? false)))]))))

(defn draw-tree [tree legend {:keys [mag x x-offset y-offset] :as options}]
  [:g {:transform (format "translate(%s,%s)"
                          (* mag x-offset)
                          (* mag y-offset))}
   [:g {:transform (format "scale(%s)" mag)}
    (draw-tree* tree legend options)]])

(defn emit-trees [svg-trees
                  legend
                  {:keys [mag emit? svg-h svg-w x-offset y-offset]}]
  (emit (svg {:height (* mag (float svg-h))
              :width (* mag (float svg-w))}
             [:g (cons :g (render-legend legend))
              (apply group svg-trees)])))

(defn tree-layout-offsets [num-trees options]
  (let [padded-diameter (* (+ 2 (get options :padding 0.25))
                           (:r options))]
    (take num-trees
          (for [y-offset (range (:y-offset options)
                                (:svg-h options)
                                padded-diameter)
                x-offset (range (:x-offset options)
                                (:svg-w options)
                                padded-diameter)]
            (merge options {:x-offset x-offset
                            :y-offset y-offset})))))
