(ns word-inventor.views.welcome
  (:require [word-inventor.views.common :as common]
            [word-inventor.models.markov :as lang]
            [word-inventor.config :as config])
  (:use [noir.core :only [defpage defpartial]]
        [noir.fetch.remotes :only [defremote]]
        [hiccup.element :only [link-to]]))

(defremote get-langs []
  (lang/get-lang-descs config/languages))

(defremote generate-words [l n]
  (for [_ (range n)] (lang/generate-word-for-language l config/languages)))

(defpartial header []
  [:div#header
   [:p.header "Click on a language title to get a new random set of 'words' "
    (link-to "https://www.github.com/bapehbe/word-inventor" "(source code)")]])

(defpage "/" []
  (common/layout
   (header)
   [:div#languages]))
