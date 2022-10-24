(ns starter-slatejs.core
  (:require [cljs-bean.core :refer [->js]]
            [slate :as slate :refer [createEditor]]
            [slate-history :as slate-history :refer [withHistory]]
            [slate-react :as slate-react :refer [Slate Editable withReact]]
            [uix.dom.alpha :as uix.dom]))

(def initial-value (->js [{:type "paragraph"
                           :children [{:text "This is some text..."}]}]))

(defn app
  []
  [:> Slate {:editor (withHistory (withReact (createEditor)))
             :value initial-value}
   [:> Editable {:placeholder "Enter some plain text..."}]])

(defn mount
  []
  (uix.dom/render [app] (.getElementById js/document "app")))

(defn init
  []
  (enable-console-print!)
  (prn "Initialising!")
  (mount))
