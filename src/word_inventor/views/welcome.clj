(ns word-inventor.views.welcome
  (:require [word-inventor.views.common :as common]
            [noir.content.getting-started])
  (:use [noir.core :only [defpage]]))

(defpage "/welcome" []
         (common/layout
           [:p "Welcome to word-inventor"]))
