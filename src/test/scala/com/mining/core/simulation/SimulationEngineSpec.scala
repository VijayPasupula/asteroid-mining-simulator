package com.mining.core.simulation

import com.mining.core.physics.{CelestialBody, State, Vector3D, NBodyDynamics}
import com.mining.core.spacecraft.{Spacecraft, Rocket}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalactic.TolerantNumerics

class SimulationEngineSpec extends AnyFunSuite with BeforeAndAfterEach with Matchers {

  implicit val doubleEq = TolerantNumerics.tolerantDoubleEquality(1e-9)

  var engine: SimulationEngine = _
  var earth: CelestialBody = _
  var moon: CelestialBody = _
  var spacecraft: Spacecraft = _

  override def beforeEach(): Unit = {
    earth = CelestialBody(
      name = "Earth",
      mass = 5.972e24,
      radius = 6.371e6,
      position = Vector3D(0.0, 0.0, 0.0),
      velocity = Vector3D(0.0, 0.0, 0.0),
      rotationalPeriod = 86400.0
    )
    moon = CelestialBody(
      name = "Moon",
      mass = 7.342e22,
      radius = 1.737e6,
      position = Vector3D(3.844e8, 0.0, 0.0), // ~384,400 km from Earth
      velocity = Vector3D(0.0, 1.022e3, 0.0), // ~1 km/s orbital velocity
      rotationalPeriod = 2.36e6
    )

    spacecraft = Spacecraft(
      id = "LunarProbe",
      name = "Apollo 11",
      initialMass = 10000.0,
      currentState = State(
        position = Vector3D(earth.position.x + earth.radius + 200e3, earth.position.y, earth.position.z), // 200km altitude
        velocity = Vector3D(0.0, 7800.0, 0.0) // LEO velocity
      ),
      mainEngine = Some(Rocket("MainEngine", 1000.0, 5000.0, 5000.0, 300.0, 20000.0)),
      rcsThrusters = None
    )

    // A simple EulerTimeStepper for testing purposes
    val testTimeStepper = new TimeStepper {
      override def step(initialState: State, force: Vector3D, mass: Double, deltaTime: Double): State = {
        val acceleration = force / mass
        val newVelocity = initialState.velocity + acceleration * deltaTime
        val newPosition = initialState.position + newVelocity * deltaTime
        State(newPosition, newVelocity)
      }
    }

    engine = new SimulationEngine(Seq(earth, moon), Seq(spacecraft), testTimeStepper)
  }

  test("addCelestialBody should add a body to the simulation") {
    val mars = CelestialBody("Mars", 6.39e23, 3.389e6, Vector3D(0, 0, 0), Vector3D(0, 0, 0), 1.0)
    engine.addCelestialBody(mars)
    assert(engine.celestialBodies.contains(mars))
  }

  test("removeCelestialBody should remove a body from the simulation") {
    engine.removeCelestialBody(earth.name)
    assert(!engine.celestialBodies.contains(earth))
    assert(engine.celestialBodies.contains(moon)) // Other bodies unaffected
  }

  test("addSpacecraft should add a spacecraft to the simulation") {
    val newSc = spacecraft.copy(id = "NewSC")
    engine.addSpacecraft(newSc)
    assert(engine.spacecrafts.contains(newSc))
  }

  test("removeSpacecraft should remove a spacecraft from the simulation") {
    engine.removeSpacecraft(spacecraft.id)
    assert(!engine.spacecrafts.contains(spacecraft))
  }

  test("stepSimulation should advance time and update positions/velocities of all entities") {
    // Replace the ??? in SimulationEngine.stepSimulation with:
    // def stepSimulation(deltaTime: Double): Unit = {
    //   // Update Celestial Bodies (simplified: assume they only move due to initial velocity or central body if simulated)
    //   celestialBodies = celestialBodies.map { body =>
    //     val totalForce = NBodyDynamics.calculateForce(
    //       new NBodyDynamics.TestPhysicalObject(body.mass, body.position, body.velocity) {},
    //       celestialBodies ++ spacecrafts.map(sc =>
    //         new NBodyDynamics.TestPhysicalObject(sc.totalMass, sc.currentState.position, sc.currentState.velocity) {}
    //       )
    //     )
    //     val newState = timeStepper.step(
    //       State(body.position, body.velocity),
    //       totalForce,
    //       body.mass,
    //       deltaTime
    //     )
    //     body.copy(position = newState.position, velocity = newState.velocity)
    //   }
    //
    //   // Update Spacecrafts (consider their own engines and gravitational forces)
    //   spacecrafts = spacecrafts.map { sc =>
    //     // For simplicity in test, assume no engine thrust in this step
    //     val totalForce = NBodyDynamics.calculateForce(
    //       new NBodyDynamics.TestPhysicalObject(sc.totalMass, sc.currentState.position, sc.currentState.velocity) {},
    //       celestialBodies ++ spacecrafts.filter(_ != sc).map(otherSc =>
    //         new NBodyDynamics.TestPhysicalObject(otherSc.totalMass, otherSc.currentState.position, otherSc.currentState.velocity) {}
    //       )
    //     )
    //     val newState = timeStepper.step(
    //       sc.currentState,
    //       totalForce,
    //       sc.totalMass,
    //       deltaTime
    //     )
    //     sc.copy(currentState = newState)
    //   }
    // }

    val initialEarthPos = earth.position
    val initialMoonPos = moon.position
    val initialSpacecraftPos = spacecraft.currentState.position

    engine.stepSimulation(10.0) // Step for 10 seconds

    // Assert that positions/velocities have changed for all bodies
    assert(engine.celestialBodies.find(_.name == "Earth").get.position !== initialEarthPos)
    assert(engine.celestialBodies.find(_.name == "Moon").get.position !== initialMoonPos)
    assert(engine.spacecrafts.head.currentState.position !== initialSpacecraftPos)

    // More specific checks would require knowledge of the exact physics calculations (N-body)
    // For now, just ensure movement.
    assert(engine.spacecrafts.head.currentState.position.magnitude > 0.0)
  }

  test("runSimulation should call stepSimulation repeatedly for total duration") {
    // This test ensures the loop runs correctly.
    // Replace the ??? in SimulationEngine.runSimulation with:
    // def runSimulation(totalDuration: Double): Unit = {
    //   val stepSize = timeStepper.stepSize // Assume timeStepper has a stepSize property for fixed steps
    //   var currentTime = 0.0
    //   while (currentTime < totalDuration) {
    //     val actualStep = Math.min(stepSize, totalDuration - currentTime)
    //     stepSimulation(actualStep)
    //     currentTime += actualStep
    //     // logger.debug(s"Simulating... Time: $currentTime / $totalDuration")
    //   }
    // }

    // For this test, we need a timeStepper with a fixed stepSize
    val fixedTimeStepper = new TimeStepper {
      val stepSize = 1.0 // 1 second per step
      override def step(initialState: State, force: Vector3D, mass: Double, deltaTime: Double): State = {
        // Dummy step for test count
        initialState.copy(position = initialState.position + Vector3D(1,0,0) * deltaTime)
      }
    }
    engine = new SimulationEngine(Seq(earth), Seq(spacecraft), fixedTimeStepper)

    val initialScPos = spacecraft.currentState.position
    engine.runSimulation(10.0) // Run for 10 seconds

    // Since our dummy step moves by 1 unit per second, after 10 steps, x should be 10
    assert(engine.spacecrafts.head.currentState.position.x === initialScPos.x + 10.0)
  }
}