# word-inventor

A silly website generating 'words' in different languages using Markov chains.
I built it mainly to get a feeling of ClojureScript.
This version uses [noir](https://github.com/ibdknox/noir) on the server-side and [jayq](https://github.com/ibdknox/jayq) [crate](https://github.com/ibdknox/crate) on the client side. Frequency tables are generated during compile time and send over to the client using [fetch](https://github.com/ibdknox/fetch)
For a (somewhat slower) version where all calcs are done on the client side see the cljs-all branch.

## Usage

```bash
lein deps
lein trampoline cljsbuild once
lein run
```

To add a language put a big gzipped text written in this language (a book) to resources/private and edit `src/word-inventor/config.clj`

## License

Distributed under the Eclipse Public License, the same as Clojure.

