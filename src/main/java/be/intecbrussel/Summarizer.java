package be.intecbrussel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class Summarizer {

    private String summaryPath;

    //GLOBAL SUMMARY METHOD
    public void summarize0(String newRoot, List<String> filesPaths) {
        createSummaryFile(newRoot);
        writeSummaryFile0(filesPaths);
    }

    public void summarize1(String newRoot, List<String> filesPaths) {
        createSummaryFile(newRoot);
        writeSummaryFile1(filesPaths);
    }

    private void createSummaryFile(String newRoot) {
        summaryPath = Paths.get(newRoot)
                .resolve("summary")
                .resolve("summary.txt")
                .toString();
        File summary = new File(summaryPath);
        try {
            if (summary.exists()) {
                summary.delete();
            }
            summary.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeSummaryFile0(List<String> filesPaths) {
        String summaryCanvas = String.format("%-50s %-6c %-14s %-6c %-14s %c",
                "name", '|', "readable", '|', "writeable", '|');

        String folder = "";

        try (FileWriter fWriter = new FileWriter(summaryPath, true);
             BufferedWriter bWriter = new BufferedWriter(fWriter)) {
            bWriter.write(summaryCanvas);
            bWriter.newLine();
            for (String file : filesPaths) {
                if (folder.compareToIgnoreCase(getFolderName(file)) < 0) {
                    folder = getFolderName(file);
                    bWriter.newLine();
                    bWriter.write("\n----\n" + folder + ":\n----");

                }
                bWriter.newLine();
                bWriter.write(getFileAttribute(file));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\n### Summary file is available ###");
    }

    private void writeSummaryFile1(List<String> filesPaths) {
        String summaryCanvas = String.format("%-50s %-6c %-14s %-6c %-14s %c",
                "name", '|', "readable", '|', "writeable", '|');

        String folder = "";

        try (FileWriter fWriter = new FileWriter(summaryPath, true);
             BufferedWriter bWriter = new BufferedWriter(fWriter)) {
            bWriter.write(summaryCanvas);
            bWriter.newLine();
            for (String file : filesPaths) {
                if (folder.compareToIgnoreCase(getFolderDateName(file)) < 0) {
                    folder = getFolderDateName(file);
                    bWriter.newLine();
                    bWriter.write("\n----\n" + folder + ":\n----");

                }
                bWriter.newLine();
                bWriter.write(getFileAttribute(file));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\n### Summary file is available ###");
    }

    private String getFileAttribute(String path) {
        File file = new File(path);
        char isReadable = file.canRead() ? 'x' : '/';
        char isWriteable = file.canWrite() ? 'x' : '/';

        return String.format("%-50s %-10c %-10c %-10c %-10c %c",
                file.getName(), '|', isReadable, '|', isWriteable, '|');
    }

    private String getFolderName(String path) {
        File file = new File(path);
        return file.getParentFile().getName();
    }

    private String getFolderDateName(String path) {
        File file = new File(path);
        return file.getParentFile().getParentFile().getName() + "-" + file.getParentFile().getName();
    }

}
