(ns bamm.bamm
  (:require [bamm.impl :refer :all]))

(def defaults
  {:mag          5     ; magnification factor
   :svg-h        300   ; svg document height
   :svg-w        300   ;           "" width
   :x-offset     5     ; for one tree, or starting point for generated
   :y-offset     0     ; ""
   :x            25    ; x coord relative to location of group
   :y            25    ; y coord ""
   :radius       25    ; radius
   :padding      0.25  ; space between circular trees
   #_:tree-offsets #_nil})

(defn tree [category & children]
  {::category category
   ::children children})

(defn gen-legend
  "automatic color legend from tree's categories
   accepts tree (a map), or a list of all the keys"
  [tree-or-keys & [options]]
  (let [distinct-cats (if (map? tree-or-keys)
                        (tree->keys tree-or-keys)
                        (distinct tree-or-keys))]
    (zipmap distinct-cats (gen-swatch (get options :mix-color "#93C572")
                                      (count distinct-cats)))))

(defn draw
  ([trees]
     (draw trees (gen-legend (first tree))))
  ([trees legend]
     (draw trees legend defaults))
  ([trees legend options]
     (let [options (merge defaults options)]
       (emit-trees (map #(draw-tree %1 legend %2)
                        trees
                        (get options :tree-offsets
                             (tree-layout-offsets (count trees) options)))
                   legend
                   options))))
