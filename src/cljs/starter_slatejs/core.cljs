(ns ^:figwheel-hooks starter-slatejs.core
  (:require [hashp.core :include-macros true]
            [medley.core :as m]
            [slate :as slate :refer [Value]]
            [slate-react :as rslate]
            [uix.compiler.alpha :as uixc]
            [uix.compiler.aot :as aot]
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

(defn render-block
  [props _editor next]
  (prn "Rendering block!")
  (let [comments [{:id 1234567890
                   :comment "This is a comment"}]
        node-type (goog.object/getValueByKeys props #js ["node" "type"])
        uix-component (get reactified-component-lookup node-type)]

    (if uix-component
      (let [_ (goog.object/set props "comments" (clj->js comments))]
        (aot/>el uix-component props))
      (next))))

(defn app
  []
  (let [document-value (document->value minimal-document)
        value* (uix/state document-value)]
    [:> rslate/Editor {:value @value*
                       :onChange on-change
                       :renderBlock render-block}]))

(defn mount
  []
  (uix.dom/render [app] (.getElementById js/document "app")))

;; Must be exported so it is available even in :advanced release builds
(defn ^:export init
  []
  (enable-console-print!)
  (js/console.log "Initialising!")
  (mount))

 ;; This is what figwheel calls after each save
(defn ^:after-load re-render
  []
  (prn "Reloading!")
  (mount))
