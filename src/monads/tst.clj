(ns monads.tst
  (:use monads.generic)
  (:use monads.state)
  (:use clojure.walk))

(defn tst []
  ((>>= (state-ref) vector) 'foo ))
