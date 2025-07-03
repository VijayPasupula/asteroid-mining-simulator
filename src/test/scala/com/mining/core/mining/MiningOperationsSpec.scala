package com.mining.core.mining

import com.mining.core.logistics.{Inventory, Resources}
import com.mining.core.physics.{CelestialBody, Vector3D}
import com.mining.core.spacecraft.{Spacecraft, Rocket}
import org.scalatest.funsuite.AnyFunSuite
import org.scalactic.TolerantNumerics

class MiningOperationsSpec extends AnyFunSuite {

  implicit val doubleEq = TolerantNumerics.tolerantDoubleEquality(1e-9)

  val initialAsteroid = CelestialBody(
    name = "AsteroidX",
    mass = 1e10,
    radius = 100.0,
    position = Vector3D(0, 0, 0),
    velocity = Vector3D.Zero,
    rotationalPeriod = 1.0,
    resources = Map(Resources.Regolith -> 1000.0, Resources.Water -> 100.0, Resources.Iron -> 50.0)
  )

  val minerRobot = MinerRobot("TestMiner", miningPower = 50.0, powerConsumption = 10.0, cargoCapacity = 500.0)
  val testSpacecraft = Spacecraft(
    id = "SC001",
    name = "TestSc",
    initialMass = 1000.0,
    currentState = com.mining.core.physics.State(Vector3D.Zero, Vector3D.Zero),
    mainEngine = Some(Rocket("MainEngine", 100.0, 1000.0, 1000.0, 300.0, 10000.0)),
    rcsThrusters = None,
    cargoBay = Inventory(),
    powerSupply = 1000.0
  )

  test("extractResources should extract correct amount of resources based on rate and duration") {
    // Replace the ??? in AsteroidMiningOperations.extractResources with:
    // def extractResources(
    //   asteroid: CelestialBody,
    //   miner: MinerRobot,
    //   extractionRate: Double, // e.g., kg/sec
    //   duration: Double // seconds
    // ): (CelestialBody, Inventory) = {
    //   val potentialExtractAmount = extractionRate * duration
    //   val availableRegolith = asteroid.resources.getOrElse(Resources.Regolith, 0.0)
    //   val actualExtractAmount = Math.min(potentialExtractAmount, availableRegolith)
    //
    //   val updatedAsteroidResources = asteroid.resources.updatedWith(Resources.Regolith) {
    //     case Some(qty) if qty > actualExtractAmount => Some(qty - actualExtractAmount)
    //     case _ => None
    //   }
    //   val updatedAsteroid = asteroid.copy(resources = updatedAsteroidResources)
    //   val extractedInventory = Inventory().add(Resources.Regolith, actualExtractAmount)
    //
    //   (updatedAsteroid, extractedInventory)
    // }

    val rate = 10.0 // kg/sec
    val duration = 50.0 // seconds
    val (updatedAsteroid, extracted) = AsteroidMiningOperations.extractResources(initialAsteroid, minerRobot, rate, duration)

    assert(extracted.items.get(Resources.Regolith).contains(500.0))
    assert(updatedAsteroid.resources.get(Resources.Regolith).contains(500.0)) // 1000 - 500
  }

  test("extractResources should not extract more than available in asteroid") {
    val rate = 10.0
    val duration = 200.0 // Will try to extract 2000kg, but only 1000 available
    val (updatedAsteroid, extracted) = AsteroidMiningOperations.extractResources(initialAsteroid, minerRobot, rate, duration)

    assert(extracted.items.get(Resources.Regolith).contains(1000.0)) // Only 1000 should be extracted
    assert(!updatedAsteroid.resources.contains(Resources.Regolith)) // Regolith should be depleted
  }

  test("refineResources should process raw materials with given efficiency") {
    // Replace the ??? in AsteroidMiningOperations.refineResources with:
    // def refineResources(
    //   rawMaterials: Inventory,
    //   refiningEfficiency: Double // 0.0 to 1.0
    // ): Inventory = {
    //   val refined = rawMaterials.items.map {
    //     case (Resources.Regolith, qty) =>
    //       // Simplified: Regolith yields 10% Water, 5% Iron
    //       val waterYield = qty * 0.1 * refiningEfficiency
    //       val ironYield = qty * 0.05 * refiningEfficiency
    //       Map(Resources.Water -> waterYield, Resources.Iron -> ironYield)
    //     case (res, qty) => Map(res -> qty) // Pass through other resources
    //   }.foldLeft(Inventory()) { (acc, map) =>
    //     map.foldLeft(acc) { case (i, (res, qty)) => i.add(res, qty) }
    //   }
    //   refined.remove(Resources.Regolith, rawMaterials.items.getOrElse(Resources.Regolith, 0.0))._1 // Consume regolith
    // }

    val raw = Inventory().add(Resources.Regolith, 100.0) // 100kg regolith
    val efficiency = 0.8 // 80% efficient

    val refined = AsteroidMiningOperations.refineResources(raw, efficiency)

    // Based on simplified example (10% water, 5% iron from regolith)
    assert(refined.items.get(Resources.Water).contains(100.0 * 0.1 * 0.8)) // 8.0 kg water
    assert(refined.items.get(Resources.Iron).contains(100.0 * 0.05 * 0.8)) // 4.0 kg iron
    assert(!refined.items.contains(Resources.Regolith))
  }

  test("producePropellant should convert water ice to propellant with efficiency") {
    // Replace the ??? in AsteroidMiningOperations.producePropellant with:
    // def producePropellant(
    //   waterIce: Double, // kg of water ice
    //   efficiency: Double // 0.0 to 1.0
    // ): Double = {
    //   waterIce * 0.9 * efficiency // Assume 90% mass conversion from water to propellant
    // }

    val waterAmount = 100.0 // kg
    val efficiency = 0.75 // 75% efficient

    val propellant = AsteroidMiningOperations.producePropellant(waterAmount, efficiency)
    assert(propellant === 100.0 * 0.9 * 0.75) // 67.5 kg
  }

  test("producePropellant should return 0 if no water or 0 efficiency") {
    assert(AsteroidMiningOperations.producePropellant(0.0, 0.8) === 0.0)
    assert(AsteroidMiningOperations.producePropellant(100.0, 0.0) === 0.0)
  }
}