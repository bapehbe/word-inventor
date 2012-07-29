(ns word-inventor.views.welcome
  (:require [word-inventor.views.common :as common]
            [word-inventor.models.markov :as lang]
            [word-inventor.config :as config])
  (:use [noir.core :only [defpage defpartial]]))

(defpartial word [l]
  [:div {:class "word"} (lang/generate-word-for-language l)])

(defpartial language [l]
  [:div {:id (first l) :class "language"}
   [:h1 (-> l second :title)]
   (for [_ (range 10)] (word l))])

(defpartial languages [langs]
  [:div {:id "languages"}
   (for [l langs] (language l))])

(defpage "/words" []
  (common/layout
   (languages lang/languages)))
