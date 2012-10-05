(ns betamax.test.core
  (:use betamax.core :reload)
  (:use
   [midje.sweet :excluding [file]]
   [clojure.java.io :only [file]])
  (:require
   [clj-http.client]))

(fact "no cassette exists - call out to the network"
  (with-cassette "non-existant"
    (:body (clj-http.client/get "http://www.google.co.uk")) =>
    #"<title>Google")
  (provided
    (spit anything anything) => true :times 1))

(fact "cassette exists - don't call out to the network"
  (with-cassette "example.com"
    (:body (clj-http.client/get "http://example.com")) =>
    #"domains such as EXAMPLE.COM"))

(fact "can configure cassette location"
  (do
    (configure "/tmp/woot")
    @cassette-location) => "/tmp/woot")
