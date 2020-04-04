# lets-shard

A Clojure library designed to shard and distribute load/use on any set of objects!

## Installation

Add the following dependency to your project.clj file:

```clojure
[lets-shard "0.1.0"]
```

## Usage

```clojure
;; In your ns statement
(ns my.ns
  (:require [lets-shard.core :refer :all]))

;; A simple example to shard usage on 3 hashes.
;; Let's prepare the shards first!

(def objects
  [{:name "hash-1"}
   {:name "hash-2"}
   {:name "hash-3"}])

(def weights (get-weights objects))

;; Or you can provide weights if you want!
(def weights (get-weights objects [1 1 3]))

(def slots (get-slots weights))

(def shards (generate-shards objects weights))

;; Now your shards are ready to use. Enjoy!

(get-shard "my-key" shards slots)
(get-object "my-key" shards slots)
```

## License

Copyright © 2020 Omar Alshboul

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
