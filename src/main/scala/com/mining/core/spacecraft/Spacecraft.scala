package com.mining.core.spacecraft

import com.mining.core.physics.{State, Vector3D}
import com.mining.core.logistics.Inventory

// Trait for any component that is part of a spacecraft
trait SpacecraftComponent:
  def totalMass: Double

// Main Spacecraft entity
case class Spacecraft(
  id: String,
  name: String,
  initialMass: Double, // dry mass without fuel/cargo
  currentState: State,
  mainEngine: Option[Rocket], // Main propulsion
  rcsThrusters: Option[Map[String, Double]], // Reaction Control System thrusters (e.g., N, E, S, W) and their thrust
  cargoBay: Inventory = Inventory(),
  powerSupply: Double = 1000.0, // Watts, available power
  powerConsumption: Double = 0.0 // Watts, current consumption
) extends SpacecraftComponent:
  override def totalMass: Double = ??? // initialMass + mainEngine.map(_.totalMass).getOrElse(0.0) + cargoBay.getTotalMass

  // Apply thrust from main engine
  def applyMainThrust(direction: Vector3D, duration: Double): (Spacecraft, Vector3D) = ???

  // Apply RCS thrust for attitude control or fine maneuvers
  def applyRCSThrust(thrusterId: String, duration: Double): (Spacecraft, Vector3D) = ???

  // Update spacecraft state based on forces (e.g., from engines, gravity)
  def updateState(force: Vector3D, deltaTime: Double): Spacecraft = ???

  // Land on a celestial body
  def land(targetBody: com.mining.core.physics.CelestialBody): Spacecraft = ???

  // Rendezvous and dock with another spacecraft or station
  def rendezvousAndDock(target: Spacecraft): Spacecraft = ???

  // Undock from another spacecraft or station
  def undock(target: Spacecraft): Spacecraft = ???

  // Transfer resources to/from another spacecraft/station
  def transferResources(target: Spacecraft, resources: Inventory): (Spacecraft, Spacecraft) = ???