/*
 * Copyright (c) 2015 Mind Eratosthenes Kft.
 * License: AGPL v3
 */
package com.mind_era.zizized.sat

import com.mind_era.zizized.sat.SatTypes._
import com.mind_era.zizized.util._
import spire.math.UInt
import com.mind_era.zizized.util.IdGenerator

sealed trait ClauseWrapper extends Iterable[ Literal ] {
  def apply( i : Int ) : Literal
  def contains( l : Literal ) : Boolean
  def contains( v : BoolVar ) : Boolean
}

/**
 * TODO document package com.mind_era.zizized.sat.Clause
 * 
 * not implemented: get_size, get_obj_size
 * 
 * Constructor made private since ID should never be supplied by the user. Call companion object's apply. 
 * 
 * @author Szabolcs Ivan
 * @since 1.0
 */
class Clause private ( val id : UInt, literals : Iterable[ Literal ], learned : Boolean) extends ClauseWrapper{
  val capacity = literals.size // TODO this field is probably safe to remove
  var varApproxSet : VarApproxSet = new VarApproxSet( x => x.toInt );
  // TODO consider using a BitSet here instead of this one for status
  var status : Int = Clause.LEARNED // STRENGTHENED | REMOVED | LEARNED | USED | FROZEN | REINIT_STACK
  var inactRounds : Int = 0
  var glue : Int = 0;
  var psm : Int = 0;
  var lits : Vector[ Literal ] = new Vector[ Literal ]()
  discard { lits ++=( literals ) }
  markStrengthened()
  
  override def size : Int = lits.size
  def apply( i : Int ) : Literal = lits( i )
  def isLearned : Boolean = (status & Clause.LEARNED) != 0
  def unsetLearned() : Unit = status &= ~Clause.LEARNED
  def shrink( numLits : Int ) : Unit = { discard { lits.drop( numLits ) }; markStrengthened() }
  def isStrengthened : Boolean = (status & Clause.STRENGTHENED) != 0
  def markStrengthened() : Unit = { status |= Clause.STRENGTHENED ; updateApprox() }
  def unmarkStrengthened() : Unit = { status &= ~Clause.STRENGTHENED }
  def elim( l : Literal ) : Unit = { discard { lits -= l } ; markStrengthened() }
  def isRemoved : Boolean = (status & Clause.REMOVED ) != 0
  def setRemoved( f : Boolean ) : Unit = if ( f ) status |= Clause.REMOVED else status &= ~Clause.REMOVED
  def approx : VarApproxSet = varApproxSet
  def approx( ls : Iterable[ Literal ] ) : VarApproxSet = {
    discard { varApproxSet.clear }
    varApproxSet ++= ( ls map ( l => l.variable ) )
  }
  def updateApprox() : Unit = discard { approx( lits ) }
  def checkApprox : Boolean = true // TODO port from cpp if needed -- serves debug purposes only
  override def iterator : Iterator[ Literal ] = lits.iterator
  def contains( l : Literal ) : Boolean = lits.contains( l )
  def contains( b : BoolVar ) : Boolean = lits.find { l => (l.variable == b) } match { case Some(l) => true; case _ => false }
  def satisfiedBy( m : Model ) : Boolean = true // TODO port from cpp
  def markUsed() : Unit = { status |= Clause.USED }
  def unmarkUsed() : Unit = { status &= ~Clause.USED }
  def isUsed : Boolean = { (status & Clause.USED ) != 0 }
  def incInactRounds() : Unit = inactRounds += 1
  def resetInactRounds() : Unit = inactRounds = 0
  def getInactRounds : Int = inactRounds
  def isFrozen : Boolean = { (status & Clause.FROZEN) != 0 }
  def freeze() : Unit = { status |= Clause.FROZEN }
  def unfreeze() : Unit = { status &= ~Clause.FROZEN }
  def setGlue( g : UInt ) : Unit = { glue = Math.min( g.toInt, 255 ) }
  def getGlue : Int = glue
  def setPsm( p : UInt ) : Unit = { psm = Math.min( p.toInt, 255 )}
  def getPsm() : Int = psm 
  def onReinitStack : Boolean = ( status & Clause.REINIT_STACK ) != 0
  def setReinitStack( b : Boolean ) : Unit = if ( b ) status |= Clause.REINIT_STACK else status &= ~Clause.REINIT_STACK
  override def toString = {
    var suffix = ""
    if ( isRemoved ) suffix = suffix + "x"
    if ( isStrengthened ) suffix = suffix + "+"
    if ( isLearned ) suffix = suffix + "*"
    lits.mkString("("," ",")" + suffix);
  }
}

object Clause {
  val STRENGTHENED = 1
  val REMOVED = 2
  val LEARNED = 4
  val USED = 8
  val FROZEN = 16
  val REINIT_STACK = 32

  val idGen : IdGenerator = new IdGenerator()
  
  // TODO: this is transformed from tmp_clause.set(). assumption is that a tmp_clause can be set exactly once. Check it.
  def apply( lits: Iterable[ Literal ], learned: Boolean ) : Clause = new Clause( idGen.mk(), lits, learned )
  def apply( literal1 : Literal, literal2 : Literal, learned : Boolean ) : Clause = Clause( Array(literal1,literal2), learned )
  def apply( binaryClause : BinaryClause ) : Clause = Clause( binaryClause.literal1, binaryClause.literal2, binaryClause.isLearned )
  
}

/**
 * for some reason, probably efficiency, binary clauses are treated separately
 */
final case class BinaryClause( val literal1 : Literal, val literal2 : Literal, val isLearned : Boolean ) extends ClauseWrapper {
  override def size = 2
  override def apply( i : Int ) = if ( i == 0 ) literal1 else literal2 
  override def iterator : Iterator[ Literal ] = Array( literal1 , literal2 ).iterator
  override def contains( l : Literal ) = ( literal1 == l || literal2 == l )
  override def contains( b : BoolVar ) = ( literal1.variable == b || literal2.variable == b )
  override def toString() : String = "(" + literal1.toString() + " " + literal2.toString() + ")"
} 

