package com.mining.ui

import com.mining.core.physics.{CelestialBody, State}
import com.mining.core.spacecraft.Spacecraft
import com.typesafe.scalalogging.LazyLogging

// Trait for any visualizer implementation
trait Visualizer:
  def render(
    celestialBodies: Seq[CelestialBody],
    spacecrafts: Seq[Spacecraft]
  ): Unit

// A very basic text-based visualizer for console output
class ConsoleVisualizer extends Visualizer with LazyLogging:
  override def render(
    celestialBodies: Seq[CelestialBody],
    spacecrafts: Seq[Spacecraft]
  ): Unit =
    logger.info("--- Simulation State ---")
    celestialBodies.foreach { body =>
      logger.info(s"Body: ${body.name}, Pos: ${body.position}, Vel: ${body.velocity}")
    }
    spacecrafts.foreach { sc =>
      logger.info(s"Spacecraft: ${sc.name}, Pos: ${sc.currentState.position}, Vel: ${sc.currentState.velocity}, Fuel: ${sc.mainEngine.map(_.currentFuel).getOrElse(0.0)}kg")
    }
    logger.info("------------------------")

// Placeholder for a graphical visualizer (e.g., using AWT/Swing, or a game engine integration)
class GraphicalVisualizer extends Visualizer:
  override def render(
    celestialBodies: Seq[CelestialBody],
    spacecrafts: Seq[Spacecraft]
  ): Unit = ??? // Integrate with a 3D rendering library/engine here