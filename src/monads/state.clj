(ns monads.state
  ;;(:use monads.generic) useless since the import has to be done at
  ;;the end user.
  )

;; the monadic type (?) is: (state cont) -> no return (CPS), cont is
;; receiving the new state [[[and new cont.  NO  hm ? ]]]

;; cont in the case of >> receives just state

(defn >> [a b]
  (fn [state cont]
    (a state
       (fn [state]
         (b state
            cont)))))

;; cont in the case of >>= receives [state value]
;; ç

(defn >>= [a b]
  (fn [state cont]
    (a state
       (fn [state value]
         ((b value)
          state
          cont)))))

(defn return [val]
  (fn [state cont]
    (cont state
          val)))

(defn state-ref []
  (fn [state cont]
    (cont state)))

(defn state-set [x]
  ;; with or without "!"?
  (fn [state cont]
    (cont x)))

