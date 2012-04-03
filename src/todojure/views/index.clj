(ns todojure.views.index
  (:require [todojure.views.common :as common])
  (:use [noir.core :only [defpage render]]
        [hiccup.core :only [html]]))

(defpage "/" []
  (common/layout
   [:h1 "Todo list"]
   (common/todo-items)
   [:hr]
   (common/add-todo-form)))

(defpage [:post "/"] {:as item}
  (do (common/add-todo (:desc item)) (render "/")))
