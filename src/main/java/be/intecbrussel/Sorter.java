package be.intecbrussel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;


public class Sorter {

    private List<String> filesPaths;
    private Set<String> extensions;

    public Sorter() {
        filesPaths = new ArrayList<>();
        extensions = new HashSet<>();
    }

    //GLOBAL SORTING METHOD
    public void sortingFolder(String rootPath, String newRootPath) {
        filesPaths = filesListMaker(rootPath);
        extensionListMaker();
        folderMaker(newRootPath);
        copyFilesToExtensionFolder(newRootPath);
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

    private void extensionListMaker() {
        for (String file : filesPaths) {
            extensions.add(extractExtension(file));
        }
        // Extensions will become folders, the 3 below needed :
        extensions.add("database");
        extensions.add("hidden");
        extensions.add("summary");
    }

    private void folderMaker(String path) {
        for (String extension : extensions) {
            Path folderPath = Paths.get(path).resolve(extension);
            try {
                Files.createDirectories(folderPath);
                System.out.println(extension + " folder created");
            } catch (IOException e) {
                System.out.println("Unable to create this folder: " + extension);
            }
        }
    }

    private void copyFilesToExtensionFolder(String newRootPath) {
        for (String file : filesPaths) {
            String extOfFile = extractExtension(file);
            String fileName = Paths.get(file).getFileName().toString();
            String destination = Paths.get(newRootPath)
                    .resolve(extOfFile)
                    .resolve(fileName)
                    .toString();

            System.out.println("\n" + file);
            System.out.println(destination);
            copyFile(file, destination);
        }
    }

    private String extractExtension(String fileName) {
        String extension = null;
        File file = new File(fileName);
        if (file.isHidden()) {
            extension = "hidden";
        } else {
            extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return extension;
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
