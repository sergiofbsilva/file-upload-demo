# File Upload REST API Demo

This is an example of a web application that provides an upload endpoint that receives several parts (Json + Files)

The `upload` part is a `JsonArray` that describes a component.

```
    [ ....
    { 'name' : 'component 1',
      'description' : 'this is component 1',
      'fileRef' : 'f99e237f-c728-4d13-b64a-0fef7c89230f'
    }
    ... ]
```
For each `JsonObject` in the previous array it will attach another part named by `fileRef`.

```
--------------------------c9bca4f1d3cc2553
Content-Disposition: form-data; name="f99e237f-c728-4d13-b64a-0fef7c89230f"; filename="file1.txt"
Content-Type: text/plain
this is file 1 content
--------------------------c9bca4f1d3cc2553
```

# setup

1. create database `fileupload` at `localhost`
2. copy file `webapp/src/main/resources/configuration.properties.sample` to `webapp/src/main/resources/configuration.properties`
3. copy file `webapp/src/main/resources/fenix-framework.properties.sample` to `webapp/src/main/resources/fenix-framework.properties`
4. change database credentials in `webapp/src/main/resources/fenix-framework.properties` (if necessary)

# start the application

1. run `mvn clean install` in the `core` module
2. run `mvn clean tomcat7:run` in the `webapp` module

The webapp will available at `http://localhost:8080/file-upload`

# using rest java client

Please create two files: 

1. `/tmp/file1.txt`
2. `/tmp/file2.txt`

Run the following commands:

1. `cd client`
2. `mvn compile exec:java -Dexec.mainClass="org.fenixedu.file.upload.client.Main"`

The client will output the response from server something like:
```
component 1
this is the component 1
    file path:/var/folders/sm/g6y5bdys69j99ffpxfm3cxlm0000gn/T/upload4068927349132980535.tmp
    file ref:f99e237f-c728-4d13-b64a-0fef7c89230f
    file content:
        this is file 1 content
component 2
this is the component 2
    file path:/var/folders/sm/g6y5bdys69j99ffpxfm3cxlm0000gn/T/upload6567429081364978904.tmp
    file ref:8440ecfb-8f07-4487-9ac2-bfdf0101e69d
    file content:
        this is file 2 content
```
