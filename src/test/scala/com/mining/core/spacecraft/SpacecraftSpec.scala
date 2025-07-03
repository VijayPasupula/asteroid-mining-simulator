package com.mining.core.spacecraft

import com.mining.core.physics.{State, Vector3D, PhysicsConstants}
import com.mining.core.logistics.{Inventory, Resources}
import org.scalatest.funsuite.AnyFunSuite
import org.scalactic.TolerantNumerics

class SpacecraftSpec extends AnyFunSuite {

  implicit val doubleEq = TolerantNumerics.tolerantDoubleEquality(1e-9)

  val initialRocket = Rocket("MainEngine", 1000.0, 5000.0, 5000.0, 300.0, 20000.0, enginesOn = false)
  val initialSpacecraft = Spacecraft(
    id = "SC001",
    name = "Test Vessel",
    initialMass = 2000.0,
    currentState = State(Vector3D(0, 0, 0), Vector3D(0, 0, 0)),
    mainEngine = Some(initialRocket),
    rcsThrusters = Some(Map("fore" -> 100.0, "aft" -> 100.0)), // N
    cargoBay = Inventory().add(Resources.Iron, 100.0),
    powerSupply = 5000.0, // Watts
    powerConsumption = 100.0
  )

  test("totalMass should correctly sum initial mass, engine fuel, and cargo") {
    // Replace the ??? in Spacecraft.totalMass with:
    // override def totalMass: Double = initialMass + mainEngine.map(_.totalMass).getOrElse(0.0) + cargoBay.getTotalMass

    val expectedTotalMass = initialSpacecraft.initialMass + initialRocket.totalMass + initialSpacecraft.cargoBay.getTotalMass
    assert(initialSpacecraft.totalMass === expectedTotalMass)

    val scNoEngine = initialSpacecraft.copy(mainEngine = None)
    assert(scNoEngine.totalMass === initialSpacecraft.initialMass + initialSpacecraft.cargoBay.getTotalMass)

    val scNoCargo = initialSpacecraft.copy(cargoBay = Inventory())
    assert(scNoCargo.totalMass === initialSpacecraft.initialMass + initialRocket.totalMass)
  }

  test("applyMainThrust should consume fuel and update velocity") {
    // Replace the ??? in Spacecraft.applyMainThrust with:
    // def applyMainThrust(direction: Vector3D, duration: Double): (Spacecraft, Vector3D) = {
    //   mainEngine match {
    //     case Some(engine) =>
    //       val fuelConsumptionRate = engine.thrust / (engine.isp * PhysicsConstants.G) // kg/s
    //       val fuelToConsume = fuelConsumptionRate * duration
    //       val (updatedEngine, actualFuelConsumed) = engine.consumeFuel(fuelToConsume)
    //
    //       val effectiveDuration = actualFuelConsumed / fuelConsumptionRate
    //       val newEngine = updatedEngine.copy(enginesOn = true)
    //       val thrustVector = newEngine.calculateThrustVector(direction)
    //
    //       val impulse = thrustVector * effectiveDuration
    //       val deltaV = impulse / totalMass // Approximate change in velocity
    //
    //       val newState = currentState.copy(velocity = currentState.velocity + deltaV)
    //       (this.copy(mainEngine = Some(newEngine.copy(enginesOn = false)), currentState = newState), thrustVector)
    //     case None => (this, Vector3D.Zero)
    //   }
    // }

    val thrustDirection = Vector3D(1.0, 0.0, 0.0)
    val duration = 10.0 // seconds

    // Assuming a simplified delta-V calculation (impulse / mass)
    val fuelConsumptionRate = initialRocket.thrust / (initialRocket.isp * PhysicsConstants.G)
    val fuelConsumed = fuelConsumptionRate * duration
    val expectedDeltaV = (initialRocket.thrust * duration) / initialSpacecraft.totalMass // Simple approximation

    val (updatedSc, actualThrustVector) = initialSpacecraft.copy(mainEngine = Some(initialRocket.copy(enginesOn = true))).applyMainThrust(thrustDirection, duration)

    assert(updatedSc.mainEngine.get.currentFuel === initialRocket.currentFuel - fuelConsumed)
    assert(updatedSc.currentState.velocity.x === initialSpacecraft.currentState.velocity.x + expectedDeltaV)
    assert(actualThrustVector.magnitude === initialRocket.thrust)
  }

  test("applyRCSThrust should update velocity for attitude control") {
    // Implement RCS thrust similar to main engine but for smaller maneuvers
    // This will require knowing which thruster applies force in which direction relative to spacecraft orientation
    // For simplicity, let's assume "fore" gives positive X velocity change.
    // Replace ??? with:
    // def applyRCSThrust(thrusterId: String, duration: Double): (Spacecraft, Vector3D) = {
    //   rcsThrusters.flatMap(_.get(thrusterId)) match {
    //     case Some(thrustMagnitude) =>
    //       // Simplified: assume 'fore' is +X, 'aft' is -X etc.
    //       val direction = thrusterId match {
    //         case "fore" => Vector3D(1.0, 0.0, 0.0)
    //         case "aft"  => Vector3D(-1.0, 0.0, 0.0)
    //         case _      => Vector3D.Zero // Add other directions as needed
    //       }
    //       val impulse = direction.normalize * thrustMagnitude * duration
    //       val deltaV = impulse / totalMass
    //       val newState = currentState.copy(velocity = currentState.velocity + deltaV)
    //       (this.copy(currentState = newState), impulse)
    //     case None => (this, Vector3D.Zero)
    //   }
    // }

    val duration = 1.0 // second
    val rcsThrust = initialSpacecraft.rcsThrusters.get("fore") * duration
    val expectedDeltaV = rcsThrust / initialSpacecraft.totalMass

    val (updatedSc, appliedImpulse) = initialSpacecraft.applyRCSThrust("fore", duration)

    assert(updatedSc.currentState.velocity.x === initialSpacecraft.currentState.velocity.x + expectedDeltaV)
    assert(appliedImpulse.magnitude === rcsThrust)
  }

  test("updateState should correctly update position and velocity based on force and delta time") {
    // Replace the ??? in Spacecraft.updateState with:
    // def updateState(force: Vector3D, deltaTime: Double): Spacecraft = {
    //   val acceleration = force / totalMass
    //   val newVelocity = currentState.velocity + acceleration * deltaTime
    //   val newPosition = currentState.position + newVelocity * deltaTime // Euler integration
    //   this.copy(currentState = State(newPosition, newVelocity))
    // }

    val force = Vector3D(1000.0, 0.0, 0.0) // 1000 N in X
    val deltaTime = 10.0 // seconds

    val mass = initialSpacecraft.totalMass
    val acceleration = force / mass
    val expectedNewVelocity = initialSpacecraft.currentState.velocity + acceleration * deltaTime
    val expectedNewPosition = initialSpacecraft.currentState.position + expectedNewVelocity * deltaTime

    val updatedSc = initialSpacecraft.updateState(force, deltaTime)

    assert(updatedSc.currentState.position === expectedNewPosition)
    assert(updatedSc.currentState.velocity === expectedNewVelocity)
  }

  test("land should change spacecraft state (simplified)") {
    // This is a high-level function. For a real simulation, `land` would involve complex
    // physics, thrust profiles, and collision detection. For this skeleton,
    // let's just assert that its state changes in some way indicative of landing.
    // Replace ??? with a simplified implementation (e.g., setting velocity to zero
    // relative to the body and setting position to body surface + safe altitude).
    // def land(targetBody: CelestialBody): Spacecraft = {
    //   val landedPosition = targetBody.position + Vector3D(targetBody.radius + 10, 0, 0) // Just above surface
    //   val landedVelocity = targetBody.velocity // Match body's velocity
    //   this.copy(currentState = State(landedPosition, landedVelocity))
    // }

    val asteroid = com.mining.core.physics.CelestialBody(
      name = "TestAsteroid",
      mass = 1e15, radius = 500.0,
      position = Vector3D(10000, 20000, 30000),
      velocity = Vector3D(10, 5, 2),
      rotationalPeriod = 1000.0
    )

    val landedSc = initialSpacecraft.land(asteroid)

    assert(landedSc.currentState.velocity === asteroid.velocity)
    // Check if position is 'on' or very close to the asteroid surface
    assert((landedSc.currentState.position - asteroid.position).magnitude >= asteroid.radius)
  }

  test("transferResources should move resources between spacecraft") {
    // Replace ??? with:
    // def transferResources(target: Spacecraft, resources: Inventory): (Spacecraft, Spacecraft) = {
    //   val (updatedSourceCargo, successRemove) = cargoBay.remove(resources)
    //   if (successRemove) {
    //     val updatedTargetCargo = target.cargoBay.add(resources)
    //     (this.copy(cargoBay = updatedSourceCargo), target.copy(cargoBay = updatedTargetCargo))
    //   } else {
    //     (this, target) // No change if transfer failed
    //   }
    // }

    val sc1 = initialSpacecraft.copy(cargoBay = Inventory().add(Resources.Water, 200.0).add(Resources.Iron, 50.0))
    val sc2 = initialSpacecraft.copy(id = "SC002", name = "Receiver", cargoBay = Inventory().add(Resources.Iron, 10.0))

    val transferAmount = Inventory().add(Resources.Water, 100.0)
    val (updatedSc1, updatedSc2) = sc1.transferResources(sc2, transferAmount)

    assert(updatedSc1.cargoBay.items.get(Resources.Water).contains(100.0))
    assert(updatedSc1.cargoBay.items.get(Resources.Iron).contains(50.0))

    assert(updatedSc2.cargoBay.items.get(Resources.Water).contains(100.0))
    assert(updatedSc2.cargoBay.items.get(Resources.Iron).contains(10.0))
  }

  test("transferResources should not transfer if source doesn't have enough") {
    val sc1 = initialSpacecraft.copy(cargoBay = Inventory().add(Resources.Water, 50.0))
    val sc2 = initialSpacecraft.copy(id = "SC002", name = "Receiver")

    val transferAmount = Inventory().add(Resources.Water, 100.0) // More than sc1 has
    val (updatedSc1, updatedSc2) = sc1.transferResources(sc2, transferAmount)

    assert(updatedSc1 === sc1) // No change
    assert(updatedSc2 === sc2) // No change
  }
}