package com.lightbend.akkassembly

import akka.NotUsed
import akka.stream.scaladsl.{Flow, Source}

class EngineShop(shipmentSize: Int) {
  val shipments: Source[Shipment, NotUsed] = Source.cycle(
    () => Iterator.continually(Shipment(Seq.fill(shipmentSize)(new Engine)))
  )

  val engines: Source[Engine, NotUsed] = {
    shipments.mapConcat(_.engines)
  }

  val installEngine: Flow[UnfinishedCar, UnfinishedCar, NotUsed] = {
    Flow[UnfinishedCar].zip(engines)
      .map{
        case (unfinishedCar, engine) => unfinishedCar.installEngine(engine)
      }
  }
}
