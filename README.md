# JSON writing test

Simple project to demonstrate speed comparison of writing JSON to a string using both
[argonaut](https://github.com/argonaut-io/argonaut) and [circe](https://github.com/circe/circe).

The example in [JsonWriting.scala](src/main/scala/example/JsonWriting.scala) shows that argonaut writes JSON
incredibly slowly when the JSON contains an encoded JSON string. Here's the output of running it:

```
[info] Running example.JsonWriting
Generating UUIDs took 274ms
Generating argonaut JSON took 131ms
Generating circe JSON took 127ms

**********************************************************************
Argonaut
**********************************************************************
Writing plain text argonaut JSON with nospaces took 6ms
Writing plain text argonaut JSON with spaces2 took 6ms

Writing JSON array argonaut JSON with nospaces took 17ms
Writing JSON array argonaut JSON with spaces2 took 14ms

Writing JSON text argonaut JSON with nospaces took 14316ms
Writing JSON text argonaut JSON with spaces2 took 14122ms


**********************************************************************
Circe
**********************************************************************
Writing plain text circe JSON with nospaces took 3ms
Writing plain text circe JSON with spaces2 took 3ms

Writing JSON array circe JSON with nospaces took 11ms
Writing JSON array circe JSON with spaces2 took 9ms

Writing JSON text circe JSON with nospaces took 33ms
Writing JSON text circe JSON with spaces2 took 15ms
```

## Running the example

```bash
$ sbt run
```
