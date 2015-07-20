/*
 * Copyright (c) 2015 Mind Eratosthenes Kft.
 * License: AGPL v3
 */
package com.mind_era.zizized.sat

import com.mind_era.zizized.sat.SatTypes._
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
 */


sealed trait Watched
final case class BinaryWatched( val l : Literal, val isLearned : Boolean ) extends Watched {}
final case class TernaryWatched( val l1 : Literal, val l2 : Literal ) extends Watched {}
final case class ClauseWatched( val blockedLiteral : Literal, val clauseOffset : ClauseOffset ) {}
final case class ConstraintWatched( val constrintIndex : ExtConstraintIdx ) {}