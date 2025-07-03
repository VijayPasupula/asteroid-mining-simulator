package com.mining.core.physics

import org.scalatest.funsuite.AnyFunSuite
import org.scalactic.TolerantNumerics

class DynamicsSpec extends AnyFunSuite {

  implicit val doubleEq = TolerantNumerics.tolerantDoubleEquality(1e-9)

  // A simple concrete implementation of PhysicalObject for testing Dynamics
  case class TestPhysicalObject(
    mass: Double,
    position: Vector3D,
    velocity: Vector3D,
    acceleration: Vector3D = Vector3D.Zero
  ) extends PhysicalObject

  test("NBodyDynamics.calculateForce should return zero force for no other bodies") {
    val obj = TestPhysicalObject(100.0, Vector3D(0, 0, 0), Vector3D(0, 0, 0))
    // This will initially use the ??? from the main code.
    // Replace the ??? in main code with:
    // override def calculateForce(obj: PhysicalObject, system: Seq[PhysicalObject]): Vector3D = {
    //   system.filter(_ != obj).foldLeft(Vector3D.Zero) { (totalForce, other) =>
    //     val r = other.position - obj.position
    //     val distanceSquared = r.magnitude * r.magnitude
    //     if (distanceSquared == 0) totalForce // Avoid division by zero if objects perfectly overlap
    //     else {
    //       val forceMagnitude = PhysicsConstants.G * obj.mass * other.mass / distanceSquared
    //       totalForce + r.normalize * forceMagnitude
    //     }
    //   }
    // }
    assert(NBodyDynamics.calculateForce(obj, Seq(obj)) === Vector3D.Zero)
  }

  test("NBodyDynamics.calculateForce should calculate gravitational force between two bodies") {
    // Replace the ??? in main code with the actual gravitational force calculation
    val obj1 = TestPhysicalObject(1.0, Vector3D(0, 0, 0), Vector3D(0, 0, 0))
    val obj2 = TestPhysicalObject(1.0, Vector3D(1, 0, 0), Vector3D(0, 0, 0)) // 1 meter away

    val expectedForceMagnitude = PhysicsConstants.G * obj1.mass * obj2.mass / (1.0 * 1.0)
    val expectedForceOnObj1 = Vector3D(expectedForceMagnitude, 0, 0)
    val expectedForceOnObj2 = Vector3D(-expectedForceMagnitude, 0, 0)

    assert(NBodyDynamics.calculateForce(obj1, Seq(obj1, obj2)).x === expectedForceOnObj1.x)
    assert(NBodyDynamics.calculateForce(obj1, Seq(obj1, obj2)).y === expectedForceOnObj1.y)
    assert(NBodyDynamics.calculateForce(obj1, Seq(obj1, obj2)).z === expectedForceOnObj1.z)

    assert(NBodyDynamics.calculateForce(obj2, Seq(obj1, obj2)).x === expectedForceOnObj2.x)
    assert(NBodyDynamics.calculateForce(obj2, Seq(obj1, obj2)).y === expectedForceOnObj2.y)
    assert(NBodyDynamics.calculateForce(obj2, Seq(obj1, obj2)).z === expectedForceOnObj2.z)
  }

  test("NBodyDynamics.applyForce should update position and velocity correctly (basic Euler)") {
    // Replace the ??? in main code with:
    // override def applyForce(obj: PhysicalObject, force: Vector3D, deltaTime: Double): PhysicalObject = {
    //   val newAcceleration = force / obj.mass
    //   val newVelocity = obj.velocity + newAcceleration * deltaTime
    //   val newPosition = obj.position + newVelocity * deltaTime // Simple Euler integration
    //   obj.copy(position = newPosition, velocity = newVelocity, acceleration = newAcceleration)
    // }

    val obj = TestPhysicalObject(1.0, Vector3D(0, 0, 0), Vector3D(0, 0, 0))
    val force = Vector3D(1.0, 0, 0) // 1 Newton force in X direction
    val deltaTime = 1.0 // 1 second

    val updatedObj = NBodyDynamics.applyForce(obj, force, deltaTime)

    assert(updatedObj.acceleration === Vector3D(1.0, 0, 0)) // a = F/m = 1/1
    assert(updatedObj.velocity === Vector3D(1.0, 0, 0)) // v_new = v_old + a*dt = 0 + 1*1
    assert(updatedObj.position === Vector3D(1.0, 0, 0)) // p_new = p_old + v_new*dt = 0 + 1*1
  }
}