(ns word-inventor.client.main
  (:require [crate.core :as crate]
            [fetch.remotes :as remotes]
            [clojure.browser.repl :as repl]
            [jayq.core :as jq])
  (:use-macros [crate.macros :only [defpartial]])
  (:require-macros [fetch.macros :as fm]))

(def langs (atom {}))

(def body (jq/$ :body))
(def languages-box (jq/$ "#languages"))

(def ^:const words-count 10)

(defn lang-selector [lang-id]
  (str "#" lang-id))

(defn get-id [l]
  (first l))

(defn get-title [l]
  (-> l second :title))

(defpartial word [lang-id w]
  [:div {:class "word"} w])

(defpartial language [l]
  (let [id (get-id l)]
    [:div.language {:id id}
     [:a.button {:href "#" :data-lang-id id} (get-title l)]]))



(defn delete-words [lang-id]
  (jq/remove (jq/$ (str (lang-selector lang-id) " > .word"))))

(defn put-words [container lang-id words]
  (do 
    ;; (delete-words l)
                                        ;clean up old
    (doseq [w words]
      (jq/append container (word lang-id w)))))

(defn get-words [l]
  (fm/remote (generate-words l words-count) [result]
             (let [lang-box (jq/$ (lang-selector l))]
               (put-words lang-box l result))))

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
          lang-id (get-id l)]
      (jq/append container lang-box)
      (get-words lang-id))))

;;; get langs from the server
(defn get-langs []
  (fm/remote (get-langs) [result]
             (swap! langs merge result)))

(add-watch langs :show-langs (fn [k r o n]
                               (when (not= o n)
                                 (put-languages @langs languages-box))))

(.ready (jq/$ :document)
        (fn [e] (get-langs)))



(repl/connect "http://localhost:9000/repl")
