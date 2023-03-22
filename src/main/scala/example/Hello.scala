package example

import _root_.pureconfig.error.{CannotConvert, FailureReason}
import _root_.pureconfig.{ConfigReader, ConfigWriter}
import cats.syntax.all._
import eu.timepit.refined.pureconfig._
import eu.timepit.refined.{W, string}
import eu.timepit.refined.api.{RefType, Refined, Validate}
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.NonNegative
import pureconfig.ConfigSource
import pureconfig.generic.auto._
import shapeless.Lazy

package object refined {
  type NonNegativeLong = Long Refined NonNegative
  type IsSeverity = string.MatchesRegex[W.`"Warning|Stop|CalloutSignal"`.T]
  type Severity = String Refined IsSeverity

  implicit def deriveRefinedMap[F[_, _], P, T](implicit
                                               reader: Lazy[ConfigReader[T]],
                                               rt: RefType[F],
                                               v: Validate[String, P]
                                              ): ConfigReader[Map[F[String, P], T]] = {
    val cr = reader.value
    ConfigReader.mapReader[T](cr).emap { strKeyMap =>
      strKeyMap
        .map { case (key, value) =>
          rt.refine.apply(key)(v).map(_ -> value)
        }
        .toList
        .sequence
        .leftMap(CannotConvert(strKeyMap.toString, "Refined Map", _))
        .leftWiden[FailureReason]
        .map(_.toMap)
    }
  }

  implicit def deriveRefinedMapWriter[F[_, _], P, T](implicit
                                                     writer: ConfigWriter[T],
                                                     rt: RefType[F]
                                                    ): ConfigWriter[Map[F[String, P], T]] =
    ConfigWriter.mapWriter[T].contramap[Map[F[String, P], T]] { refinedMap =>
      refinedMap.map { case (k, v) =>
        rt.unwrap(k) -> v
      }
    }


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