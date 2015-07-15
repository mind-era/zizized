/*
 * Copyright (c) 2015 Mind Eratosthenes Kft.
 * License: AGPL v3
 */
package com.mind_era.zizized.util

package object util {
  // scala's Vector is at least as good as the one in "util/vector.h"
  // TODO this way it's util.util.Vector which is ugly a bit
  type Vector[T] = scala.`package`.Vector[T]
}

/**
 * util/lbool.h and util/lbool.cpp : ternary Boolean value
 * operator<< is now toString()
 * to_sat_str( LBool* ) is now a member toSatString() of trait LBool
 * there are three case objects
 * LBool( true ) and LBool( false ) returns the case objects TRUE and FALSE respectively
 * unary negation ~ maps TRUE to FALSE and FALSE to TRUE, leaves UNDEF unchanged
 */ 
sealed trait LBool {
  /**
   * ~TRUE = FALSE, ~FALSE = TRUE, ~UNDEF = UNDEF
   */
  def unary_~ : LBool = if (this == TRUE) FALSE else if ( this == FALSE )  TRUE else UNDEF
  /**
   * LBool( true ) returns TRUE, LBool( false ) returns FALSE
   */
  def apply( b : Boolean ) : LBool = if ( b ) TRUE else FALSE
  def toSatString : String
}
case object TRUE extends LBool {
  override def toString() : String = "l_true"
  val toSatString : String = "satisfiable"
}
case object FALSE extends LBool {
  override def toString() : String = "l_false"
  val toSatString : String = "unsatisfiable"
}
case object UNDEF extends LBool {
  override def toString() : String = "l_undef"
  val toSatString : String = "unknonwn"
}