/*
 * Copyright (c) 2015 Mind Eratosthenes Kft.
 * License: AGPL v3
 */
package com.mind_era.zizized.sat

import com.mind_era.zizized.util._
import spire.math.UInt
import scala.collection.mutable.Stack

/**
 * TODO document package com.mind_era.zizized.sat.IdGenerator
 * 
 * @author Szabolcs Ivan
 * @since 1.0
 */
class IdGenerator( var nextId : UInt = UInt(0) ) {
  val freeIds : Stack[ UInt ] = new Stack[ UInt ]
  def mk() : UInt = if( freeIds.isEmpty ) { val r = nextId; nextId += UInt(1) ; r} else { freeIds.pop() }
  def recycle( id : UInt ) : Unit = discard { freeIds.push( id ) }
  def reset( start : UInt = UInt(0) ) : Unit = { nextId = start; freeIds.clear() }
  def cleanup( start : UInt = UInt(0) ) : Unit = reset( start ) // TODO probably should be merged with reset(_)
  def showHash() : Int = 0 // TODO implement string hashing
  def getIdRange : UInt = nextId
  def setNextId( id : UInt ) : UInt= { 
    nextId = id;
    while( freeIds.contains( id ) ) { nextId = nextId + UInt(1) };
    nextId 
  }
  def displayFreeIds : String = freeIds.mkString(",") //TODO check that ::display func
}