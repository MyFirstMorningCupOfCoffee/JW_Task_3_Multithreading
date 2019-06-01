package by.javaweb.multithreading.demo;

import by.javaweb.multithreading.exception.SomethingWentWrongException;
import by.javaweb.multithreading.matrix.Cell;
import by.javaweb.multithreading.matrix.Matrix;
import by.javaweb.multithreading.matrix.processing.Dispenser;
import by.javaweb.multithreading.matrix.processing.CellProcessor;
import by.javaweb.multithreading.matrix.processing.MatrixWorker;
import by.javaweb.multithreading.matrix.processing.ReportCollector;
import by.javaweb.multithreading.util.FileWorker;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MultythreadMatrixDemo {

    public static void main(String[] args) {
        Logger LOGGER = LogManager.getLogger();
        Matrix matrix = Matrix.INSTANCE;

        //// initializing matrix with elements got from external file
        ////   INFO:
        ////   in case we want our custom size matrix initialized with random
        ////   2-digit numbers, instead of this block we need to use something like this:
        ////     matrix.setSize(50, 50);
        ////     matrix.fillRandomly();
        try {
            MatrixWorker.initMatrixFromFile(matrix, "data\\matrix.txt");
        } catch (SomethingWentWrongException ex) {
            LOGGER.error("Error during matrix initialization. Programm will be terminated.", ex);
            return;
        }

        //// getting id for matrix cell processing threads from external file
        ArrayList<Integer> processorIdList = new ArrayList<>();
        try {
            String idListString = FileWorker.readFile("data\\processors.txt");
            Scanner sc = new Scanner(idListString);
            while (sc.hasNextInt()) {
                processorIdList.add(sc.nextInt());
            }
            if (processorIdList.isEmpty()) {
                throw new SomethingWentWrongException("Matrix processors id list is empty. "
                        + "Programm will be terminated.");
            }
        } catch (SomethingWentWrongException ex) {
            LOGGER.error("Error during reading processors id. Programm will be terminated.", ex);
            return;
        }

        //// printing initial matrix
        System.out.println(matrix);

        //// getting main diagonal elements
        ArrayList<Cell> diagonalCells = new ArrayList<>();

        int xPos = 0;
        int yPos = 0;
        while (xPos < matrix.getWidth() && yPos < matrix.getHeight()) {
            diagonalCells.add(new Cell(matrix, xPos++, yPos++));
        }

        //// creating and initializing ancillary objects
        ReportCollector reportCollector = new ReportCollector();
        Dispenser<Cell> dispenser = new Dispenser<>(diagonalCells);

        //// creating matrix cell processor threads list
        //// creating cell processors threads and adding them to list
        ArrayList<Thread> processorList = new ArrayList<>();
        for (Integer id : processorIdList) {
            CellProcessor proc = new CellProcessor(id, dispenser, reportCollector);
            processorList.add(proc);
        }

        //// launching all matrix cell processors
        for (Thread tr : processorList) {
            tr.start();
        }

        //// waiting for all matrix processing threads to complete so we print
        //// result matrix only after it was complitely processed
        for (Thread tr : processorList) {
            try {
                tr.join();
            } catch (InterruptedException ex) {
                LOGGER.error("Error occured during bounding matrix processing threads "
                        + "to main thread. Programm will be terminated.", ex);
                return;
            }
        }
        //// printing result matrix
        //// printing statistics collected by reports collector
        System.out.println(matrix);
        System.out.println(reportCollector.results());

    }

}
