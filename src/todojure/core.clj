(ns todojure.core)

;; ## Functional model ##
(defn add-item [items desc]
  (concat items [{:desc desc :marked false}]))

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

(defn readd-item [items desc]
  (-> (remove-item items desc)
      (add-item desc)))

(defn add-urgent-item [items desc]
  (-> (add-item items desc)
      (do-mark desc true)))

;; ## Mutable model ##
(def master-list (atom [{:desc "Hello" :marked false}
                        {:desc "Noir" :marked true}
                        {:desc "webapp" :marked false}]
                       :validator (partial every? #(not= "" (:desc %)))))

(defn add-todo [desc]
  (swap! master-list add-item desc))

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

(defn readd [desc]
  (swap! master-list readd-item desc))

(defn add-urgent [desc]
  (swap! master-list add-urgent-item desc))