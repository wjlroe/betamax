(ns betamax.core
  (:use [clojure.java.io :only [file]])
  (:require
   [clj-http.client :as http]))

(def cassette-location (atom "resources/cassettes"))

(defn configure
  [location]
  (reset! cassette-location location))

(defn get-track
  "Find the track corresponding to the provided parameters. method is get,post,options...
and req is a map of options"
  [tracks method url req]
  (first (filter #(and (= (:method %) method)
                       (= (:req %) req)
                       (= (:url %) url))
                 tracks)))

(defmacro with-cassette
  [name & body]
  `(let [real-http# (deref #'http/get)
         current-cassette# (atom nil)]
     ;; redef each fun [get post options...]
     ;; pass fun name to fake client
     ;; within cassette, locate correct 'track'
     (with-redefs [http/get (partial cassette-client real-http# current-cassette# ~name :get)]
       (let [ret# ~@body
             cassette# @current-cassette#]
         (when cassette#
           (spit (file @cassette-location ~name) (pr-str cassette#)))
         ret#))))

(defn cassette-client
  [real-http current-cassette name method url & [req]]
  (let [loc (file @cassette-location name)]
    (if (.exists loc)
      (let [contents (read-string (slurp loc))
            track (get-track contents method url req)]
        (if track
          (:response track)
          (throw
           (Exception.
            (format "No track in cassette: %s matches required url: %s, method: %s and request: %s"
                    name url method req)))))
      (let [actual-response (real-http url)
            serialized-response {:method method
                                 :url url
                                 :req req
                                 :response actual-response}]
        (swap! current-cassette
               (fn [cassette]
                 (conj cassette serialized-response)))
        actual-response))))
