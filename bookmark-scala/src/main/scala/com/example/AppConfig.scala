package com.example

import javax.sql.DataSource

import net.sf.log4jdbc.Log4jdbcProxyDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.{DataSourceAutoConfiguration, DataSourceBuilder, DataSourceProperties}
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.{Bean, ComponentScan, Configuration}

@Configuration
@EnableAutoConfiguration
@ComponentScan
class AppConfig {
  @Autowired
  var properties: DataSourceProperties = _
  var ds: DataSource = _

  @ConfigurationProperties(prefix = DataSourceAutoConfiguration.CONFIGURATION_PREFIX)
  @Bean(destroyMethod = "close")
  def realDataSource: DataSource = {
    val factory: DataSourceBuilder = DataSourceBuilder.create(this.properties.getClassLoader)
      .url(this.properties.getUrl)
      .username(this.properties.getUsername)
      .password(this.properties.getPassword)
    this.ds = factory.build()
    this.ds
  }

  @Bean
  def dataSource: DataSource = {
    new Log4jdbcProxyDataSource(this.ds)
  }
}
