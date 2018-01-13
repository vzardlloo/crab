package crab.http;


import java.io.File;

public class HttpFileUpload {

    private String fileName;

    private File tempFile;

    public HttpFileUpload(String fileName, File tempFile) {
        this.fileName = fileName;
        this.tempFile = tempFile;
    }

    public String getFileName() {
        return fileName;
    }

    public File getTempFile() {
        return tempFile;
    }
}
