package com.mining.core.simulation

import com.mining.core.physics.{CelestialBody, NBodyDynamics}
import com.mining.core.spacecraft.Spacecraft
import com.mining.core.logistics.Resource
import com.typesafe.scalalogging.LazyLogging

// Main simulation engine
class SimulationEngine(
  var celestialBodies: Seq[CelestialBody],
  var spacecrafts: Seq[Spacecraft],
  val timeStepper: TimeStepper
) extends LazyLogging:

  def runSimulation(totalDuration: Double): Unit = ???

  def stepSimulation(deltaTime: Double): Unit = ??? // Advance simulation by deltaTime

  // Add/remove entities from the simulation
  def addCelestialBody(body: CelestialBody): Unit = ???
  def removeCelestialBody(name: String): Unit = ???
  def addSpacecraft(sc: Spacecraft): Unit = ???
  def removeSpacecraft(id: String): Unit = ???