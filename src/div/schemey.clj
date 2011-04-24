(ns div.schemey
  (:use div.ist))

(defn error [str]
  (throw (Exception. str)))

(defn null? [x]
  (and (list? x) ;; (coll? x)  ?
       (not (seq x))))

