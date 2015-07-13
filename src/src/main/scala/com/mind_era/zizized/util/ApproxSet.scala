/*
 * Copyright (c) 2015 Mind Eratosthenes Kft.
 * License: AGPL v3
 */
package com.mind_era.zizized.util

/**
 * TODO document package util.ApproxSet
 * ApproxSet: port of /util/approx_set.h and /util/approx_set.cpp
 * 
 * A data structure for set approximation.
 * 
 * Initialized by a function e2u mapping the underlying type T to Ints, maintains a bitset
 * (represented internally by a Long) such that element e : T is represented by the e2u(e)&63-th bit.
 * Thus collisions can occur, that's why it's "approximate".
 * 
 * This implementation is mutable.
 * 
 * TODO: the original implementation wastes resources and has inconsistent naming issues, several methods are repeated. Clean them up.
 * TODO: implement Iterator.  
 * 
 * @author Szabolcs Ivan
 * @since version
 */
class ApproxSet[T]( val e2u : (T) => Int, set : Long  = ApproxSet.zero ) {
  /**
   * the Long member encoding the bit field 
   */
  var m_set : Long = set
  def e2s( e: T ): Long = { ApproxSet.u2s(e2u(e)) }
  def insert( e: T ): Unit = { m_set |= e2s( e ) }
  def mayContain( e: T ): Boolean = { (m_set & e2s(e))!=ApproxSet.zero }
  def mustNotContain( e: T ): Boolean = { !(mayContain( e )) }
  def |=( rhs: ApproxSet[T] ) : ApproxSet[T] = { m_set |= rhs.m_set; this }
  def &=( rhs: ApproxSet[T] ) : ApproxSet[T] = { m_set &= rhs.m_set; this }
  def -=( rhs: ApproxSet[T] ) : ApproxSet[T] = { m_set &= ~rhs.m_set; this }
  def isEmpty : Boolean = { m_set == ApproxSet.zero }
  def mustNotSubset( rhs: ApproxSet[T] ) : Boolean = { (m_set & ~rhs.m_set) != ApproxSet.zero }
  def mustNotSubsume( rhs: ApproxSet[T] ) : Boolean = mustNotSubset( rhs ) //TODO: why?
  def equiv( rhs: ApproxSet[T] ) : Boolean = { m_set == rhs.m_set } //TODO: clean up namings
  def reset: ApproxSet[T] = { m_set = ApproxSet.zero; this}
  def emptyIntersection( rhs: ApproxSet[T] ) : Boolean = { (m_set & rhs.m_set) == ApproxSet.zero }
  
}

object ApproxSet {
  val capacity : Int = 64
  val zero : Long = 0
  val one : Long = 1
  def r2s[T]( e2u: (T) => Int, s : Long ) : ApproxSet[T] =  { new ApproxSet[T]( e2u, s ) }
  def u2s( u: Int ) : Long = { ApproxSet.one << (u & ( ApproxSet.capacity - 1)) }
  def apply[T]( e2u: (T) => Int, e : T ) : ApproxSet[T] = { new ApproxSet[T]( e2u, u2s(e2u(e) ) ) }
  def apply[T]( e2u: (T) => Int, size: Int, a : Array[T] ) : ApproxSet[T] = {
    val ret = new ApproxSet[T]( e2u )
    a.foreach( ret.insert(_) )
    ret
  }
  def union[T]( e2u: (T) => Int, a: ApproxSet[T], b: ApproxSet[T] ): ApproxSet[T] = { new ApproxSet[T]( e2u, a.m_set | b.m_set ) }
  def intersection[T]( e2u: (T) => Int, a: ApproxSet[T], b: ApproxSet[T] ): ApproxSet[T] = { new ApproxSet[T]( e2u, a.m_set & b.m_set ) }
  def mustNotSubset[T]( a: ApproxSet[T], b: ApproxSet[T] ) : Boolean = { a.mustNotSubset( b ) }
  def mustNotSubsume[T]( a: ApproxSet[T], b: ApproxSet[T] ) : Boolean = { a.mustNotSubsume( b ) }
  def mustNotEq[T]( a: ApproxSet[T], b: ApproxSet[T] ) : Boolean = { a.m_set != b.m_set }
  def mayEq[T]( a: ApproxSet[T], b: ApproxSet[T] ) : Boolean = { a.m_set == b.m_set }
  def equiv[T]( a: ApproxSet[T], b: ApproxSet[T] ) : Boolean = { a.m_set == b.m_set } //TODO: why??
  def approxSubset[T]( a: ApproxSet[T], b: ApproxSet[T]): Boolean = { !a.mustNotSubset(b) } // TODO: FIX NAMING ISSUES HERE
  
  
}