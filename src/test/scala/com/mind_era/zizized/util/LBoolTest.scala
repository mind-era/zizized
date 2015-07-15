/*
 * Copyright (c) 2015 Mind Eratosthenes Kft.
 * License: AGPL v3
 */
package com.mind_era.zizized.util

import org.scalatest.FlatSpec

/**
 * Basic test suite for testing functionality of the util.LBool class
 * 
 * @author Szabolcs Ivan
 * @since version
 */
class LBoolTest extends FlatSpec {
  "An LBool " must " be one of the predefined case objects" in {
    val b1 = LBool( true )
    val b2 = LBool( true )
    val b3 = LBool( false )
    assert( b1 == b2 )
    assert( b1 == TRUE )
    assert( b2 != FALSE )
    assert( UNDEF != b1 )
    assert( b2 != UNDEF )
    assert( UNDEF == UNDEF )
  }  
  it must " convert ~TRUE to FALSE and vice versa" in {
    val b1 = TRUE
    val b2 = ~b1
    assert( b2 == LBool( false ))
    val b3 = ~(~FALSE)
    assert( b3 == FALSE )    
  }
  it must " satisfy ~UNDEF == UNDEF" in {
    assert( ~UNDEF == UNDEF )
  }
}