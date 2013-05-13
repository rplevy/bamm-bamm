(ns bamm.bamm.example
  (:require [midje.sweet :refer :all]
            [me.raynes.fs :refer [size]]
            [bamm.bamm :refer [tree draw]]))

(fact
 "draw tree"
 (println "drawing tree and saving as circular.svg")
 (spit "circular.svg"
       (draw
        [(tree :foo
               (tree :bar)
               (tree :baz
                     (tree :foo)
                     (tree :qux)))

         (tree :foo
               (tree :bar
                     (tree :foo)
                     (tree :qux))
               (tree :baz))

         (tree :foo
               (tree :bar
                     (tree :foo)
                     (tree :qux))
               (tree :baz
                     (tree :foo)
                     (tree :qux)))

         (tree :foo
               (tree :baz
                     (tree :foo)
                     (tree :qux))
               (tree :bar
                     (tree :foo)
                     (tree :qux)))

         (tree :foo
               (tree :baz
                     (tree :foo)
                     (tree :qux
                           (tree :foo)))
               (tree :bar
                     (tree :foo)
                     (tree :qux)))

         (tree :foo
               (tree :baz
                     (tree :foo)
                     (tree :qux
                           (tree :foo
                                 (tree :baz
                                       (tree :foo
                                             (tree :bar
                                                   (tree :foo)
                                                   (tree :qux))
                                             (tree :baz
                                                   (tree :foo)
                                                   (tree :qux)))))))
               (tree :bar
                     (tree :foo)
                     (tree :qux)))

         (tree :foo
               (tree :baz
                     (tree :foo)
                     (tree :qux))
               (tree :bar
                     (tree :foo
                           (tree :bar
                                 (tree :qux)))
                     (tree :qux
                           (tree :foo
                                 (tree :qux)))))]
        {:foo "#000000", :bar "#FF0000", :baz "#00FF00", :qux "#0000FF"}))
 (size "circular.svg") => #(> % 10))
