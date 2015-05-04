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
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (-> "README.md"
             (slurp)
             (md/md-to-html-string))})

(defn random-maru []
  (rand-nth maru-gifs))

(defn random-marus [num]
  "Return a collection of up to 10 marus"
  (let [num (Integer/parseInt num)
        n (if (<= num 10) num 10)
        gifs (->> maru-gifs shuffle (take n))
        body (str "<html>"
                  (clojure.string/join " " (map #(str "<img src=\"" % "\"/>") gifs))
                  "</html>")]
    body
    ))

(defn redirect-to [code url]
  {:status code
   :headers {"Location" url}
   :body nil})

(def r301 (partial redirect-to 301))
(def r302 (partial redirect-to 302))

(defroutes app
  (GET "/" []
       home-page)
  (GET "/random.gif" []
       (r302 (random-maru)))
  (GET ["/:id.gif" :id #"[0-9]+"] [id]
       (try
         (r301 (->> id (Integer/parseInt) dec (nth maru-gifs)))
         (catch Exception _
           (route/not-found "No such maru"))))
  (GET ["/random/:count.html" :count #"[0-9]+"] [count]
       (random-marus count))
  (GET "/count" []
       (str (count maru-gifs)))
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
