/*
 * Copyright (c) 2015 Mind Eratosthenes Kft.
 * License: AGPL v3
 */
package com.mind_era.zizized.util

import org.scalatest.FlatSpec
import spire.math.UInt

/**
 * Tests for the com.mind_era.util package object.
 * 
 * @author Gabor Bakos
 */
class UtilTest extends FlatSpec {
  "byteToUInt" should "handle values outside the range of 0-255" in {
    assert(byteToUInt(256.toByte) == UInt(0))
    assert(byteToUInt(-1) == UInt(255))
  }
  it must "work well for values in the range of 0-127" in {
    assert(byteToUInt(127.toByte) == UInt(127))
  } 
  "charToUInt" should "handle values larger than 255" in {
    assert(charToUInt('\u0100') == UInt(0))
    assert(charToUInt('\u00ff') == UInt(255))
  }
  it must "work well for values in the range of 0-127" in {
	  assert(charToUInt(127) == UInt(127))
  } 
}