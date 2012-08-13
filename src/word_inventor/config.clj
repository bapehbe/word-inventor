(ns word-inventor.config
  (:use [word-inventor.models.markov :only [make-langs]]))

;;; languages will be assigned at compile time!
(def ^:const languages
  (make-langs
   "ru" {:title "Русский" :source "resources/private/russian.txt.gz"}
   "it" {:title "L'Italiano" :source "resources/private/italian.txt.gz"}
   "en" {:title "English" :source "resources/private/english.txt.gz"}
   "de" {:title "Deutsche" :source "resources/private/german.txt.gz"}
   "fr" {:title "Le Français" :source "resources/private/french.txt.gz"}
   "f500" {:title "Fortune 500" :source "resources/private/fortune500.txt.gz"}))

