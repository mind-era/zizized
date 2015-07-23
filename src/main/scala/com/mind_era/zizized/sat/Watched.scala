/*
 * Copyright (c) 2015 Mind Eratosthenes Kft.
 * License: AGPL v3
 */
package com.mind_era.zizized.sat

import com.mind_era.zizized.sat.SatTypes._
import com.mind_era.zizized.util._
import spire.math.UInt

/**
 * TODO document package com.mind_era.zizized.sat.Watched
 * 
 * @author Szabolcs Ivan
 * @since version
 */

/**
 * From sat/sat_watched.h :
       A watched element can be:
       
       1) A literal:               for watched binary clauses
       2) A pair of literals:      for watched ternary clauses
       3) A pair (literal, clause-offset): for watched clauses, where the first element of the pair is a literal of the clause.
       4) A external constraint-idx: for external constraints.
       For binary clauses: we use a bit to store whether the binary clause was learned or not.
       
       Remark: there is not Clause object for binary clauses.
    */

/**
 * Case class, then.
 * Unimplemented methods that should be handled by pattern matching:
 * get_kind(), is_binary_clause(), is_ternary_clause(), is_clause(), is_ext_constraint
 * TODO: it might be better to use the auto-generated getters of case classes
 */


sealed trait Watched
final case class BinaryWatched( var literal : Literal, var learned : Boolean ) extends Watched {
  def getLiteral = literal
  def setLiteral( l : Literal ) : Unit = {literal = l}
  def isLearned = learned
  def markNotLearned() : Unit = {learned = false}
  def isNonLearned() : Boolean = !learned  
}
final case class TernaryWatched( val l1 : Literal, val l2 : Literal ) extends Watched {
  def getLiteral1 = l1
  def getLiteral2 = l2  
}
final case class ClauseWatched( var blockedLiteral : Literal, var clauseOffset : ClauseOffset ) extends Watched {
  def getClauseOffset = clauseOffset
  def setClauseOffset( c : ClauseOffset ) : Unit = clauseOffset = c
  def getBlockedLiteral = blockedLiteral
  def setBlockedLiteral( l : Literal ) : Unit = blockedLiteral = l
  def setClause( l : Literal, c : ClauseOffset ) : Unit = {setClauseOffset( c ); setBlockedLiteral( l )}  
}
final case class ConstraintWatched( val constraintIndex : ExtConstraintIdx ) extends Watched {
  def getExtConstraintIdx = constraintIndex
}

object Watched {
  def eraseClauseWatch( wList : WatchList, c : ClauseOffset ) : Boolean = {
    val filter = wList.filter { x => { x match { case ClauseWatched( l, c1 ) => c1 == c; case _ => false } } }
    if( filter.isEmpty ) false else { discard{ wList --= filter}; true }
  }
  def eraseTernaryWatch( wList : WatchList, l1 : Literal, l2 : Literal ) : Unit = { 
    discard {  wList -= TernaryWatched( l1, l2 ) }
  }
  def displayWatchList( clauseAllocator : ClauseAllocator, wList : WatchList ) : String = {
    wList.map { watched => { watched match {
      case BinaryWatched( literal, learned ) => if( learned ) literal.toString() + "*" else literal.toString()
      case TernaryWatched( l1, l2 ) => "(" + l1.toString() + " " + l2.toString() + ")"
      case ClauseWatched( literal, offset ) => "(" + literal + " " + clauseAllocator.getClause( offset ) + ")"
      case ConstraintWatched( c ) => c.toString()
      case _ => ""
    }} }.mkString(" ")
  }
}