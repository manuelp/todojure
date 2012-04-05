(ns todojure.views.common
  (:use [todojure.core :as core]
        [noir.core :only [defpartial]]
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

(defpartial error-item [[first-error]]
  [:p.error first-error])

(defpartial todo-item [{:keys [desc marked]}]
  [:li (if marked [:strong desc] desc)])

(defpartial todo-items []
  [:ol (map todo-item @core/todo-list)])

(defpartial add-item-fields [{:keys [desc]}]
  (vali/on-error :desc error-item)
  (text-field "desc" desc))