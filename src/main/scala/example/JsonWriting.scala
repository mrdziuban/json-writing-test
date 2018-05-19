package example

import argonaut.{Json => AJson}
import io.circe.{Json => CJson}
import java.time.Instant
import java.util.UUID

object JsonWriting {
  def timed[A](action: String, fn: () => A): A = {
    val start = Instant.now
    val res = fn()
    println(s"$action took ${Instant.now.toEpochMilli - start.toEpochMilli}ms")
    res
  }

  def testJsonWriting[J](tpe: String, json: J, writeNoSpaces: J => String, writeSpaces2: J => String): Unit = {
    timed(s"Writing $tpe JSON with nospaces", () => writeNoSpaces(json))
    timed(s"Writing $tpe JSON with spaces2", () => writeSpaces2(json))
    println("")
  }

  case class GeneratedJson[J](str: J, arr: J, encoded: J)

  /**
   * Returns a `GeneratedJson` object using the given UUIDS.
   *
   * The `str` element is a JSON object containing one key/value pair of
   * "uuids", <uuids joined with space>, e.g.
   *
   *   { "uuids": "66d23424-4ec1-4775-bc15-ac937339e74f ea5347cc-1204-4222-906e-e1c292dde008 etc..." }
   *
   * The `arr` element is a JSON object containing one key/value pair of
   * "uuids", <JSON array of uuids>, e.g.
   *
   *   { "uuids": ["66d23424-4ec1-4775-bc15-ac937339e74f", "ea5347cc-1204-4222-906e-e1c292dde008", etc...] }
   *
   * The `encoded` element is a JSON object containing one key value pair of
   * "uuids", <string representation of JSON array of uuids>, e.g.
   *
   *   { "uuids": "[\"66d23424-4ec1-4775-bc15-ac937339e74f\",\"ea5347cc-1204-4222-906e-e1c292dde008\",etc...]" }
   *
   */
  def generateJson[J](
      uuids: List[String],
      mkString: String => J,
      mkObj: Seq[(String, J)] => J,
      mkArr: Seq[J] => J,
      writeJson: J => String): GeneratedJson[J] =
    GeneratedJson[J](
      mkObj(Seq("uuids" -> mkString(uuids.mkString(" ")))),
      mkObj(Seq("uuids" -> mkArr(uuids.map(mkString(_)).toSeq))),
      mkObj(Seq("uuids" -> mkString(writeJson(mkArr(uuids.map(mkString(_)).toSeq))))))

  def main(args: Array[String]): Unit = {
    val uuids = timed("Generating UUIDs", () => List.tabulate(25000)(_ => UUID.randomUUID.toString))

    val argJson = timed("Generating argonaut JSON", () =>
      generateJson[AJson](uuids, AJson.jString(_), l => AJson.obj(l:_*), l => AJson.array(l:_*), _.nospaces))

    val circeJson = timed("Generating circe JSON", () =>
      generateJson[CJson](uuids, CJson.fromString(_), l => CJson.obj(l:_*), l => CJson.arr(l:_*), _.noSpaces))

    println(s"\n${"*" * 70}\nArgonaut\n${"*" * 70}")
    testJsonWriting[AJson]("plain text argonaut", argJson.str, _.nospaces, _.spaces2)
    testJsonWriting[AJson]("JSON array argonaut", argJson.arr, _.nospaces, _.spaces2)
    testJsonWriting[AJson]("JSON text argonaut", argJson.encoded, _.nospaces, _.spaces2)

    println(s"\n${"*" * 70}\nCirce\n${"*" * 70}")
    testJsonWriting[CJson]("plain text circe", circeJson.str, _.noSpaces, _.spaces2)
    testJsonWriting[CJson]("JSON array circe", circeJson.arr, _.noSpaces, _.spaces2)
    testJsonWriting[CJson]("JSON text circe", circeJson.encoded, _.noSpaces, _.spaces2)

    ()
  }
}
