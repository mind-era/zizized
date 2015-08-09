/*
 * Copyright (c) 2015 Mind Eratosthenes Kft.
 * License: AGPL v3
 */
package com.mind_era.zizized.util

import org.scalatest.FlatSpec
import scala.collection.mutable.HashSet
import scala.util.Random

/**
 * TODO document package com.mind_era.zizized.util.HeapTest
 *
 * @author Szabolcs Ivan
 */
class HeapTest extends FlatSpec {
  "A Heap " should " act as a set " in {
    val ordering = new Ordering[Int] { override def compare(a: Int, b: Int): Int = a - b }
    val heap: Heap[Int] = new Heap[Int](ordering, 0)
    val N: Int = 10000
    val set: HashSet[Int] = new HashSet[Int]()
    val r: Random = scala.util.Random
    var i = 0;
    for (i <- 0 to 3 * N - 1) {
      val value = r.nextInt(N)
      if (heap.contains(value)) {
        assert(set.contains(value))
      } else {
        assert(!set.contains(value))
        discard { set += value }
        heap.insert(value)
      }
    }
    set.foreach(n => assert(heap.contains(n)))
    while (!set.isEmpty) {
      assert(heap.isWellFormed)
      val setMin = set.min(ordering)
      discard { set.remove(setMin) }
      val priMin = heap.eraseMin
      assert(setMin == priMin)
    }
  }

  it should " support change of priorities" in {
    val N = 10000
    val priorities: Vector[Int] = new Vector[Int]
    var i = 0
    val r = Random
    for (i <- 0 to N - 1) priorities += r.nextInt(1000000) // siman nextinttel tulcsordul
    val ordering = new Ordering[Int] { override def compare(a: Int, b: Int) = priorities(a) - priorities(b) }
    val heap: Heap[Int] = new Heap(ordering, 0)
    for (i <- 0 to 10 * N - 1) {
      val cmd = r.nextInt(10)
      if (cmd <= 3) {
        val value = r.nextInt(N)
        if (!heap.contains(value)) {
          heap.insert(value);
          assert(heap.contains(value))
        }
      } else if (cmd <= 6) {
        val value = r.nextInt(N)
        if (heap.contains(value)) {
          heap.erase(value);
          assert(!heap.contains(value))
        }
      } else if (cmd <= 8) {
        val value = r.nextInt(N)
        if (heap.contains(value)) {
          priorities(value) = r.nextInt(1000000)
          heap.changed(value)
        }
      } else {
        assert(heap.isWellFormed)
      }
    }
  }

}