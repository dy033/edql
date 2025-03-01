package com.github.chengpohi.edql.parser.json

import com.fasterxml.jackson.core.io.JsonStringEncoder
import org.apache.commons.lang3.StringUtils

import scala.reflect.runtime.universe._

/**
 * eql
 * Created by chengpohi on 2/17/16.
 */
object JsonCollection {

  sealed trait Val extends Any {
    def value: Any

    def apply(i: Int): Val = this.asInstanceOf[Arr].value(i)

    def apply(s: java.lang.String): Val =
      this.asInstanceOf[Obj].value.find(_._1.value == s).get._2

    def toJson: String

    def vars: Seq[Var] = Seq()

    def funs: Seq[Fun] = Seq()

    def get(path: String): Option[Val]

    def \\(path: String): Option[Val] = get(path)

    def copy: Val
  }

  case class Str(value: java.lang.String) extends AnyVal with Arith {
    override def toJson: String = {
      "\"" + new String(JsonStringEncoder.getInstance().quoteAsString(value)) + "\""
    }

    def raw: String = value

    override def get(path: String): Option[Val] = None

    override def plus(i: Arith): Arith = i match {
      case n: JsonCollection.Num => JsonCollection.Str(value + n.value.toString)
      case s: JsonCollection.Str => JsonCollection.Str(value + s.value)
      case _ => throw new RuntimeException("not support + type: " + i)
    }

    override def minus(i: Arith): Arith = throw new RuntimeException("not support + type: " + i)

    override def multiply(i: Arith): Arith = throw new RuntimeException("not support + type: " + i)

    override def div(i: Arith): Arith = throw new RuntimeException("not support + type: " + i)

    override def copy: Val = this
  }

  abstract class Dynamic extends Val {
    def clean(): Unit

    var realValue: Option[JsonCollection.Val] = None
  }

  case class Var(value: java.lang.String) extends Dynamic {

    override def toJson: String = realValue.map(_.toJson).getOrElse("")

    override def get(path: String): Option[Val] = None

    override def vars: Seq[Var] = Seq(this)

    override def copy: Val = {
      val va = Var(value)
      va.realValue = realValue.map(_.copy)
      va.realValue.getOrElse(this)
    }

    override def clean(): Unit = realValue = None
  }

  case class Fun(value: (String, Seq[Val])) extends Dynamic {
    override def toJson: String = realValue.map(_.toJson).getOrElse("")

    override def get(path: String): Option[Val] = None

    override def funs: Seq[Fun] = Seq(this)

    override def copy: Val = {
      val f = Fun((value._1, value._2.map(i => i.copy)))
      realValue.map(_.copy).getOrElse(this)
    }

    override def clean(): Unit = {
      value._2.filter(_.isInstanceOf[Dynamic]).foreach(_.asInstanceOf[Dynamic].clean())
      realValue = None
    }
  }

  case class ArithTree(var a: JsonCollection.Val, var op: Option[String], var b: Option[JsonCollection.Val], var order: Option[Int] = None) extends Dynamic {
    override def toJson: String = realValue.map(_.toJson).getOrElse(a.toJson)

    override def get(path: String): Option[Val] = None

    override def copy: Val = {
      val tree = ArithTree(a.copy, op, b.map(_.copy), None)
      realValue.map(_.copy.asInstanceOf[Arith]).getOrElse(tree)
    }

    override def value: (JsonCollection.Val, Option[String], Option[JsonCollection.Val]) = (a, op, b)

    override def clean(): Unit = {
      a match {
        case d: Dynamic => d.clean()
        case _ =>
      }
      a.vars.foreach(_.clean())
      b match {
        case Some(o) => {
          if (o.isInstanceOf[Dynamic]) {
            o.asInstanceOf[Dynamic].clean()
          }
          o.vars.foreach(_.clean())
        }
        case _ =>
      }
      realValue = None
    }
  }

  case class Obj(value: (Val, Val)*) extends AnyVal with Arith {
    override def toJson: String = {
      val valueJson = value.map {
        case (n, v) => {
          val j = v.toJson
          n.toJson + ":" + j
        }
      }.filter(i => StringUtils.isNotBlank(i)).mkString(",")
      "{" + valueJson + "}"
    }

    override def get(path: String): Option[Val] =
      value.find(p => p._1.value == path).map(_._2)

    override def vars: Seq[Var] = this.value.flatMap(i => i._1.vars ++ i._2.vars)

    override def funs: Seq[Fun] = this.value.flatMap(i => i._1.funs ++ i._2.funs)

    def remove(v: String): Obj = {
      val nvs = value.filter(i => i._1.asInstanceOf[Str].value != v)
      Obj(nvs: _*)
    }

    def add(k: String, v: Val): Obj = {
      val nvs = value :+ (Str(k), v)
      Obj(nvs: _*)
    }

    override def copy: Val = Obj(value.map(i => (i._1.copy, i._2.copy)): _*)

    override def plus(i: Arith): Arith = i match {
      case n: JsonCollection.Obj => {
        JsonCollection.Obj((value ++ n.value): _*)
      }
      case _ => throw new RuntimeException("not support + type: " + i)
    }

    override def minus(i: Arith): Arith = throw new RuntimeException("not support + type: " + i)

    override def multiply(i: Arith): Arith = throw new RuntimeException("not support + type: " + i)

    override def div(i: Arith): Arith = throw new RuntimeException("not support + type: " + i)
  }

  case class Arr(value: Val*) extends AnyVal with Val {
    override def toJson: String =
      "[" + value.map(i => i.toJson).mkString(",") + "]"

    override def get(path: String): Option[Val] = None

    override def vars: Seq[Var] = this.value.flatMap(_.vars)

    override def funs: Seq[Fun] = this.value.flatMap(_.funs)

    override def copy: Val = Arr(value.map(_.copy): _*)
  }

  case class Tuple(value: Val*) extends AnyVal with Val {
    override def toJson: String =
      "(" + value.map(i => i.toJson).mkString(",") + ")"

    override def get(path: String): Option[Val] = None

    override def vars: Seq[Var] = value.flatMap(_.vars)

    override def funs: Seq[Fun] = value.flatMap(_.funs)

    override def copy: Val = Tuple(value.map(_.copy): _*)
  }

  trait Arith extends Any with Val {
    def plus(i: Arith): Arith

    def minus(i: Arith): Arith

    def multiply(i: Arith): Arith

    def div(i: Arith): Arith
  }

  case class Num(value: Number) extends AnyVal with Arith {
    override def toJson: String = java.math.BigDecimal.valueOf(value.doubleValue()).stripTrailingZeros().toPlainString;


    override def get(path: String): Option[Val] = None

    override def plus(i: Arith): Arith = {
      i match {
        case n: JsonCollection.Num => JsonCollection.Num(addNumbers(value, n.value))
        case s: JsonCollection.Str => JsonCollection.Str(value + s.value)
        case _ => throw new RuntimeException("not support + type: " + i)
      }
    }

    override def minus(i: Arith): Arith = {
      i match {
        case n: JsonCollection.Num => JsonCollection.Num(minusNumbers(value, n.value))
        case _ => throw new RuntimeException("not support + type: " + i)
      }
    }

    override def multiply(i: Arith): Arith = {
      i match {
        case n: JsonCollection.Num => JsonCollection.Num(multiplyNumbers(value, n.value))
        case _ => throw new RuntimeException("not support + type: " + i)
      }
    }

    override def div(i: Arith): Arith = {
      i match {
        case n: JsonCollection.Num => JsonCollection.Num(divNumbers(value, n.value))
        case _ => throw new RuntimeException("not support + type: " + i)
      }
    }

    override def copy: Val = this
  }

  def addNumbers(a: Number, b: Number): Number = {
    if (a.isInstanceOf[Double] || b.isInstanceOf[Double]) a.doubleValue + b.doubleValue
    else if (a.isInstanceOf[Float] || b.isInstanceOf[Float]) a.floatValue + b.floatValue
    else if (a.isInstanceOf[Long] || b.isInstanceOf[Long]) a.longValue + b.longValue
    else a.intValue + b.intValue
  }

  def minusNumbers(a: Number, b: Number): Number = {
    if (a.isInstanceOf[Double] || b.isInstanceOf[Double]) a.doubleValue - b.doubleValue
    else if (a.isInstanceOf[Float] || b.isInstanceOf[Float]) a.floatValue - b.floatValue
    else if (a.isInstanceOf[Long] || b.isInstanceOf[Long]) a.longValue - b.longValue
    else a.intValue - b.intValue
  }

  case object False extends Val {
    def value = false

    override def toJson: String = value.toString

    override def get(path: String): Option[Val] = None

    override def copy: Val = this
  }

  case object True extends Val {
    def value: Boolean = true

    override def toJson: String = value.toString

    override def get(path: String): Option[Val] = None

    override def copy: Val = this
  }

  case object Null extends Val {
    def value: Option[Nothing] = None

    override def toJson: String = value.map(_.toString).orNull

    override def get(path: String): Option[Val] = None

    override def copy: Val = this
  }


  def multiplyNumbers(a: Number, b: Number): Number = {
    if (a.isInstanceOf[Double] || b.isInstanceOf[Double]) a.doubleValue * b.doubleValue
    else if (a.isInstanceOf[Float] || b.isInstanceOf[Float]) a.floatValue * b.floatValue
    else if (a.isInstanceOf[Long] || b.isInstanceOf[Long]) a.longValue * b.longValue
    else a.intValue * b.intValue
  }

  def divNumbers(a: Number, b: Number): Number = {
    if (a.isInstanceOf[Double] || b.isInstanceOf[Double]) a.doubleValue / b.doubleValue
    else if (a.isInstanceOf[Float] || b.isInstanceOf[Float]) a.floatValue / b.floatValue
    else if (a.isInstanceOf[Long] || b.isInstanceOf[Long]) a.longValue / b.longValue
    else a.intValue / b.intValue
  }

  case object Comment extends Val {
    def value: Option[Nothing] = None

    override def toJson: String = value.toString

    override def get(path: String): Option[Val] = None

    override def copy: Val = this
  }

  implicit class JsonConverter(value: Val) {
    private[JsonCollection] def extract(tag: Type): Any = {
      if (tag <:< typeOf[Map[_, _]]) {
        val subType2: Type = tag.typeArgs(1)
        value
          .asInstanceOf[Obj]
          .value
          .toList
          .map(i => (i._1, i._2.extract(subType2)))
          .toMap
      } else if (tag <:< typeOf[List[(_, _)]]) {
        val subType: Type = tag.typeArgs.head
        value
          .asInstanceOf[Obj]
          .value
          .toList
          .map(i => (i._1, i._2.extract(subType.typeArgs(1))))
      } else if (tag <:< typeOf[List[_]]) {
        val subType: Type = tag.typeArgs.head
        value
          .asInstanceOf[Arr]
          .value
          .toList
          .map(i => {
            i.extract(subType)
          })
      } else if (tag <:< typeOf[(_, _)]) {
        val subType = tag.typeArgs
        val (tp1, tp2) = (subType.head, subType(1))
        val vals = value.asInstanceOf[Arr].value.toList
        (vals.head.extract(tp1), vals(1).extract(tp2))
      } else if (tag =:= typeOf[Int]) {
        value.asInstanceOf[Num].value
      } else {
        value.value
      }
    }

    def extract[T](implicit tag: TypeTag[T]): T =
      extract(tag.tpe).asInstanceOf[T]
  }

}
