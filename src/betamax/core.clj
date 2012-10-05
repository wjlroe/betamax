(ns betamax.core
  (:use [clojure.java.io :only [file]])
  (:require
   [clj-http.client :as http]))

(def cassette-location (atom "resources/cassettes"))

(defn configure
  [location]
  (reset! cassette-location location))

(defmacro with-cassette
  [name & body]
  `(let [real-http# (deref #'http/get)]
     (with-redefs [http/get (partial cassette-client real-http# ~name)]
      ~@body)))

(defn cassette-client
  [real-http name url]
  (let [loc (file @cassette-location name)]
    (if (.exists loc)
      (let [contents (read-string (slurp loc))]
        contents)
      (let [actual-response (real-http url)
            save (spit loc (prn-str actual-response))]
        actual-response))))
