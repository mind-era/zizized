/*
 * Copyright (c) 2015 Mind Eratosthenes Kft.
 * License: AGPL v3
 */
package com.mind_era.zizized.sat

import spire.math.UInt
import com.mind_era.zizized.util.ApproxSet
import com.mind_era.zizized.util.LBool
import com.mind_era.zizized.util.util.Vector
import com.mind_era.zizized.util.UNDEF
import com.mind_era.zizized.util.TRUE
import com.mind_era.zizized.util.FALSE

/**
 * TODO document package com.mind_era.zizized.sat.SatTypes
 * 
 * @author Szabolcs Ivan
 * @since version
 */
object SatTypes {
  type BoolVar = UInt
  type BoolVarVector = com.mind_era.zizized.util.util.Vector[Int] 
  
  val NullBoolVar : BoolVar = com.mind_era.zizized.util.cpp.UINT_MAX >> 1
  
  type LiteralVector = Vector[Literal]
  type ClauseOffset = UInt
  type ExtConstraintIdx = UInt
  type ExtJustificationIdx = UInt
  
  type LiteralApproxSet = ApproxSet[ Literal ] // TODO: instantiate with e2u being Literal.toUInt
  type VarApproxSet = ApproxSet[ BoolVar ] // TODO: instantiate with e2u being identity u=>u

}

class Literal( v : SatTypes.BoolVar = SatTypes.NullBoolVar, isPositive : Boolean = true) {
  var value : UInt = if ( isPositive ) v << 1 else (v << 1) + UInt(1)
  def variable : SatTypes.BoolVar = value >> 1
  def sign : Boolean = (value & UInt(1)) == UInt(1)
  def unsign : Literal = new Literal( variable, true )
  def index : UInt = value
  def neg : Literal = { value = value ^ UInt(1) ; this }
  def unary_~ : Literal = new Literal( variable, !sign )
  def toUInt : UInt = value
  def hash : UInt = value // TODO hash differs!!
  def < ( rhs : Literal ) : Boolean = value < rhs.value
  def == ( rhs : Literal ) : Boolean = value == rhs.value
  def != ( rhs : Literal ) : Boolean = value != rhs.value
  override def toString : String = if ( sign ) "-" + variable.toString() else variable.toString()
  
}

object Literal {
  val nullLiteral : Literal = new Literal()
  def apply( u : UInt ) : Literal = { val r = new Literal(); r.value = u; r }
  def toUInt( l : Literal ) : UInt = l.value
  
}

class Model( val v : Vector[LBool] ) {
  def valueAt( _v : SatTypes.BoolVar ) : LBool = v( _v.toInt )
  def valueAt( _v : Literal ) : LBool = { if ( _v.sign ) ~v( _v.variable.toInt ) else v( _v.variable.toInt ) }
  def evaluate( p : (LBool, Int )) : Option[String] = p match { case (b,i) => if (b == TRUE ) Some(i.toString) else if (b == FALSE ) Some((-i).toString) else None }
  override def toString : String = v.zipWithIndex.flatMap( evaluate(_) ).mkString(" ")
}

//TODO line 150-től sat_types.h
//TODO tesztelni!!!