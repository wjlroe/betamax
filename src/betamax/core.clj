(ns betamax.core
  (:use [clojure.java.io :only [file]])
  (:require
   [clj-http.client :as http]))

(def ^:dynamic cassette-location "resources/cassettes")

(defmacro with-cassette
  [name & body]
  `(let [real-http# (deref #'http/get)]
     (with-redefs [http/get (partial cassette-client real-http# ~name)]
      ~@body)))

(defn cassette-client
  [real-http name url]
  (let [loc (file cassette-location name)
        woot (println "loc:" loc)]
    (println "real-http:" real-http "cassette-client name:" name "url:" url)
    (if (.exists loc)
      (let [contents (read-string (slurp loc))
            haha (println "loc exists. contents: " contents)]
        contents)
      (let [actual-response (real-http url)
            save (spit loc (prn-str actual-response))
            ha (println "loc doesn't exist.")]
        actual-response))))
