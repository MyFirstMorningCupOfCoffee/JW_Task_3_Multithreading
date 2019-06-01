package by.javaweb.multithreading.matrix.processing;

import by.javaweb.multithreading.exception.SomethingWentWrongException;
import by.javaweb.multithreading.matrix.Matrix;
import by.javaweb.multithreading.util.FileWorker;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class MatrixWorker {

    public static void initMatrixFromFile(Matrix matrix, String filePath) throws SomethingWentWrongException {
        List<String> lines = FileWorker.readLines(filePath);

        ArrayList<ArrayList<Integer>> elements = new ArrayList<>();
        for (String line : lines) {
            elements.add(lineToIntegerList(line));
        }

        if (!validateElementsQuantity(elements)) {
            throw new SomethingWentWrongException("Corrupted file data: elements can't "
                    + "be used to create matrix");
        }

        int width = elements.size();
        int height = elements.get(0).size();
        matrix.setSize(height, width);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                matrix.set(i, j, elements.get(i).get(j));
            }
        }
    }

    public static void fillMatrixRandomly(Matrix matrix) {

        Random random = new Random();
        int width = matrix.getWidth();
        int height = matrix.getHeight();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                matrix.set(i, j, random.nextInt(90) + 10);
            }
        }
    }

    private static ArrayList<Integer> lineToIntegerList(String line) {
        ArrayList<Integer> responce = new ArrayList<>();
        Scanner sc = new Scanner(line);

        while (sc.hasNextInt()) {
            responce.add(sc.nextInt());
        }

        return responce;
    }

    private static boolean validateElementsQuantity(ArrayList<ArrayList<Integer>> elements) {
        if (elements.size() > 0 && elements.get(0).size() > 0) {
            for (int i = 0; i < elements.size() - 1; i++) {
                if (elements.get(i).size() != elements.get(i + 1).size()) {
                    return false;
                }
            }
        }

        return true;
    }
}
