(ns example
  (:require [lupin-do.core :refer [lupin-do!]]
            [ajax.core :refer [GET]]
            [clojure.core.match :refer [match]]))

(defn get-random [dispatch-fn]
  (GET "https://www.random.org/integers/?num=1&min=0&max=100&col=1&base=10&format=plain&rnd=new"
    {:handler (fn [res]
                (dispatch-fn :ajax [:random (js/parseInt (js->clj res))]))}))

(defn render [state]
  [:div
   [:h2 "The current value is " (:counter state)]
   [:div
    [:button {:on {:click [:decrement 10]}} "-10"]
    [:button {:on {:click [:decrement 1]}} "-1"]
    [:button {:on {:click [:random]}} "random"]
    [:button {:on {:click [:increment 1]}} "+1"]
    [:button {:on {:click [:increment 10]}} "+10"]]
   (when (:loading-random state) [:p "Finding a random number..."])])

(defn dispatch [state replicant-data handler-data dispatch-fn]
  (match [replicant-data handler-data]
    [:init init-state]       [init-state nil]
    [_ [:increment amount]]  [(update state :counter (partial + amount)) nil]
    [_ [:decrement amount]]  [(update state :counter #(- % amount)) nil]
    [_ [:random]]            (do (get-random dispatch-fn)
                                 [(assoc state :loading-random true) nil])
    [:ajax [:random val]]    [(-> state (assoc :counter val)
                                  (assoc :loading-random false)) nil]))

(defn init []
  (let [lupin-dispatch (lupin-do! :example-app render dispatch)]
    (lupin-dispatch :init {:counter 0
                           :loading-random false})))
