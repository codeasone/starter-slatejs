(ns user
  (:require [shadow.cljs.devtools.api :as shadow]
            [shadow.cljs.devtools.server :as server]))

(defn cljs-repl
  []
  (server/start!)
  (shadow/watch :app)
  (shadow/nrepl-select :app))
