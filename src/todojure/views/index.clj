(ns todojure.views.index
  (:require [todojure.views.common :as common])
  (:use [todojure.core :as core]
        [noir.core :only [defpage render]]
        [noir.validation :as vali]
        [noir.response :only [redirect]]
        [hiccup.core :only [html]]
        [hiccup.page-helpers :only [link-to]]
        [hiccup.form-helpers :only [form-to submit-button]]))

(defpage "/todo" {:as item}
  (common/layout
   [:h1 "Todo list"]
   (form-to [:post "/action"]
            (common/complete-list)
            (submit-button "Action!"))
   [:hr]
   (form-to [:post "/todo"]
            (common/add-item-fields item)
            (submit-button "Add"))))

(defn valid? [{:keys [desc]}]
  (vali/rule (vali/has-value? desc)
             [:desc "The description can't be void!"])
  (not (vali/errors? :desc)))

(defpage [:post "/todo"] {:as item}
  (if (valid? item)
    (do (core/add-todo (:desc item)) (render "/todo"))
    (render "/todo" item)))

(defpage [:post "/action"] {:as items}
  (doall (map core/mark (map name (keys items))))
  (render "/action"))

(defpage "/action" []
  (common/layout
   [:h1 "Action!"]
   (common/actions-list)
   [:hr]
   (link-to "/reset" "Reset")))

(defpage "/reset" []
  (do (core/reset)
      (redirect "/todo")))

(defpage "/done" {:keys [desc]}
  (core/rm desc)
  (redirect "/action"))

(defpage "/readd" {:keys [desc]}
  (core/readd desc)
  (redirect "/action"))