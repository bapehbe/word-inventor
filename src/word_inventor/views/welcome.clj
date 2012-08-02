(ns word-inventor.views.welcome
  (:require [word-inventor.views.common :as common]
            [word-inventor.models.markov :as lang]
            [word-inventor.config :as config])
  (:use [noir.core :only [defpage defpartial]]
        [noir.fetch.remotes :only [defremote]]))

(defpartial word [l]
  [:div {:class "word"} (lang/generate-word-for-language (lang/get-id l))])

(defpartial language [l]
  [:div {:id (lang/get-id l) :class "language"}
   [:h1 (lang/get-title l)]
   (for [_ (range 25)] (word l))])

(defpartial languages [langs]
  [:div {:id "languages"}
   (for [l langs] (language l))])

(defpage "/words" []
  (common/layout
   (languages (lang/get-lang-descs))))

(comment defpage "/words" []
  (common/layout
   [:div {:id "languages"}]))
