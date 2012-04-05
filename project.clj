(defproject todojure "0.1.0-SNAPSHOT"
            :description "Web application to keep a todo list using the FV system."
            :dependencies [[org.clojure/clojure "1.3.0"]
                           [noir "1.2.1"]]
            :dev-dependencies [[lein-marginalia "0.7.0"]]
            :main todojure.server)

