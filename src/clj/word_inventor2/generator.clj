(ns word-inventor2.generator
  (:require [clojure.java.io :as io]
            [word-inventor2.spec :as spec]
            [clojure.pprint :as pp]
            [clojure.spec.alpha :as s]))

(def ^:const languages-file "src/cljc/word_inventor2/languages.cljc")
(def ^:const languages-ns '(ns word-inventor2.languages))

(defn- add-to-chain [[chain c1] c2]
  (vector
   (update-in chain [c1 c2] (fnil inc 0)) c2))

(defn- prepare-str [string]
  "leaves only letters in the string, lowercases it and appends :end to it"
  (concat (map #(if (Character/isLetter %)
                  (clojure.string/lower-case %)
                  :end)
               string) '(:end)))

(s/fdef build-chain
        :args (s/cat :chain ::spec/chain
                     :string string?)
        :ret ::spec/chain)
(defn- build-chain
  "Returns a map like {char1 {char2 freq1, char3 freq3}}"
  [chain string]
  (first (reduce add-to-chain [chain :start] (prepare-str string))))

(defn build-chain-from-file [file]
  (with-open [rdr (io/reader (java.util.zip.GZIPInputStream. (io/input-stream file)))]
    (reduce build-chain {} (line-seq rdr))))

(defn language [id {:keys [::spec/lang-source] :as language-source}]
  (do
    (println (str "generating frequency table from " lang-source))
    (let [chain (build-chain-from-file lang-source)
          _ (println (str "generated frequency table from " lang-source))]
      (hash-map id (assoc language-source ::spec/chain chain))))) 

(defn make-langs [& langs]
  {:pre [(even? (count langs))]}
  (let [langs (pmap #(apply language %) (partition 2 langs))]
    (apply merge langs)))

(defn -main [& args]
  (let [languages
        (make-langs
         "ru" {::spec/lang-title "Русский" ::spec/lang-source "resources/private/russian.txt.gz"}
         "it" {::spec/lang-title "L'Italiano" ::spec/lang-source "resources/private/italian.txt.gz"}
         "en" {::spec/lang-title "English" ::spec/lang-source "resources/private/english.txt.gz"}
         "de" {::spec/lang-title "Deutsche" ::spec/lang-source "resources/private/german.txt.gz"}
         "fr" {::spec/lang-title "Le Français" ::spec/lang-source "resources/private/french.txt.gz"}
         "f500" {::spec/lang-title "Fortune 500" ::spec/lang-source "resources/private/fortune500.txt.gz"})]
    (with-open [w (io/writer languages-file)]
      (pp/pprint languages-ns w)
      (pp/pprint (list 'def 'languages languages) w))))
