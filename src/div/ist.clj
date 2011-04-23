(ns div.ist
  (:require clojure.test))

(defmacro ist [expr res]
  `(clojure.test/is (= '~res ~expr)))

