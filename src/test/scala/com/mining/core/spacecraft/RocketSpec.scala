package com.mining.core.spacecraft

import com.mining.core.physics.Vector3D
import org.scalatest.funsuite.AnyFunSuite
import org.scalactic.TolerantNumerics

class RocketSpec extends AnyFunSuite {

  implicit val doubleEq = TolerantNumerics.tolerantDoubleEquality(1e-9)

  val basicRocket = Rocket(
    id = "R1",
    massEmpty = 1000.0,
    fuelCapacity = 5000.0,
    currentFuel = 5000.0,
    isp = 300.0, // seconds
    thrust = 20000.0 // Newtons
  )

  test("totalMass should include empty mass and current fuel") {
    assert(basicRocket.totalMass === 6000.0) // 1000 + 5000
    assert(basicRocket.copy(currentFuel = 0.0).totalMass === 1000.0)
  }

  test("consumeFuel should reduce current fuel and return actual consumed amount") {
    val (updatedRocket, consumed) = basicRocket.consumeFuel(1000.0)
    assert(updatedRocket.currentFuel === 4000.0)
    assert(consumed === 1000.0)
  }

  test("consumeFuel should not consume more fuel than available") {
    val (updatedRocket, consumed) = basicRocket.consumeFuel(6000.0) // More than available
    assert(updatedRocket.currentFuel === 0.0) // Should deplete fuel
    assert(consumed === 5000.0) // Only 5000 was available
  }

  test("calculateThrustVector should return zero if engines are off") {
    val rocketOff = basicRocket.copy(enginesOn = false)
    assert(rocketOff.calculateThrustVector(Vector3D(1, 0, 0)) === Vector3D.Zero)
  }

  test("calculateThrustVector should return correct thrust vector if engines are on") {
    // Replace the ??? in Rocket.calculateThrustVector with:
    // override def calculateThrustVector(direction: Vector3D): Vector3D = {
    //   if (enginesOn && currentFuel > 0) direction.normalize * thrust
    //   else Vector3D.Zero
    // }

    val rocketOn = basicRocket.copy(enginesOn = true)
    val direction = Vector3D(0.0, 1.0, 0.0) // Thrust upwards

    val thrustVector = rocketOn.calculateThrustVector(direction)
    assert(thrustVector.magnitude === basicRocket.thrust)
    assert(thrustVector.normalize === direction)

    // Test with non-normalized direction
    val nonNormalizedDirection = Vector3D(0.0, 2.0, 0.0)
    val thrustVectorNonNorm = rocketOn.calculateThrustVector(nonNormalizedDirection)
    assert(thrustVectorNonNorm.magnitude === basicRocket.thrust)
    assert(thrustVectorNonNorm.normalize === nonNormalizedDirection.normalize)
  }

  test("calculateThrustVector should return zero if no fuel left") {
    val rocketNoFuel = basicRocket.copy(currentFuel = 0.0, enginesOn = true)
    assert(rocketNoFuel.calculateThrustVector(Vector3D(1, 0, 0)) === Vector3D.Zero)
  }
}