(ns monads.tst
  (:use monads.generic)
  (:use monads.state)
  (:use clojure.walk))

(defn tst []
  ((>>= (state-ref)
        (fn [val]
          (println "val="val)
          (return (inc val))))
   10
   vector))
