(ns marume.web
  (:require [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [ring.middleware.stacktrace :as trace]
            [ring.middleware.session :as session]
            [ring.middleware.session.cookie :as cookie]
            [org.httpkit.server :refer [run-server]]
            [environ.core :refer [env]]
            [markdown.core :as md]))


(def maru-gifs
  (-> "resources/gifs.txt"
    (slurp)
    (clojure.string/split-lines)))

(def home-page
  (-> "README.md"
    (slurp)
    (md/md-to-html-string)))

(defn random-maru []
  (rand-nth maru-gifs))

(defroutes app
  (GET "/" []
       {:status 200
        :headers {"Content-Type" "text/html"}
        :body home-page})
  (GET "/random.gif" []
       {:status 302
        :headers {"Location" (random-maru)}
        :body nil})
  (GET "/count" []
       {:status 200
        :headers {"Content-Type" "text/plain"}
        :body (str (count maru-gifs))})
  (ANY "*" []
       (route/not-found (slurp (io/resource "404.html")))))

(defn wrap-error-page [handler]
  (fn [req]
    (try (handler req)
         (catch Exception e
           {:status 500
            :headers {"Content-Type" "text/html"}
            :body (slurp (io/resource "500.html"))}))))

(defn wrap-app [app]
  (-> app
      ((if (env :production)
         wrap-error-page
         trace/wrap-stacktrace))))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))]
    (run-server (wrap-app #'app) {:port port :join? false})))

;; For interactive development:
;; (.stop server)
;; (def server (-main))
