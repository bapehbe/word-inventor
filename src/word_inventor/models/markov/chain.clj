(ns word-inventor.models.markov.chain
  (:require [clojure.java.io :as io]))

(defn- add-to-chain [[chain c1] c2]
  (vector
   (update-in chain [c1 c2] (fnil inc 0)) c2))

(defn- prepare-str [string]
  "leaves only letters in the string, lowercases it and appends :end to it"
  (concat (map #(if (Character/isLetter %)
                  (clojure.string/lower-case %)
                  :end)
               string) '(:end)))

(defn- build-chain
  "Returns a map like {char1 {char2 freq1, char3 freq3}}"
  [chain string]
  (first (reduce add-to-chain [chain :start] (prepare-str string))))

(defn build-chain-from-file [file]
  (with-open [rdr (io/reader (java.util.zip.GZIPInputStream. (io/input-stream file)))]
    (reduce build-chain {} (line-seq rdr))))

(defn language [id {:keys [source] :as language-source}]
  (let [chain (build-chain-from-file source)]
    (hash-map id (assoc language-source :chain chain)))) 

(defn make-langs [& langs]
  {:pre [(even? (count langs))]}
  (let [langs (pmap #(apply language %) (partition 2 langs))]
    (apply merge langs)))
