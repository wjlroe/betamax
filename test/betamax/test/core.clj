(ns betamax.test.core
  (:use betamax.core :reload)
  (:use
   midje.sweet
   clojure.java.io)
  (:require
   [clj-http.client]))

(against-background
 [(around :facts
          (binding [cassette-location "/tmp/betamax-cassettes"]
            ?form))]

 (fact "no cassette exists - call out to the network"
       (with-cassette "non-existant"
         (:body (clj-http.client/get "http://example.com")) =>
         #"Example text")
       (provided
        (clj-http.client/get anything) => "Example text" :times 1
        (spit "Example text" (file cassette-location "non-existant")) => true :times 1))

 (fact "cassette exists - don't call out to the network"
       (with-cassette "existing-cassette"
         (:body (clj-http.client/get "http://example.com")) =>
         #"Example text")
       (provided
        (clj-http.client/get anything) => 1 :times 0
        (slurp "existing-cassette") => "Example text")))
