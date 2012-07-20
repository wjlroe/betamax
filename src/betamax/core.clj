(ns betamax.core
  (:require
   [clj-http.client :as http]))

(def ^:dynamic cassette-location "resources/cassettes")

(defmacro with-cassette
  [name & body]
  `(let [real-http# #'http/get]
     (with-redefs [http/get (partial cassette-client real-http# ~name)]
      ~@body)))

(defn cassette-client
  [real-http name url]
  (do
    (println "real-http:" real-http "cassette-client name:" name "url:" url)
    (real-http url)))
