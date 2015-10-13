package org.fenixedu.file.upload.client;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Main {

    public static void main(String[] args) {
        final String BASE_URL = "http://localhost:8080/file-upload/api/upload";

        Map<String, File> files = new HashMap<>();

        Client CLIENT = ClientBuilder.newClient();
        CLIENT.register(MultiPartFeature.class);

        JsonArray components = new JsonArray();

        JsonObject component = new JsonObject();

        final String file1 = UUID.randomUUID().toString();
        component.addProperty("name", "component 1");
        component.addProperty("description", "this is the component 1");
        component.addProperty("fileRef", file1);
        files.put(file1, new File("/tmp/file1.txt"));
        components.add(component);

        component = new JsonObject();
        final String file2 = UUID.randomUUID().toString();
        component.addProperty("name", "component 2");
        component.addProperty("description", "this is the component 2");
        component.addProperty("fileRef", file2);
        files.put(file2, new File("/tmp/file2.txt"));
        components.add(component);

        FormDataMultiPart form = new FormDataMultiPart();
        form.bodyPart(new FormDataBodyPart("payload", components.toString(), MediaType.APPLICATION_JSON_TYPE));
        for (String fileRef : files.keySet()) {
            form.bodyPart(new FileDataBodyPart(fileRef, files.get(fileRef)));
        }

        String response = CLIENT.target(BASE_URL).request().header("X-Requested-With", "java file-upload-client")
                .post(Entity.entity(form, MediaType.MULTIPART_FORM_DATA_TYPE), String.class);

        System.out.println(response);
    }
}
