package by.javaweb.multithreading.util;

import by.javaweb.multithreading.exception.SomethingWentWrongException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileWorker {

    public static List<String> readLines(String filePath) throws SomethingWentWrongException {
        List<String> result = new ArrayList<>();

        try {
            result = Files.readAllLines(Paths.get(filePath));
        } catch (IOException ex) {
            throw new SomethingWentWrongException(ex);
        }

        return result;
    }

    public static String readFile(String filePath) throws SomethingWentWrongException {
        String responce = "";

        try {
            responce = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException ex) {
            throw new SomethingWentWrongException(ex);
        }

        return responce;
    }
}
