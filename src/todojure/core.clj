(ns todojure.core)

(def todo-list (atom [{:desc "Hello" :marked false} {:desc "Noir" :marked true}]
                     :validator (fn [{:keys [desc]}] (not (= "" desc)))))

(defn add-todo [desc]
  (swap! todo-list conj {:desc desc :marked false}))