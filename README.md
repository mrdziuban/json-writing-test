# JSON writing test

Simple project to demonstrate speed comparison of writing JSON to a string using both
[argonaut](https://github.com/argonaut-io/argonaut) and [circe](https://github.com/circe/circe).

The example in [JsonWriting.scala](src/main/scala/example/JsonWriting.scala) shows that argonaut writes JSON
incredibly slowly when the JSON contains an encoded JSON string. Here's the output of running it:

```
[info] Running example.JsonWriting
Generating UUIDs took 277ms
Generating argonaut JSON took 120ms
Generating circe JSON took 131ms

**********************************************************************
Argonaut
**********************************************************************
Writing plain text argonaut JSON with nospaces took 6ms
Writing plain text argonaut JSON with spaces2 took 5ms
Writing JSON text argonaut JSON with nospaces took 14422ms
Writing JSON text argonaut JSON with spaces2 took 14378ms

**********************************************************************
Circe
**********************************************************************
Writing plain text circe JSON with nospaces took 3ms
Writing plain text circe JSON with spaces2 took 3ms
Writing JSON text circe JSON with nospaces took 31ms
Writing JSON text circe JSON with spaces2 took 15ms
```

## Running the example

```bash
$ sbt run
```
