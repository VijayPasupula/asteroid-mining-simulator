package com.mining.core.logistics

// Represents a type of resource (e.g., Water, Iron, Nickel)
case class Resource(name: String, molecularFormula: Option[String] = None)

object Resources:
  val Water: Resource = Resource("Water", Some("H2O"))
  val Iron: Resource = Resource("Iron", Some("Fe"))
  val Nickel: Resource = Resource("Nickel", Some("Ni"))
  val Regolith: Resource = Resource("Regolith")
  val Propellant: Resource = Resource("Propellant") // Generic propellant

// Represents an inventory of resources with quantities
case class Inventory(items: Map[Resource, Double] = Map.empty):
  def add(resource: Resource, quantity: Double): Inventory = ???
  def remove(resource: Resource, quantity: Double): (Inventory, Boolean) = ??? // Returns updated inventory and success status
  def contains(resource: Resource, quantity: Double): Boolean = ???
  def getTotalMass: Double = ??? // Calculate total mass of resources