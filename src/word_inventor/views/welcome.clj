(ns word-inventor.views.welcome
  (:require [word-inventor.views.common :as common])
  (:use [noir.core :only [defpage defpartial]]))

(defpage "/" []
  (common/layout
   [:div {:id "languages"}]))
