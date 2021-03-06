package com.nuna.trustdb.core

import java.sql.Date

import com.github.mrpowers.spark.fast.tests.DataFrameComparer
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.scalatest.funsuite.AnyFunSuite

class SparkTestBase extends AnyFunSuite {
  lazy val sparkSession = SparkSession.builder()
      .master("local[1]")
      .config("spark.ui.disable", "true")
      .config("spark.sql.shuffle.partitions", "1")
      .appName(this.getClass.getName)
      .getOrCreate()

  def assertDatasetEquality[T](actual: Dataset[T], expected: Dataset[T], ignoreNullable: Boolean = false,
      ignoreColumnNames: Boolean = false, ignoreOrdering: Boolean = true): Unit = {
    SparkTestBase.comparer.assertSmallDatasetEquality(actual, expected, ignoreNullable = ignoreNullable,
      ignoreColumnNames = ignoreColumnNames, orderedComparison = !ignoreOrdering)
  }

  def assertDataFrameEquality(actual: DataFrame, expected: DataFrame, ignoreNullable: Boolean = false,
      ignoreColumnNames: Boolean = false, ignoreOrdering: Boolean = true, precision: Double = 0.0): Unit = {
    if (precision == 0.0) {
      SparkTestBase.comparer.assertSmallDataFrameEquality(actual, expected, ignoreNullable = ignoreNullable,
        ignoreColumnNames = ignoreColumnNames, orderedComparison = !ignoreOrdering)
    } else {
      SparkTestBase.comparer.assertApproximateDataFrameEquality(actual, expected, precision = precision,
        ignoreNullable = ignoreNullable, ignoreColumnNames = ignoreColumnNames, orderedComparison = !ignoreOrdering)
    }
  }
}

object SparkTestBase {
  private[SparkTestBase] val comparer = new DataFrameComparer {}

  // Dangerous and powerful implicits lives here. Be careful what we add here.
  // Do NOT copy this to main - it is for tests only!
  object implicits {
    import scala.language.implicitConversions

    implicit def toOption[T](value: T): Option[T] = Some(value)

    implicit def toDate(date: String): Date = Date.valueOf(date)
  }
}
