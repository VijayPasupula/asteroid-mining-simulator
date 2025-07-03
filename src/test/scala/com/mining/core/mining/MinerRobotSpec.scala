package com.mining.core.mining

import com.mining.core.logistics.{Inventory, Resources}
import com.mining.core.physics.{CelestialBody, Vector3D}
import com.mining.core.spacecraft.{Spacecraft, Rocket}
import org.scalatest.funsuite.AnyFunSuite
import org.scalactic.TolerantNumerics

class MinerRobotSpec extends AnyFunSuite {

  implicit val doubleEq = TolerantNumerics.tolerantDoubleEquality(1e-9)

  val initialAsteroid = CelestialBody(
    name = "Bennu",
    mass = 7.8e10, // Example mass
    radius = 280.0, // Example radius
    position = Vector3D(1e9, 1e9, 1e9),
    velocity = Vector3D.Zero,
    rotationalPeriod = 1.0,
    resources = Map(Resources.Water -> 1e6, Resources.Iron -> 5e5, Resources.Regolith -> 1e7)
  )

  val initialSpacecraft = Spacecraft(
    id = "SC001",
    name = "Mining Vessel Alpha",
    initialMass = 10000.0,
    currentState = com.mining.core.physics.State(Vector3D.Zero, Vector3D.Zero),
    mainEngine = Some(Rocket("MainEngine", 1000.0, 5000.0, 5000.0, 300.0, 20000.0)),
    rcsThrusters = None,
    cargoBay = Inventory(Map.empty),
    powerSupply = 10000.0, // Watts
    powerConsumption = 0.0
  )

  test("MinerRobot should mine resources up to its capacity") {
    val miner = MinerRobot("MR001", miningPower = 100.0, powerConsumption = 50.0, cargoCapacity = 500.0)

    // Replace the ??? in MinerRobot.mine with:
    // override def mine(target: CelestialBody, amount: Double, spacecraft: Spacecraft): (CelestialBody, Spacecraft, Double) = {
    //   val actualAmount = Math.min(amount, target.resources.getOrElse(Resources.Regolith, 0.0)) // Only mine existing regolith for simplicity
    //   val transferAmount = Math.min(actualAmount, spacecraft.cargoBay.add(Inventory(Map(Resources.Regolith -> actualAmount))).getTotalMass - spacecraft.cargoBay.getTotalMass) // How much can fit
    //
    //   val updatedTargetResources = target.resources.updatedWith(Resources.Regolith) {
    //     case Some(qty) if qty > 0 => Some(qty - transferAmount)
    //     case _ => None
    //   }
    //   val updatedTarget = target.copy(resources = updatedTargetResources)
    //
    //   val updatedSpacecraftCargo = spacecraft.cargoBay.add(Resources.Regolith, transferAmount)
    //   val updatedSpacecraft = spacecraft.copy(cargoBay = updatedSpacecraftCargo)
    //
    //   (updatedTarget, updatedSpacecraft, transferAmount)
    // }

    val amountToMine = 300.0 // kg
    val (updatedAsteroid, updatedSpacecraft, actualMined) = miner.mine(initialAsteroid, amountToMine, initialSpacecraft)

    assert(actualMined === 300.0)
    assert(updatedAsteroid.resources.get(Resources.Regolith).contains(initialAsteroid.resources(Resources.Regolith) - 300.0))
    assert(updatedSpacecraft.cargoBay.items.get(Resources.Regolith).contains(300.0))
  }

  test("MinerRobot should not mine more than available on asteroid") {
    val miner = MinerRobot("MR001", miningPower = 100.0, powerConsumption = 50.0, cargoCapacity = 500.0)
    val smallAsteroid = initialAsteroid.copy(resources = Map(Resources.Regolith -> 100.0))

    val amountToMine = 300.0 // More than available
    val (updatedAsteroid, updatedSpacecraft, actualMined) = miner.mine(smallAsteroid, amountToMine, initialSpacecraft)

    assert(actualMined === 100.0) // Only 100kg should be mined
    assert(!updatedAsteroid.resources.contains(Resources.Regolith)) // Resource should be depleted
    assert(updatedSpacecraft.cargoBay.items.get(Resources.Regolith).contains(100.0))
  }

  test("MinerRobot should respect spacecraft cargo capacity") {
    val miner = MinerRobot("MR001", miningPower = 100.0, powerConsumption = 50.0, cargoCapacity = 500.0)
    val smallCargoSpacecraft = initialSpacecraft.copy(cargoBay = Inventory(Map.empty), initialMass = initialSpacecraft.initialMass)

    val amountToMine = 500.0
    // Fill up the cargo bay
    val (midAsteroid, midSpacecraft, _) = miner.mine(initialAsteroid, amountToMine, smallCargoSpacecraft)

    val remainingAmountToMine = 100.0 // Try to mine more
    val (finalAsteroid, finalSpacecraft, actualMined) = miner.mine(midAsteroid, remainingAmountToMine, midSpacecraft)

    // Assuming a simple "fit what you can" logic
    assert(finalSpacecraft.cargoBay.getTotalMass === 600.0) // initial 500 + 100 from second mine
    assert(actualMined === 100.0)
  }

  // Add more tests for:
  // - Mining when no regolith resource
  // - Mining with different resource types
  // - Miner power limits (if mining power affects max output per step)
}