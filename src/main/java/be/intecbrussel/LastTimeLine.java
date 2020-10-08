package be.intecbrussel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class LastTimeLine {

    private List<String> filesPaths;
    private Set<String> dates;

    public LastTimeLine() {
        filesPaths = new ArrayList<>();
        dates = new HashSet<>();
    }

    //GLOBAL TIMELINE METHOD
    public void timelineFolder(String rootPath, String newRootPath) {
        filesPaths = filesListMaker(rootPath);
        datesListMaker();
        folderMaker(newRootPath);
        copyFilesToDatesFolder(newRootPath);
    }


    public List<String> filesListMaker(String rootPath) {

        List<String> filesList = new ArrayList<>();

        try (Stream<Path> walk = Files.walk(Paths.get(rootPath))) {
            filesList = walk.filter(Files::isRegularFile)
                    .map(x -> x.toString()).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        filesList.sort(String::compareToIgnoreCase);

        System.out.println("\n### All files list ###");
        filesList.forEach(System.out::println);
        return filesList;
    }

    private void folderMaker(String path) {
        for (String date : dates) {
            Path folderPath = Paths.get(path).resolve(date);
            try {
                Files.createDirectories(folderPath);
                System.out.println(date + " folder created");
            } catch (IOException e) {
                System.out.println("Unable to create this folder: " + date);
            }
        }
    }

    private void datesListMaker() {
        for (String file : filesPaths) {
            dates.add(extractMonth(file));
        }
        dates.add("summary");
        dates.add("hidden");
    }

    private void copyFilesToDatesFolder(String newRootPath) {
        for (String file : filesPaths) {
            String monthOfFile = extractMonth(file);
            String fileName = Paths.get(file).getFileName().toString();
            String destination = Paths.get(newRootPath)
                    .resolve(monthOfFile)
                    .resolve(fileName)
                    .toString();

            System.out.println("\n" + file);
            System.out.println(destination);
            copyFile(file, destination);
        }
    }

    private String extractMonth(String file) {
        FileTime creationTime = null;
        try {
            creationTime = (FileTime) Files.getAttribute(Path.of(file), "lastModifiedTime");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(creationTime.toString());
        String date = creationTime.toString();
        String month = date.substring(date.indexOf("-") + 1, date.lastIndexOf("-"));
        String year = date.substring(0, date.indexOf("-"));

        return Paths.get(year)
                .resolve(month)
                .toString();
    }

    private void copyFile(String oldPath, String newPath) {
        Path file = Paths.get(oldPath);
        Path newFile = Paths.get(newPath);
        try {
            Files.copy(file, newFile, REPLACE_EXISTING);
            System.out.println("File copied to new folder.");
        } catch (IOException e) {
            System.out.println("Unable to copy file.");
        }

    }


}
