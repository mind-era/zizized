/*
 * Copyright (c) 2015 Mind Eratosthenes Kft.
 * License: AGPL v3
 */
package com.mind_era.zizized

import scala.collection.mutable.ArrayBuffer
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
  def discard[T](t: T): Unit = ()
}