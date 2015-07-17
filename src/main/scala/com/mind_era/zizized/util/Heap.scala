/*
 * Copyright (c) 2015 Mind Eratosthenes Kft.
 * License: AGPL v3
 */
package com.mind_era.zizized.util

import scala.collection.concurrent.TrieMap
import scala.collection.generic.Growable

/**
 * TODO document package com.mind_era.zizized.util.Heap
 * 
 * @author Szabolcs Ivan
 * @since version
 */
class Heap[ T ] ( val ordering : Ordering[T], val defaultT : T ) extends Iterable[ T ]{
  val m_values : Vector[T] = new Vector[T]()
  m_values( 0 ) = defaultT
  val m_valuesToIndices : scala.collection.mutable.Map[ T, Int ] = new TrieMap[ T, Int ]()
  
  def set( idx : Int, value : T ) : Unit = {
    m_values( idx ) = value
    m_valuesToIndices( value ) = idx
  }
  
  def moveUp( idx: Int, value: T ) : Unit = {
    val parentIdx : Int = Heap.parent( idx )
    if( (parentIdx > 0) && (ordering.lt(m_values(parentIdx),value))) {
      set( idx, m_values( parentIdx ) )
      moveUp( parentIdx, value )
    } else set( idx, value )
  }  
  def moveUp( idx: Int ) : Unit = moveUp( idx, m_values(idx) )
  
  def moveDown( idx : Int, value : T ) : Unit = {
    val sz : Int = m_values.size
    val leftIdx = Heap.left( idx )
    if( leftIdx < sz ){
      val rightIdx = Heap.right( idx )
      val minIdx = if( rightIdx < sz && ordering.lt( m_values(rightIdx), m_values(leftIdx)) ) rightIdx else leftIdx
      val minVal = m_values( minIdx )
      if( ordering.lt( minVal, value ) ){
        set( idx, minVal )
        moveDown( minIdx, value )
      }else set( idx, value )
    }else set( idx, value )
  }
  def moveDown( idx : Int) : Unit = moveDown( idx, m_values( idx ) )
  override def isEmpty : Boolean = m_values.size == 1
  override def iterator : Iterator[T] = { val it = m_values.iterator; it.next; it }
  def contains( value : T ) : Boolean = m_valuesToIndices.contains( value )
  def clear() : Unit = { m_values.clear(); m_values(0) = defaultT; m_valuesToIndices.clear() }
  def getBounds() : T = { m_valuesToIndices.keySet.max( ordering ) }
  def minValue : T = { m_values(1) }
  override def size : Int = m_values.size - 1
  def eraseMin : T = {
    val result = minValue
    m_valuesToIndices.remove( result )
    if( size > 1 ) {
      set( 1, m_values( m_values.size - 1 ) )
      m_values.remove( m_values.size - 1 )
      moveDown( 1 )
    } else {
      m_values.remove( 1 )
    }
    result
  }
  def erase( value : T ) : Unit = {
    val idx : Int = m_valuesToIndices.remove( value ) match { case Some( n ) => n; case None => -1 }
    if( idx == m_values.size - 1 ){
      m_values.remove( m_values.size - 1 )
    } else {
      val lastValue = m_values( m_values.size - 1 )
      set( idx, lastValue )
      m_values.remove( m_values.size - 1 )
      handleChange( idx )
    }    
  }
  def handleChange( idx: Int ) : Unit = {
    val parentIdx = Heap.parent( idx )
    if( parentIdx > 0 && ordering.lt( m_values(idx), m_values(parentIdx))) moveUp( idx ) else moveDown( idx )
  }
  def decreased( value : T ) : Unit = moveUp( m_valuesToIndices( value ))
  def increased( value : T ) : Unit = moveDown( m_valuesToIndices( value ))
  def insert( value : T ) : Unit = {
    val idx : Int = m_values.size
    set( idx, value )
    moveUp( idx, value )
  }
  def findLE( value : T , result : Growable[T], idx : Int ){
    if(( idx < m_values.size )&& ( ordering.lteq(m_values(idx), value) ) ){
      result += m_values(idx)
      findLE( value, result, Heap.left( idx ))
      findLE( value, result, Heap.right( idx ))
    }
  }
}

object Heap {
  def left( i : Int ) : Int = 2 * i
  def right( i : Int ) : Int = 2 * i + 1
  def parent( i : Int ) : Int = i / 2
}