(defproject betamax "0.1.1"
  :description "Mock out clj-http with style"
  :url "http://github.com/wjlroe/betamax"
  :license {:name "MIT"
            :url "http://www.opensource.org/licenses/mit-license.php"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [clj-http "0.4.1"]]
  :profiles {:dev {:dependencies [[midje "1.4.0"]]
                   :plugins [[lein-midje "2.0.0-SNAPSHOT"]]}})
