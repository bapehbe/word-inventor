(ns word-inventor.views.welcome
  (:require [word-inventor.views.common :as common])
  (:use [noir.core :only [defpage defpartial]]
        [hiccup.element :only [link-to]]))

(defpartial header []
  [:div#header
   [:p.header "Click on a language title to get a new random set of 'words' "
    (link-to "https://www.github.com/bapehbe/word-inventor/tree/cljs-all" "(source code)")]])

(defpage "/" []
  (common/layout
   (header)
   [:div#languages]))
