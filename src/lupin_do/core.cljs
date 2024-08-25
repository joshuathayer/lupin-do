(ns lupin-do.core
  (:require [replicant.dom :as d]))

(defn make-renderer [el render]
  (fn [state]
    (d/render
     (js/document.getElementById el)
     (render state))))

(defn make-dispatcher [state dispatch]
  (fn [replicant-data handler-data]
    (reset! state (dispatch @state replicant-data handler-data))))

(defn lupin-do! [el render dispatch]
  (let [state      (atom {})
        renderer   (make-renderer (name el) render)
        dispatcher (make-dispatcher state dispatch)

        ;; a function to call when things are updated but not from the UI
        update-fn (fn [ks v] (swap! state update-in ks v))]

    ;; set up our dispatcher: all interactions go through here...
    (d/set-dispatch! dispatcher)

    ;; react to changes in our atom, tell our dispatcher
    (add-watch state :lupin-watch
               (fn [key atom _old-state new-state]
                 (renderer @atom)))

    (renderer @state)

    [dispatcher update-fn]))
