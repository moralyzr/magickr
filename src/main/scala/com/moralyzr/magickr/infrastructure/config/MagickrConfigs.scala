package com.moralyzr.magickr.infrastructure.config

import cats.effect.IO
import cats.effect.kernel.Resource
import com.moralyzr.magickr.infrastructure.httpserver.AkkaHttpConfig
import com.typesafe.config.ConfigFactory
import com.typesafe.sslconfig.util.ConfigLoader

class MagickrConfigs extends AkkaHttpConfig :
  private lazy val configs = ConfigFactory.load()

  override def host(): String = configs.getString("magickr.http.host")

  override def port(): Int = configs.getInt("magickr.http.port")

  def close() = IO {}

object MagickrConfigs:
  def load(): Resource[IO, MagickrConfigs] =
    Resource.make(IO.pure(new MagickrConfigs()))(_.close())
