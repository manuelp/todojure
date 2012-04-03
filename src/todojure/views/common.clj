(ns todojure.views.common
  (:use [noir.core :only [defpartial]]
        [hiccup.page-helpers :only [include-css html5]]))

(defpartial layout [& content]
            (html5
              [:head
               [:title "todojure"]
               ;(include-css "/css/reset.css")
               ]
              [:body
               [:div#wrapper
                content]]))

(def items ["Hello" "Noir"])

(defpartial todo-item [desc]
  [:li desc])

(defpartial todo-items []
  [:ol (map todo-item items)])
