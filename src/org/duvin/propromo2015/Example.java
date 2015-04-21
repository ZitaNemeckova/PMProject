/*
 * (c) Johannes Verwijnen, duvin@duvin.org
 */
package org.duvin.propromo2015;

import java.io.File;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Locale;
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
    private static DecimalFormat df = new DecimalFormat();
	private static int row = 0;
	private static double minimalValue = 0.00000000001;

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
                System.out.println("PONG\t" + parts[1]);
            } else {
                System.err.println("Uhhuh!\nReceived: " + command);
                System.exit(0);
            }
        }
    }

    public static String arrayToCSV(double[] a) {
        df.setMaximumFractionDigits(340);
        df.setMinimumFractionDigits(1);
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        StringBuilder sb = new StringBuilder();
        for (double d : a) {
            sb.append(df.format(d)).append(",");
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
        for (int i = 0; i < parts.length; i++) {
            result[i] = Integer.parseInt(parts[i]);
        }
        return result;
    }
//HERE
         private static double[] normalizeDistribution(double[] distribution){
         double norm = 0;
         for(double probability:distribution)
             norm = norm + probability;
         for(int i = 0; i < distribution.length; i++)
             distribution[i] = distribution[i]/norm;
         return distribution;
     }
    private static double[] getDistributionCloseTo(int value){
        double[] distribution = new double[100];
        for(int i = 0; i < distribution.length; i ++)
            distribution[i] = minimalValue;
        distribution[value] = 0.3;
        distribution[value + 1] = 0.2;
        distribution[value + 2] = 0.2;
        distribution[value + 3] = 0.1;
        distribution[value + 4] = 0.1;
        distribution[value + 5] = 0.1;
        distribution [value - 1] = 0.2;
        distribution [value - 2] = 0.2;
        distribution [value - 3] = 0.1;
        distribution [value - 4] = 0.1;
        distribution [value - 5] = 0.1;
        return normalizeDistribution(distribution);
    }
    private static double[] getDistribution(double probabilityOfZero, double probabilityOfMostFrequent , double probabilityOfMax, double probabilityOfMin,int mostFrequent, int minimum, int maximum){
        double[] distribution = new double[100];
        for(int i = 0; i < distribution.length; i ++)
            distribution[i] = minimalValue;
        distribution[0] = probabilityOfZero;
        distribution[mostFrequent] = probabilityOfMostFrequent;
        distribution[maximum] = probabilityOfMax;
        distribution[minimum] = probabilityOfMin;
        double m1 = (probabilityOfMostFrequent - probabilityOfMin)/(mostFrequent - minimum);
        double m2 = (probabilityOfMostFrequent - probabilityOfMax)/(mostFrequent - maximum);
        for(int i = minimum + 1; i < mostFrequent; i ++){
            distribution[i] = probabilityOfMostFrequent - (mostFrequent - (i - minimum))*m1;
            if(distribution[i] < 0)
                distribution[i] = distribution[i]*-1;
        }
        for(int i = mostFrequent + 1; i < maximum ; i ++){
            distribution[i] = probabilityOfMostFrequent - m2*(mostFrequent - (mostFrequent + i));
            if(distribution[i] < 0)
                distribution[i] = distribution[i]*-1;
        }
        return normalizeDistribution(distribution);
    }
    //DONE
    private static double[] dependingOnSixtyFive(int row){
        double[] distribution = new double[100];
        int value = context[65][row];
        for(int i = 0; i < distribution.length; i ++)
            distribution[i] = minimalValue;
        if(value == 0){
            distribution = getDistributionCloseTo(value);
            distribution[0] = 0.89;
        }
        else{
            distribution = getDistributionCloseTo(value);
        }
        return normalizeDistribution(distribution);
    }
    //TODO
    private static double[] dependingOnFiftyNine(int row){
        double[] distribution = new double[100];
        int value = context[59][row];
        for(int i = 0; i < distribution.length; i ++)
            distribution[i] = minimalValue;
        if(value == 0)
            distribution = zeroColumns();
        else{
            distribution = getDistributionCloseTo(value);
        }
        return normalizeDistribution(distribution);
    }
    //DONE
    private static double[] basedOnSixteen(int row){
        double[] distribution = new double[100];
        int value = context[16][row];
        if(value == 0)
            distribution = zeroColumns();
        else
            distribution[0] = 0.0138711049320669; distribution[1] = 4.86879687890645E-08; distribution[2] = 4.86879687890645E-08; distribution[3] = 4.86879687890645E-08; distribution[4] = 4.86879687890645E-08; distribution[5] = 4.86879687890645E-08; distribution[6] = 4.86879687890645E-08; distribution[7] = 4.86879687890645E-08; distribution[8] = 4.86879687890645E-08; distribution[9] = 4.86879687890645E-08; distribution[10] = 4.86879687890645E-08; distribution[11] = 4.86879687890645E-08; distribution[12] = 4.86879687890645E-08; distribution[13] = 4.86879687890645E-08; distribution[14] = 4.86879687890645E-08; distribution[15] = 4.86879687890645E-08; distribution[16] = 4.86879687890645E-08; distribution[17] = 4.86879687890645E-08; distribution[18] = 4.86879687890645E-08; distribution[19] = 4.86879687890645E-08; distribution[20] = 4.86879687890645E-08; distribution[21] = 4.86879687890645E-08; distribution[22] = 4.86879687890645E-08; distribution[23] = 4.86879687890645E-08; distribution[24] = 4.86879687890645E-08; distribution[25] = 4.86879687890645E-08; distribution[26] = 4.86879687890645E-08; distribution[27] = 4.86879687890645E-08; distribution[28] = 4.86879687890645E-08; distribution[29] = 4.86879687890645E-08; distribution[30] = 4.86879687890645E-08; distribution[31] = 4.86879687890645E-08; distribution[32] = 4.86879687890645E-08; distribution[33] = 4.86879687890645E-08; distribution[34] = 4.86879687890645E-08; distribution[35] = 4.86879687890645E-08; distribution[36] = 4.86879687890645E-08; distribution[37] = 4.86879687890645E-08; distribution[38] = 4.86879687890645E-08; distribution[39] = 4.86879687890645E-08; distribution[40] = 4.86879687890645E-08; distribution[41] = 4.86879687890645E-08; distribution[42] = 4.86879687890645E-08; distribution[43] = 4.86879687890645E-08; distribution[44] = 4.86879687890645E-08; distribution[45] = 4.86879687890645E-08; distribution[46] = 4.86879687890645E-08; distribution[47] = 4.86879687890645E-08; distribution[48] = 4.86879687890645E-08; distribution[49] = 4.86879687890645E-08; distribution[50] = 4.86879687890645E-08; distribution[51] = 4.86879687890645E-08; distribution[52] = 4.86879687890645E-08; distribution[53] = 4.86879687890645E-08; distribution[54] = 9.7375937578129E-08; distribution[55] = 4.86879687890645E-08; distribution[56] = 4.86879687890645E-08; distribution[57] = 4.86879687890645E-08; distribution[58] = 4.86879687890645E-08; distribution[59] = 4.86879687890645E-08; distribution[60] = 4.86879687890645E-08; distribution[61] = 9.7375937578129E-08; distribution[62] = 1.46063906367193E-07; distribution[63] = 1.07113531335942E-06; distribution[64] = 1.60670297003913E-06; distribution[65] = 5.06354875406271E-06; distribution[66] = 2.63401911148839E-05; distribution[67] = 2.25912175181259E-05; distribution[68] = 4.45008034732049E-05; distribution[69] = 0.00016758398857196; distribution[70] = 0.000131262763855318; distribution[71] = 0.000225620047368525; distribution[72] = 0.000486928375859434; distribution[73] = 0.000212133480013954; distribution[74] = 0.000239788246286143; distribution[75] = 0.000475438015225215; distribution[76] = 0.000179269101081335; distribution[77] = 0.000155217244499538; distribution[78] = 0.000156434443719264; distribution[79] = 2.75087023658214E-05; distribution[80] = 1.75276687640632E-05; distribution[81] = 2.7898206116134E-05; distribution[82] = 1.00297215705473E-05; distribution[83] = 7.44925922472687E-06; distribution[84] = 8.66645844445348E-06; distribution[85] = 1.26588718851568E-06; distribution[86] = 2.43439843945322E-07; distribution[87] = 9.7375937578129E-08; distribution[88] = 4.86879687890645E-08; distribution[89] = 4.86879687890645E-08; distribution[90] = 4.86879687890645E-08; distribution[91] = 4.86879687890645E-08; distribution[92] = 4.86879687890645E-08; distribution[93] = 4.86879687890645E-08; distribution[94] = 4.86879687890645E-08; distribution[95] = 4.86879687890645E-08; distribution[96] = 4.86879687890645E-08; distribution[97] = 4.86879687890645E-08; distribution[98] = 4.86879687890645E-08; distribution[99] = 4.86879687890645E-08; 
        return normalizeDistribution(distribution);
    }
    //TODO
    private static double[] dependingOnSixteen(int row){
        double[] distribution = new double[100];
        int value = context[16][row];
        for(int i = 0; i < distribution.length; i ++)
            distribution[i] = minimalValue;
        if(value == 0)
            distribution = zeroColumns();
        else{
            distribution = getDistributionCloseTo(value);
        }
        return normalizeDistribution(distribution);
    }
    //DONE
    private static double[] dependingOnFifty(int row){
        double[] distribution = new double[100];
        int value = context[50][row];
        for(int i = 0; i < distribution.length; i ++)
            distribution[i] = minimalValue;
        if(value == 0)
            distribution = zeroColumns();
        else{
            distribution = getDistributionCloseTo(value);
        }
        return normalizeDistribution(distribution);
    }
    //DONE
    private static double[] dependingOnHundredTwentySix(int row){
        double[] distribution = new double[100];
        int value = context[50][row];
        for(int i = 0; i < distribution.length; i ++)
            distribution[i] = minimalValue;
        if(value == 0)
            distribution = zeroColumns();
        else{
            distribution = getDistributionCloseTo(value);
        }
        return normalizeDistribution(distribution);
    }
    //UPDATED
    private static double[] betweenTenAndOneThousand(){
        double[] distribution = new double[100];
        for(int i = 0; i < distribution.length; i ++)
            distribution[i] = minimalValue;
        distribution[0] = 0.94473226600446; distribution[1] = 1.56377545435496E-07; distribution[2] = 1.56377545435496E-07; distribution[3] = 1.56377545435496E-07; distribution[4] = 1.56377545435496E-07; distribution[5] = 1.56377545435496E-07; distribution[6] = 1.56377545435496E-07; distribution[7] = 1.56377545435496E-07; distribution[8] = 1.56377545435496E-07; distribution[9] = 1.56377545435496E-07; distribution[10] = 1.56377545435496E-07; distribution[11] = 1.56377545435496E-07; distribution[12] = 1.56377545435496E-07; distribution[13] = 1.56377545435496E-07; distribution[14] = 1.56377545435496E-07; distribution[15] = 1.56377545435496E-07; distribution[16] = 1.56377545435496E-07; distribution[17] = 1.56377545435496E-07; distribution[18] = 1.56377545435496E-07; distribution[19] = 1.56377545435496E-07; distribution[20] = 1.56377545435496E-07; distribution[21] = 1.56377545435496E-07; distribution[22] = 1.56377545435496E-07; distribution[23] = 1.56377545435496E-07; distribution[24] = 1.56377545435496E-07; distribution[25] = 1.56377545435496E-07; distribution[26] = 1.56377545435496E-07; distribution[27] = 1.56377545435496E-07; distribution[28] = 1.56377545435496E-07; distribution[29] = 1.56377545435496E-07; distribution[30] = 1.56377545435496E-07; distribution[31] = 1.56377545435496E-07; distribution[32] = 1.56377545435496E-07; distribution[33] = 1.56377545435496E-07; distribution[34] = 1.25102036348397E-06; distribution[35] = 1.28229587257107E-05; distribution[36] = 0.00015137346398156; distribution[37] = 0.000446614269763776; distribution[38] = 0.000499157125030103; distribution[39] = 0.000449429065581615; distribution[40] = 0.000382968608771529; distribution[41] = 0.000414556872949499; distribution[42] = 0.000339808406231332; distribution[43] = 9.64849455337009E-05; distribution[44] = 6.44275487194243E-05; distribution[45] = 6.81806098098762E-05; distribution[46] = 0.000277257388057134; distribution[47] = 0.00041627702594929; distribution[48] = 0.000568119622567156; distribution[49] = 0.000532152787116992; distribution[50] = 0.000362952282955786; distribution[51] = 0.000457091565307954; distribution[52] = 0.000515733144846265; distribution[53] = 0.000360606619774253; distribution[54] = 0.000548103296751413; distribution[55] = 0.000584539264837883; distribution[56] = 0.00026803111287644; distribution[57] = 0.000310409427689459; distribution[58] = 0.000235348205880421; distribution[59] = 0.000374211466227142; distribution[60] = 0.0011692349072212; distribution[61] = 0.000748422932454283; distribution[62] = 0.00118534179440106; distribution[63] = 0.00264872286458643; distribution[64] = 0.00148073897772871; distribution[65] = 0.00237068358880212; distribution[66] = 0.00547258858006061; distribution[67] = 0.00216504711655444; distribution[68] = 0.00258679735659397; distribution[69] = 0.00523223629272626; distribution[70] = 0.0022926511936298; distribution[71] = 0.00268719174076356; distribution[72] = 0.00429788045874917; distribution[73] = 0.00129089663757002; distribution[74] = 0.00135798260456185; distribution[75] = 0.00303591366708472; distribution[76] = 0.00144070632609722; distribution[77] = 0.00135032010483551; distribution[78] = 0.00174314049896947; distribution[79] = 0.000494153043576167; distribution[80] = 0.00035622804850206; distribution[81] = 0.000615345641288676; distribution[82] = 0.000232220654971711; distribution[83] = 0.00016075611670769; distribution[84] = 0.000100238006624153; distribution[85] = 5.94234672654884E-06; distribution[86] = 1.09464281804847E-06; distribution[87] = 3.12755090870992E-07; distribution[88] = 1.56377545435496E-07; distribution[89] = 1.56377545435496E-07; distribution[90] = 1.56377545435496E-07; distribution[91] = 1.56377545435496E-07; distribution[92] = 1.56377545435496E-07; distribution[93] = 1.56377545435496E-07; distribution[94] = 1.56377545435496E-07; distribution[95] = 1.56377545435496E-07; distribution[96] = 1.56377545435496E-07; distribution[97] = 1.56377545435496E-07; distribution[98] = 1.56377545435496E-07; distribution[99] = 1.56377545435496E-07; 
        return normalizeDistribution(distribution);
    }
    //UPDATED
    private static double[] zeroColumns(){
        double[] distribution = new double[100];
        for(int i = 0; i < distribution.length; i ++)
            distribution[i] = minimalValue;
        distribution[0] = 0.998562300319489; distribution[1] = 1.45222189950624E-05; distribution[2] = 1.45222189950624E-05; distribution[3] = 1.45222189950624E-05; distribution[4] = 1.45222189950624E-05; distribution[5] = 1.45222189950624E-05; distribution[6] = 1.45222189950624E-05; distribution[7] = 1.45222189950624E-05; distribution[8] = 1.45222189950624E-05; distribution[9] = 1.45222189950624E-05; distribution[10] = 1.45222189950624E-05; distribution[11] = 1.45222189950624E-05; distribution[12] = 1.45222189950624E-05; distribution[13] = 1.45222189950624E-05; distribution[14] = 1.45222189950624E-05; distribution[15] = 1.45222189950624E-05; distribution[16] = 1.45222189950624E-05; distribution[17] = 1.45222189950624E-05; distribution[18] = 1.45222189950624E-05; distribution[19] = 1.45222189950624E-05; distribution[20] = 1.45222189950624E-05; distribution[21] = 1.45222189950624E-05; distribution[22] = 1.45222189950624E-05; distribution[23] = 1.45222189950624E-05; distribution[24] = 1.45222189950624E-05; distribution[25] = 1.45222189950624E-05; distribution[26] = 1.45222189950624E-05; distribution[27] = 1.45222189950624E-05; distribution[28] = 1.45222189950624E-05; distribution[29] = 1.45222189950624E-05; distribution[30] = 1.45222189950624E-05; distribution[31] = 1.45222189950624E-05; distribution[32] = 1.45222189950624E-05; distribution[33] = 1.45222189950624E-05; distribution[34] = 1.45222189950624E-05; distribution[35] = 1.45222189950624E-05; distribution[36] = 1.45222189950624E-05; distribution[37] = 1.45222189950624E-05; distribution[38] = 1.45222189950624E-05; distribution[39] = 1.45222189950624E-05; distribution[40] = 1.45222189950624E-05; distribution[41] = 1.45222189950624E-05; distribution[42] = 1.45222189950624E-05; distribution[43] = 1.45222189950624E-05; distribution[44] = 1.45222189950624E-05; distribution[45] = 1.45222189950624E-05; distribution[46] = 1.45222189950624E-05; distribution[47] = 1.45222189950624E-05; distribution[48] = 1.45222189950624E-05; distribution[49] = 1.45222189950624E-05; distribution[50] = 1.45222189950624E-05; distribution[51] = 1.45222189950624E-05; distribution[52] = 1.45222189950624E-05; distribution[53] = 1.45222189950624E-05; distribution[54] = 1.45222189950624E-05; distribution[55] = 1.45222189950624E-05; distribution[56] = 1.45222189950624E-05; distribution[57] = 1.45222189950624E-05; distribution[58] = 1.45222189950624E-05; distribution[59] = 1.45222189950624E-05; distribution[60] = 1.45222189950624E-05; distribution[61] = 1.45222189950624E-05; distribution[62] = 1.45222189950624E-05; distribution[63] = 1.45222189950624E-05; distribution[64] = 1.45222189950624E-05; distribution[65] = 1.45222189950624E-05; distribution[66] = 1.45222189950624E-05; distribution[67] = 1.45222189950624E-05; distribution[68] = 1.45222189950624E-05; distribution[69] = 1.45222189950624E-05; distribution[70] = 1.45222189950624E-05; distribution[71] = 1.45222189950624E-05; distribution[72] = 1.45222189950624E-05; distribution[73] = 1.45222189950624E-05; distribution[74] = 1.45222189950624E-05; distribution[75] = 1.45222189950624E-05; distribution[76] = 1.45222189950624E-05; distribution[77] = 1.45222189950624E-05; distribution[78] = 1.45222189950624E-05; distribution[79] = 1.45222189950624E-05; distribution[80] = 1.45222189950624E-05; distribution[81] = 1.45222189950624E-05; distribution[82] = 1.45222189950624E-05; distribution[83] = 1.45222189950624E-05; distribution[84] = 1.45222189950624E-05; distribution[85] = 1.45222189950624E-05; distribution[86] = 1.45222189950624E-05; distribution[87] = 1.45222189950624E-05; distribution[88] = 1.45222189950624E-05; distribution[89] = 1.45222189950624E-05; distribution[90] = 1.45222189950624E-05; distribution[91] = 1.45222189950624E-05; distribution[92] = 1.45222189950624E-05; distribution[93] = 1.45222189950624E-05; distribution[94] = 1.45222189950624E-05; distribution[95] = 1.45222189950624E-05; distribution[96] = 1.45222189950624E-05; distribution[97] = 1.45222189950624E-05; distribution[98] = 1.45222189950624E-05; distribution[99] = 1.45222189950624E-05;
        return normalizeDistribution(distribution);
    }
    //UPDATED
    private static double[] sixteen(){
        double[] distribution = new double[100];
        for(int i = 0; i < distribution.length; i ++)
            distribution[i] = minimalValue;
        distribution[0] = 0.277621260528609; distribution[1] = 1.45222189950624E-05; distribution[2] = 1.45222189950624E-05; distribution[3] = 1.45222189950624E-05; distribution[4] = 1.45222189950624E-05; distribution[5] = 1.45222189950624E-05; distribution[6] = 1.45222189950624E-05; distribution[7] = 1.45222189950624E-05; distribution[8] = 1.45222189950624E-05; distribution[9] = 1.45222189950624E-05; distribution[10] = 1.45222189950624E-05; distribution[11] = 1.45222189950624E-05; distribution[12] = 1.45222189950624E-05; distribution[13] = 1.45222189950624E-05; distribution[14] = 1.45222189950624E-05; distribution[15] = 1.45222189950624E-05; distribution[16] = 1.45222189950624E-05; distribution[17] = 1.45222189950624E-05; distribution[18] = 1.45222189950624E-05; distribution[19] = 1.45222189950624E-05; distribution[20] = 1.45222189950624E-05; distribution[21] = 1.45222189950624E-05; distribution[22] = 1.45222189950624E-05; distribution[23] = 1.45222189950624E-05; distribution[24] = 1.45222189950624E-05; distribution[25] = 1.45222189950624E-05; distribution[26] = 1.45222189950624E-05; distribution[27] = 1.45222189950624E-05; distribution[28] = 1.45222189950624E-05; distribution[29] = 1.45222189950624E-05; distribution[30] = 1.45222189950624E-05; distribution[31] = 1.45222189950624E-05; distribution[32] = 1.45222189950624E-05; distribution[33] = 1.45222189950624E-05; distribution[34] = 1.45222189950624E-05; distribution[35] = 1.45222189950624E-05; distribution[36] = 1.45222189950624E-05; distribution[37] = 1.45222189950624E-05; distribution[38] = 1.45222189950624E-05; distribution[39] = 1.45222189950624E-05; distribution[40] = 1.45222189950624E-05; distribution[41] = 1.45222189950624E-05; distribution[42] = 1.45222189950624E-05; distribution[43] = 1.45222189950624E-05; distribution[44] = 1.45222189950624E-05; distribution[45] = 1.45222189950624E-05; distribution[46] = 1.45222189950624E-05; distribution[47] = 1.45222189950624E-05; distribution[48] = 5.80888759802498E-05; distribution[49] = 0.000188788846935812; distribution[50] = 0.000377577693871624; distribution[51] = 0.00348533255881499; distribution[52] = 0.00493755445832123; distribution[53] = 0.00129247749056056; distribution[54] = 0.000638977635782748; distribution[55] = 0.0025123438861458; distribution[56] = 0.00153935521347662; distribution[57] = 0.00248329944815568; distribution[58] = 0.00241068835318037; distribution[59] = 0.00646238745280279; distribution[60] = 0.0155097298867267; distribution[61] = 0.0192855068254429; distribution[62] = 0.0296543711879175; distribution[63] = 0.0452076677316294; distribution[64] = 0.0389050246877723; distribution[65] = 0.065814696485623; distribution[66] = 0.0968922451350566; distribution[67] = 0.0452512343886146; distribution[68] = 0.0638832413592797; distribution[69] = 0.0932762126052861; distribution[70] = 0.0291460935230903; distribution[71] = 0.0333865814696486; distribution[72] = 0.0553732210281731; distribution[73] = 0.0174266627940749; distribution[74] = 0.0164681963404008; distribution[75] = 0.0185593958756898; distribution[76] = 0.00485042114435086; distribution[77] = 0.00363055474876561; distribution[78] = 0.0022799883822248; distribution[79] = 0.000188788846935812; distribution[80] = 4.35666569851873E-05; distribution[81] = 1.45222189950624E-05; distribution[82] = 1.45222189950624E-05; distribution[83] = 1.45222189950624E-05; distribution[84] = 1.45222189950624E-05; distribution[85] = 1.45222189950624E-05; distribution[86] = 1.45222189950624E-05; distribution[87] = 1.45222189950624E-05; distribution[88] = 1.45222189950624E-05; distribution[89] = 1.45222189950624E-05; distribution[90] = 1.45222189950624E-05; distribution[91] = 1.45222189950624E-05; distribution[92] = 1.45222189950624E-05; distribution[93] = 1.45222189950624E-05; distribution[94] = 1.45222189950624E-05; distribution[95] = 1.45222189950624E-05; distribution[96] = 1.45222189950624E-05; distribution[97] = 1.45222189950624E-05; distribution[98] = 1.45222189950624E-05; distribution[99] = 1.45222189950624E-05; 
        return normalizeDistribution(distribution);
    }
    //UPDATED
    private static double[] sixtyFive(){
        double[] distribution = new double[100];
        for(int i = 0; i < distribution.length; i ++)
            distribution[i] = minimalValue;
        distribution[0] = 0.670418239907058; distribution[1] = 1.45222189950624E-05; distribution[2] = 1.45222189950624E-05; distribution[3] = 1.45222189950624E-05; distribution[4] = 1.45222189950624E-05; distribution[5] = 1.45222189950624E-05; distribution[6] = 1.45222189950624E-05; distribution[7] = 1.45222189950624E-05; distribution[8] = 1.45222189950624E-05; distribution[9] = 1.45222189950624E-05; distribution[10] = 1.45222189950624E-05; distribution[11] = 1.45222189950624E-05; distribution[12] = 1.45222189950624E-05; distribution[13] = 1.45222189950624E-05; distribution[14] = 1.45222189950624E-05; distribution[15] = 1.45222189950624E-05; distribution[16] = 1.45222189950624E-05; distribution[17] = 1.45222189950624E-05; distribution[18] = 1.45222189950624E-05; distribution[19] = 1.45222189950624E-05; distribution[20] = 1.45222189950624E-05; distribution[21] = 1.45222189950624E-05; distribution[22] = 1.45222189950624E-05; distribution[23] = 1.45222189950624E-05; distribution[24] = 1.45222189950624E-05; distribution[25] = 1.45222189950624E-05; distribution[26] = 1.45222189950624E-05; distribution[27] = 1.45222189950624E-05; distribution[28] = 1.45222189950624E-05; distribution[29] = 1.45222189950624E-05; distribution[30] = 1.45222189950624E-05; distribution[31] = 1.45222189950624E-05; distribution[32] = 1.45222189950624E-05; distribution[33] = 1.45222189950624E-05; distribution[34] = 1.45222189950624E-05; distribution[35] = 1.45222189950624E-05; distribution[36] = 5.80888759802498E-05; distribution[37] = 0.00113273308161487; distribution[38] = 0.000929422015683996; distribution[39] = 0.00213476619227418; distribution[40] = 0.00575079872204473; distribution[41] = 0.00553296543711879; distribution[42] = 0.00729015393552135; distribution[43] = 0.00405169909962242; distribution[44] = 0.00225094394423468; distribution[45] = 0.00315132152192855; distribution[46] = 0.00334011036886436; distribution[47] = 0.000784199825733372; distribution[48] = 0.001161777519605; distribution[49] = 0.00184432181237293; distribution[50] = 0.00124891083357537; distribution[51] = 0.00119082195759512; distribution[52] = 0.00103107754864943; distribution[53] = 0.000813244263723497; distribution[54] = 0.000493755445832123; distribution[55] = 0.000188788846935812; distribution[56] = 0.000551844321812373; distribution[57] = 0.00108916642462968; distribution[58] = 0.00123438861458031; distribution[59] = 0.00252686610514087; distribution[60] = 0.00328202149288411; distribution[61] = 0.000972988672669184; distribution[62] = 0.000435666569851873; distribution[63] = 0.00106012198663956; distribution[64] = 0.000319488817891374; distribution[65] = 0.000726110949753122; distribution[66] = 0.00659308742375835; distribution[67] = 0.00460354342143479; distribution[68] = 0.00682544292767935; distribution[69] = 0.0160180075515539; distribution[70] = 0.00855358698809178; distribution[71] = 0.0124891083357537; distribution[72] = 0.0348678478071449; distribution[73] = 0.0174992738890502; distribution[74] = 0.022102817310485; distribution[75] = 0.0549375544583212; distribution[76] = 0.0205053732210282; distribution[77] = 0.020461806564043; distribution[78] = 0.0286523380772582; distribution[79] = 0.00794365379029916; distribution[80] = 0.00546035434214348; distribution[81] = 0.00421144350856811; distribution[82] = 0.000421144350856811; distribution[83] = 0.0001161777519605; distribution[84] = 1.45222189950624E-05; distribution[85] = 1.45222189950624E-05; distribution[86] = 1.45222189950624E-05; distribution[87] = 1.45222189950624E-05; distribution[88] = 1.45222189950624E-05; distribution[89] = 1.45222189950624E-05; distribution[90] = 1.45222189950624E-05; distribution[91] = 1.45222189950624E-05; distribution[92] = 1.45222189950624E-05; distribution[93] = 1.45222189950624E-05; distribution[94] = 1.45222189950624E-05; distribution[95] = 1.45222189950624E-05; distribution[96] = 1.45222189950624E-05; distribution[97] = 1.45222189950624E-05; distribution[98] = 1.45222189950624E-05; distribution[99] = 1.45222189950624E-05; 
        return normalizeDistribution(distribution);
    }
    //UPDATED
    private static double[] fiftyNine(){
        double[] distribution = new double[100];
        for(int i = 0; i < distribution.length; i ++)
            distribution[i] = minimalValue;
        distribution[0] = 0.572916061574208; distribution[1] = 1.45222189950624E-05; distribution[2] = 1.45222189950624E-05; distribution[3] = 1.45222189950624E-05; distribution[4] = 1.45222189950624E-05; distribution[5] = 1.45222189950624E-05; distribution[6] = 1.45222189950624E-05; distribution[7] = 1.45222189950624E-05; distribution[8] = 1.45222189950624E-05; distribution[9] = 1.45222189950624E-05; distribution[10] = 1.45222189950624E-05; distribution[11] = 1.45222189950624E-05; distribution[12] = 1.45222189950624E-05; distribution[13] = 1.45222189950624E-05; distribution[14] = 1.45222189950624E-05; distribution[15] = 1.45222189950624E-05; distribution[16] = 1.45222189950624E-05; distribution[17] = 1.45222189950624E-05; distribution[18] = 1.45222189950624E-05; distribution[19] = 1.45222189950624E-05; distribution[20] = 1.45222189950624E-05; distribution[21] = 1.45222189950624E-05; distribution[22] = 1.45222189950624E-05; distribution[23] = 1.45222189950624E-05; distribution[24] = 1.45222189950624E-05; distribution[25] = 1.45222189950624E-05; distribution[26] = 1.45222189950624E-05; distribution[27] = 1.45222189950624E-05; distribution[28] = 1.45222189950624E-05; distribution[29] = 1.45222189950624E-05; distribution[30] = 1.45222189950624E-05; distribution[31] = 1.45222189950624E-05; distribution[32] = 1.45222189950624E-05; distribution[33] = 1.45222189950624E-05; distribution[34] = 1.45222189950624E-05; distribution[35] = 1.45222189950624E-05; distribution[36] = 1.45222189950624E-05; distribution[37] = 1.45222189950624E-05; distribution[38] = 1.45222189950624E-05; distribution[39] = 1.45222189950624E-05; distribution[40] = 2.90444379901249E-05; distribution[41] = 1.45222189950624E-05; distribution[42] = 1.45222189950624E-05; distribution[43] = 0.000174266627940749; distribution[44] = 0.000261399941911124; distribution[45] = 0.000566366540807435; distribution[46] = 0.000929422015683996; distribution[47] = 0.00214928841126924; distribution[48] = 0.00357246587278536; distribution[49] = 0.000900377577693872; distribution[50] = 0.000421144350856811; distribution[51] = 0.000740633168748185; distribution[52] = 0.000551844321812373; distribution[53] = 0.00071158873075806; distribution[54] = 0.00113273308161487; distribution[55] = 0.000871333139703747; distribution[56] = 0.000435666569851873; distribution[57] = 0.000479233226837061; distribution[58] = 0.000813244263723497; distribution[59] = 0.00065349985477781; distribution[60] = 0.00156839965146674; distribution[61] = 0.00158292187046181; distribution[62] = 0.00273017717107174; distribution[63] = 0.0122277083938426; distribution[64] = 0.00795817600929422; distribution[65] = 0.00922160906186465; distribution[66] = 0.0169619517862329; distribution[67] = 0.00821957595120534; distribution[68] = 0.013011908219576; distribution[69] = 0.0329509148997967; distribution[70] = 0.026038338658147; distribution[71] = 0.0332704037176881; distribution[72] = 0.0673540516990996; distribution[73] = 0.0292767934940459; distribution[74] = 0.0321812372930584; distribution[75] = 0.0556200987510892; distribution[76] = 0.0190095846645367; distribution[77] = 0.0155387743247168; distribution[78] = 0.0174266627940749; distribution[79] = 0.00310775486494336; distribution[80] = 0.00145222189950624; distribution[81] = 0.00158292187046181; distribution[82] = 0.000914899796688934; distribution[83] = 0.000871333139703747; distribution[84] = 0.000740633168748185; distribution[85] = 5.80888759802498E-05; distribution[86] = 2.90444379901249E-05; distribution[87] = 1.45222189950624E-05; distribution[88] = 1.45222189950624E-05; distribution[89] = 1.45222189950624E-05; distribution[90] = 1.45222189950624E-05; distribution[91] = 1.45222189950624E-05; distribution[92] = 1.45222189950624E-05; distribution[93] = 1.45222189950624E-05; distribution[94] = 1.45222189950624E-05; distribution[95] = 1.45222189950624E-05; distribution[96] = 1.45222189950624E-05; distribution[97] = 1.45222189950624E-05; distribution[98] = 1.45222189950624E-05; distribution[99] = 1.45222189950624E-05; 
        return normalizeDistribution(distribution);
    }
    //UPDATED
    private static double[] fifty(){
        double[] distribution = new double[100];
        for(int i = 0; i < distribution.length; i ++)
            distribution[i] = minimalValue;
        distribution[0] = 0.268254429276793; distribution[1] = 1.45222189950624E-05; distribution[2] = 1.45222189950624E-05; distribution[3] = 1.45222189950624E-05; distribution[4] = 1.45222189950624E-05; distribution[5] = 1.45222189950624E-05; distribution[6] = 1.45222189950624E-05; distribution[7] = 1.45222189950624E-05; distribution[8] = 1.45222189950624E-05; distribution[9] = 1.45222189950624E-05; distribution[10] = 1.45222189950624E-05; distribution[11] = 1.45222189950624E-05; distribution[12] = 1.45222189950624E-05; distribution[13] = 1.45222189950624E-05; distribution[14] = 1.45222189950624E-05; distribution[15] = 1.45222189950624E-05; distribution[16] = 1.45222189950624E-05; distribution[17] = 1.45222189950624E-05; distribution[18] = 1.45222189950624E-05; distribution[19] = 1.45222189950624E-05; distribution[20] = 1.45222189950624E-05; distribution[21] = 1.45222189950624E-05; distribution[22] = 1.45222189950624E-05; distribution[23] = 1.45222189950624E-05; distribution[24] = 1.45222189950624E-05; distribution[25] = 1.45222189950624E-05; distribution[26] = 1.45222189950624E-05; distribution[27] = 1.45222189950624E-05; distribution[28] = 1.45222189950624E-05; distribution[29] = 1.45222189950624E-05; distribution[30] = 1.45222189950624E-05; distribution[31] = 1.45222189950624E-05; distribution[32] = 1.45222189950624E-05; distribution[33] = 1.45222189950624E-05; distribution[34] = 1.45222189950624E-05; distribution[35] = 1.45222189950624E-05; distribution[36] = 1.45222189950624E-05; distribution[37] = 1.45222189950624E-05; distribution[38] = 1.45222189950624E-05; distribution[39] = 1.45222189950624E-05; distribution[40] = 1.45222189950624E-05; distribution[41] = 1.45222189950624E-05; distribution[42] = 1.45222189950624E-05; distribution[43] = 1.45222189950624E-05; distribution[44] = 1.45222189950624E-05; distribution[45] = 1.45222189950624E-05; distribution[46] = 1.45222189950624E-05; distribution[47] = 2.90444379901249E-05; distribution[48] = 2.90444379901249E-05; distribution[49] = 0.000232355503920999; distribution[50] = 0.000435666569851873; distribution[51] = 0.00418239907057798; distribution[52] = 0.00483589892535579; distribution[53] = 0.000682544292767935; distribution[54] = 0.000972988672669184; distribution[55] = 0.00326749927388905; distribution[56] = 0.00213476619227418; distribution[57] = 0.00287539936102236; distribution[58] = 0.00242521057217543; distribution[59] = 0.00826314260819053; distribution[60] = 0.0171507406331687; distribution[61] = 0.0222480395004357; distribution[62] = 0.0281876270694162; distribution[63] = 0.0459918675573628; distribution[64] = 0.041635201858844; distribution[65] = 0.0697502178332849; distribution[66] = 0.0952221899506245; distribution[67] = 0.0461080453093233; distribution[68] = 0.0634620970084229; distribution[69] = 0.09185303514377; distribution[70] = 0.0266482718559396; distribution[71] = 0.0338803369154807; distribution[72] = 0.056055765320941; distribution[73] = 0.0177171071739762; distribution[74] = 0.0158001742666279; distribution[75] = 0.0190095846645367; distribution[76] = 0.00530060993319779; distribution[77] = 0.0025123438861458; distribution[78] = 0.00168457740342724; distribution[79] = 0.000188788846935812; distribution[80] = 2.90444379901249E-05; distribution[81] = 1.45222189950624E-05; distribution[82] = 1.45222189950624E-05; distribution[83] = 1.45222189950624E-05; distribution[84] = 1.45222189950624E-05; distribution[85] = 1.45222189950624E-05; distribution[86] = 1.45222189950624E-05; distribution[87] = 1.45222189950624E-05; distribution[88] = 1.45222189950624E-05; distribution[89] = 1.45222189950624E-05; distribution[90] = 1.45222189950624E-05; distribution[91] = 1.45222189950624E-05; distribution[92] = 1.45222189950624E-05; distribution[93] = 1.45222189950624E-05; distribution[94] = 1.45222189950624E-05; distribution[95] = 1.45222189950624E-05; distribution[96] = 1.45222189950624E-05; distribution[97] = 1.45222189950624E-05; distribution[98] = 1.45222189950624E-05; distribution[99] = 1.45222189950624E-05; 
        return normalizeDistribution(distribution);
    }
    //UPDATED
    private static double[] hundredTwentySix(){
        double[] distribution = new double[100];
        for(int i = 0; i < distribution.length; i ++)
            distribution[i] = minimalValue;
        distribution[0] = 0.212271275050828; distribution[1] = 1.45222189950624E-05; distribution[2] = 1.45222189950624E-05; distribution[3] = 1.45222189950624E-05; distribution[4] = 1.45222189950624E-05; distribution[5] = 1.45222189950624E-05; distribution[6] = 1.45222189950624E-05; distribution[7] = 1.45222189950624E-05; distribution[8] = 1.45222189950624E-05; distribution[9] = 1.45222189950624E-05; distribution[10] = 1.45222189950624E-05; distribution[11] = 1.45222189950624E-05; distribution[12] = 1.45222189950624E-05; distribution[13] = 1.45222189950624E-05; distribution[14] = 1.45222189950624E-05; distribution[15] = 1.45222189950624E-05; distribution[16] = 1.45222189950624E-05; distribution[17] = 1.45222189950624E-05; distribution[18] = 1.45222189950624E-05; distribution[19] = 1.45222189950624E-05; distribution[20] = 1.45222189950624E-05; distribution[21] = 1.45222189950624E-05; distribution[22] = 1.45222189950624E-05; distribution[23] = 1.45222189950624E-05; distribution[24] = 1.45222189950624E-05; distribution[25] = 1.45222189950624E-05; distribution[26] = 1.45222189950624E-05; distribution[27] = 1.45222189950624E-05; distribution[28] = 1.45222189950624E-05; distribution[29] = 1.45222189950624E-05; distribution[30] = 1.45222189950624E-05; distribution[31] = 1.45222189950624E-05; distribution[32] = 2.90444379901249E-05; distribution[33] = 5.80888759802498E-05; distribution[34] = 0.000261399941911124; distribution[35] = 0.000406622131861749; distribution[36] = 0.00479233226837061; distribution[37] = 0.0194742956723787; distribution[38] = 0.0167876851582922; distribution[39] = 0.0274905605576532; distribution[40] = 0.0498838222480395; distribution[41] = 0.0406622131861748; distribution[42] = 0.0652483299448156; distribution[43] = 0.0737002613999419; distribution[44] = 0.0531658437409236; distribution[45] = 0.0624019750217833; distribution[46] = 0.0515103107754865; distribution[47] = 0.0372349695033401; distribution[48] = 0.047908800464711; distribution[49] = 0.0397473133894859; distribution[50] = 0.0273017717107174; distribution[51] = 0.0298431600348533; distribution[52] = 0.0153935521347662; distribution[53] = 0.0128957304676155; distribution[54] = 0.0146964856230032; distribution[55] = 0.0125036305547488; distribution[56] = 0.0110514086552425; distribution[57] = 0.00919256462387453; distribution[58] = 0.00701423177461516; distribution[59] = 0.00771129828637816; distribution[60] = 0.00586697647400523; distribution[61] = 0.00210572175428405; distribution[62] = 0.00232355503920999; distribution[63] = 0.00511182108626198; distribution[64] = 0.00310775486494336; distribution[65] = 0.00264304385710137; distribution[66] = 0.00374673250072611; distribution[67] = 0.00148126633749637; distribution[68] = 0.00169909962242231; distribution[69] = 0.00410978797560267; distribution[70] = 0.0030932326459483; distribution[71] = 0.00338367702584955; distribution[72] = 0.00633168748184723; distribution[73] = 0.00165553296543712; distribution[74] = 0.00076967760673831; distribution[75] = 0.000943944234679059; distribution[76] = 0.000130699970955562; distribution[77] = 5.80888759802498E-05; distribution[78] = 4.35666569851873E-05; distribution[79] = 1.45222189950624E-05; distribution[80] = 1.45222189950624E-05; distribution[81] = 1.45222189950624E-05; distribution[82] = 1.45222189950624E-05; distribution[83] = 1.45222189950624E-05; distribution[84] = 1.45222189950624E-05; distribution[85] = 1.45222189950624E-05; distribution[86] = 1.45222189950624E-05; distribution[87] = 1.45222189950624E-05; distribution[88] = 1.45222189950624E-05; distribution[89] = 1.45222189950624E-05; distribution[90] = 1.45222189950624E-05; distribution[91] = 1.45222189950624E-05; distribution[92] = 1.45222189950624E-05; distribution[93] = 1.45222189950624E-05; distribution[94] = 1.45222189950624E-05; distribution[95] = 1.45222189950624E-05; distribution[96] = 1.45222189950624E-05; distribution[97] = 1.45222189950624E-05; distribution[98] = 1.45222189950624E-05; distribution[99] = 1.45222189950624E-05; 
        return normalizeDistribution(distribution);
    }
    
    private static double[] getDistribution(int row){
        double[] distribution = new double[100];
        for(int i = 0; i < distribution.length; i ++)
            distribution[i] = minimalValue;
        switch(round){
            case 16:
                distribution = sixteen();
                break;
            case 65:
                distribution = sixtyFive();
                break;
            case 59:
                distribution = fiftyNine();
                break;
            case 50:
                distribution = fifty();
                break;
            case 126:
                distribution = hundredTwentySix();
                break;
            case 74:
            case 230:
            case 246:
            case 274:
                distribution = dependingOnSixtyFive(row);
                break;
            case 162:
            case 185:
            case 267:
            case 295:
                distribution = dependingOnFiftyNine(row);
                break;
            case 90:
            case 128:
            case 172:
            case 240:
            case 251:
                distribution = basedOnSixteen(row);
                break;
            case 184:
            case 244:
            case 248:
                distribution = dependingOnFifty(row);
                break;
            case 237:
            case 270:
            case 279:
            case 291:
                distribution = dependingOnHundredTwentySix(row);
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
        return normalizeDistribution(distribution);
    }
//HERE
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
                if (round++ == 0) {
                    //don't have context yet
                    context = new int[303][1000];
					
                    //return dist over first column for each vector
                    double[] randomVector = getRandomDist();
                    for (double[] vector : myGuess) {
                        if (Thread.currentThread().isInterrupted()) {
                            throw new InterruptedException();
                        }
                        vector = getDistribution(row);
						row ++;
                    }
                } else {
                    //read context
                    context[round - 2] = getValues(s.nextLine());
                    //now calculate based on context
                    for (int i = 0; i < 1000; i++) {
                        myGuess[i] = getDistribution(row);
						row++;
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