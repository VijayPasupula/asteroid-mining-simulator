package com.mining.ui

import com.mining.core.physics.{CelestialBody, Vector3D, State}
import com.mining.core.spacecraft.{Spacecraft, Rocket}
import com.mining.core.simulation.{SimulationEngine, EulerTimeStepper}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfterEach
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import scala.Console

class UserInterfaceSpec extends AnyFunSuite with BeforeAndAfterEach {

  private val inContent = new ByteArrayInputStream("".getBytes())
  private val outContent = new ByteArrayOutputStream()

  var engine: SimulationEngine = _

  override def beforeEach(): Unit = {
    inContent.reset()
    outContent.reset()
    Console.withIn(inContent) {
      Console.withOut(outContent) {
        // Redirect System.in and System.out for testing
      }
    }

    val earth = CelestialBody(
      name = "Earth",
      mass = 5.972e24, radius = 6.371e6,
      position = Vector3D(0.0, 0.0, 0.0),
      velocity = Vector3D(0.0, 0.0, 0.0),
      rotationalPeriod = 86400.0
    )
    val spacecraft = Spacecraft(
      id = "TestSC", name = "Explorer",
      initialMass = 1000.0,
      currentState = State(Vector3D(1e7, 0, 0), Vector3D(0, 1000, 0)),
      mainEngine = None,
      rcsThrusters = None
    )
    engine = new SimulationEngine(Seq(earth), Seq(spacecraft), EulerTimeStepper)
  }

  test("ConsoleUserInterface.displayInfo should print engine status") {
    val ui = new ConsoleUserInterface()
    Console.withOut(outContent) {
      ui.displayInfo() // This will call the ??? in the main code
      // For testing, modify your main code `displayInfo` to print something specific, e.g.:
      // override def displayInfo(): Unit = {
      //   println("--- UI Info ---")
      //   println(s"Current Simulation Time: ${engine.currentTime}") // Assuming engine has currentTime
      //   println(s"Number of Celestial Bodies: ${engine.celestialBodies.size}")
      //   println(s"Number of Spacecrafts: ${engine.spacecrafts.size}")
      // }
    }
    val output = outContent.toString()

    assert(output.contains("--- UI Info ---"))
    assert(output.contains("Number of Celestial Bodies: 1"))
    assert(output.contains("Number of Spacecrafts: 1"))
  }

  test("ConsoleUserInterface.handleInput should process basic commands") {
    val ui = new ConsoleUserInterface()
    // Prepare input for the test
    val inputCommand = "help\n" // Simulate typing "help" and pressing Enter
    inContent.close() // Close the current stream
    val newInContent = new ByteArrayInputStream(inputCommand.getBytes())
    Console.withIn(newInContent) {
      Console.withOut(outContent) {
        ui.handleInput() // This will call the ??? in the main code
        // For testing, modify your main code `handleInput` to print something specific:
        // override def handleInput(): Unit = {
        //   print("Enter command: ")
        //   val line = scala.io.StdIn.readLine()
        //   line.trim.toLowerCase match {
        //     case "help" => println("Available commands: help, exit, status")
        //     case _ => println("Unknown command.")
        //   }
        // }
      }
    }
    val output = outContent.toString()
    assert(output.contains("Enter command: "))
    assert(output.contains("Available commands: help, exit, status"))
  }

  // To test `start`, you would need to simulate a full interactive session
  // which is more complex and often involves mocking `StdIn.readLine` behavior
  // or providing a specific sequence of inputs. For now, we'll skip `start`'s full test.
  // The displayInfo and handleInput tests provide coverage for its sub-components.
}