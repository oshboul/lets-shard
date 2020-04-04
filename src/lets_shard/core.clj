(ns lets-shard.core
  (:require [biscuit.core :refer [crc16-xmodem]]))

(def ^:private default-slots 16384)

(defn- in-range? [min-val max-val val]
  (and (>= val min-val) (<= val max-val)))

(defn- get-hkey
  [key slots]
  (mod (crc16-xmodem (str key)) slots))

(defn get-weights
  [objects & [wts]]
  (if (and wts (= (count objects) (count wts)))
        wts
        (vec (repeat (count objects) 1))))

(defn get-slots
  [weights]
  (max (reduce + weights) default-slots))

(defn generate-shards
  [objects & [wts]]
  (let
    [objs-count (count objects)
     weights (get-weights objects wts)
     weights-sum (reduce + weights)
     slots (get-slots weights)
     unit-weight (int (/ slots weights-sum))
     remainder-slots (- slots (* unit-weight weights-sum))]
    (loop
      [shards []
       start-slot 0
       rem-slots remainder-slots
       i 0]
      (if (< i objs-count)
        (let
          [end-slot (dec (+ start-slot (* (nth weights i) unit-weight)))
           slots-to-add (min (nth weights i) rem-slots)
           end-slot (if (> rem-slots 0) (+ end-slot slots-to-add) end-slot)
           tmp-shards (conj
                        shards
                        {:start-slot start-slot
                         :end-slot end-slot
                         :object (nth objects i)})]
        (recur
          tmp-shards
          (inc end-slot)
          (- rem-slots slots-to-add)
          (inc i)))
        shards))))

(defn get-shard
  [key shards slots]
  (let
    [hkey (mod (crc16-xmodem (str key)) slots)
     shards-count (count shards)]
  (loop
    [i 0
     not-found true]
    (if (and not-found (< i shards-count))
      (let
        [shard (nth shards i)
         found (in-range? (:start-slot shard) (:end-slot shard) hkey)]
        (recur
          (inc i)
          (not found)))
      (nth shards (dec i))))))

(defn get-object
  [key shards slots]
  (:object (get-shard key shards slots)))
