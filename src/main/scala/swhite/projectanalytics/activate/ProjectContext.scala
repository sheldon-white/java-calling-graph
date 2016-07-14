package swhite.projectanalytics.activate

import net.fwbrasil.activate.ActivateContext
import net.fwbrasil.activate.storage.relational.PooledJdbcRelationalStorage
import net.fwbrasil.activate.storage.relational.idiom.mySqlDialect

// Initially, must be created the persistence context. It must be a singleton, so it makes sense to declare as "object".
object ProjectContext extends ActivateContext {

  val storage = new PooledJdbcRelationalStorage {
    val jdbcDriver = "com.mysql.jdbc.Driver"
    val user = Some("root")
    val password = None
    val url = "jdbc:mysql://192.168.99.100:32779/test"
    val dialect = mySqlDialect
  }
}
