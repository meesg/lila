package lila.insight

import play.api.libs.json._

case class JsonQuestion(
    dimension: String,
    metric: String,
    filters: Map[String, List[String]]
) {

  def question: Option[Question[_]] = {
    import InsightDimension._
    for {
      realMetric <- Metric.byKey get metric
      realFilters =
        filters
          .flatMap {
            case (filterKey, valueKeys) => {
              def build[X](dimension: InsightDimension[X]) =
                Filter[X](
                  dimension,
                  valueKeys.flatMap {
                    InsightDimension.valueByKey(dimension, _)
                  }
                ).some

              filterKey match {
                case Period.key           => build(Period)
                case Perf.key             => build(Perf)
                case Phase.key            => build(Phase)
                case Result.key           => build(Result)
                case Termination.key      => build(Termination)
                case Color.key            => build(Color)
                case OpeningFamily.key    => build(OpeningFamily)
                case OpeningVariation.key => build(OpeningVariation)
                case OpponentStrength.key => build(OpponentStrength)
                case PieceRole.key        => build(PieceRole)
                case MovetimeRange.key    => build(MovetimeRange)
                case MyCastling.key       => build(MyCastling)
                case OpCastling.key       => build(OpCastling)
                case QueenTrade.key       => build(QueenTrade)
                case MaterialRange.key    => build(MaterialRange)
                case CplRange.key         => build(CplRange)
                case EvalRange.key        => build(EvalRange)
                case Blur.key             => build(Blur)
                case TimeVariance.key     => build(TimeVariance)
                case _                    => none
              }
            }
          }
          .filterNot(_.isEmpty)
          .toList
      question <- {
        def build[X](dimension: InsightDimension[X]) = Question[X](dimension, realMetric, realFilters).some
        dimension match {
          case Date.key             => build(Date)
          case Perf.key             => build(Perf)
          case Phase.key            => build(Phase)
          case Result.key           => build(Result)
          case Termination.key      => build(Termination)
          case Color.key            => build(Color)
          case OpeningFamily.key    => build(OpeningFamily)
          case OpeningVariation.key => build(OpeningVariation)
          case OpponentStrength.key => build(OpponentStrength)
          case PieceRole.key        => build(PieceRole)
          case MovetimeRange.key    => build(MovetimeRange)
          case MyCastling.key       => build(MyCastling)
          case OpCastling.key       => build(OpCastling)
          case QueenTrade.key       => build(QueenTrade)
          case MaterialRange.key    => build(MaterialRange)
          case EvalRange.key        => build(EvalRange)
          case CplRange.key         => build(CplRange)
          case Blur.key             => build(Blur)
          case TimeVariance.key     => build(TimeVariance)
          case _                    => none
        }
      }
    } yield question
  }
}

object JsonQuestion {

  def fromQuestion(q: Question[_]) =
    JsonQuestion(
      dimension = q.dimension.key,
      metric = q.metric.key,
      filters = q.filters.view.map { case Filter(dimension, selected) =>
        dimension.key -> selected.map(InsightDimension.valueKey(dimension))
      }.toMap
    )

  implicit val QuestionFormats = Json.format[JsonQuestion]
}
