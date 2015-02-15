(ns marume.web
  (:require [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [ring.middleware.stacktrace :as trace]
            [ring.middleware.session :as session]
            [ring.middleware.session.cookie :as cookie]
            [ring.adapter.jetty :as jetty]
            [environ.core :refer [env]]))


(def maru-gifs
  ["http://31.media.tumblr.com/tumblr_m9dq9yD4cN1r1a3nto2_250.gif"
   "http://38.media.tumblr.com/tumblr_m9dq9yD4cN1r1a3nto3_400.gif"
   "http://33.media.tumblr.com/tumblr_m7dojboOM51qb675yo1_500.gif"
   "http://33.media.tumblr.com/tumblr_m7a41mSQDu1qzrlhgo2_500.gif"
   "http://38.media.tumblr.com/tumblr_m730x5OYKm1qf3s1fo1_500.gif"
   "http://33.media.tumblr.com/tumblr_m6r9t5N9h41r1a3nto2_500.gif"
   "http://38.media.tumblr.com/tumblr_m59fh2L3E81qg20muo2_250.gif"
   "http://38.media.tumblr.com/tumblr_m3awrdRDpG1qg20muo1_500.gif"
   "http://38.media.tumblr.com/tumblr_m2zseyO1eW1qg20muo1_500.gif"
   "http://31.media.tumblr.com/tumblr_m1tdxyd05I1qg20muo1_500.gif"
   "http://38.media.tumblr.com/tumblr_m1kxojOHye1qibxp4o1_500.gif"
   "http://38.media.tumblr.com/tumblr_m17js3Kl3c1qb1ryso1_500.gif"
   "http://38.media.tumblr.com/tumblr_lyhxev6rmX1r34a8ho2_500.gif"
   "http://38.media.tumblr.com/tumblr_lyag1rbYuj1qb675yo2_500.gif"
   "http://33.media.tumblr.com/tumblr_lxoet4gdHr1qcyxj3o1_r1_500.gif"
   "http://33.media.tumblr.com/tumblr_lxcdamK0x01qg20muo1_500.gif"
   "http://38.media.tumblr.com/tumblr_lxapz0yzqC1qg20muo1_500.gif"
   "http://33.media.tumblr.com/tumblr_lx5ontc5mN1qg20muo1_500.gif"
   "http://33.media.tumblr.com/tumblr_lwxwodpayS1qg20muo1_500.gif"
   "http://38.media.tumblr.com/tumblr_lwxo3tYWiv1qg20muo1_500.gif"
   "http://media.tumblr.com/tumblr_ldz535p7cp1qb012q.gif"
   "http://33.media.tumblr.com/tumblr_lvwjcsxwrJ1qbhtrto1_500.gif"])

(defn random-maru []
  (rand-nth maru-gifs))

(defroutes app
  (GET "/" []
       {:status 200
        :headers {"Content-Type" "text/html"}
        :body "<img src=\"random.gif\">"})
  (GET "/random.gif" []
       {:status 302
        :headers {"Location" (random-maru)}
        :body nil})
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
  ;; TODO: heroku config:add SESSION_SECRET=$RANDOM_16_CHARS
  (let [store (cookie/cookie-store {:key (env :session-secret)})]
    (-> app
        ((if (env :production)
           wrap-error-page
           trace/wrap-stacktrace))
        (site {:session {:store store}}))))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))]
    (jetty/run-jetty (wrap-app #'app) {:port port :join? false})))

;; For interactive development:
;; (.stop server)
;; (def server (-main))
