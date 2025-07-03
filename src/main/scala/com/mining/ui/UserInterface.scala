package com.mining.ui

import com.mining.core.simulation.SimulationEngine

// Trait for user input and interaction
trait UserInterface:
  def start(engine: SimulationEngine): Unit
  def handleInput(): Unit
  def displayInfo(): Unit

// A simple console-based UI
class ConsoleUserInterface extends UserInterface:
  override def start(engine: SimulationEngine): Unit = ??? // Start main loop, read commands
  override def handleInput(): Unit = ??? // Process user commands (e.g., "launch", "mine")
  override def displayInfo(): Unit = ??? // Show current simulation status

// Placeholder for a GUI
class GraphicalUserInterface extends UserInterface:
  override def start(engine: SimulationEngine): Unit = ??? // Initialize GUI framework
  override def handleInput(): Unit = ??? // Event loop for GUI
  override def displayInfo(): Unit = ??? // Update GUI elements