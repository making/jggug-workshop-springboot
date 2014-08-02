package com.example

import org.springframework.boot.SpringApplication

object App extends scala.App {
  SpringApplication.run(classOf[AppConfig], args: _ *)
}
