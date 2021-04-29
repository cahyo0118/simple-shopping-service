package com.dicicip.starter.util.file.model;

public class DicicipFileInfo {

    public String full_path;
    public String full_thumb_path;
    public String path;
    public String thumb_path;
    public String filename;

    public DicicipFileInfo() {
    }

    public DicicipFileInfo(
            String full_path,
            String full_thumb_path,
            String path,
            String thumb_path,
            String filename
    ) {
        this.full_path = full_path;
        this.full_thumb_path = full_thumb_path;
        this.path = path;
        this.thumb_path = thumb_path;
        this.filename = filename;
    }
}
