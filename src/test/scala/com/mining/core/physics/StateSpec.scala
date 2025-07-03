package com.mining.core.physics

import org.scalatest.funsuite.AnyFunSuite

class StateSpec extends AnyFunSuite {

  test("State should be created correctly with position and velocity") {
    val pos = Vector3D(1.0, 2.0, 3.0)
    val vel = Vector3D(4.0, 5.0, 6.0)
    val state = State(pos, vel)
    assert(state.position === pos)
    assert(state.velocity === vel)
  }

  test("State equality should work correctly") {
    val pos1 = Vector3D(1.0, 2.0, 3.0)
    val vel1 = Vector3D(4.0, 5.0, 6.0)
    val state1 = State(pos1, vel1)

    val pos2 = Vector3D(1.0, 2.0, 3.0)
    val vel2 = Vector3D(4.0, 5.0, 6.0)
    val state2 = State(pos2, vel2)

    val pos3 = Vector3D(1.1, 2.0, 3.0)
    val state3 = State(pos3, vel1)

    assert(state1 === state2)
    assert(state1 !== state3)
  }
}