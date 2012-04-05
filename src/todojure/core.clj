(ns todojure.core)

;; ## Functional model ##
(defn do-mark [items desc state]
  (map #(if (= (:desc %) desc)
          (assoc % :marked state)
          %)
       items))

(defn small-list [list]
  (filter :marked (reverse list)))

(defn remove-item [items desc]
  (remove #(= (:desc %) desc) items))

(defn reset-all [items]
  (map #(assoc % :marked false) items))

;; ## Mutable model ##
(def master-list (atom [{:desc "Hello" :marked false}
                        {:desc "Noir" :marked true}]
                       :validator (partial every? #(not= "" (:desc %)))))

(defn add-todo [txt]
  (swap! master-list conj {:desc txt :marked false}))

(defn mark [desc]
  (swap! master-list do-mark desc true))

(defn unmark [desc]
  (swap! master-list do-mark desc false))

(defn done [desc]
  (swap! master-list remove-item desc))

(defn compile-small-list []
  (small-list @master-list))

(defn reset []
  (reset-all @master-list))