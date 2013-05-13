(defproject bamm-bamm "0.1.0"
  :description "Circular tree visualization inspired by Kai Wetzel's Pebbles."
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojars.pallix/analemma "1.0.0"]]
  :profiles {:dev {:dependencies [[me.raynes/fs "1.4.2"]
                                  [midje "1.6-alpha1"]]}}
  :plugins [[lein-midje "3.0.1"]])
