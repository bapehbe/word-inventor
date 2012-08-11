# word-inventor

A silly website generating 'words' in different languages using Markov chains, you can see it in action on [Heroku](http://word-inventor.herokuapp.com).
I built it mainly to get a feeling of ClojureScript.
This version does word generation on the client side, frequency tables are generated at compile time.
For a client-server version see the master branch.

## Usage

```bash
lein deps
lein trampoline cljsbuild once
lein run
```

To add a language put a big gzipped text written in this language (a book) to resources/private and edit `src/word-inventor/config.clj

## License

Distributed under the Eclipse Public License, the same as Clojure.

