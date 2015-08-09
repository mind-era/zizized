/*
 * Copyright (c) 2015 Mind Eratosthenes Kft.
 * License: AGPL v3
 */
package com.mind_era.zizized.util

import spire.math.UInt
import spire.math.ULong

/**
 * various hashing methods for primitives & strings
 *
 * @author Szabolcs Ivan
 * @since 1.0
 */
object Hash {
  private def mix(a: UInt, b: UInt, c: UInt, da: Int, db: Int, dc: Int): (UInt, UInt, UInt) = {
    val a2 = (a - b - c) ^ (c >> dc)
    val b2 = (b - c - a2) ^ (a2 << da)
    val c2 = (c - a2 - b2) ^ (b2 >> db)
    (a2, b2, c2)
  }
  @SuppressWarnings(Array("org.brianmckenna.wartremover.warts.Throw" /*False positive*/ ))
  def mix(a: UInt, b: UInt, c: UInt): (UInt, UInt, UInt) = {
    val (a2, b2, c2) = mix(a, b, c, 8, 13, 13)
    val (a3, b3, c3) = mix(a2, b2, c2, 16, 5, 12)
    mix(a3, b3, c3, 1, 15, 3)
  }
  def hashUnsigned(a: UInt): UInt = {
    val a2 = (a + UInt(0x7ed55d16)) + (a << 12)
    val a3 = (a2 ^ UInt(0xc761c23c)) ^ (a2 >> 19)
    val a4 = (a3 + UInt(0x165667b1)) + (a3 << 5)
    val a5 = (a4 + UInt(0xd3a2646c)) ^ (a4 << 9)
    val a6 = (a5 + UInt(0xfd7046c5)) + (a5 << 3)
    (a6 ^ UInt(0xb55a4f09)) ^ (a6 >> 16)
  }
  def hashUnsignedLongLong(a: ULong): UInt = {
    val a2 = (~a) + (a << 18)
    val a3 = a2 ^ (a2 >> 31)
    val a4 = a3 + (a3 << 2) + (a3 << 4);
    val a5 = a4 ^ (a4 >> 11)
    val a6 = a5 + (a5 << 6)
    UInt((a6 ^ (a6 >> 22)).toInt)
  }
  def combineHash(h1: UInt, h2: UInt): UInt = {
    val h22 = (h2 - h1) ^ (h1 << 8)
    val h12 = h1 - h22
    ((h22 ^ (h12 << 16)) - h12) ^ (h12 << 10)
  }
  def hashUU(a: UInt, b: UInt): UInt = combineHash(hashUnsigned(a), hashUnsigned(b))
  private def stringHashCases(strIter: Iterator[Char], len: Int, _a: UInt, _b: UInt, _c: UInt): (UInt, UInt, UInt) = {
    var a = _a
    var b = _b
    var c = _c
    if (len == 11) c = c + (charToUInt(strIter.next()) << 24)
    if (len >= 10) c = c + (charToUInt(strIter.next()) << 16)
    if (len >= 9) c = c + (charToUInt(strIter.next()) << 8)
    if (len >= 8) b = b + (charToUInt(strIter.next()) << 24)
    if (len >= 7) b = b + (charToUInt(strIter.next()) << 16)
    if (len >= 6) b = b + (charToUInt(strIter.next()) << 8)
    if (len >= 5) b = b + charToUInt(strIter.next())
    if (len >= 4) a = a + (charToUInt(strIter.next()) << 24)
    if (len >= 3) a = a + (charToUInt(strIter.next()) << 16)
    if (len >= 2) a = a + (charToUInt(strIter.next()) << 8)
    if (len >= 1) a = a + charToUInt(strIter.next())
    mix(a, b, c)
  }
  def stringHash(str: String, initValue: UInt): UInt = {
    var a: UInt = UInt(0x9e3779b9)
    var b: UInt = UInt(0x9e3779b9)
    var c: UInt = initValue
    var len: Int = str.length()
    var iter: Iterator[Char] = str.iterator
    while (len >= 12) {
      a = a + (charToUInt(iter.next()) << 16) + charToUInt(iter.next())
      b = b + (charToUInt(iter.next()) << 16) + charToUInt(iter.next())
      c = c + (charToUInt(iter.next()) << 16) + charToUInt(iter.next())
      @SuppressWarnings(Array("org.brianmckenna.wartremover.warts.Throw" /*False positive*/ ))
      val (a1, b1, c1) = mix(a, b, c)
      a = a1
      b = b1
      c = c1
      len = len - 12
    }
    c = c + UInt(str.length())
    @SuppressWarnings(Array("org.brianmckenna.wartremover.warts.Throw" /*False positive*/ ))
    val (_, _, c2) = stringHashCases(str.reverseIterator, len, a, b, c)
    c2
  }

}