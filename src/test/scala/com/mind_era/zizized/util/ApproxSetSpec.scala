/*
 * Copyright (c) 2015 Mind Eratosthenes Kft.
 * License: AGPL v3
 */


package com.mind_era.zizized.util

import org.scalatest.FlatSpec
import org.scalatest.Matchers

/**
 * TODO document .ApproxSetSpec
 * 
 * @author Szabolcs Ivan
 * @since version
 */
class ApproxSetSpec extends FlatSpec with Matchers{
  "An IntApproxSet" should " be empty when constructed" in {
    val set : IntApproxSet = new IntApproxSet()
    val set2 : IntApproxSet = new IntApproxSet()
    assert( set.isEmpty )
    assert( set.mayEqual( set2 ))
    assert( set != set2 )
  } 
  it should " be empty after insertion-removal of different object with the same hash % capacity" in {
    val set : IntApproxSet = new IntApproxSet()
    set.insert( 7 )
    set.remove( 7 + set.capacity )
    assert( set.isEmpty )
  }
  it should " handle negative hashes well" in {
    val set : IntApproxSet = new IntApproxSet()
    set.insert( -1 )
    assert( set.size == 1 )
    assert( set.toString().equals("{63}"))
  }
  it should " support union, intersection and difference properly" in {
    val set : IntApproxSet = new IntApproxSet()
    set.insert( 2 ); set.insert( 10 ); set.insert( 17 );
    val set2 : IntApproxSet = new IntApproxSet() 
    set2.insert( 10 ); set2.insert( 20 ); set2.insert( 30 );
    val set3 = set | set2
    assert( set3.size == 5 )
    assert( set3.toString.equals("{2,10,17,20,30}"))
    set3 -= set
    assert( set3.size == 2 )
    set3 &= set
    assert( set3.isEmpty )
  }
}