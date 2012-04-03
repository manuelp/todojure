(ns todojure.views.index
  (:require [todojure.views.common :as common])
  (:use [noir.core :only [defpage]]
        [hiccup.core :only [html]]))

(defpage "/" []
         (common/layout
          [:h1 "Todo list"]
          (common/todo-items)))
