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
  }

  /**
   * Returns a tuple of two JSON objects using the given UUIDS.
   *
   * The first element is a JSON object containing one key/value pair of
   * "uuids", <uuids joined with space>, e.g.
   *
   *   { "uuids": "66d23424-4ec1-4775-bc15-ac937339e74f ea5347cc-1204-4222-906e-e1c292dde008 etc..." }
   *
   * The second element is a JSON object containing one key value pair of
   * "uuids", <uuids encoded as JSON array>, e.g.
   *
   *   { "uuids": "[\"66d23424-4ec1-4775-bc15-ac937339e74f\",\"ea5347cc-1204-4222-906e-e1c292dde008\",etc...]" }
   *
   */
  def generateJson[J](
      uuids: List[String],
      mkString: String => J,
      mkObj: Seq[(String, J)] => J,
      mkArr: Seq[J] => J,
      writeJson: J => String): (J, J) = {
    val plainText = mkObj(Seq("uuids" -> mkString(uuids.mkString(" "))))
    val jsonText = mkObj(Seq("uuids" -> mkString(writeJson(mkArr(uuids.map(mkString(_)).toSeq)))))
    (plainText, jsonText)
  }

  def main(args: Array[String]): Unit = {
    val uuids = timed("Generating UUIDs", () => List.tabulate(25000)(_ => UUID.randomUUID.toString))

    val (argPlainText, argJsonText) = timed("Generating argonaut JSON", () =>
      generateJson[AJson](uuids, AJson.jString(_), l => AJson.obj(l:_*), l => AJson.array(l:_*), _.nospaces))

    val (circePlainText, circeJsonText) = timed("Generating circe JSON", () =>
      generateJson[CJson](uuids, CJson.fromString(_), l => CJson.obj(l:_*), l => CJson.arr(l:_*), _.noSpaces))

    println(s"\n${"*" * 70}\nArgonaut\n${"*" * 70}")
    testJsonWriting[AJson]("plain text argonaut", argPlainText, _.nospaces, _.spaces2)
    testJsonWriting[AJson]("JSON text argonaut", argJsonText, _.nospaces, _.spaces2)

    println(s"\n${"*" * 70}\nCirce\n${"*" * 70}")
    testJsonWriting[CJson]("plain text circe", circePlainText, _.noSpaces, _.spaces2)
    testJsonWriting[CJson]("JSON text circe", circeJsonText, _.noSpaces, _.spaces2)

    ()
  }
}
