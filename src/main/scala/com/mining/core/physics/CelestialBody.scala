package com.mining.core.physics

import com.mining.core.logistics.Resource

// Case class to represent a celestial body (planet, moon, asteroid)
case class CelestialBody(
  name: String,
  mass: Double, // kg
  radius: Double, // meters
  position: Vector3D, // meters (relative to origin, e.g., Sun)
  velocity: Vector3D, // meters/sec
  rotationalPeriod: Double, // seconds
  resources: Map[Resource, Double] = Map.empty // Available resources and their quantity (e.g., tons)
)

// Trait for anything that can be affected by gravity
trait GravitationalBody:
  def mass: Double
  def position: Vector3D

// Definition of a 3D Vector for positions, velocities, forces
case class Vector3D(x: Double, y: Double, z: Double):
  def +(other: Vector3D): Vector3D = Vector3D(x + other.x, y + other.y, z + other.z)
  def -(other: Vector3D): Vector3D = Vector3D(x - other.x, y - other.y, z - other.z)
  def *(scalar: Double): Vector3D = Vector3D(x * scalar, y * scalar, z * scalar)
  def /(scalar: Double): Vector3D = Vector3D(x / scalar, y / scalar, z / scalar)
  def magnitude: Double = math.sqrt(x*x + y*y + z*z)
  def normalize: Vector3D = this / magnitude
  def dot(other: Vector3D): Double = x * other.x + y * other.y + z * other.z
  def cross(other: Vector3D): Vector3D = Vector3D(
    y * other.z - z * other.y,
    z * other.x - x * other.z,
    x * other.y - y * other.x
  )

object Vector3D:
  val Zero: Vector3D = Vector3D(0.0, 0.0, 0.0)