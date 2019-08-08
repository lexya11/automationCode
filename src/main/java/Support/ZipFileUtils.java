package Support;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import Environments.Constants;
import org.apache.commons.io.FileUtils;
import java.io.File;

public class ZipFileUtils {
    // Zip a file
    public static void compress(String dirPath) {
        Path sourceDir = Paths.get(dirPath);
        String zipFileName = dirPath.concat(".zip");
        try {
            ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(zipFileName));
            Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) {
                    try {
                        Path targetFile = sourceDir.relativize(file);
                        outputStream.putNextEntry(new ZipEntry(targetFile.toString()));
                        byte[] bytes = Files.readAllBytes(file);
                        outputStream.write(bytes, 0, bytes.length);
                        outputStream.closeEntry();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Move file to folder
    public void moveTheFile (String path, String file) {
        try {
            File destDir = new File(path);
            File srcFile = new File(file);
            if(!destDir.exists())
                FileUtils.forceMkdir(destDir);
            FileUtils.copyFileToDirectory(srcFile, destDir);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // Move and zip file report
    public void zipFileReport(String htmlReport){
        try {
            // Remove old folder
            FileUtils.deleteDirectory(new File(Constants.REPORT_PATH));
            // Move CSS to folder
            moveTheFile(Constants.REPORT_PATH, Constants.CSS_FILE);
            // Move report html to folder
            moveTheFile(Constants.REPORT_PATH + "/Default Suite", htmlReport);
            // Zip folder
            compress(Constants.REPORT_PATH);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}