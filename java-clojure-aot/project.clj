(defproject java-clojure-aot "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]]

  :main main.core
  :uberjar-name "main.jar"

  :profiles {:uberjar {:aot :all}})
