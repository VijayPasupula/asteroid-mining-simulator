package com.mining.core.spacecraft

import com.mining.core.physics.{State, Vector3D}
import com.mining.core.logistics.Inventory

// Represents a rocket stage or a full rocket
case class Rocket(
  id: String,
  massEmpty: Double, // kg
  fuelCapacity: Double, // kg
  currentFuel: Double, // kg
  isp: Double, // Specific Impulse (seconds)
  thrust: Double, // Newtons
  enginesOn: Boolean = false
) extends SpacecraftComponent:
  override def totalMass: Double = ??? // massEmpty + currentFuel
  override def consumeFuel(amount: Double): (Rocket, Double) = ??? // Returns updated rocket and actual fuel consumed
  override def calculateThrustVector(direction: Vector3D): Vector3D = ??? // Calculates thrust in a given direction if engines are on