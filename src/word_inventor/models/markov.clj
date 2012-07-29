(ns word-inventor.models.markov
  (:require [word-inventor.config :as config]
            [clojure.java.io :as io]))

(defn- add-to-chain [[chain c1] c2]
  (vector
   (update-in chain [c1 c2] (fnil inc 0)) c2))

(defn- prepare-str [string]
  "leaves only letters in the string, lowercases it and appends :end to it"
  (concat (map #(if (Character/isLetter %)
                  (clojure.string/lower-case %)
                  :end)
               string) '(:end)))

(defn build-chain
  "Returns a map like {char1 {char2 freq1, char3 freq3}}"
  [chain string]
  (first (reduce add-to-chain [chain :start] (prepare-str string))))

(defn choose-next [link]
  (let [total (reduce + (vals link))
        random (rand total)
        sorted (sort-by #(second %) link)]
    (loop [remaining sorted counter 0]
      (when remaining
        (let [[char freq] (first remaining)
              next (+ counter freq)]
          (if (<= counter random next)
            char
            (recur (rest remaining) next)))))))

(defn- generate-word-seq [chain first]
            (let [next (choose-next (chain first))]
                 (cons next (lazy-seq (generate-word-seq chain next)))))

(defn generate-word
  ([chain first-char]
     (apply str (take-while #(not (= :end %))
                            (generate-word-seq chain first-char))))
  ([chain] (generate-word chain :start)))

(defn build-chain-from-file [file]
  (with-open [rdr (io/reader (java.util.zip.GZIPInputStream. (io/input-stream file)))]
    (reduce build-chain {} (line-seq rdr))))

(defn- setup-language [[id {:keys [source]} :as language-source]]
  (let [chain (build-chain-from-file source)]
    (assoc-in language-source [1 :chain] chain))) 

(def languages (pmap #(setup-language %) config/language-sources))

(defn generate-word-for-language [language]
  (loop [word ""]
    (if (>= 3 (count word))
      (recur (generate-word (get-in language [1 :chain])))
      word)))
