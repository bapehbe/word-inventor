(ns word-inventor2.spec
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.string :as str]))

;;; specs
(s/def ::lang-name string?)
(s/def ::lang-title string?)
(s/def ::lang-source string?)

(s/def ::char
  (s/with-gen
    (s/or :char (s/and string? #(= (count %) 1))
          :start #{:start}
          :end #{:end})
    #(gen/one-of [(gen/not-empty (gen/fmap
                                  (fn [c] (str (str/lower-case c)))
                                  (gen/char-alpha)))
                  (gen/return :start)
                  (gen/return :end)])))

(s/def ::frequency (s/map-of ::char (s/and int? #(>= % 1))))
(s/def ::chain (s/map-of ::char ::frequency))
(s/def ::language (s/keys :req [::lang-title
                                ::lang-source
                                ::chain]))
