package com.mining.core.mining

import com.mining.core.logistics.Inventory

// Represents a robotic miner
case class MinerRobot(
  id: String,
  miningPower: Double, // e.g., kg/hour
  powerConsumption: Double, // Watts
  cargoCapacity: Double, // kg
  currentCargo: Inventory = Inventory()
) extends Miner:
  override def mine(target: com.mining.core.physics.CelestialBody, amount: Double, spacecraft: com.mining.core.spacecraft.Spacecraft): (com.mining.core.physics.CelestialBody, com.mining.core.spacecraft.Spacecraft, Double) = ??? // Implementation here