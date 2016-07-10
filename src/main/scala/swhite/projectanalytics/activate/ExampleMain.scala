package swhite.projectanalytics.activate

import swhite.projectanalytics.activate.ProjectContext._
import net.fwbrasil.radon.transaction.Transaction

//// An entity shall extend the trait "Entity"
//// You can declare the properties as val or var, where they are immutable or not.
//trait Person extends Entity {
//  var name: String
//
//  // Invariants are validation predicates that are verified in the entity lifecycle.
//  // They are special instance methods:
//  def invariantNameMustNotBeEmpty = invariant {
//    name != null && name.nonEmpty
//  }
//}
//class NaturalPerson(var name: String, var motherName: String) extends CommitEntity
//
//object ExampleMain extends App {
//
//  // Use whenever entities within transactions
//  // It is not necessary to call a method like "store" or "save" to add the entity.
//  // Just create, use, and it will be persisted.
//  transactional {
//    val person = new NaturalPerson("John", "Marie")
//    person.name = "John2"
//    println(person.name)
//  }
//
//  // Queries
//  // The query operators available are :==, :<, :>, :<=, :>=, isNone, isSome, :||, :&&, like and matches.
//  // Note that the queries can be made about abstract entities (traits and classes).
//  // Perform queries within transactions
//  transactional {
//    val result = query {
//      (person: CommitEntity) => where(person.name :== "John2") select (person)
//    }
//    for (person <- result)
//      println(person.name)
//  }
//
//  // There are alternative forms of query.
//  // With the "select where" you can use a list of criterias.
//  transactional {
//    val personList1 = all[CommitEntity]
//    val personList2 = select[NaturalPerson] where (_.name :== "John2", _.motherName :== "Marie")
//    println(personList1, personList2)
//  }
//
//  // To delete an entity
//  transactional {
//    for (person <- all[CommitEntity])
//      person.delete
//  }
//
//  // Typically transactional blocks are controlled by the framework.
//  // But you can control the transaction as follows
//  val transaction = new Transaction
//  transactional(transaction) {
//    new NaturalPerson("Test", "Mother")
//  }
//  transaction.commit
//
//  // Defining the propagation of the transaction
//  transactional {
//    val person = new NaturalPerson("Test", "Mother")
//    transactional(mandatory) {
//      person.name = "Test2"
//    }
//    println(person.name)
//  }
//
//  // Nested transactions are a type of propagation
//  transactional {
//    val person = new NaturalPerson("Test", "Mother")
//    transactional(nested) {
//      person.name = "Test2"
//    }
//    println(person.name)
//  }
//
//  // Activate supports mass update/delete statements.
//  // Use them when you have to perform a really big update/delete operation.
//  transactional {
//    update {
//      (person: NaturalPerson) => where(person.name :== "Test") set (person.name := "Test2")
//    }
//    //		delete {
//    //			(person: NaturalPerson) => where(person.name :== "Test2")
//    //		}
//  }
//}