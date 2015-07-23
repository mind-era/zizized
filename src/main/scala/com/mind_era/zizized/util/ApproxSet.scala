/*
 * Copyright (c) 2015 Mind Eratosthenes Kft.
 * License: AGPL v3
 */
package com.mind_era.zizized.util

import scala.collection.mutable.BitSet

/**
 * ApproxSet: port of /util/approx_set.h and /util/approx_set.cpp
 * 
 * A data structure for set approximation.
 * 
 * Initialized by a function e2u mapping the underlying type T to Ints, maintains a bitset
 * such that element e : T is represented by the e2u(e)&63-th bit.
 * Thus collisions can occur, that's why it's "approximate".
 * 
 * This implementation is mutable.
 * 
 * @author Szabolcs Ivan
 * @since 1.0
 */
class ApproxSet[T] ( val e2u : (T) => Int, val capacity : Int = 64) extends Iterable[ Int ]{
  /**
   * Instead of a Long we use a BitSet here 
   */
  val bitset : BitSet = new BitSet( capacity );
  /**
   * e2s : maps a position of the bitset to the object e : T
   */
  def e2s( e: T ): Int = { u2s(e2u(e)) }
  /**
   * u2s: maps an int to a position of the bitset 
   */
  def u2s( u: Int ) : Int = Math.floorMod( u, capacity )
  /**
   * insert: inserts the value's hashcode to the bitset
   */
  def insert( e: T ): Boolean = bitset.add( e2s(e))
  def remove( e: T ): Boolean = bitset.remove( e2s(e))
  /**
   * mayContain: returns true iff the bitset contains the hashcode of the object. 
   */
  def mayContain( e: T ): Boolean = bitset.contains( e2s(e))
  /**
   * mustNotContain: returns true iff the bitset does not contain the hashcode of the object.
   */
  def mustNotContain( e: T ): Boolean = !bitset.contains( e2s(e) )
  
  /**
   * Boolean operators |= (union), &= (intersection), -= (difference) mutating the object.
   */
  def |=( rhs: ApproxSet[T] ) : ApproxSet[T] = { discard{ bitset |= rhs.bitset }; this }  
  def &=( rhs: ApproxSet[T] ) : ApproxSet[T] = { discard{ bitset &= rhs.bitset }; this }
  def -=( rhs: ApproxSet[T] ) : ApproxSet[T] = { discard{ bitset &~= rhs.bitset }; this }
  /**
   * Boolean operators | (union), & (intersection), - (difference) constructing a new object. 
   */
  def |( rhs: ApproxSet[T] ) : ApproxSet[T] = { clone() |= rhs }
  def &( rhs: ApproxSet[T] ) : ApproxSet[T] = { clone() &= rhs }
  def -( rhs: ApproxSet[T] ) : ApproxSet[T] = { clone() -= rhs }

  /**
   * isEmpty returns true iff the bitset is empty
   */
  override def isEmpty : Boolean = bitset.isEmpty
  
  /**
   * H.mustNotSubsume( K ) holds iff K contains a hash code not present in H
   */
  def mustNotSubsume( rhs: ApproxSet[T] ) : Boolean = !rhs.bitset.subsetOf( bitset )
  
  /**
   * H.maySubsetOf( K ) holds iff each hash of H also appears in K as well 
   */
  def maySubsetOf( rhs: ApproxSet[T] ) : Boolean = bitset.subsetOf( rhs.bitset )
  
  /**
   * H.mayEqual( K ) iff the set of the stored hashes coincide 
   */
  def mayEqual( rhs: ApproxSet[T] ) : Boolean = bitset.equals( rhs.bitset )
  
  /**
   * Clears the set.
   */
  def clear: ApproxSet[T] = { bitset.clear(); this }

  /**
   * H.isDisjointFrom( K ) if they have no common hash.
   */
  def isDisjointFrom( rhs: ApproxSet[T] ) : Boolean = (bitset & rhs.bitset).isEmpty
  
  /**
   * size returns the number of distinct hash codes.
   */
  override def size : Int = bitset.size
  
  /**
   * iterator iterates through the stored hashcodes (from 0 up to capacity-1) 
   */
  def iterator = bitset.iterator
  /**
   * toString returns a string representation of the bitset of the form e.g. {1,2,4}
   */
  override def toString : String = iterator.mkString("{", ",", "}")
  
  /**
   * clones an ApproxSet
   * TODO type safety for subclasses
   */
  override def clone() : ApproxSet[T] = { 
    val ret = new ApproxSet[T]( e2u )
    discard{ ret.bitset |= bitset }
    ret
  } 
}

object ApproxSet {
  def apply[T]( e2u: (T) => Int ) : ApproxSet[T] = new ApproxSet[T](e2u)
  /**
   * Constructs a singleton approxset containing the element e.
   */
  def apply[T]( e2u: (T) => Int, e : T ) : ApproxSet[T] = { 
    val ret = new ApproxSet[T]( e2u )
    discard{ ret.insert(e) }
    ret
  }
  /**
   * Constructs an approxset containing the elements of the array.
   */
  def apply[T]( e2u: (T) => Int, iterable: Iterable[T] ) : ApproxSet[T] = {
    val ret = new ApproxSet[T]( e2u )
    iterable.foreach( ret.insert(_) )
    ret
  }
}

//No idea why or how Nothing was inferred.
@SuppressWarnings(Array("org.brianmckenna.wartremover.warts.Nothing"))
class IntApproxSet extends ApproxSet[ Int ]( ((n:Int) => n): Int => Int ){}
