package com.dicicip.starter.util.file;

import com.dicicip.starter.util.file.model.DicicipFileInfo;
import net.bytebuddy.utility.RandomString;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FileUtil {

    @Value("${contextPath}")
    private String rootFilesDirectory;

    private String relativeFilesDirectory = "temp";

    private int directoryPermission = 777;

    public FileUtil() {
    }

    public FileUtil(String rootFilesDirectory, String relativeFilesDirectory) {
        this.rootFilesDirectory = rootFilesDirectory;
        this.relativeFilesDirectory = relativeFilesDirectory;
    }

    public FileUtil(String rootFilesDirectory, String relativeFilesDirectory, int directoryPermission) {
        this.rootFilesDirectory = rootFilesDirectory;
        this.relativeFilesDirectory = relativeFilesDirectory;
        this.directoryPermission = directoryPermission;
    }

    public DicicipFileInfo storeBase64ToTemp(String strBase64, int thumbQuality) {

        /*Is Directory Exist ?*/
        File directory = new File(this.rootFilesDirectory + "/" + this.relativeFilesDirectory);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        File directoryThumb = new File(this.rootFilesDirectory + "/thumb/" + this.relativeFilesDirectory);

        if (!directoryThumb.exists()) {
            directoryThumb.mkdirs();
        }

        /*Is Image File ?*/
        byte[] bytes = Base64.getMimeDecoder().decode((strBase64.split(",")[1]));

        String filename = new Date().getTime()
                + "." + RandomString.make(6)
                + "." + extractMimeType(strBase64).split("/")[1];

        Path path = Paths.get(directory + "/" + filename);
        Path pathThumb = Paths.get(directoryThumb + "/" + filename);

        try {
            /*Store File*/
            Files.write(path, bytes);

            /*Generate Thumbnail*/
            if (extractMimeType(strBase64).split("/")[0].equals("image")) {

                Thumbnails
                        .of(new File(path.toString()))
                        .size(640, 480)
                        .keepAspectRatio(true)
                        .outputFormat(extractMimeType(strBase64).split("/")[1])
                        .toFile(new File(pathThumb.toString()));

            }

            return new DicicipFileInfo(
                    path.toString(),
                    pathThumb.toString(),
                    this.relativeFilesDirectory + "/" + filename,
                    this.relativeFilesDirectory + "/thumb/" + filename,
                    filename
            );

        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }


    }

    public DicicipFileInfo storeBase64ToTemp(String strBase64) {
        return this.storeBase64ToTemp(strBase64, 15);
    }

    public static String extractMimeType(final String encoded) {
        final Pattern mime = Pattern.compile("^data:([a-zA-Z0-9]+/[a-zA-Z0-9]+).*,.*");
        final Matcher matcher = mime.matcher(encoded);
        if (!matcher.find())
            return "";
        return matcher.group(1).toLowerCase();
    }

}
