package com.lightbend.akkassembly

import akka.NotUsed
import akka.stream.{ActorAttributes, Supervision}
import akka.stream.scaladsl.Flow

class QualityAssurance {

  val decider: Supervision.Decider = {
    case _: QualityAssurance.CarFailedException => Supervision.Resume
    case _ => Supervision.Stop
  }

  val inspect: Flow[UnfinishedCar, Car, NotUsed] = {
    Flow[UnfinishedCar]
      .collect {
      case unfinishedCar if inspectCar(unfinishedCar)=> Car(new SerialNumber, unfinishedCar.color.get, unfinishedCar.engine.get, unfinishedCar.wheels, unfinishedCar.upgrade)
      case failedUnfinishedCar =>
        throw new QualityAssurance.CarFailedException(failedUnfinishedCar)
    }.withAttributes(
        ActorAttributes.supervisionStrategy(decider)
      )
  }

  def inspectCar(car: UnfinishedCar): Boolean = {
    car.engine.isDefined && car.color.isDefined && car.wheels.size == 4
  }
}

object QualityAssurance {
  class CarFailedException(car: UnfinishedCar) extends IllegalStateException(s"Car $car failed inspection.")
}
