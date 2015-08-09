/*
 * Copyright (c) 2015 Mind Eratosthenes Kft.
 * License: AGPL v3
 */
package com.mind_era.zizized.sat

import com.mind_era.zizized.sat.SatTypes._

/**
 * TODO document package com.mind_era.zizized.sat.ClauseAllocator
 *
 * A ClauseAllocator in the cpp code gets a memory chunk and treats a clause as an int offset within that chunk.
 * This class should probably be removed in the near future.
 *
 * @author Szabolcs Ivan
 * @since 1.0
 */
class ClauseAllocator {

  def getClause(c: ClauseOffset): Clause = c

}