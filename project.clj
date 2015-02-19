(defproject marume "1.0.0-SNAPSHOT"
  :description "A maru server inspired by pugme"
  :url "http://marume.herokuapp.com"
  :license {:name "MIT"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.3.1"]
                 [javax.servlet/servlet-api "2.5"]
                 [http-kit "2.1.16"]
                 [markdown-clj "0.9.62"]
                 [ring/ring-devel "1.3.2"]
                 [environ "0.5.0"]]
  :min-lein-version "2.0.0"
  :plugins [[environ/environ.lein "0.2.1"]]
  :hooks [environ.leiningen.hooks]
  :uberjar-name "marume-standalone.jar"
  :profiles {:production {:env {:production true}}})
