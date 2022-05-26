package com.lightbend.akkassembly

import akka.NotUsed
import akka.stream.scaladsl.{Flow, Source}

class WheelShop {
  val wheels: Source[Wheel, NotUsed] = Source.repeat(
    new Wheel
  )

  val installWheels: Flow[UnfinishedCar, UnfinishedCar, NotUsed] = {
    Flow[UnfinishedCar].zip(wheels)
      .map{
        case (unfinishedCar, wheel) => unfinishedCar.installWheels(Seq.fill(4)(wheel))
      }
  }
}
