betamax
=======

[![Build Status](https://secure.travis-ci.org/wjlroe/betamax.png)](http://travis-ci.org/wjlroe/betamax)

Mock out [clj-http](https://github.com/dakrone/clj-http) with style. Works just like [VCR](https://github.com/myronmarston/vcr).

## Usage

Put the following in `test_helper.clj` and use it in all your test namespaces:

```clojure
(betamax/configure "fixtures/betamax_cassettes")
```

To test using [Midje](https://github.com/marick/Midje):

```clojure
(deftest something
  (betamax/with-cassette "example-domains"
    (fact (:body (clj-http.client/get "http://www.iana.org/domains/example/")) =>
          #"Example Domains")))
```

## Submitting a pull request

1. Fork the project
1. Create a topic branch
1. Implement the feature or fix the bug
1. Add documentation for the feature or bug
1. Add tests for the feature or bug
1. Run the test suite to ensure you haven't broken anything
1. Commit and push the changes
1. Submit a pull request. Please do not include changes to the project.clj, version or history file.

## License

Copyright (C) 2012 Will Roe

Released under the MIT License: <http://www.opensource.org/licenses/mit-license.php>
