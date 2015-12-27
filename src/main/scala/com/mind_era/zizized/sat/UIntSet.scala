/*
 * Copyright (c) 2015 Mind Eratosthenes Kft.
 * License: AGPL v3
 */
package com.mind_era.zizized.sat

import scala.collection.mutable.BitSet
import spire.math.UInt
import scala.collection.mutable.Stack
import _root_.com.mind_era.zizized.util.discard
import java.util.NoSuchElementException

/**
 * UIntSet (in util/sat_types.h) is a bitset of unsigned ints, with an additional method
 * erase() which erases the element of the set THAT WAS INSERTED LAST.
 *
 * We can view this also as a Stack of unsigneds with an added bitset for a quick contains() method.
 *
 * @author Szabolcs Ivan
 * @since 1.0
 */
class UIntSet extends Iterable[UInt] {
  /* Either we extend BitSet or use a bitset as a field. In the former case, we have to override
   * all the mutators which is risky to say the least.
   */
  private[this] val bitset: BitSet = new BitSet
  private[this] var stack: Stack[UInt] = Stack()
  /**
   * Inserts `v` in case it was not present before.
   * @param v The new value.
   */
  def insert(v: UInt): Unit = if (bitset.add(v.signed)) discard { stack.push(v) }
  /**
   * Checks for containment.
   * @param v The value to check.
   * @return Whether it contained `v` or not.
   */
  def contains(v: UInt): Boolean = bitset.contains(v.signed)
  /**
   * @return The [[UIntSet]] is empty or not.
   */
  override def isEmpty: Boolean = stack.isEmpty
  
  /**
   * Removes the last added element.
   * @throws NoSuchElementException in case this was empty.
   */
  @throws[NoSuchElementException]
  def erase(): UInt = {
    val v = stack.pop
    discard { bitset.remove(v.signed) }
    v
  }
  /**
   * @return The values added in reverse order.
   */
  override def iterator = stack.iterator
  /**
   * Removes all elements.
   */
  def clear(): Unit = { bitset.clear(); stack.clear() }
}