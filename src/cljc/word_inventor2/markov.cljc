(ns word-inventor2.markov
  (:require [word-inventor2.spec :as spec]
            [clojure.spec.alpha :as s]))

(defn- choose-next [link]
  (let [total (reduce + (vals link))
        random (rand total)
        sorted (sort-by #(second %) link)]
    (loop [remaining sorted counter 0]
      (when (seq remaining)
        (let [[char freq] (first remaining)
              next (+ counter freq)]
          (if (<= counter random next)
            char
            (recur (rest remaining) next)))))))

(defn- generate-word-seq [chain first]
            (let [next (choose-next (chain first))]
                 (cons next (lazy-seq (generate-word-seq chain next)))))

(s/fdef generate-word
        :args (s/alt :start (s/cat :chain ::spec/chain)
                     :continue (s/cat :chain ::spec/chain
                                      :char ::spec/char))
        :ret string?)
(defn generate-word
  ([chain first-char]
     (apply str (take-while #(not= :end %)
                            (generate-word-seq chain first-char))))
  ([chain] (generate-word chain :start)))

(defn generate-word-for-language [lang-id languages]
  (loop [word ""]
    (if (>= 3 (count word))
      (recur (generate-word (get-in languages [lang-id ::spec/chain])))
      word)))

(defn- dissoc-in [data keys & dissoc-keys]
  (assoc-in data keys (apply dissoc (get-in data keys) dissoc-keys)))

(defn get-lang-descs [languages]
  "return a collection of language descriptions [lang-id {:title title & attrs}]"
  (map #(dissoc-in % [1] ::spec/lang-source ::spec/chain) languages))

(defn get-id [lang-desc]
  (first lang-desc))

(defn get-title [lang-desc]
  (-> lang-desc second ::spec/lang-title))
