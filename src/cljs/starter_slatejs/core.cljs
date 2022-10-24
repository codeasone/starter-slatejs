(ns starter-slatejs.core
  (:require [medley.core :as m]
            [slate :as slate :refer [Value]]
            [slate-react :as slate-react]
            [uix.compiler.alpha :as uixc]
            [uix.core.alpha :as uix]
            [uix.dom.alpha :as uix.dom]))

(def minimal-document
  {:data {}
   :nodes [{:data {},
            :nodes [{:marks [], :object "text", :text "This is a test"}]
            :object "block"
            :type "p"}]
   :object "document"})

(defn document->value
  [json]
  (.fromJSON Value (clj->js {:document json})))

(defn on-change
  [editor _next]
  (let [new-value (.-value editor)]
    (prn "Change:" new-value)))

(defn decorated-block-node
  [tag {:keys [attributes comments _node children _isSelected _editor] :as _props}]
  (prn "Rendering decorated block!")
  [tag (js->clj attributes)
   [:span
    (when (#{:p} tag)
      (str (js->clj comments)))]
   children])

(def paragraph
  (fn [props]
    (decorated-block-node :p props)))

(def uix-component-lookup
  {"p" paragraph})

(def reactified-component-lookup (m/map-vals uixc/as-react uix-component-lookup))

(defn app
  []
  (let [document-value (document->value minimal-document)
        value* (uix/state document-value)]
    [:> slate-react/Editor {:value @value*
                            :onChange on-change}]))

(defn mount
  []
  (uix.dom/render [app] (.getElementById js/document "app")))

(defn init
  []
  (enable-console-print!)
  (prn "Initialising!")
  (mount))
