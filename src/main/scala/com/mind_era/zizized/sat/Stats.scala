/*
 * Copyright (c) 2015 Mind Eratosthenes Kft.
 * License: AGPL v3
 */
package com.mind_era.zizized.sat

import spire.math.UInt
import com.mind_era.zizized.util.Statistics

/**
 * Statistic counters for the SAT solver.
 *
 * @author Szabolcs Ivan
 * @since 1.0
 */
final case class Stats(
    var mkVar: UInt = UInt(0),
    var mkBinClause: UInt = UInt(0),
    var mkTerClause: UInt = UInt(0),
    var conflict: UInt = UInt(0),
    var propagate: UInt = UInt(0),
    var terPropagate: UInt = UInt(0),
    var decision: UInt = UInt(0),
    var restart: UInt = UInt(0),
    var gcClause: UInt = UInt(0),
    var delClause: UInt = UInt(0),
    var minimizedLits: UInt = UInt(0),
    var dynSubRes: UInt = UInt(0)) {
  def reset(): Unit = {
    mkVar = UInt(0)
    mkBinClause = UInt(0)
    mkTerClause = UInt(0)
    conflict = UInt(0)
    propagate = UInt(0)
    terPropagate = UInt(0)
    decision = UInt(0)
    restart = UInt(0)
    gcClause = UInt(0)
    delClause = UInt(0)
    minimizedLits = UInt(0)
    dynSubRes = UInt(0)
  }
  def collectStatistics(st: Statistics): Unit = {}
}