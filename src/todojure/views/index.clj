(ns todojure.views.index
  (:require [todojure.views.common :as common])
  (:use [noir.core :only [defpage render]]
        [noir.validation :as vali]
        [hiccup.core :only [html]]
        [hiccup.form-helpers :only [form-to submit-button]]))

(defpage "/" {:as item}
  (common/layout
   [:h1 "Todo list"]
   (common/todo-items)
   [:hr]
   (form-to [:post "/"]
            (common/add-item-fields item)
            (submit-button "Add"))))

(defn valid? [{:keys [desc]}]
  (vali/rule (vali/has-value? desc)
             [:desc "The description can't be void!"])
  (not (vali/errors? :desc)))

(defpage [:post "/"] {:as item}
  (if (valid? item)
    (do (common/add-todo (:desc item)) (render "/"))
    (render "/" item)))
