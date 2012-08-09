(ns word-inventor.config
  (:use [word-inventor.models.markov.chain :only [make-langs]]))

;;; languages will be assigned at compile time!
;;; edit the map below to add/edit/delete languages
(def ^:const ^:private langs
  (make-langs
   "ru" {:title "Русский" :source "resources/private/russian.txt.gz"}
   "it" {:title "L'Italiano" :source "resources/private/italian.txt.gz"}
   "en" {:title "English" :source "resources/private/english.txt.gz"}
   "de" {:title "Deutsche" :source "resources/private/german.txt.gz"}
   "fr" {:title "Le Français" :source "resources/private/french.txt.gz"}))

;;; it's a macro becase that's the only way I can use it in cljs
(defmacro get-languages []
  langs)
