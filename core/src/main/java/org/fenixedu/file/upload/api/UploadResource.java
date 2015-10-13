package org.fenixedu.file.upload.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import com.google.common.io.Files;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

@Path("upload")
public class UploadResource {

    protected static class FilePart {

        private final Part part;
        private final File targetFile;

        public FilePart(Part part) {
            this.part = part;
            try {
                targetFile = File.createTempFile("upload", null);
                part.write(targetFile.getAbsolutePath());
            } catch (IOException e) {
                throw new Error(e);
            }
        }

        public String getName() {
            String header = part.getHeader("content-disposition");
            if (header == null) {
                return null;
            }
            for (String headerPart : header.split(";")) {
                if (headerPart.trim().startsWith("filename")) {
                    return headerPart.substring(headerPart.indexOf('=') + 1).trim().replace("\"", "");
                }
            }
            return null;
        }

        public long getSize() {
            return part.getSize();
        }

        public File getFile() {
            return targetFile;
        }

    }

    private JsonElement extractJson(HttpServletRequest request, String partName) {
        try {
            return new JsonParser().parse(
                    new JsonReader(new InputStreamReader(request.getPart(partName).getInputStream(), StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new WebApplicationException(Status.BAD_REQUEST);
        }
    }

    private File getFile(HttpServletRequest request, String partName) {
        try {
            return new FilePart(request.getPart(partName)).getFile();
        } catch (Exception e) {
            throw new WebApplicationException(Status.BAD_REQUEST);
        }
    }
/*

 */

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public String upload(@Context HttpServletRequest request) {
        StringBuilder builder = new StringBuilder();
        JsonArray elements = extractJson(request, "payload").getAsJsonArray();
        for (JsonElement element : elements) {
            JsonObject obj = element.getAsJsonObject();
            String fileRef = obj.get("fileRef").getAsString();
            String name = obj.get("name").getAsString();
            String description = obj.get("description").getAsString();
            File file = getFile(request, fileRef);
            builder.append(name + "\n");
            builder.append(description + "\n");
            builder.append("\tfile path:" + file.getAbsolutePath() + "\n");
            builder.append("\tfile ref:" + fileRef + "\n");
            builder.append("\tfile content:\n");
            try {
                for (String line : Files.readLines(file, StandardCharsets.UTF_8)) {
                    builder.append("\t\t" + line + "\n");
                }
            } catch (IOException e) {
                throw new WebApplicationException(Status.BAD_REQUEST);
            }
        }
        return builder.toString();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String xpto() {
        return "Hello!";
    }
}
