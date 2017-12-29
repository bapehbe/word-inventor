# word-inventor

A silly website generating 'words' in different languages using Markov chains.

## Usage

```bash
lein run
lein clean
lein figwheel dev
```

To add a language put a big gzipped text written in this language (a book) to resources/private and edit `src/clj/word-inventor2/generator.clj`, then do ```lein run``` to generate new languages.cljs

## License

Distributed under the Eclipse Public License, the same as Clojure.

