package com.lightbend.akkassembly

import akka.NotUsed
import akka.stream.FlowShape
import akka.stream.scaladsl.{Flow, GraphDSL, Merge, Balance}

class UpgradeShop {
  val installUpgrades: Flow[UnfinishedCar, UnfinishedCar, NotUsed] = {
    Flow.fromGraph(
      GraphDSL.create() {
        implicit builder: GraphDSL.Builder[NotUsed] =>
          import GraphDSL.Implicits._

          val installDx = Flow[UnfinishedCar].map(_.installUpgrade(Upgrade.DX))
          val installSport = Flow[UnfinishedCar].map(_.installUpgrade(Upgrade.Sport))
          val standardCars = Flow[UnfinishedCar]

          val balance = builder.add(Balance[UnfinishedCar](3))
          val merge = builder.add(Merge[UnfinishedCar](3))

          balance ~> installDx ~> merge
          balance ~> installSport ~> merge
          balance ~> standardCars ~> merge

          FlowShape(balance.in, merge.out)
      }
    )
  }
}
