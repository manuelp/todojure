(ns todojure.core)

;; ## Mutable model ##
(def master-list (atom [{:desc "Hello" :marked false} {:desc "Noir" :marked true}]
                       :validator (fn [{:keys [desc]}] (not (= "" desc)))))

(defn add-todo [txt]
  (swap! master-list conj {:desc txt :marked false}))

(defn mark [desc]
  (swap! master-list do-mark desc true))

(defn unmark [desc]
  (swap! master-list do-mark desc false))

;; ## Functional model ##
(defn do-mark [items desc state]
  (map #(if (= (:desc %) desc)
          (assoc % :marked state)
          %)
       items))

(defn small-list []
  (filter :marked (reverse @master-list)))