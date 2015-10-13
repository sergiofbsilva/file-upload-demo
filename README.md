# File Upload REST API Demo

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

1. cd client
2. `mvn compile exec:java -Dexec.mainClass="org.fenixedu.file.upload.client.Main"`
3. The client will output the response from server

Example:

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
