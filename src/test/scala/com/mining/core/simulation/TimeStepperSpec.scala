package com.mining.core.simulation

import com.mining.core.physics.{State, Vector3D}
import org.scalatest.funsuite.AnyFunSuite
import org.scalactic.TolerantNumerics

class TimeStepperSpec extends AnyFunSuite {

  implicit val doubleEq = TolerantNumerics.tolerantDoubleEquality(1e-9)

  val initialMass = 10.0 // kg
  val initialForce = Vector3D(100.0, 0.0, 0.0) // 100 N in X direction
  val initialAcc = initialForce / initialMass // 10 m/s^2

  test("EulerTimeStepper should correctly update state for constant force") {
    // Replace the ??? in EulerTimeStepper.step with:
    // override def step(initialState: State, force: Vector3D, mass: Double, deltaTime: Double): State = {
    //   val acceleration = force / mass
    //   val newVelocity = initialState.velocity + acceleration * deltaTime
    //   val newPosition = initialState.position + newVelocity * deltaTime // Simple Euler integration
    //   State(newPosition, newVelocity)
    // }

    val initialState = State(Vector3D.Zero, Vector3D.Zero)
    val deltaTime = 1.0 // 1 second

    val newState = EulerTimeStepper.step(initialState, initialForce, initialMass, deltaTime)

    val expectedVelocity = initialAcc * deltaTime
    val expectedPosition = expectedVelocity * deltaTime

    assert(newState.velocity === expectedVelocity)
    assert(newState.position === expectedPosition)
  }

  test("EulerTimeStepper should handle non-zero initial velocity") {
    val initialState = State(Vector3D(0,0,0), Vector3D(5.0, 0.0, 0.0)) // 5 m/s in X
    val deltaTime = 1.0

    val newState = EulerTimeStepper.step(initialState, initialForce, initialMass, deltaTime)

    val expectedVelocity = initialState.velocity + initialAcc * deltaTime
    val expectedPosition = initialState.position + expectedVelocity * deltaTime

    assert(newState.velocity === expectedVelocity) // 5 + 10 = 15 m/s
    assert(newState.position === expectedPosition) // 0 + 15 * 1 = 15 m
  }

  test("EulerTimeStepper should handle zero force (constant velocity)") {
    val initialState = State(Vector3D(0,0,0), Vector3D(5.0, 0.0, 0.0))
    val deltaTime = 1.0
    val zeroForce = Vector3D.Zero

    val newState = EulerTimeStepper.step(initialState, zeroForce, initialMass, deltaTime)

    val expectedVelocity = initialState.velocity
    val expectedPosition = initialState.position + expectedVelocity * deltaTime

    assert(newState.velocity === expectedVelocity)
    assert(newState.position === expectedPosition)
  }
}