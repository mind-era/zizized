/*
 * Copyright (c) 2015 Mind Eratosthenes Kft.
 * License: AGPL v3
 */
package com.mind_era.zizized

import scala.collection.mutable.ArrayBuffer
import spire.math.UInt
/**
 * Package object for package com.mind_era.zizized.util.
 *
 * @author Szabolcs Ivan
 * @since 1.0
 */
package object util {
  // scala's Vector is at least as good as the one in "util/vector.h"
  type Vector[T] = ArrayBuffer[T]

  /** Discards its input value explicitly. */
  @inline
  final def discard[T](t: T): Unit = ()

  /** Converts a byte to UInt */
  def byteToUInt(b: Byte): UInt = UInt(b.toInt & 0xff)
  /** Converts a char to UInt (keeping only the low 8 bits) */
  def charToUInt(c: Char): UInt = UInt(c.toInt & 0xff)
}