(ns marume.web-test
  (:require [clojure.test :refer :all]
            [marume.web :refer :all]))

(defn gif? [s]
  (string? (re-find #"http.*\.gif" s)))

(deftest get-a-maru
  (is (gif? (random-maru))))

(deftest all-marus
  (is (every? gif? maru-gifs)))
