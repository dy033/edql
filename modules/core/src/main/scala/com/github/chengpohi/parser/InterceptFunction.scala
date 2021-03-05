package com.github.chengpohi.parser

import com.github.chengpohi.context.EQLContext
import com.github.chengpohi.dsl.eql.{Definition, ErrorHealthRequestDefinition, PureStringDefinition}
import com.typesafe.config.ConfigFactory

trait InterceptFunction {
  val MAX_NUMBER: Int = 500

  case class GetMappingInstruction(indexName: String) extends Instruction2 {
    override def name: String = "GetMapping"

    def execute(implicit eql: EQLContext): Definition[_] = {
      import eql._
      get mapping indexName
    }
  }

  case class CreateIndexInstruction(indexName: String) extends Instruction2 {
    override def name: String = "GetMapping"

    def execute(implicit eql: EQLContext): Definition[_] = {
      import eql._
      create index indexName
    }
  }


  case class GetClusterStateInstruction() extends Instruction2 {
    override def name: String = "GetMapping"

    def execute(implicit eql: EQLContext): Definition[_] = {
      import eql._
      cluster state
    }
  }

  case class GetClusterSettingsInstruction() extends Instruction2 {
    override def name: String = "GetMapping"

    def execute(implicit eql: EQLContext): Definition[_] = {
      import eql._
      cluster settings
    }
  }

  case class GetClusterStatsInstruction() extends Instruction2 {
    override def name: String = "GetMapping"

    def execute(implicit eql: EQLContext): Definition[_] = {
      import eql._
      cluster stats
    }
  }

  case class CatNodesInstruction() extends Instruction2 {
    override def name: String = "GetMapping"

    def execute(implicit eql: EQLContext): Definition[_] = {
      import eql._
      cat nodes
    }
  }

  case class GetAllocationInstruction() extends Instruction2 {
    override def name: String = "GetMapping"

    def execute(implicit eql: EQLContext): Definition[_] = {
      import eql._
      cat allocation
    }
  }

  case class CatMasterInstruction() extends Instruction2 {
    override def name: String = "GetMapping"

    def execute(implicit eql: EQLContext): Definition[_] = {
      import eql._
      cat master
    }
  }

  case class CatIndicesInstruction() extends Instruction2 {
    override def name: String = "GetMapping"

    def execute(implicit eql: EQLContext): Definition[_] = {
      import eql._
      cat indices
    }
  }

  case class CatShardsInstruction() extends Instruction2 {
    override def name: String = "GetMapping"

    def execute(implicit eql: EQLContext): Definition[_] = {
      import eql._
      cat shards
    }
  }

  case class CatCountInstruction() extends Instruction2 {
    override def name: String = "GetMapping"

    def execute(implicit eql: EQLContext): Definition[_] = {
      import eql._
      cat count
    }
  }

  case class CatAllocationInstruction() extends Instruction2 {
    override def name: String = "Allocation"

    def execute(implicit eql: EQLContext): Definition[_] = {
      import eql._
      cat allocation
    }
  }

  case class CatPendingInstruction() extends Instruction2 {
    override def name: String = "GetMapping"

    def execute(implicit eql: EQLContext): Definition[_] = {
      import eql._
      cat pending_tasks
    }
  }

  case class CatRecoveryInstruction() extends Instruction2 {
    override def name: String = "GetMapping"

    def execute(implicit eql: EQLContext): Definition[_] = {
      import eql._
      cat recovery
    }
  }

  case class IndicesStatsInstruction() extends Instruction2 {
    override def name: String = "GetMapping"

    def execute(implicit eql: EQLContext): Definition[_] = {
      import eql._
      indice stats NodeType.ALL flag FlagType.ALL
    }
  }


  case class NodeStatsInstruction() extends Instruction2 {
    override def name: String = "GetMapping"

    def execute(implicit eql: EQLContext): Definition[_] = {
      import eql._
      node stats NodeType.ALL flag FlagType.ALL
    }
  }


  case class ClusterSettingsInstruction() extends Instruction2 {
    override def name: String = "GetMapping"

    def execute(implicit eql: EQLContext): Definition[_] = {
      import eql._
      cluster settings
    }
  }


  case class NodeSettingsInstruction() extends Instruction2 {
    override def name: String = "GetMapping"

    def execute(implicit eql: EQLContext): Definition[_] = {
      import eql._
      node info
    }
  }

  case class PendingTasksInstruction() extends Instruction2 {
    override def name: String = "GetMapping"

    def execute(implicit eql: EQLContext): Definition[_] = {
      import eql._
      pending tasks
    }
  }

  case class IndexSettingsInstruction(indexName: String) extends Instruction2 {
    override def name: String = "GetMapping"

    def execute(implicit eql: EQLContext): Definition[_] = {
      import eql._
      get settings indexName
    }
  }


  case class ShutdownInstruction() extends Instruction2 {
    override def name: String = "ShutDown"

    def execute(implicit eql: EQLContext): Definition[_] = {
      import eql._
      ShutDownRequestDefinition()
    }
  }

  case class CountInstruction(indexName: String) extends Instruction2 {
    override def name: String = "GetMapping"

    def execute(implicit eql: EQLContext): Definition[_] = {
      import eql._
      get settings indexName
    }
  }


  case class DeleteIndexInstruction(indexName: String) extends Instruction2 {
    override def name: String = "GetMapping"

    def execute(implicit eql: EQLContext): Definition[_] = {
      import eql._
      delete index indexName
    }
  }

  case class DeleteDocInstruction(indexName: String, indexType: String, _id: String) extends Instruction2 {
    override def name: String = "GetMapping"

    def execute(implicit eql: EQLContext): Definition[_] = {
      import eql._
      delete in indexName / indexType id _id
    }
  }

  case class MatchQueryInstruction(indexName: String, indexType: Option[String], queryData: Map[String, String]) extends Instruction2 {
    override def name: String = "GetMapping"

    def execute(implicit eql: EQLContext): Definition[_] = {
      import eql._
      search in indexName / indexType must queryData from 0 size MAX_NUMBER
    }
  }

  case class QueryInstruction(indexName: String, indexType: Option[String],
                              queryData: Map[String, String]) extends Instruction2 {
    override def name: String = "GetMapping"

    def execute(implicit eql: EQLContext): Definition[_] = {
      import eql._
      search in indexName / indexType must queryData from 0 size MAX_NUMBER
    }
  }

  case class BulkUpdateInstruction(indexName: String,
                                   indexType: Option[String],
                                   updateFields: Map[String, String]) extends Instruction2 {
    override def name: String = "GetMapping"

    def execute(implicit eql: EQLContext): Definition[_] = {
      import eql._
      bulk update indexName / indexType fields updateFields
    }
  }


  case class UpdateDocInstruction(indexName: String, indexType: Option[String],
                                  updateFields: Map[String, String], _id: String) extends Instruction2 {
    override def name: String = "GetMapping"

    def execute(implicit eql: EQLContext): Definition[_] = {
      import eql._
      update id _id in indexName / indexType docAsUpsert updateFields
    }
  }


  //  def reindexIndex: INSTRUMENT_TYPE = {
  //    case Seq(sourceIndex, targetIndex, sourceIndexType, fields) => {
  //      reindex into targetIndex / sourceIndexType from sourceIndex fields fields
  //        .extract[List[String]]
  //    }
  //  }
  //
  //  def bulkIndex: INSTRUMENT_TYPE = {
  //    case Seq(indexName, indexType, fields) => {
  //      bulk index indexName / indexType doc fields
  //        .extract[List[List[(String, String)]]]
  //    }
  //  }
  //
  //  def createDoc: INSTRUMENT_TYPE = {
  //    case Seq(indexName, indexType, fields) => {
  //      index into indexName / indexType fields fields
  //        .extract[List[(String, String)]]
  //    }
  //    case Seq(indexName, indexType, fields, _id) => {
  //      index into indexName / indexType fields fields
  //        .extract[List[(String, String)]] id _id
  //    }
  //  }
  //
  //  def analysisText: INSTRUMENT_TYPE = {
  //    case Seq(doc, analyzer) => {
  //      analyze text doc in ELASTIC_SHELL_INDEX_NAME analyzer analyzer
  //    }
  //  }
  //
  //  def createAnalyzer: INSTRUMENT_TYPE = {
  //    case Seq(analyzer) => {
  //      val analysisSettings = Obj(("analysis", analyzer))
  //      create analyzer analysisSettings.toJson
  //    }
  //  }
  //
  //  def mapping: INSTRUMENT_TYPE = {
  //    case Seq(indexName, mapping) => {
  //      create index indexName mappings mapping.toJson
  //    }
  //  }
  //
  //  def updateMapping: INSTRUMENT_TYPE = {
  //    case Seq(indexName, indexType, mapping) => {
  //      update index indexName / indexType mapping mapping.toJson
  //    }
  //  }
  //
  //  def aggsCount: INSTRUMENT_TYPE = {
  //    case Seq(indexName, indexType, name) => {
  //      aggs in indexName / indexType avg name
  //    }
  //  }
  //
  //  def aggsTerm: INSTRUMENT_TYPE = {
  //    case Seq(indexName, indexType, name) => {
  //      aggs in indexName / indexType term name
  //    }
  //  }
  //
  //  def histAggs: INSTRUMENT_TYPE = {
  //    case Seq(indexName, indexType, name, _interval, _field) => {
  //      aggs in indexName / indexType hist name interval _interval field _field
  //    }
  //  }
  //
  //  def alias: INSTRUMENT_TYPE = {
  //    case Seq(targetIndex, sourceIndex) => {
  //      add alias targetIndex on sourceIndex
  //    }
  //  }
  //
  //  def getDocById: INSTRUMENT_TYPE = {
  //    case Seq(indexName, indexType, _id) => {
  //      search in indexName / indexType where id equal _id
  //    }
  //  }
  //
  //  def createRepository: INSTRUMENT_TYPE = {
  //    case Seq(repositoryName, repositoryType, settings) => {
  //      create repository repositoryName tpe repositoryType settings settings
  //        .extract[Map[String, String]]
  //    }
  //  }
  //
  //  def createSnapshot: INSTRUMENT_TYPE = {
  //    case Seq(snapshotName, repositoryName) => {
  //      create snapshot snapshotName in repositoryName
  //    }
  //  }
  //
  //  def deleteSnapshot: INSTRUMENT_TYPE = {
  //    case Seq(snapshotName, repositoryName) => {
  //      delete snapshot snapshotName from repositoryName
  //    }
  //  }
  //
  //  def restoreSnapshot: INSTRUMENT_TYPE = {
  //    case Seq(snapshotName, repositoryName) => {
  //      restore snapshot snapshotName from repositoryName
  //    }
  //  }
  //
  //  def closeIndex: INSTRUMENT_TYPE = {
  //    case Seq(indexName) => {
  //      close index indexName
  //    }
  //  }
  //
  //  def openIndex: INSTRUMENT_TYPE = {
  //    case Seq(indexName) => {
  //      open index indexName
  //    }
  //  }
  //
  //  def dumpIndex: INSTRUMENT_TYPE = {
  //    case Seq(indexName, fileName) => {
  //      dump index indexName into fileName
  //    }
  //  }
  //
  //  def getSnapshot: INSTRUMENT_TYPE = {
  //    case Seq(snapshotName, repositoryName) => {
  //      get snapshot snapshotName from repositoryName
  //    }
  //    case Seq(repositoryName) => {
  //      get snapshot "*" from repositoryName
  //    }
  //  }
  //
  //  def waitForStatus: INSTRUMENT_TYPE = {
  //    case Seq(status) => {
  //      waiting index "*" timeout "100s" status "GREEN"
  //    }
  //  }
  //
  //  def error: INSTRUMENT_TYPE = parameters => {
  //    ParserErrorDefinition(parameters)
  //  }
  //
  //  implicit def valToString(v: Val): String = v.extract[String]
  //
  //  case class Instruction(name: String, f: INSTRUMENT_TYPE, params: Seq[Val])

  trait Instruction2 {
    def name: String

    def execute(implicit eql: EQLContext): Definition[_]
  }

  lazy val instrumentations = ConfigFactory.load("instrumentations.json")

  case class HelpInstruction(params: Seq[String]) extends Instruction2 {
    override def name = "help"

    def execute(implicit eql: EQLContext): Definition[_] = {
      import eql._
      params match {
        case Seq(i) =>
          val example: String =
            instrumentations.getConfig(i).getString("example")
          val description: String =
            instrumentations.getConfig(i).getString("description")
          val r: Map[String, AnyRef] =
            Map(("example", example), ("description", description))
          PureStringDefinition("help")
        case _ =>
          PureStringDefinition("I have no idea for this.")
      }
    }
  }


  trait ScriptContextInstruction2 extends Instruction2

  case class EndpointBindInstruction(endpoint: String) extends ScriptContextInstruction2 {
    override def name = "host"

    def execute(implicit eql: EQLContext): Definition[_] = {
      PureStringDefinition(s"$endpoint")
    }
  }

  case class PostActionInstruction(path: String, action: String) extends Instruction2 {
    override def name = "post"

    def execute(implicit eql: EQLContext): Definition[_] = {
      PureStringDefinition(s"$path, $action")
    }
  }

  case class DeleteActionInstruction(path: String, action: String) extends Instruction2 {
    override def name = "delete"

    def execute(implicit eql: EQLContext): Definition[_] = {
      PureStringDefinition(s"$path, $action")
    }
  }

  case class PutActionInstruction(path: String, action: String) extends Instruction2 {
    override def name = "put"

    def execute(implicit eql: EQLContext): Definition[_] = {
      PureStringDefinition(s"$path, $action")
    }
  }

  case class GetActionInstruction(path: String, action: String) extends Instruction2 {
    override def name = "get"

    def execute(implicit eql: EQLContext): Definition[_] = {
      import eql._
      GetActionDefinition(path, action)
    }
  }


  case class HealthInstruction() extends Instruction2 {

    override def name: String = "health"

    def execute(implicit eql: EQLContext): Definition[_] = {
      import eql._
      cluster health
    }
  }

  case class ErrorInstruction(error: String) extends Instruction2 {

    override def name: String = "error"

    def execute(implicit eql: EQLContext): Definition[_] = {
      ErrorHealthRequestDefinition(error)
    }
  }

}

