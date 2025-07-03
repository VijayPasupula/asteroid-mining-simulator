package com.mining.core.logistics

import org.scalatest.funsuite.AnyFunSuite
import org.scalactic.TolerantNumerics

class InventorySpec extends AnyFunSuite {

  implicit val doubleEq = TolerantNumerics.tolerantDoubleEquality(1e-9)

  test("Inventory should be empty by default") {
    val inv = Inventory()
    assert(inv.items.isEmpty)
  }

  test("add should correctly add a new resource") {
    val inv = Inventory().add(Resources.Water, 100.0)
    assert(inv.items.get(Resources.Water).contains(100.0))
  }

  test("add should correctly add to an existing resource") {
    val inv = Inventory(Map(Resources.Water -> 50.0)).add(Resources.Water, 100.0)
    assert(inv.items.get(Resources.Water).contains(150.0))
  }

  test("add should handle adding different resources") {
    val inv = Inventory().add(Resources.Water, 100.0).add(Resources.Iron, 50.0)
    assert(inv.items.get(Resources.Water).contains(100.0))
    assert(inv.items.get(Resources.Iron).contains(50.0))
  }

  test("remove should correctly remove an existing resource") {
    val initialInv = Inventory(Map(Resources.Water -> 100.0, Resources.Iron -> 50.0))
    val (updatedInv, success) = initialInv.remove(Resources.Water, 50.0)
    assert(success)
    assert(updatedInv.items.get(Resources.Water).contains(50.0))
    assert(updatedInv.items.get(Resources.Iron).contains(50.0)) // Other resources unchanged
  }

  test("remove should remove resource entry if quantity becomes zero") {
    val initialInv = Inventory(Map(Resources.Water -> 50.0))
    val (updatedInv, success) = initialInv.remove(Resources.Water, 50.0)
    assert(success)
    assert(updatedInv.items.isEmpty)
  }

  test("remove should not remove if not enough quantity and return false") {
    val initialInv = Inventory(Map(Resources.Water -> 50.0))
    val (updatedInv, success) = initialInv.remove(Resources.Water, 100.0)
    assert(!success)
    assert(updatedInv === initialInv) // Inventory should be unchanged
  }

  test("remove should not remove if resource not present and return false") {
    val initialInv = Inventory(Map(Resources.Iron -> 50.0))
    val (updatedInv, success) = initialInv.remove(Resources.Water, 10.0)
    assert(!success)
    assert(updatedInv === initialInv)
  }

  test("contains should return true if resource and quantity are present") {
    val inv = Inventory(Map(Resources.Water -> 100.0, Resources.Iron -> 50.0))
    assert(inv.contains(Resources.Water, 75.0))
    assert(inv.contains(Resources.Iron, 50.0))
  }

  test("contains should return false if not enough quantity") {
    val inv = Inventory(Map(Resources.Water -> 100.0))
    assert(!inv.contains(Resources.Water, 101.0))
  }

  test("contains should return false if resource not present") {
    val inv = Inventory(Map(Resources.Iron -> 50.0))
    assert(!inv.contains(Resources.Water, 10.0))
  }

  test("getTotalMass should calculate sum of all resource quantities") {
    val inv = Inventory(Map(Resources.Water -> 100.0, Resources.Iron -> 50.0, Resources.Nickel -> 25.0))
    assert(inv.getTotalMass === 175.0)
  }

  test("getTotalMass should be zero for an empty inventory") {
    val inv = Inventory()
    assert(inv.getTotalMass === 0.0)
  }
}