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

(defn- build-chain
  "Returns a map like {char1 {char2 freq1, char3 freq3}}"
  [chain string]
  (first (reduce add-to-chain [chain :start] (prepare-str string))))

(defn- choose-next [link]
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

;;; languages will be assigned at compile time!
(def ^:private ^:const languages
  (let [langs (pmap #(setup-language %) config/language-sources)]
    (reduce #(apply assoc %1 %2) {} langs)))

(defn generate-word-for-language [lang-id]
  (loop [word ""]
    (if (>= 3 (count word))
      (recur (generate-word (get-in languages [lang-id :chain])))
      word)))

(defn- dissoc-in [data keys & dissoc-keys]
  (assoc-in data keys (apply dissoc (get-in data keys) dissoc-keys))) 

(defn get-lang-descs []
  "return a collection of language descriptions [lang-id {:title title & attrs}]"
  (map #(dissoc-in % [1] :source :chain) languages))

(defn get-id [lang-desc]
  (first lang-desc))

(defn get-title [lang-desc]
  (-> lang-desc second :title))
