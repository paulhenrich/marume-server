(ns marume.web-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [marume.web :refer :all]))

(defn gif? [s]
  (string? (re-find #"http.*\.gif" s)))

(deftest all-marus
  (is (every? gif? maru-gifs)))

(deftest routes
  (is (= 200 (:status (app (mock/request :get "/")))))
  (is (= 200 (:status (app (mock/request :get "/random/5.html")))))
  (is (= 301 (:status (app (mock/request :get "/1.gif")))))
  (is (= 302 (:status (app (mock/request :get "/random.gif"))))))
