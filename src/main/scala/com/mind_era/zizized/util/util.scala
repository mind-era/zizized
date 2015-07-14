/*
 * Copyright (c) 2015 Mind Eratosthenes Kft.
 * License: AGPL v3
 */
package com.mind_era.zizized.util

/**
 * util/Vector.h and util/Vector.cpp : the Scala vector is just as good
 * 
 * @author Szabolcs Ivan
 * @since version
 */

package object util{
  type Vector[T] = scala.`package`.Vector[T]
}

sealed trait LBool {
  def unary_~ : LBool = if (this == TRUE) FALSE else if ( this == FALSE )  TRUE else UNDEF
  def apply( b : Boolean ) : LBool = if ( b ) TRUE else FALSE
}
case object TRUE extends LBool
case object FALSE extends LBool
case object UNDEF extends LBool