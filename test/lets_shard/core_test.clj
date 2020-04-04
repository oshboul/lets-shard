(ns lets-shard.core-test
  (:require [clojure.test :refer :all]
            [lets-shard.core :refer :all]))

(deftest get-weights-test
  (testing "should return a vector of ones with size like objects vector"
    (let
      [objects [1 2 3 4]]
      (is (= [1 1 1 1] (get-weights objects)))))
  (testing "should return the given weights while they already given")
    (let
      [objects [1 2 3 4]
       weights [5 6 7 8]]
      (is (= weights (get-weights objects weights)))))

(deftest get-slots-test
  (testing "should return 16384 while weights summation less than 16384"
    (let
      [weights [1 2 3 4]]
      (is (= 16384 (get-slots weights)))))
  (testing "should return weights summation while weights summation more than 16384"
    (let
      [weights [10 16384]]
      (is (= 16394 (get-slots weights))))))

(deftest get-shard-test
  (testing "should return the covering shard for a given key"
    (let
      [key "blah"
       objects [1 2 3 4]
       weights (get-weights objects)
       slots (get-slots weights)
       shards (generate-shards objects)]
      (is (= {:start-slot 0 :end-slot 4095 :object 1} (get-shard key shards slots))))))

(deftest get-object-test
  (testing "should return the object of the covering shard for a given key"
    (let
      [key "blah"
       objects [1 2 3 4]
       weights (get-weights objects)
       slots (get-slots weights)
       shards (generate-shards objects)]
      (is (= 1 (get-object key shards slots))))))
