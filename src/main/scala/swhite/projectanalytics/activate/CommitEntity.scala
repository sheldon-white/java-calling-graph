package swhite.projectanalytics.activate

import swhite.projectanalytics.activate.ProjectContext._

trait CommitEntity extends Entity {
  var filename: String

  // Invariants are validation predicates that are verified in the entity lifecycle.
  // They are special instance methods:
  def invariantNameMustNotBeEmpty = invariant {
    filename != null && filename.nonEmpty
  }
}

