package example

import eu.timepit.refined.api.Refined
import eu.timepit.refined.pureconfig._
import eu.timepit.refined.{W, string}
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.NonNegative
import pureconfig.ConfigSource
import pureconfig.generic.auto._

object refined {
  type NonNegativeLong = Long Refined NonNegative
  type IsSeverity = string.MatchesRegex[W.`"Warning|Stop|CalloutSignal"`.T]
  type Severity = String Refined IsSeverity
  val severity: Severity = "Warning"
}

import refined._

case class AlertHighNumberOfUnknownKeys(
   severityAndThreshold: Map[Severity, (NonNegativeLong, NonNegativeLong)] = Map(severity -> (25000L, 5L)),
)

object Hello extends App {
  implicit val projectConfig = ConfigSource.default.at("ProjectName").loadOrThrow[AlertHighNumberOfUnknownKeys]
  println(projectConfig)

}