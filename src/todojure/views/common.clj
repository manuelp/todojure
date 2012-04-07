(ns todojure.views.common
  (:use [todojure.core :as core]
        [noir.core :only [defpartial]]
        [noir.validation :as vali]
        [hiccup.page-helpers :only [include-css html5 link-to url]]
        [hiccup.form-helpers :only [text-field check-box]]))

(defpartial layout [& content]
            (html5
              [:head
               [:title "todojure"]
               ;(include-css "/css/reset.css")
               (include-css "/css/main.css")
               ]
              [:body
               [:div#wrapper
                content]]))

(defpartial error-item [[first-error]]
  [:p.error first-error])

(defpartial todo-item [{:keys [desc marked]}]
  [:li (check-box desc marked) desc])

(defpartial complete-list []
  [:ol (map todo-item @core/master-list)])

(defpartial action-item [{:keys [desc]}]
  [:li desc " "
   [:a {:class "item-action" :href (url "/done" {:desc desc})} "Done"] " "
   [:a {:class "item-action" :href (url "/readd" {:desc desc})} "Re-add"]])

(defpartial actions-list []
  [:ol (map action-item (core/compile-small-list))])

(defpartial add-item-fields [{:keys [desc]}]
  (vali/on-error :desc error-item)
  (text-field "desc" desc))