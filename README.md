# bamm-bamm

<img src="https://raw.github.com/rplevy/bamm-bamm/master/circular.png">

Circular tree visualization inspired by Kai Wetzel's Pebbles.

## Coordinates

```clojure
  [bamm.bamm :refer [tree draw]]
```

## Usage

For usage, see example in test/bamm/bamm/example.clj.

To run the example:

```sh
  lein midje # renders and spits an example svg file
  chromium-browser circular.svg
```

## TODO

The original Pebbles implementation has some algorithms for optimally packing
circles. Because my current needs are only for binary trees, I'm not bothering
to implement that at the moment.

## License

Copyright © 2013 FIXME

Distributed under the Eclipse Public License, the same as Clojure.
