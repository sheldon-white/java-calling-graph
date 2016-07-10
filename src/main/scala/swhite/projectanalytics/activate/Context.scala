package swhite.projectanalytics.activate

import net.fwbrasil.activate.ActivateContext
import net.fwbrasil.activate.storage.relational.PooledJdbcRelationalStorage
import net.fwbrasil.activate.storage.relational.idiom.mySqlDialect

// Initially, must be created the persistence context. It must be a singleton, so it makes sense to declare as "object".
object projectContext extends ActivateContext {

  val storage = new PooledJdbcRelationalStorage {
    val jdbcDriver = "com.mysql.jdbc.Driver"
    val user = Some("swhite")
    val password = Some("swhite")
    val url = "jdbc:mysql://192.168.99.101:32771/test"
    val dialect = mySqlDialect
  }
}
