(ns todojure.views.common
  (:use [noir.core :only [defpartial]]
        [noir.validation :as vali]
        [hiccup.page-helpers :only [include-css html5]]
        [hiccup.form-helpers :only [text-field]]))

(defpartial layout [& content]
            (html5
              [:head
               [:title "todojure"]
               ;(include-css "/css/reset.css")
               ]
              [:body
               [:div#wrapper
                content]]))

(def todo-list (atom ["Hello" "Noir"]
                 :validator (fn [desc] (not (= "" desc)))))

(defpartial error-item [[first-error]]
  [:p.error first-error])

(defpartial todo-item [desc]
  [:li desc])

(defpartial todo-items []
  [:ol (map todo-item @todo-list)])

(defpartial add-item-fields [{:keys [desc]}]
  (vali/on-error :desc error-item)
  (text-field "desc" desc))

(defn add-todo [desc]
  (swap! todo-list conj desc))
