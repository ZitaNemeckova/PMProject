/*
 * (c) Johannes Verwijnen, duvin@duvin.org
 */
package org.duvin.propromo2015;

import java.io.BufferedWriter;
import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Johannes Verwijnen <duvin@duvin.org>
 */
public class Example {

    private static String refineryId = "";
    private static String currentContext;
    private static String currentRequest;
    private static String currentInput;
    private static String currentWork;
    private static ExampleWorker worker;
    private static int[][] context;
    private static int round = 0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        //let repository know we're alive
        System.out.println("HELLO\t1.0");

        while (true) {
            String command = s.nextLine();
            String[] parts = command.split("\\t");
            if (command.startsWith("SETUP\t")) {
                refineryId = parts[1];
                System.out.println("ALIVE\t" + refineryId + "\tinfo=Test program\tversion=0.666");
            } else if (command.startsWith("PERFORM\t") && !refineryId.isEmpty()) {
                currentRequest = parts[1];
                currentContext = parts[2];
                currentWork = parts[3];
                currentInput = parts[4];
                worker = new ExampleWorker();
                worker.start();
            } else if (command.startsWith("ABORT\t") && !refineryId.isEmpty()) {
                worker.cancel();
            } else if (command.startsWith("PING\t")) {
                System.out.println("PONG\t"+parts[1]);
            } else {
                System.err.println("Uhhuh!\nReceived: " + command);
                System.exit(0);
            }
        }
    }

    public static String arrayToCSV(double[] a) {
        StringBuilder sb = new StringBuilder();
        for (double d : a) {
            sb.append("" + d + ",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    private static double[] getRandomDist() {
        Random r = new Random();
        double[] result = new double[100];
        double sum = 0;
        double value;
        for (int i = 0; i < 100; i++) {
            result[i] = value = r.nextDouble();
            sum += value;
        }
        //normalize
        for (int i = 0; i < 100; i++) {
            result[i] /= sum;
        }
        return result;
    }
    
    private static int[] getValues(String line) {
        String[] parts = line.split(",");
        int[] result = new int[parts.length];
        for (int i = 0; i<parts.length; i++) {
            result[i] = Integer.parseInt(parts[i]);
        }
        return result;
    }
    
    private static double[] basedOnLastValue(int last) {
        double minimal = 0.0000001;
        double[] result = new double[100];
        //make sure we don't have 0 probability
        Arrays.fill(result, minimal);
        if (last == 0) {
            //put all but minimal to bet on zero
            result[0] = 1-99*minimal;
        } else {
            //25% to adjacent values, 49.999% to previous
            result[last]=.5-97*minimal;
            result[last-1]=.25;
            result[last+1]=.25;
        }
        return result;
    }
    private static double[] getDistributionCloseTo(){
        double[] distribution = new double[100];
        for (double number : distribution)
            number = 0.0000001;
        return distribution;
    }
    private static double[] getDistribution(double probabilityOfZero, double probabilityOfMostFrequent ,int mostFrequent, int minimum, int maximum){
        double[] distribution = new double[100];
        for (double number : distribution)
            number = 0.0000001;
        distribution[0] = probabilityOfZero;
        distribution[mostFrequent] = probabilityOfMostFrequent;
        for(int i = minimum; i < mostFrequent; i ++){
            distribution[i] = 0;
            //TODO
        }
        for(int i = mostFrequent + 1; i <= maximum ; i ++){
            distribution[i] = 0;
            //TODO
        }
        return distribution;
    }
    private static double[] dependingOnSixtyFive(){
        double[] distribution = new double[100];
        for (double number : distribution)
            number = 0.0000001;
        return distribution;
    }
    private static double[] dependingOnFiftyNine(){
        double[] distribution = new double[100];
        for (double number : distribution)
            number = 0.0000001;
        return distribution;
    }
    private static double[] basedOnSixteen(){
        double[] distribution = new double[100];
        for (double number : distribution)
            number = 0.0000001;
        return distribution;
    }
    private static double[] dependingOnSixteen(){
        double[] distribution = new double[100];
        for (double number : distribution)
            number = 0.0000001;
        return distribution;
    }
    private static double[] dependingOnFifty(){
        double[] distribution = new double[100];
        for (double number : distribution)
            number = 0.0000001;
        return distribution;
    }
    private static double[] dependingOnHundredTwentySix(){
        double[] distribution = new double[100];
        for (double number : distribution)
            number = 0.0000001;
        return distribution;
    }
    private static double[] betweenTenAndOneThousand(){
        double[] distribution = new double[100];
        for (double number : distribution)
            number = 0.0000001;
        return distribution;
    }
    private static double[] zeroColumns(){
        double[] distribution = new double[100];
        for (double number : distribution)
            number = 0.0000001;
        return distribution;
    }
    private static double[] getDistribution(){
        double[] distribution = new double[100];
        for (double number : distribution)
            number = 0.0000001;
        switch(round){
            case 74:
            case 230:
            case 246:
            case 274:
                distribution = dependingOnSixtyFive();
                break;
            case 162:
            case 185:
            case 267:
            case 295:
                distribution = dependingOnFiftyNine();
                break;
            case 90:
            case 128:
            case 172:
            case 240:
            case 251:
                distribution = basedOnSixteen();
                break;
            case 126:
                distribution = dependingOnSixteen();
                break;
            case 184:
            case 244:
            case 248:
                distribution = dependingOnFifty();
                break;
            case 237:
            case 270:
            case 279:
            case 291:
                distribution = dependingOnHundredTwentySix();
                break;
                
            case 1: 
            case 2: 
            case 5: 
            case 9: 
            case 11: 
            case 21: 
            case 28: 
            case 31: 
            case 34: 
            case 37: 
            case 41: 
            case 42: 
            case 47: 
            case 52: 
            case 56: 
            case 60: 
            case 61: 
            case 63: 
            case 66: 
            case 67: 
            case 69: 
            case 71: 
            case 77: 
            case 82: 
            case 84:
            case 85: 
            case 86: 
            case 87: 
            case 91: 
            case 97: 
            case 102: 
            case 103: 
            case 109: 
            case 110: 
            case 111: 
            case 113: 
            case 114: 
            case 115: 
            case 116: 
            case 118: 
            case 120: 
            case 122: 
            case 132: 
            case 134: 
            case 141: 
            case 143: 
            case 145: 
            case 147: 
            case 152: 
            case 155: 
            case 158: 
            case 176: 
            case 178: 
            case 179: 
            case 181: 
            case 186: 
            case 187: 
            case 190: 
            case 191: 
            case 196: 
            case 197: 
            case 203: 
            case 204: 
            case 206: 
            case 209: 
            case 213: 
            case 214: 
            case 221: 
            case 227: 
            case 228: 
            case 229: 
            case 231: 
            case 233: 
            case 234: 
            case 235: 
            case 242: 
            case 250: 
            case 253: 
            case 257: 
            case 258: 
            case 259: 
            case 265: 
            case 268: 
            case 271: 
            case 275: 
            case 277: 
            case 280: 
            case 281: 
            case 283: 
            case 289: 
            case 292: 
            case 299: 
            case 300:
                distribution = betweenTenAndOneThousand();
                break;
            default:
                distribution = zeroColumns();
        }
        return distribution;
    }
    public static class ExampleWorker extends Thread {

        @Override
        public void run() {
            double[][] myGuess = new double[1000][100];
            File inputDir = new File(currentInput);
            //just take first file
            File inputFile = inputDir.listFiles()[0];
            try {
                Scanner s = new Scanner(inputFile);
                File outputDir = new File(currentWork + "/output");
                if (!outputDir.exists()) {
                    outputDir.mkdir();
                }
                File outputFile = new File(outputDir, "output.csv");
                PrintWriter pw = new PrintWriter(outputFile);
                if (round == 0) {
                    //don't have context yet
                    context = new int[303][1000];

                    //return dist over first column for each vector
                    for (double[] vector : myGuess) {
                        if (Thread.currentThread().isInterrupted()) {
                            throw new InterruptedException();
                        }
                        vector = getDistribution();
                    }
                } else {
                    //read context
                    context[round-1] = getValues(s.nextLine());
                    //now calculate based on context
                    for (int i = 0; i<1000; i++) {
                        myGuess[i] = basedOnLastValue(context[round-1][i]);
                    }
                }
                for (double[] vector : myGuess) {
                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException();
                    }
                    pw.println(arrayToCSV(vector));
                }
                pw.close();
                s.close();
                //increment round
                round ++;
                System.out.println("READY\t" + currentRequest + "\t" + currentWork + "/output/output.csv");
            } catch (InterruptedException e) {
                System.out.println("ABORTED\t" + currentRequest);
            } catch (Exception e) {
                System.err.println("Oops, " + e.getMessage());
                e.printStackTrace();
                System.out.println("FAILED\t" + currentRequest + "\t" + currentWork + "/output/output.csv");
            }
        }
        
        public void cancel() {
            interrupt();
        }

    }

}
