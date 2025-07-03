package com.mining.core.physics

import com.mining.core.logistics.Resources
import org.scalatest.funsuite.AnyFunSuite
import org.scalactic.TolerantNumerics

class CelestialBodySpec extends AnyFunSuite {

  implicit val doubleEq = TolerantNumerics.tolerantDoubleEquality(1e-9) // For comparing doubles

  test("Vector3D addition should work correctly") {
    val v1 = Vector3D(1.0, 2.0, 3.0)
    val v2 = Vector3D(4.0, 5.0, 6.0)
    assert(v1 + v2 === Vector3D(5.0, 7.0, 9.0))
  }

  test("Vector3D subtraction should work correctly") {
    val v1 = Vector3D(5.0, 7.0, 9.0)
    val v2 = Vector3D(4.0, 5.0, 6.0)
    assert(v1 - v2 === Vector3D(1.0, 2.0, 3.0))
  }

  test("Vector3D scalar multiplication should work correctly") {
    val v = Vector3D(1.0, 2.0, 3.0)
    assert(v * 2.0 === Vector3D(2.0, 4.0, 6.0))
  }

  test("Vector3D scalar division should work correctly") {
    val v = Vector3D(2.0, 4.0, 6.0)
    assert(v / 2.0 === Vector3D(1.0, 2.0, 3.0))
  }

  test("Vector3D magnitude should be calculated correctly") {
    val v = Vector3D(3.0, 4.0, 0.0)
    assert(v.magnitude === 5.0)
    assert(Vector3D(0.0, 0.0, 0.0).magnitude === 0.0)
  }

  test("Vector3D normalization should produce a unit vector") {
    val v = Vector3D(3.0, 4.0, 0.0)
    val normalizedV = v.normalize
    assert(normalizedV.magnitude === 1.0)
    assert(normalizedV.x === 0.6)
    assert(normalizedV.y === 0.8)
    assert(normalizedV.z === 0.0)
  }

  test("Vector3D dot product should work correctly") {
    val v1 = Vector3D(1.0, 2.0, 3.0)
    val v2 = Vector3D(4.0, 5.0, 6.0)
    assert(v1.dot(v2) === 32.0) // 1*4 + 2*5 + 3*6 = 4 + 10 + 18 = 32
  }

  test("Vector3D cross product should work correctly") {
    val i = Vector3D(1.0, 0.0, 0.0)
    val j = Vector3D(0.0, 1.0, 0.0)
    val k = Vector3D(0.0, 0.0, 1.0)
    assert(i.cross(j) === k)
    assert(j.cross(i) === k * -1.0)
    assert(i.cross(i) === Vector3D.Zero)
  }

  test("CelestialBody should be created with correct properties") {
    val earth = CelestialBody(
      name = "Earth",
      mass = 5.972e24, // kg
      radius = 6.371e6, // meters
      position = Vector3D(0.0, 0.0, 0.0),
      velocity = Vector3D(0.0, 0.0, 0.0),
      rotationalPeriod = 86400.0,
      resources = Map(Resources.Water -> 1e15)
    )
    assert(earth.name === "Earth")
    assert(earth.mass === 5.972e24)
    assert(earth.resources.get(Resources.Water).contains(1e15))
  }
}