(ns word-inventor.views.welcome
  (:require [word-inventor.views.common :as common]
            [word-inventor.models.markov :as lang]
            [word-inventor.config :as config])
  (:use [noir.core :only [defpage defpartial]]
        [noir.fetch.remotes :only [defremote]]))

(defremote get-langs []
  (lang/get-lang-descs config/languages))

(defremote generate-words [l n]
  (for [_ (range n)] (lang/generate-word-for-language l config/languages)))

(defpage "/words" []
  (common/layout
   [:div {:id "languages"}]))
