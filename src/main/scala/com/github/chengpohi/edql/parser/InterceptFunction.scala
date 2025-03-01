package com.github.chengpohi.edql.parser

import com.fasterxml.jackson.databind.{ObjectMapper, ObjectWriter}
import com.github.chengpohi.context.{Context, Definition, PureStringDefinition}
import com.github.chengpohi.edql.parser.json.JsonCollection
import com.jayway.jsonpath.{Configuration, JsonPath}

import java.net.URL
import java.nio.file.{Files, Paths}
import java.util.stream.Collectors


trait InterceptFunction {
  private val ow: ObjectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

  trait Instruction2 {
    def name: String

    def execute(implicit eql: Context): Definition[_]

    def ds: Seq[JsonCollection.Dynamic] = Seq()
  }

  trait ScriptContextInstruction2 extends Instruction2

  case class CommentInstruction() extends ScriptContextInstruction2 {
    override def name = "comment"

    def execute(implicit eql: Context): Definition[_] = {
      PureStringDefinition("")
    }
  }

  case class EndpointBindInstruction(endpoint: String, kibanaProxy: Boolean = false) extends ScriptContextInstruction2 {
    override def name = "host"

    def execute(implicit eql: Context): Definition[_] = {
      PureStringDefinition(s"$endpoint")
    }
  }

  case class TimeoutInstruction(timeout: Int) extends ScriptContextInstruction2 {
    override def name = "timeout"

    def execute(implicit eql: Context): Definition[_] = {
      PureStringDefinition(s"timeout $timeout")
    }
  }

  case class ImportInstruction(imp: URL) extends ScriptContextInstruction2 {
    override def name = "import"

    def execute(implicit eql: Context): Definition[_] = {
      PureStringDefinition(s"$imp")
    }
  }

  case class AuthorizationBindInstruction(auth: String) extends ScriptContextInstruction2 {
    override def name = "authorization"

    def execute(implicit eql: Context): Definition[_] = {
      PureStringDefinition(s"$auth")
    }
  }

  case class UsernameBindInstruction(username: String) extends ScriptContextInstruction2 {
    override def name = "Username"

    def execute(implicit eql: Context): Definition[_] = {
      PureStringDefinition(s"$username")
    }
  }

  case class PasswordBindInstruction(password: String) extends ScriptContextInstruction2 {
    override def name = "Password"

    def execute(implicit eql: Context): Definition[_] = {
      PureStringDefinition(s"$password")
    }
  }

  case class ApiKeyIdBindInstruction(apikeyId: String) extends ScriptContextInstruction2 {
    override def name = "apikeyId"

    def execute(implicit eql: Context): Definition[_] = {
      PureStringDefinition(s"$apikeyId")
    }
  }

  case class ApiKeySecretBindInstruction(apiSecret: String) extends ScriptContextInstruction2 {
    override def name = "secret"

    def execute(implicit eql: Context): Definition[_] = {
      PureStringDefinition(s"$apiSecret")
    }
  }

  case class ApiSessionTokenBindInstruction(apiSessionToken: String) extends ScriptContextInstruction2 {
    override def name = "session"

    def execute(implicit eql: Context): Definition[_] = {
      PureStringDefinition(s"$apiSessionToken")
    }
  }

  case class AWSRegionBindInstruction(awsRegion: String) extends ScriptContextInstruction2 {
    override def name = "region"

    def execute(implicit eql: Context): Definition[_] = {
      PureStringDefinition(s"$awsRegion")
    }
  }

  case class PostActionInstruction(path: String, action: Seq[JsonCollection.Val]) extends Instruction2 {
    override def name = "post"

    def execute(implicit eql: Context): Definition[_] = {
      import eql._
      val newPath = mapNewPath(eql.variables, path)

      if (newPath.startsWith("/")) {
        PostActionDefinition(newPath, action)
      } else {
        PostActionDefinition("/" + newPath, action)
      }
    }

    override def ds: Seq[JsonCollection.Dynamic] =
      action.flatMap(j => extractDynamics(j))
  }


  case class DeleteActionInstruction(path: String, action: Option[JsonCollection.Val]) extends Instruction2 {
    override def name = "delete"

    def execute(implicit eql: Context): Definition[_] = {
      import eql._
      val newPath = mapNewPath(eql.variables, path)

      if (newPath.startsWith("/")) {
        DeleteActionDefinition(newPath, action.map(_.toJson))
      } else {
        DeleteActionDefinition("/" + newPath, action.map(_.toJson))
      }
    }

    override def ds: Seq[JsonCollection.Dynamic] =
      action.map(i => extractDynamics(i)).getOrElse(Seq())
  }

  case class PutActionInstruction(path: String, action: Seq[JsonCollection.Val]) extends Instruction2 {
    override def name = "put"

    def execute(implicit eql: Context): Definition[_] = {
      import eql._
      val newPath = mapNewPath(eql.variables, path)

      if (newPath.startsWith("/")) {
        PutActionDefinition(newPath, action)
      } else {
        PutActionDefinition("/" + newPath, action)
      }
    }

    override def ds: Seq[JsonCollection.Dynamic] =
      action.flatMap(i => extractDynamics(i))
  }

  case class GetActionInstruction(path: String, action: Option[JsonCollection.Val]) extends Instruction2 {
    override def name = "get"

    def execute(implicit eql: Context): Definition[_] = {
      import eql._
      val newPath = mapNewPath(eql.variables, path)

      if (newPath.startsWith("/")) {
        GetActionDefinition(newPath, action)
      } else {
        GetActionDefinition("/" + newPath, action)
      }
    }

    override def ds: Seq[JsonCollection.Dynamic] =
      action.map(i => extractDynamics(i)).getOrElse(Seq())
  }

  case class HeadActionInstruction(path: String, action: Option[JsonCollection.Val]) extends Instruction2 {
    override def name = "head"

    def execute(implicit eql: Context): Definition[_] = {
      import eql._
      val newPath = mapNewPath(eql.variables, path)

      if (newPath.startsWith("/")) {
        HeadActionDefinition(newPath, action.map(_.toJson))
      } else {
        HeadActionDefinition("/" + newPath, action.map(_.toJson))
      }
    }

    override def ds: Seq[JsonCollection.Dynamic] =
      action.map(i => extractDynamics(i)).getOrElse(Seq())

  }

  case class VariableInstruction(variableName: String, value: JsonCollection.Val) extends ScriptContextInstruction2 {
    override def name = "variable"

    def execute(implicit eql: Context): Definition[_] = {
      PureStringDefinition(s"")
    }

    override def ds: Seq[JsonCollection.Dynamic] = {
      extractDynamics(value)
    }
  }

  case class FunctionInstruction(funcName: String, variableNames: Seq[String], instructions: Seq[Instruction2]) extends ScriptContextInstruction2 {
    override def name = "function"

    def execute(implicit eql: Context): Definition[_] = {
      PureStringDefinition(s"")
    }
  }

  case class MapIterInstruction(a: JsonCollection.Val, fun: FunctionInstruction) extends Instruction2 {
    override def name = "mapiter"

    def execute(implicit eql: Context): Definition[_] = {
      PureStringDefinition(s"")
    }
  }

  case class ForInstruction(tempVariable: String,
                            iterVariable: JsonCollection.Val,
                            instructions: Seq[Instruction2]) extends Instruction2 {
    override def name = "for"

    def execute(implicit eql: Context): Definition[_] = {
      PureStringDefinition(s"")
    }

    override def ds: Seq[JsonCollection.Dynamic] =
      extractDynamics(iterVariable)
  }


  case class ReturnInstruction(value: JsonCollection.Val) extends Instruction2 {
    override def name = "return"

    def execute(implicit eql: Context): Definition[_] = {
      PureStringDefinition(s"")
    }

    override def ds: Seq[JsonCollection.Dynamic] = {
      extractDynamics(value)
    }
  }

  case class FunctionInvokeInstruction(funcName: String, vals: Seq[JsonCollection.Val], map: Option[MapIterInstruction] = None) extends Instruction2 {
    override def name = "functionInvoke"

    def execute(implicit eql: Context): Definition[_] = {
      PureStringDefinition(s"")
    }

    override def ds: Seq[JsonCollection.Dynamic] = {
      vals.flatMap(i => extractDynamics(i))
    }
  }

  case class ReadJSONInstruction(filePath: JsonCollection.Var) extends Instruction2 {
    override def name = "readJSONInstruction"

    def execute(implicit eql: Context): Definition[_] = {
      val contextPath = eql.variables.get("CONTEXT_PATH")
      val currentDir = contextPath.map(_.asInstanceOf[JsonCollection.Str].value).map(_ + "/").getOrElse("")
      val targetPath = filePath.toJson
      val content =
        Files.readAllLines(Paths.get(currentDir + targetPath.replaceAll("^\"|\"$", "")))
          .stream()
          .collect(Collectors.joining(System.lineSeparator()))

      PureStringDefinition(content)
    }

    override def ds: Seq[JsonCollection.Dynamic] =
      extractDynamics(filePath)
  }

  case class WriteJSONInstruction(filePath: JsonCollection.Val, data: JsonCollection.Val) extends Instruction2 {
    override def name = "writeJSONInstruction"

    def execute(implicit eql: Context): Definition[_] = {
      val contextPath = eql.variables.get("CONTEXT_PATH")
      val currentDir = contextPath.map(_.asInstanceOf[JsonCollection.Str].value).map(_ + "/").getOrElse("")

      Files.write(
        Paths.get(currentDir + filePath.toJson.replaceAll("^\"|\"$", "")),
        data.toJson.getBytes())

      PureStringDefinition("")
    }

    override def ds: Seq[JsonCollection.Dynamic] =
      Seq(filePath, data).flatMap(i => extractDynamics(i))
  }

  case class JQInstruction(data: JsonCollection.Val, path: JsonCollection.Val) extends Instruction2 {
    override def name = "jqInstruction"

    def execute(implicit eql: Context): Definition[_] = {
      val jsonO = data.toJson
      val jsonPath = path.toJson.replaceAll("^\"|\"$", "")
      val configuration = Configuration.defaultConfiguration().addOptions(com.jayway.jsonpath.Option.DEFAULT_PATH_LEAF_TO_NULL)
      val pathObj: Object = JsonPath.using(configuration).parse(jsonO).read(jsonPath)
      PureStringDefinition(ow.writeValueAsString(pathObj))
    }

    override def ds: Seq[JsonCollection.Dynamic] =
      Seq(path, data).flatMap(i => extractDynamics(i))
  }

  case class PrintInstruction(v: JsonCollection.Val) extends Instruction2 {
    override def name = "printInstruction"

    def execute(implicit eql: Context): Definition[_] = {
      PureStringDefinition(v.toJson)
    }

    override def ds: Seq[JsonCollection.Dynamic] =
      Seq(v).flatMap(i => extractDynamics(i))
  }

  case class PrintlnInstruction(v: JsonCollection.Val) extends Instruction2 {
    override def name = "printlnInstruction"

    def execute(implicit eql: Context): Definition[_] = {
      PureStringDefinition(v.toJson + "\n")
    }

    override def ds: Seq[JsonCollection.Dynamic] =
      Seq(v).flatMap(i => extractDynamics(i))
  }

  private def mapNewPath(variables: scala.collection.mutable.Map[String, JsonCollection.Val], path: String) = {
    val invokePath = variables.get("INVOKE_PATH").map(_.asInstanceOf[JsonCollection.Str].value).filter(_ != null)
      .getOrElse("").split("\\$").distinct.mkString("$")
    variables.filter(i => i._1.startsWith(invokePath))
      .filter(_._2 != null)
      .foldLeft(path)((i, o) => {
        val vName = o._1.replace(invokePath + "$", "");
        val v: String = o._2 match {
          case s: JsonCollection.Str => {
            s.value
          }
          case va: JsonCollection.Var => {
            va.realValue match {
              case Some(fa) => fa match {
                case s: JsonCollection.Str => s.value
                case j => j.toJson
              }
              case None => va.value
            }
          }
          case va: JsonCollection.ArithTree => {
            va.realValue match {
              case Some(fa) => fa match {
                case s: JsonCollection.Str => s.value
                case j => j.toJson
              }
              case None => va.toJson
            }
          }
          case s => s.toJson
        }
        if (v != null) {
          i.replace("$" + vName, v)
        } else {
          i
        }
      })
  }


  private def extractDynamics(iterVariable: JsonCollection.Val): Seq[JsonCollection.Dynamic] = {
    iterVariable match {
      case d: JsonCollection.Dynamic =>
        Seq(d)
      case obj: JsonCollection.Obj =>
        obj.value.flatMap(v => extractDynamics(v._1) ++ extractDynamics(v._2))
      case obj: JsonCollection.Tuple =>
        obj.value.flatMap(v => extractDynamics(v))
      case obj: JsonCollection.Arr =>
        obj.value.flatMap(v => extractDynamics(v))
      case _ =>
        Seq()
    }
  }

  def systemFunction: Map[String, FunctionInstruction] = {
    Map(
      "jq2" -> FunctionInstruction("jq", Seq("data", "path"), Seq(JQInstruction(JsonCollection.Var("data"), JsonCollection.Var("path")))),
      "print1" -> FunctionInstruction("print", Seq("v"), Seq(PrintInstruction(JsonCollection.Var("v")))),
      "println1" -> FunctionInstruction("println", Seq("str"), Seq(PrintlnInstruction(JsonCollection.Var("str")))),
      "readJSON1" -> FunctionInstruction("readJSON", Seq("filePath"), Seq(ReadJSONInstruction(JsonCollection.Var("filePath")))),
      "writeJSON2" -> FunctionInstruction("writeJSON", Seq("filePath", "data"), Seq(WriteJSONInstruction(JsonCollection.Var("filePath"), JsonCollection.Var("data"))))
    )
  }
}

