/*
 * Copyright (c) 2015 Mind Eratosthenes Kft.
 * License: AGPL v3
 */
package com.mind_era.zizized.sat

import com.mind_era.zizized.util.Heap
import com.mind_era.zizized.sat.SatTypes._

/**
 * TODO document package com.mind_era.zizized.sat.VarQueue
 * 
 * @author Szabolcs Ivan
 * @since version
 */

class ActivityOrdering( val activity : BoolVar => Int) extends Ordering[ BoolVar ]{
  override def compare( a : BoolVar, b : BoolVar ) : Int = activity(b) - activity(a)
}

class VarQueue( val activity : BoolVar => Int ) {
  val queue : Heap[ BoolVar ] = new Heap( new ActivityOrdering( b => activity(b) ), NullBoolVar )
  def activityIncreasedEh( b : BoolVar ) : Unit = if( queue.contains( b )) queue.decreased( b )
  def mkVarEh( b : BoolVar ) : Unit = queue.insert( b )
  def delVarEh( b : BoolVar ) : Unit = if( queue.contains( b )) queue.erase( b )
  def unassignVarEh( b : BoolVar ) : Unit = if( !queue.contains(b) ) queue.insert( b )
  def clear() : Unit = queue.clear()
  def isEmpty : Boolean = queue.isEmpty
  def nextVar : BoolVar = queue.eraseMin
}