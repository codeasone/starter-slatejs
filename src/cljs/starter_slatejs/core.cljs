(ns ^:figwheel-hooks starter-slatejs.core
  (:require [cljs-bean.core :refer [->js]]
            [hashp.core :include-macros true]
            [slate :as slate :refer [Value]]
            [slate-react :as rslate]
            [uix.core.alpha :as uix]
            [uix.dom.alpha :as uix.dom]))

(def minimal-document
  {:data {}
   :nodes [{:data {},
            :nodes [{:marks [], :object "text", :text "This is a test"}]
            :object "block"
            :type "p"}]
   :object "document"})

(defn document->value [json]
  (.fromJSON Value (clj->js {:document json})))

(defn app []
  (let [document-value (document->value minimal-document)
        value* (uix/state document-value)]
    [:> rslate/Editor {:value @value*}]))

(defn mount []
  (uix.dom/render [app] (.getElementById js/document "app")))

(defn ^:export init []
  ;; Must be exported so it is available even in :advanced release builds
  (js/console.log "Initialising!")
  (mount))

 ;; This is what figwheel calls after each save
(defn ^:after-load re-render []
  (prn "Reloading!")
  (mount))
