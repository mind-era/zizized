/*
 * Copyright (c) 2015 Mind Eratosthenes Kft.
 * License: AGPL v3
 */
package com.mind_era.zizized.sat

import scala.collection.mutable.BitSet
import spire.math.UInt

/**
 * UIntSet (in util/sat_types.h) is a bitset of unsigned ints, with an additional method
 * erase() which erases the element of the set THAT WAS INSERTED LAST.
 * 
 * We can view this also as a Stack of unsigneds with an added bitset for a quick contains() method. 
 * 
 * @author Szabolcs Ivan
 * @since version
 */
class UIntSet extends Iterable[ Int ]{
  /* Either we extend BitSet or use a bitset as a field. In the former case, we have to override
   * all the mutators which is risky to say the least.
   */
  val bitset : BitSet = new BitSet
  var stack : List[Int] = List()
  def insert( v : Int ) : Unit = if( bitset.add( v ) ) stack = v :: stack
  def contains( v : Int ) : Boolean = bitset.contains( v )
  override def isEmpty : Boolean = stack.isEmpty
  def erase() : Int = {
    val v = stack.head
    stack = stack.drop(1)
    bitset.remove( v )
    v
  }
  override def iterator = stack.iterator
  def clear() : Unit = { bitset.clear(); stack = List() }
}