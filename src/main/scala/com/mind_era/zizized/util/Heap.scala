/*
 * Copyright (c) 2015 Mind Eratosthenes Kft.
 * License: AGPL v3
 */
package com.mind_era.zizized.util

import scala.collection.concurrent.TrieMap
import scala.collection.generic.Growable

/**
 * Standard min-heap implementation of a priority queue. Keys' priorities can be modified
 * via the Ordering[T] object supplied to the constructor. The heap is also Iterable but
 * the iterator DOES NOT respect the ordering.
 * Objects of type T should have a good hash code since hash is used for reverse lookup.
 *
 * @constructor creates an empty heap which is capable of holding objects of type T.
 * @param ordering A total linear ordering on T.
 * @param defaultT a default placeholder value of type T
 *
 * TODO placeholder is needed only for filling the zeroth, unused position in the underlying vector.
 * Maybe the code could be refactored to use indices 0..N-1 instead of 1..N for a heap of size N.
 *
 * @author Szabolcs Ivan
 * @since 1.0
 */
class Heap[T](val ordering: Ordering[T], val defaultT: T) extends Iterable[T] {
  /** The underlying Vector container. */
  private val m_values: Vector[T] = new Vector[T]()
  discard { m_values += defaultT }
  /** Stores to each object in the heap its position in the vector. Needed for lookup. */
  private val m_valuesToIndices: scala.collection.mutable.Map[T, Int] = new TrieMap[T, Int]()

  /** Internal method to set the ith cell of the heap to value and the reverse lookup as well. */
  private def set(idx: Int, value: T): Unit = {
    m_values(idx) = value
    m_valuesToIndices(value) = idx
  }

  /** Called when the priority of the element value decreases and moves up in the heap from position idx. */
  private def moveUp(idx: Int, value: T): Unit = {
    val parentIdx: Int = Heap.parent(idx)
    if ((parentIdx > 0) && (ordering.lt(value, m_values(parentIdx)))) {
      set(idx, m_values(parentIdx))
      moveUp(parentIdx, value)
    } else set(idx, value)
  }
  /** Called when the priority of the element at position idx decreases. */
  private def moveUp(idx: Int): Unit = moveUp(idx, m_values(idx))

  /** Called when the priority of the element value increases and moves down in the heap from position idx. */
  private def moveDown(idx: Int, value: T): Unit = {
    val sz: Int = m_values.size
    val leftIdx = Heap.left(idx)
    if (leftIdx < sz) {
      val rightIdx = Heap.right(idx)
      val minIdx = if (rightIdx < sz && ordering.lt(m_values(rightIdx), m_values(leftIdx))) rightIdx else leftIdx
      val minVal = m_values(minIdx)
      if (ordering.lt(minVal, value)) {
        set(idx, minVal)
        moveDown(minIdx, value)
      } else set(idx, value)
    } else set(idx, value)
  }
  /** Called when the priority of the element at position idx increases. */
  private def moveDown(idx: Int): Unit = moveDown(idx, m_values(idx))
  /** Returns whether the heap is empty. */
  override def isEmpty: Boolean = m_values.size == 1
  /** Returns an iterator on the elements which DOES NOT respect the ordering of the elements. */
  override def iterator: Iterator[T] = { val it = m_values.iterator; discard { it.next }; it }
  /** Returns whether the heap contains a value. */
  def contains(value: T): Boolean = m_valuesToIndices.contains(value)
  /** Clears the heap. */
  def clear(): Unit = { discard { m_values.drop(1) }; m_valuesToIndices.clear() }
  /** Returns the maximum value present in the heap. Probably used only for debugging. */
  def getBounds(): T = { m_valuesToIndices.keySet.max(ordering) }
  /** Returns the element of the heap having the least priority. */
  def minValue: T = { m_values(1) }
  /** Returns the size of the heap. */
  override def size: Int = m_values.size - 1
  /** Returns and removes the element of the heap having the least priority. */
  def eraseMin(): T = {
    val result = minValue
    discard { m_valuesToIndices.remove(result) }
    discard[Any] {
      if (size > 1) {
        set(1, m_values(m_values.size - 1))
        discard { m_values.remove(m_values.size - 1) }
        moveDown(1)
      } else {
        m_values.remove(1)
      }
    }
    result
  }
  /** Removes a value from the heap. */
  def erase(value: T): Unit = {
    val idx: Int = m_valuesToIndices.remove(value).getOrElse(-1)
    if (idx == m_values.size - 1) {
      discard { m_values.remove(m_values.size - 1) }
    } else {
      //
      val lastValue = m_values(m_values.size - 1)
      set(idx, lastValue)
      discard { m_values.remove(m_values.size - 1) }
      handleChange(idx)
    }
  }
  /** Called when the element's priority at position idx changes. */
  private def handleChange(idx: Int): Unit = {
    val parentIdx = Heap.parent(idx)
    if (parentIdx > 0 && ordering.lt(m_values(idx), m_values(parentIdx))) moveUp(idx) else moveDown(idx)
  }
  /** Should be called by hand whenever the priority of value decreases. */
  def decreased(value: T): Unit = moveUp(m_valuesToIndices(value))
  /** Should be called by hand whenever the priority of value increases. */
  def increased(value: T): Unit = moveDown(m_valuesToIndices(value))
  /** Should be called by hand whenever the priority of value changes (either increases or decreases but is not known to caller which happened). */
  def changed(value: T): Unit = handleChange(m_valuesToIndices(value))
  /** Inserts value into the heap. */
  def insert(value: T): Unit = {
    val idx: Int = m_values.size
    discard { m_values += value }
    m_valuesToIndices(value) = idx
    assert(m_values(idx) == value)
    moveUp(idx, value)
  }
  /** Collects all the elements <= value of the subheap rooted at idx into result, NOT according to the ordering. */
  private def findLE(value: T, result: Growable[T], idx: Int): Unit = {
    if ((idx < m_values.size) && (ordering.lteq(m_values(idx), value))) {
      discard { result += m_values(idx) }
      findLE(value, result, Heap.left(idx))
      findLE(value, result, Heap.right(idx))
    }
  }
  /** Collects all the elements of the heap <= value into result, NOT according to the ordering. */
  def findLE(value: T, result: Growable[T]): Unit = findLE(value, result, 1)
  def isWellFormed: Boolean = {
    var i = 0
    var ret = true
    for (i <- 2 to m_values.size - 1) {
      if (ordering.gt(m_values(Heap.parent(i)), m_values(i))) ret = false
    }
    ret
  }
}

object Heap {
  /** Returns the position of the left son of position i */
  def left(i: Int): Int = 2 * i
  /** Returns the position of the right son of position i */
  def right(i: Int): Int = 2 * i + 1
  /** Returns the position of the parent of position i */
  def parent(i: Int): Int = i / 2
}