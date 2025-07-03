package com.mining.core.simulation

import com.mining.core.physics.State

// Trait for different time integration methods (e.g., Euler, Runge-Kutta)
trait TimeStepper:
  def step(initialState: State, force: com.mining.core.physics.Vector3D, mass: Double, deltaTime: Double): State = ???

object EulerTimeStepper extends TimeStepper:
  override def step(initialState: State, force: com.mining.core.physics.Vector3D, mass: Double, deltaTime: Double): State = ???

// You could add a more advanced one like RungeKuttaTimeStepper later
// object RungeKuttaTimeStepper extends TimeStepper:
//   override def step(...): State = ???