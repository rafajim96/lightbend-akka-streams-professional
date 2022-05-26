package com.lightbend.akkassembly

import akka.actor.Cancellable
import akka.stream.scaladsl.Source

import scala.concurrent.duration.FiniteDuration

class BodyShop(buildTime: FiniteDuration) {
  val cars: Source[UnfinishedCar, Cancellable] = Source.tick(
    initialDelay = buildTime,
    interval = buildTime,
    tick = new UnfinishedCar
  )
}
