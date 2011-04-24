(ns div.syntaxlib
  (:use div.schemey)
  (:use div.ist))

(defn _right-associate [op l]
  (let [r (reverse l)
        rst (rest r)]
    (if (null? rst)
      (error "need at least 2 arguments")
      (reduce (fn [r v]
                (list op v r))
              (first r)
              rst))))

(ist (_right-associate 'foo '(10 20 30 40))
     (foo 10 (foo 20 (foo 30 40))))
(ist (_right-associate 'foo '(10 40))
     (foo 10 40))
(iste (_right-associate 'foo '(40))
      (error "need at least 2 arguments"))

(defmacro right-associate [op & l]
  (_right-associate op l))

