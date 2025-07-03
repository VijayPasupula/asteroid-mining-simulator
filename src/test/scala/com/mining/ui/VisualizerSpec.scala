package com.mining.ui

import com.mining.core.physics.{CelestialBody, Vector3D, State}
import com.mining.core.spacecraft.{Spacecraft, Rocket}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfterEach
import java.io.ByteArrayOutputStream
import scala.Console

class VisualizerSpec extends AnyFunSuite with BeforeAndAfterEach {

  private val outContent = new ByteArrayOutputStream()

  override def beforeEach(): Unit = {
    outContent.reset()
    Console.withOut(outContent) {
      // Direct output to our stream
    }
  }

  val earth = CelestialBody(
    name = "Earth",
    mass = 5.972e24, radius = 6.371e6,
    position = Vector3D(0.0, 0.0, 0.0),
    velocity = Vector3D(0.0, 0.0, 0.0),
    rotationalPeriod = 86400.0
  )
  val moon = CelestialBody(
    name = "Moon",
    mass = 7.342e22, radius = 1.737e6,
    position = Vector3D(3.844e8, 0.0, 0.0),
    velocity = Vector3D(0.0, 1.022e3, 0.0),
    rotationalPeriod = 2.36e6
  )
  val spacecraft = Spacecraft(
    id = "TestSC", name = "Explorer",
    initialMass = 1000.0,
    currentState = State(Vector3D(1e7, 0, 0), Vector3D(0, 1000, 0)),
    mainEngine = Some(Rocket("Engine1", 100.0, 1000.0, 500.0, 300.0, 10000.0)),
    rcsThrusters = None
  )

  test("ConsoleVisualizer should print simulation state to console") {
    val visualizer = new ConsoleVisualizer()
    Console.withOut(outContent) {
      visualizer.render(Seq(earth, moon), Seq(spacecraft))
    }
    val output = outContent.toString()

    assert(output.contains("--- Simulation State ---"))
    assert(output.contains("Body: Earth, Pos: Vector3D(0.0,0.0,0.0), Vel: Vector3D(0.0,0.0,0.0)"))
    assert(output.contains("Body: Moon, Pos: Vector3D(3.844E8,0.0,0.0), Vel: Vector3D(0.0,1022.0,0.0)"))
    assert(output.contains("Spacecraft: Explorer, Pos: Vector3D(1.0E7,0.0,0.0), Vel: Vector3D(0.0,1000.0,0.0), Fuel: 500.0kg"))
    assert(output.contains("------------------------"))
  }

  test("ConsoleVisualizer should handle empty lists of bodies and spacecrafts") {
    val visualizer = new ConsoleVisualizer()
    Console.withOut(outContent) {
      visualizer.render(Seq.empty, Seq.empty)
    }
    val output = outContent.toString()
    assert(output.contains("--- Simulation State ---"))
    assert(output.contains("------------------------"))
    assert(!output.contains("Body:"))
    assert(!output.contains("Spacecraft:"))
  }
}