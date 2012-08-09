(ns word-inventor.client.main
  (:require [crate.core :as crate]
            [clojure.browser.repl :as repl]
            [jayq.core :as jq]
            [word-inventor.models.markov.generator :as generator])
  (:use-macros [crate.macros :only [defpartial]])
  (:require-macros [word-inventor.config :as config]))

(def langs (config/get-languages))

(def body (jq/$ :body))
(def languages-box (jq/$ "#languages"))

(def ^:const words-count 10)

(defn lang-selector [lang-id]
  (str "#" lang-id))

(defpartial word [lang-id w]
  [:div {:class "word"} w])

(defpartial language [l]
  (let [id (generator/get-id l)]
    [:div.language {:id id}
     [:a.button {:href "#" :data-lang-id id} (generator/get-title l)]]))

(defn delete-words [lang-id]
  (jq/remove (jq/$ (str (lang-selector lang-id) " > .word"))))

(defn put-words [container lang-id words]
  (do 
    (doseq [w words]
      (jq/append container (word lang-id w)))))

(defn get-words [l]
  (let [words (generator/generate-words langs l words-count)
        lang-box (jq/$ (lang-selector l))]
    (put-words lang-box l words)))

(jq/delegate body language :click
          (fn [e]
            (.preventDefault e)
            (this-as me
                     (let [$me (jq/$ me)
                           lang-id (jq/attr $me :id)]
                       (delete-words lang-id)
                       (get-words lang-id)))))

(defn put-languages [langs container]
  (doseq [l langs]
    (let [lang-box (language l)
          lang-id (generator/get-id l)]
      (jq/append container lang-box)
      (get-words lang-id))))

(defn show-langs []
  (put-languages langs languages-box))

(.ready (jq/$ :document)
        (fn [e] (show-langs)))

;;; uncomment if you need repl
(comment repl/connect "http://localhost:9000/repl")
