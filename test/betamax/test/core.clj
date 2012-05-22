(ns betamax.test.core
  (:use betamax.core :reload)
  (:use
   midje.sweet))

(fact "create new cassette"
      (with-cassette "new-cassette"
        ))

(fact "no cassette exists - call out to the network")
(fact "read a cassette file")
(fact "cassette exists - don't call out to the network")
