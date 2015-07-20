/*
 * Copyright (c) 2015 Mind Eratosthenes Kft.
 * License: AGPL v3
 */
package com.mind_era.zizized.util

/**
 * TODO document package com.mind_era.zizized.util.Hash
 * 
 * @author Szabolcs Ivan
 * @since 1.0
 */

class LongTriple( val _a : Long , val _b : Long , val _c : Long) {
  var a: Long = _a
  var b: Long = _b
  var c: Long = _c
  def mix( da: Int, db: Int, dc: Int ): Unit = {
    a -= b; a -= c; a ^= (c>>dc);
    b -= c; b -= a; b ^= (a<<da);
    c -= a; c -= b; c ^= (b>>db);
  }
  def mix() : Unit = {
    mix( 8, 13, 13); mix(16, 5, 12); mix(10, 15, 3)
  }
}
