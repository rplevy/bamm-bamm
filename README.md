# bamm-bamm

<img src="https://raw.github.com/rplevy/bamm-bamm/master/circular.png">

Circular tree visualization inspired by [Kai Wetzel's Pebbles](http://lip.sourceforge.net/ctreemap.html).

## Coordinates & Require

```
  [bamm-bamm "0.1.0"]
```

```clojure
  [bamm.bamm :refer [tree draw]]
```

## Usage

For usage, see example in [test/bamm/bamm/example.clj](https://github.com/rplevy/bamm-bamm/blob/master/test/bamm/bamm/example.clj).

### Generate Test Image

```sh
  lein midje
  chromium-browser circular.svg
```

## TODO

The original Pebbles implementation has some algorithms for optimally packing
circles. Because my current needs are only for binary trees, I'm not bothering
to implement that at the moment.

## License

Copyright © 2013 Robert P. Levy (@rplevy)

Distributed under the Eclipse Public License, the same as Clojure.
