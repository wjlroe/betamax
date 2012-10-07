(ns betamax.test.core
  (:use betamax.core :reload)
  (:use
   [midje.sweet :excluding [file]]
   [clojure.java.io :only [file]])
  (:require
   [clj-http.client]))

(fact "no cassette exists - call out to the network"
  (with-cassette "non-existant"
    (:body (clj-http.client/get "http://www.google.co.uk"))) => #"<title>Google"
  (provided
    (spit anything anything) => true :times 1))

(with-cassette "example.com"
 (fact "cassette exists - don't call out to the network"
   (:body (clj-http.client/get "http://example.com")) =>
   #"domains such as EXAMPLE.COM"
   (:body (clj-http.client/get "http://www.iana.org/domains/")) =>
   #"IANA is responsible"))

(fact "can configure cassette location"
  (do
    (configure "/tmp/woot")
    @cassette-location) => "/tmp/woot")

(fact "can locate the correct track in a cassette"
  (get-track (conj nil {:method :get :url "http://example.com" :req nil :response {:woot "hi"}})
             :get
             "http://example.com"
             nil) => (contains {:response {:woot "hi"}}))
