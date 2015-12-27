/*
 * Copyright (c) 2015 Mind Eratosthenes Kft.
 * License: AGPL v3
 */
package com.mind_era.zizized.util

import org.scalatest.FlatSpec
import com.mind_era.zizized.sat.UIntSet
import java.util.NoSuchElementException
import spire.math.UInt

/**
 * Tests [[UIntSet]].
 *
 * @author Gabor Bakos
 */
class UIntSetTest extends FlatSpec {
  "A new UIntSet" must "be empty" in {
    assert(new UIntSet().isEmpty)
  }

  "Inserted elemenets (5, 6, 7)" must "be contained" in {
    val set = new UIntSet()
    assert(!set.contains(UInt(5)))
    assert(!set.contains(UInt(6)))
    assert(!set.contains(UInt(7)))

    set.insert(UInt(5))
    assert(!set.isEmpty)
    assert(set.contains(UInt(5)))
    assert(!set.contains(UInt(6)))
    assert(!set.contains(UInt(7)))

    set.insert(UInt(7))
    assert(set.contains(UInt(5)))
    assert(!set.contains(UInt(6)))
    assert(set.contains(UInt(7)))

    set.insert(UInt(7))
    assert(set.contains(UInt(5)))
    assert(!set.contains(UInt(6)))
    assert(set.contains(UInt(7)))

    set.insert(UInt(6))
    assert(set.contains(UInt(5)))
    assert(set.contains(UInt(6)))
    assert(set.contains(UInt(7)))
  }
  "After inserts and clear" must "be empty" in {
    val set = new UIntSet()

    set.insert(UInt(5))
    assert(!set.isEmpty)

    set.insert(UInt(7))
    assert(!set.isEmpty)

    set.clear()
    assert(set.isEmpty)
  }

  "erase" must "throw NoSuchElementException on empty set" in {
    discard { intercept[NoSuchElementException] { new UIntSet().erase() } }
  }

  "erase" must "remove the last inserted element" in {
    val set = new UIntSet()

    set.insert(UInt(5))
    set.insert(UInt(7))
    set.insert(UInt(7))
    set.insert(UInt(6))

    assert(UInt(6) === set.erase())
    assert(!set.contains(UInt(6)))
    assert(set.contains(UInt(7)))
    assert(set.contains(UInt(5)))

    assert(UInt(7) === set.erase())
    assert(!set.contains(UInt(6)))
    assert(!set.contains(UInt(7)))
    assert(set.contains(UInt(5)))

    assert(UInt(5) === set.erase())
    assert(!set.contains(UInt(6)))
    assert(!set.contains(UInt(7)))
    assert(!set.contains(UInt(5)))

    assert(set.isEmpty)
  }

  "values iterated" must "follow reverse insertion order" in {
    val set = new UIntSet()
    set insert UInt(5)
    set insert UInt(6)
    set insert UInt(7)
    set insert UInt(5)
    set insert UInt(4)

    assert(set.iterator.toList == List(UInt(4), UInt(7), UInt(6), UInt(5)))
  }
}