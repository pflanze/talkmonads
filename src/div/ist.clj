(ns div.ist
  (:require clojure.test))

(defmacro ist [expr res]
  `(clojure.test/is (= '~res ~expr)))

(defmacro exn->str [expr]
  (let [e (gensym)]
    `(try ~expr
          (catch Exception ~e
            (.toString ~e)))))

(defmacro iste [expr res]
  `(clojure.test/is
    (= (exn->str ~res) (exn->str ~expr))))
