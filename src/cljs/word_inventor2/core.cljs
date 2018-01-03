(ns word-inventor2.core
  (:require
   [reagent.core :as reagent]
   [word-inventor2.languages :as langs]
   [word-inventor2.markov :as markov]
   [clojure.spec.alpha :as s]))

(def ^:const default-word-count 5)
(def ^:const default-max-word-count 10)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Vars

(defn generate-words [lang cnt]
  (apply vector
         (for [_ (range cnt)]
           (markov/generate-word-for-language lang langs/languages))))

(defonce app-state
  (reagent/atom {:word-count default-word-count}))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Page

(defn update-word-count [val ratom]
  (swap! ratom assoc :word-count val))

(defn word-count-selector [ratom]
  [:input {:type "range"
           :value default-word-count
           :min 1
           :max default-max-word-count
           :style {:width "100%"}
           :on-input (fn [e]
                        (update-word-count (.. e -target -value) ratom))}])

(defn update-language [lang ratom]
  (swap! ratom assoc-in [:languages lang] (generate-words lang (:word-count @ratom))))

(defn language-button [lang-desc ratom]
  [:input {:type "button" :value (markov/get-title lang-desc)
           :on-click #(update-language (markov/get-id lang-desc) ratom)}] )

(defn generated-words [lang ratom]
  (let [words (get-in @ratom [:languages lang])
        wc (count words)]
      [:ul
       (for [n (range wc)]
         ^{:key n} [:li (get words n)])]))

(defn page [ratom]
  [:div
   [:div [word-count-selector ratom]]
   [:div
    (for [lang-desc (markov/get-lang-descs langs/languages)
          :let [id (markov/get-id lang-desc)]]
      [:div {:key id}
       [language-button lang-desc ratom]
       [generated-words id ratom]])]])



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Initialize App

(defn dev-setup []
  (when ^boolean js/goog.DEBUG
    (enable-console-print!)
    (println "dev mode")
    ))

(defn reload []
  (reagent/render [page app-state]
                  (.getElementById js/document "app")))

(defn ^:export main []
  (dev-setup)
  (reload))
