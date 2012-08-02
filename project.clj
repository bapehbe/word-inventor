(defproject word-inventor "0.1.0-SNAPSHOT"
            :description "FIXME: write this!"
            :dependencies [[org.clojure/clojure "1.4.0"]
                           [org.clojure/core.incubator "0.1.1"]
                           [noir "1.3.0-beta3"]
                           [jayq "0.1.0-SNAPSHOT"]
                           [crate "0.1.0-SNAPSHOT"]
                           [fetch "0.1.0-SNAPSHOT"]
                           ;; [cssgen "0.2.6"]
                           ]
            :exclusions [org.clojure/clojure]
            :plugins [[lein-cljsbuild "0.2.4"]]
            :main ^{:skip-aot true} word-inventor.server
            :cljsbuild {:builds
                        [{:builds nil,
                          :source-path "src",
                          :compiler {:output-dir "resources/public/cljs/",
                                     :output-to "resources/public/cljs/bootstrap.js",
                                     :optimizations :simple,
                                     :pretty-print true}}]})
