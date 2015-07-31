/*
 * Copyright (c) 2015 Mind Eratosthenes Kft.
 * License: AGPL v3
 */
package com.mind_era.zizized.util

import scala.collection.mutable.HashMap
import spire.math.UInt
import scala.collection.generic.Growable
import scala.collection.SortedMap
import scala.collection.immutable.SortedSet
import scala.collection.immutable.TreeSet


/**
 * String, [UInt or Double] map. 

 * @author Szabolcs Ivan 
 * @since 1.0
 */
class Statistics {
  val map : HashMap[ String, Either[UInt, Double]  ] = HashMap(); 
  
  /** TODO : figure out the intended semantics, why is zero special */
  def update( key : String, inc : UInt ) : Unit = 
    if( inc != UInt(0) ) discard{ map += ( (key, Left(inc) ) ) } 
  def update( key : String, inc : Double ) : Unit = 
    if( inc != 0.0 ) discard{ map += ((key,Right(inc))) }
  /** TODO : figure out the intended semantics, what if maps are nonempty */
  def copy( stats : Statistics ) : Unit = discard { map ++= stats.map }
  def reset() : Unit = map.clear()
  def display( f : (String) => String ) : String = {
    val max = Statistics.getMaxLength( map.keySet )
    val sortedEntries : Vector[(String,Either[UInt,Double])] = 
      map.to[Vector].sortBy[String]( { case (s,e) => s; case _ => "" } )

   sortedEntries.map( { case (s,e) => e match {
      case Left(u) => f(s).padTo(max+1, ' ') + u 
      case Right(d) => f(s).padTo(max+1,' ') + "%.2f".format(d) 
      case _ => ""
    } ; case _=> "" } ).mkString("(", "\n", ")")
  }
  def display : String = display( s => s )
  def displaySmt2 : String = display( Statistics.displaySmt2Key )
  def displayInternal : String = display( s => s.toUpperCase().replace(' ', '_') )
}

object Statistics {
  /** TODO : this one probably has absolutely no point */
  def makeMap[K,V]( vec : TraversableOnce[ (K,V) ], map : Growable[(K,V)] ) : Unit = discard { map ++= vec }
  def getKeys[K,V]( map : TraversableOnce[ (K,V) ], keys : Growable[ K ] ) : Unit ={
    discard{ keys ++= map.map( k => k._1 ) }
  }
  def displaySmt2Key( key : String ) : String = {
    val keyP : String = if(( key.length() > 0 )&&( key(0) == ':')) key else ":" + key
    keyP.map( c => if( Smt.isSmt2SimpleSymbolChar( c ) ) c else '-')
  }
  def getMaxLength( keys : TraversableOnce[ String ]) : Int = {
    keys.map( s => if( s(0) == ':' ) s.length() - 1 else s.length() ).fold(0)(Math.max(_,_))
  }
  
}