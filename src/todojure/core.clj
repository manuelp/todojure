;; The core of *Todojure* (you can read it as in "to du jour". And no,
;; I'm not french). It's divided in 3 main "areas":
;; 
;; - a **functional model**: it's the heart of the application, a set
;; of pure functions that implements all the... core logic.
;; - a **mutable model**: this application it's built around a simple
;; mutable model, and this area encapsulates all the accesses (both
;; read and write) to that state. The stateful model itself is managed
;; through an *atom* to enable safe synchronous concurrent accesses.
;; - a **GUI**: it can be any kind of user interface since the core
;; itself is a library that can be used by any client. Currently in
;; this application there is a web UI written with the help of the
;; [Noir](http://www.webnoir.org/) library.
(ns todojure.core
  (require [clojure.string :as s]))

;; ## Functional model ##

(defn add-item
  "Append a new (unmarked) item to the list as the last element."
  [items desc]
  (concat items [{:desc desc :marked false}]))

(defn do-mark
  "Change the marked state of the item in the list with a given description."
  [items desc state]
  (map #(if (= (:desc %) desc)
          (assoc % :marked state)
          %)
       items))

(defn small-list
  "Returns all the marked items of the list in reversed order."
  [list]
  (filter :marked (reverse list)))

(defn remove-item
  "Returns a new list of items without the one with the given description."
  [items desc]
  (remove #(= (:desc %) desc) items))

(defn reset-all
  "Returns a new seq with all items unmarked."
  [items]
  (map #(assoc % :marked false) items))

(defn readd-item
  "Returns a new seq with the given item moved to the last position and unmarked."
  [items desc]
  (-> (remove-item items desc)
      (add-item desc)))

(defn add-urgent-item
  "Append a new marked item to the list."
  [items desc]
  (-> (add-item items desc)
      (do-mark desc true)))

;; ## Mutable model ##

;; The master list is a sequence of maps, each one describing a todo
;; item:
;;
;; - **desc** is a descriptive text that effectively acts as a unique
;; identifier for every item.
;; - **marked** is for, incredibly, mark an item to be later used in
;; some way.
(def master-list (atom [{:desc "Hello" :marked false}
                        {:desc "Noir" :marked false}
                        {:desc "webapp" :marked false}]
                       :validator (partial every? #(not= "" (:desc %)))))

;; All the following functions are basically wrappers around the
;; functional model to access the mutable state (the master-list
;; managed through an atom).

(defn add-todo
  "Append a new item to the master list."
  [desc]
  (swap! master-list add-item desc))

(defn mark
  "Mark the item with the given description."
  [desc]
  (swap! master-list do-mark desc true))

(defn unmark
  "*Un*mark the item with the given description."
  [desc]
  (swap! master-list do-mark desc false))

(defn rm
  "Remove an item from the master-list."
  [desc]
  (swap! master-list remove-item desc))

(defn compile-small-list
  "Returns the small-list."
  []
  (small-list @master-list))

(defn reset
  "Unmark all items."
  []
  (swap! master-list reset-all))

(defn readd
  "Readd a given item to the tail of the list as unmarked."
  [desc]
  (swap! master-list readd-item desc))

(defn add-urgent
  "Add a new item to the tail of the list as marked so that it is in
the first position in the *small list*."
  [desc]
  (swap! master-list add-urgent-item desc))

;; ## Persistence ##
;; For now, the master list is stored in a flat text
;; file. It's good enough.

(defn save-list
  "Save the master list to a file with the given name as a plain text file.
The format is simply: one description per line."
  [fname]
  (letfn [(format-master []
            (s/join \newline (map :desc @master-list)))]
    (spit fname (format-master))))

(defn load-list
  "Load a list of tasks stored in a flat text file with the given name into
  the atom."
  [fname]
  (let [loaded-list (map #(hash-map :desc % :marked false)
                         (s/split (slurp fname) #"\n"))]
    (reset! master-list loaded-list)))