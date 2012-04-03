(ns todojure.views.common
  (:use [noir.core :only [defpartial]]
        [hiccup.page-helpers :only [include-css html5]]
        [hiccup.form-helpers :only [form-to text-field submit-button]]))

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

(defpartial todo-item [desc]
  [:li desc])

(defpartial todo-items []
  [:ol (map todo-item @todo-list)])

(defpartial add-item-fields []
  (text-field "desc"))

(defpartial add-todo-form []
  (form-to [:post "/"]
           (add-item-fields)
           (submit-button "Add")))

(defn add-todo [desc]
  (swap! todo-list conj desc))
