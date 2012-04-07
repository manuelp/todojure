(ns todojure.views.index
  (:require [todojure.views.common :as common])
  (:use [todojure.core :as core]
        [noir.core :only [defpage render]]
        [noir.validation :as vali]
        [noir.response :only [redirect]]
        [hiccup.core :only [html]]
        [hiccup.page-helpers :only [link-to]]
        [hiccup.form-helpers :only [form-to submit-button]]))

(defn save-all []
  (core/save-list "tasks.txt"))

(defn load-all []
  (core/load-list "tasks.txt"))

(defpage "/todo" {:as item}
  (do (load-all)
      (common/layout
       [:h1 "Todo list"]
       (form-to [:post "/todo"]
                (common/add-item-fields item)
                (submit-button "Add"))
       [:hr]
       (form-to [:post "/action"]
                (common/complete-list)
                (submit-button "Action!")))))

(defn valid? [{:keys [desc]}]
  (vali/rule (vali/has-value? desc)
             [:desc "The description can't be void!"])
  (not (vali/errors? :desc)))

(defpage [:post "/todo"] {:as item}
  (if (valid? item)
    (do (core/add-todo (:desc item))
        (save-all)
        (render "/todo"))))

(defpage [:post "/action"] {:as items}
  (do (doall (map core/mark (map name (keys items))))
      (save-all)
      (render "/action")))

(defpage "/action" {:as item}
  (common/layout
   [:h1 "Action!"]
   (common/actions-list)
   [:hr]
   (link-to "/reset" "Reset")
   [:hr]
   (form-to [:post "/action/add-normal"]
            (common/add-item-fields item)
            (submit-button "Add normal"))
   (form-to [:post "/action/add-urgent"]
            (common/add-item-fields item)
            (submit-button "Add urgent"))))

(defpage "/reset" []
  (do (core/reset)
      (save-all)
      (redirect "/todo")))

(defpage [:post "/action/add-normal"] {:as item}
  (if (valid? item)
    (do (core/add-todo (:desc item))
        (save-all)
        (redirect "/action"))))

(defpage [:post "/action/add-urgent"] {:as item}
  (if (valid? item)
    (do (core/add-urgent (:desc item))
        (save-all)
        (redirect "/action"))))

(defpage "/done" {:keys [desc]}
  (do (core/rm desc)
      (save-all)
      (redirect "/action")))

(defpage "/readd" {:keys [desc]}
  (do (core/readd desc)
      (save-all)
      (redirect "/action")))