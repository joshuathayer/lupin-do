(ns lupin-do.core
  (:require [replicant.dom :as d]
            [cljs.pprint :refer [pprint]]))

;; ok very simple data-driven UI loop

(defn make-renderer [el render]
  (fn [state]
    (d/render
     (js/document.getElementById el)
     (render state))))

(defn make-dispatcher [state dispatch]
  ;; why the weird definition of `dispatch-fn`?
  ;; i believe this is kind-of the Y-combinator: we're passing
  ;; a function to itself, to call recursively (in our case, in things like
  ;; ajax handers, which are called from the dispatcher but themselves will need
  ;; to dispatch)
  (fn [replicant-data handler-data]
    (letfn [(disp-fn [ev-data h-data recur-fn]
              (let [[new-state new-events] (dispatch @state ev-data h-data recur-fn)]
                (reset! state new-state)
                (doall (map (fn [ev]
                              (disp-fn (first ev) (second ev) recur-fn))
                            new-events))))]

      (disp-fn replicant-data handler-data disp-fn))))

(defn lupin-do! [el render dispatch]
  (let [state      (atom {})
        renderer   (make-renderer (name el) render)
        dispatcher (make-dispatcher state dispatch)]

    ;; set up our dispatcher: all interactions go through here...
    (d/set-dispatch! dispatcher)

    ;; react to changes in our atom, tell our renderer
    (add-watch state :lupin-watch
               (fn [key atom _old-state new-state]
                 (renderer @atom)))

    (renderer @state)

    dispatcher))
