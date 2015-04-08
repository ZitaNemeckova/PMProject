/*
 * (c) Johannes Verwijnen, duvin@duvin.org
 * changed by Nidia Acosta Obscura and Zita Nemeckova
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
	private final static double minimalValue = 0.00000000001;

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
            //System.out.println(command);
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
	
    private static int[] getValues(String line) {
        String[] parts = line.split(",");
        int[] result = new int[parts.length];
        for (int i = 0; i<parts.length; i++) {
            result[i] = Integer.parseInt(parts[i]);
        }
        return result;
    }
    /**
     * @param args the command line arguments
     */
     
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
        if(value == 0)
            distribution = zeroColumns();
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
    //DONE
    private static double[] betweenTenAndOneThousand(){
        double[] distribution = new double[100];
        for(int i = 0; i < distribution.length; i ++)
            distribution[i] = minimalValue;
        distribution[0] = 0.943865465438789; distribution[1] = 1.58626799521899E-07; distribution[2] = 1.58626799521899E-07; distribution[3] = 1.58626799521899E-07; distribution[4] = 1.58626799521899E-07; distribution[5] = 1.58626799521899E-07; distribution[6] = 1.58626799521899E-07; distribution[7] = 1.58626799521899E-07; distribution[8] = 1.58626799521899E-07; distribution[9] = 1.58626799521899E-07; distribution[10] = 1.58626799521899E-07; distribution[11] = 1.58626799521899E-07; distribution[12] = 1.58626799521899E-07; distribution[13] = 1.58626799521899E-07; distribution[14] = 1.58626799521899E-07; distribution[15] = 1.58626799521899E-07; distribution[16] = 1.58626799521899E-07; distribution[17] = 1.58626799521899E-07; distribution[18] = 1.58626799521899E-07; distribution[19] = 1.58626799521899E-07; distribution[20] = 1.58626799521899E-07; distribution[21] = 1.58626799521899E-07; distribution[22] = 1.58626799521899E-07; distribution[23] = 1.58626799521899E-07; distribution[24] = 1.58626799521899E-07; distribution[25] = 1.58626799521899E-07; distribution[26] = 1.58626799521899E-07; distribution[27] = 1.58626799521899E-07; distribution[28] = 1.58626799521899E-07; distribution[29] = 1.58626799521899E-07; distribution[30] = 1.58626799521899E-07; distribution[31] = 1.58626799521899E-07; distribution[32] = 3.17253599043798E-07; distribution[33] = 4.75880398565696E-07; distribution[34] = 5.07605758470076E-06; distribution[35] = 4.96501882503543E-05; distribution[36] = 0.000220649878134961; distribution[37] = 0.000677812314357074; distribution[38] = 0.000370869457282199; distribution[39] = 0.000289493909127465; distribution[40] = 0.000428609612308171; distribution[41] = 0.000271886334380535; distribution[42] = 0.000320584761833758; distribution[43] = 0.00013530865999218; distribution[44] = 0.000125156544822778; distribution[45] = 0.000192731561419107; distribution[46] = 0.000358496566919491; distribution[47] = 0.000384669988840605; distribution[48] = 0.000299487397497345; distribution[49] = 0.000325343565819415; distribution[50] = 0.000480480575751832; distribution[51] = 0.000814865869143994; distribution[52] = 0.000603416345381303; distribution[53] = 0.000462714374205379; distribution[54] = 0.000471756101778127; distribution[55] = 0.000358813820518535; distribution[56] = 0.000184007087445403; distribution[57] = 0.000253168372036951; distribution[58] = 0.000243809390865158; distribution[59] = 0.00038514586923917; distribution[60] = 0.00102028757452485; distribution[61] = 0.000664804916796278; distribution[62] = 0.00127250418576467; distribution[63] = 0.00305943508237886; distribution[64] = 0.00159086817240512; distribution[65] = 0.0023426005753394; distribution[66] = 0.00499039911295894; distribution[67] = 0.00192731561419107; distribution[68] = 0.0025946585597797; distribution[69] = 0.0056406103641992; distribution[70] = 0.00216414542587727; distribution[71] = 0.00241636203711709; distribution[72] = 0.00428990316627023; distribution[73] = 0.00144493151684498; distribution[74] = 0.00160990338834775; distribution[75] = 0.00351707339899954; distribution[76] = 0.00157595725325006; distribution[77] = 0.00150822360985421; distribution[78] = 0.00194460593533896; distribution[79] = 0.000526799601212226; distribution[80] = 0.000395932491606659; distribution[81] = 0.000527434108410314; distribution[82] = 0.000140860597975446; distribution[83] = 0.000106755836078238; distribution[84] = 0.000119287353240468; distribution[85] = 1.61799335512337E-05; distribution[86] = 3.96566998804747E-06; distribution[87] = 1.11038759665329E-06; distribution[88] = 1.58626799521899E-07; distribution[89] = 1.58626799521899E-07; distribution[90] = 1.58626799521899E-07; distribution[91] = 1.58626799521899E-07; distribution[92] = 1.58626799521899E-07; distribution[93] = 1.58626799521899E-07; distribution[94] = 1.58626799521899E-07; distribution[95] = 1.58626799521899E-07; distribution[96] = 1.58626799521899E-07; distribution[97] = 1.58626799521899E-07; distribution[98] = 1.58626799521899E-07; distribution[99] = 1.58626799521899E-07;         
        return normalizeDistribution(distribution);
       
    }
    //DONE
    private static double[] zeroColumns(){
        double[] distribution = new double[100];
        for(int i = 0; i < distribution.length; i ++)
            distribution[i] = minimalValue;
        distribution[0] = 1 - 99*minimalValue;
        return normalizeDistribution(distribution);
    }
    private static double[] sixteen(){
        double[] distribution = new double[100];
        for(int i = 0; i < distribution.length; i ++)
            distribution[i] = minimalValue;
        distribution[0] = 0.271444354422921; distribution[1] = 1.47307947263755E-05; distribution[2] = 1.47307947263755E-05; distribution[3] = 1.47307947263755E-05; distribution[4] = 1.47307947263755E-05; distribution[5] = 1.47307947263755E-05; distribution[6] = 1.47307947263755E-05; distribution[7] = 1.47307947263755E-05; distribution[8] = 1.47307947263755E-05; distribution[9] = 1.47307947263755E-05; distribution[10] = 1.47307947263755E-05; distribution[11] = 1.47307947263755E-05; distribution[12] = 1.47307947263755E-05; distribution[13] = 1.47307947263755E-05; distribution[14] = 1.47307947263755E-05; distribution[15] = 1.47307947263755E-05; distribution[16] = 1.47307947263755E-05; distribution[17] = 1.47307947263755E-05; distribution[18] = 1.47307947263755E-05; distribution[19] = 1.47307947263755E-05; distribution[20] = 1.47307947263755E-05; distribution[21] = 1.47307947263755E-05; distribution[22] = 1.47307947263755E-05; distribution[23] = 1.47307947263755E-05; distribution[24] = 1.47307947263755E-05; distribution[25] = 1.47307947263755E-05; distribution[26] = 1.47307947263755E-05; distribution[27] = 1.47307947263755E-05; distribution[28] = 1.47307947263755E-05; distribution[29] = 1.47307947263755E-05; distribution[30] = 1.47307947263755E-05; distribution[31] = 1.47307947263755E-05; distribution[32] = 1.47307947263755E-05; distribution[33] = 1.47307947263755E-05; distribution[34] = 1.47307947263755E-05; distribution[35] = 1.47307947263755E-05; distribution[36] = 1.47307947263755E-05; distribution[37] = 1.47307947263755E-05; distribution[38] = 1.47307947263755E-05; distribution[39] = 1.47307947263755E-05; distribution[40] = 1.47307947263755E-05; distribution[41] = 1.47307947263755E-05; distribution[42] = 1.47307947263755E-05; distribution[43] = 1.47307947263755E-05; distribution[44] = 1.47307947263755E-05; distribution[45] = 1.47307947263755E-05; distribution[46] = 2.9461589452751E-05; distribution[47] = 1.47307947263755E-05; distribution[48] = 4.41923841791265E-05; distribution[49] = 2.9461589452751E-05; distribution[50] = 4.41923841791265E-05; distribution[51] = 0.000132577152537379; distribution[52] = 0.00075127053104515; distribution[53] = 0.000824924504677027; distribution[54] = 0.00307873609781248; distribution[55] = 0.00736539736318774; distribution[56] = 0.00248950430875746; distribution[57] = 0.00278412020328497; distribution[58] = 0.00385946821831038; distribution[59] = 0.00863224570965604; distribution[60] = 0.0147307947263755; distribution[61] = 0.0138174854533402; distribution[62] = 0.0289165500478751; distribution[63] = 0.0530161302202254; distribution[64] = 0.0367827944317596; distribution[65] = 0.0559180967813214; distribution[66] = 0.0800029461589453; distribution[67] = 0.0391102599985269; distribution[68] = 0.0690285040877955; distribution[69] = 0.10420564189438; distribution[70] = 0.0377108344995212; distribution[71] = 0.0465051189511674; distribution[72] = 0.0659497679899831; distribution[73] = 0.0157472195624954; distribution[74] = 0.0138764086322457; distribution[75] = 0.015968181483391; distribution[76] = 0.00296088974000147; distribution[77] = 0.00176769536716506; distribution[78] = 0.00111954039920454; distribution[79] = 0.000117846357811004; distribution[80] = 0.000176769536716506; distribution[81] = 0.000117846357811004; distribution[82] = 1.47307947263755E-05; distribution[83] = 1.47307947263755E-05; distribution[84] = 1.47307947263755E-05; distribution[85] = 1.47307947263755E-05; distribution[86] = 1.47307947263755E-05; distribution[87] = 1.47307947263755E-05; distribution[88] = 1.47307947263755E-05; distribution[89] = 1.47307947263755E-05; distribution[90] = 1.47307947263755E-05; distribution[91] = 1.47307947263755E-05; distribution[92] = 1.47307947263755E-05; distribution[93] = 1.47307947263755E-05; distribution[94] = 1.47307947263755E-05; distribution[95] = 1.47307947263755E-05; distribution[96] = 1.47307947263755E-05; distribution[97] = 1.47307947263755E-05; distribution[98] = 1.47307947263755E-05; distribution[99] = 1.47307947263755E-05; 
        return normalizeDistribution(distribution);
    }
    private static double[] sixtyFive(){
        double[] distribution = new double[100];
        for(int i = 0; i < distribution.length; i ++)
            distribution[i] = minimalValue;
        distribution[0] = 0.63962583781395; distribution[1] = 1.47307947263755E-05; distribution[2] = 1.47307947263755E-05; distribution[3] = 1.47307947263755E-05; distribution[4] = 1.47307947263755E-05; distribution[5] = 1.47307947263755E-05; distribution[6] = 1.47307947263755E-05; distribution[7] = 1.47307947263755E-05; distribution[8] = 1.47307947263755E-05; distribution[9] = 1.47307947263755E-05; distribution[10] = 1.47307947263755E-05; distribution[11] = 1.47307947263755E-05; distribution[12] = 1.47307947263755E-05; distribution[13] = 1.47307947263755E-05; distribution[14] = 1.47307947263755E-05; distribution[15] = 1.47307947263755E-05; distribution[16] = 1.47307947263755E-05; distribution[17] = 1.47307947263755E-05; distribution[18] = 1.47307947263755E-05; distribution[19] = 1.47307947263755E-05; distribution[20] = 1.47307947263755E-05; distribution[21] = 1.47307947263755E-05; distribution[22] = 1.47307947263755E-05; distribution[23] = 1.47307947263755E-05; distribution[24] = 1.47307947263755E-05; distribution[25] = 1.47307947263755E-05; distribution[26] = 1.47307947263755E-05; distribution[27] = 1.47307947263755E-05; distribution[28] = 1.47307947263755E-05; distribution[29] = 1.47307947263755E-05; distribution[30] = 1.47307947263755E-05; distribution[31] = 1.47307947263755E-05; distribution[32] = 1.47307947263755E-05; distribution[33] = 1.47307947263755E-05; distribution[34] = 2.9461589452751E-05; distribution[35] = 2.9461589452751E-05; distribution[36] = 0.000220961920895632; distribution[37] = 0.00315239007144435; distribution[38] = 0.00334389040288724; distribution[39] = 0.00519997053841055; distribution[40] = 0.00599543345363482; distribution[41] = 0.0044487000073654; distribution[42] = 0.0060101642483612; distribution[43] = 0.00206231126169257; distribution[44] = 0.00131104073064742; distribution[45] = 0.00101642483611991; distribution[46] = 0.000928040067761656; distribution[47] = 0.000869116888856154; distribution[48] = 0.00175296457243868; distribution[49] = 0.00170877218825956; distribution[50] = 0.00075127053104515; distribution[51] = 0.000972232451940782; distribution[52] = 0.00104588642557266; distribution[53] = 0.000692347352139648; distribution[54] = 0.000869116888856154; distribution[55] = 0.000427193047064889; distribution[56] = 0.000662885762686897; distribution[57] = 0.00187081093024969; distribution[58] = 0.00131104073064742; distribution[59] = 0.00166457980408043; distribution[60] = 0.00195919569860794; distribution[61] = 0.000869116888856154; distribution[62] = 0.00151727185681668; distribution[63] = 0.00259261987184209; distribution[64] = 0.00119319437283641; distribution[65] = 0.00297562053472785; distribution[66] = 0.00964867054577595; distribution[67] = 0.00472858510716653; distribution[68] = 0.00718862782647124; distribution[69] = 0.0147013331369227; distribution[70] = 0.0102673639242837; distribution[71] = 0.0169846063195109; distribution[72] = 0.0385062974147455; distribution[73] = 0.0176769536716506; distribution[74] = 0.0242910805037932; distribution[75] = 0.0597922957943581; distribution[76] = 0.0245267732194152; distribution[77] = 0.0240995801723503; distribution[78] = 0.0322899020402151; distribution[79] = 0.00720335862119761; distribution[80] = 0.00446343080209177; distribution[81] = 0.00340281358179274; distribution[82] = 0.000324077483980261; distribution[83] = 0.000103115563084628; distribution[84] = 1.47307947263755E-05; distribution[85] = 1.47307947263755E-05; distribution[86] = 1.47307947263755E-05; distribution[87] = 1.47307947263755E-05; distribution[88] = 1.47307947263755E-05; distribution[89] = 1.47307947263755E-05; distribution[90] = 1.47307947263755E-05; distribution[91] = 1.47307947263755E-05; distribution[92] = 1.47307947263755E-05; distribution[93] = 1.47307947263755E-05; distribution[94] = 1.47307947263755E-05; distribution[95] = 1.47307947263755E-05; distribution[96] = 1.47307947263755E-05; distribution[97] = 1.47307947263755E-05; distribution[98] = 1.47307947263755E-05; distribution[99] = 1.47307947263755E-05;         
        return normalizeDistribution(distribution);
    }
    private static double[] fiftyNine(){
        double[] distribution = new double[100];
        for(int i = 0; i < distribution.length; i ++)
            distribution[i] = minimalValue;
        distribution[0] = 0.63962583781395; distribution[1] = 1.47307947263755E-05; distribution[2] = 1.47307947263755E-05; distribution[3] = 1.47307947263755E-05; distribution[4] = 1.47307947263755E-05; distribution[5] = 1.47307947263755E-05; distribution[6] = 1.47307947263755E-05; distribution[7] = 1.47307947263755E-05; distribution[8] = 1.47307947263755E-05; distribution[9] = 1.47307947263755E-05; distribution[10] = 1.47307947263755E-05; distribution[11] = 1.47307947263755E-05; distribution[12] = 1.47307947263755E-05; distribution[13] = 1.47307947263755E-05; distribution[14] = 1.47307947263755E-05; distribution[15] = 1.47307947263755E-05; distribution[16] = 1.47307947263755E-05; distribution[17] = 1.47307947263755E-05; distribution[18] = 1.47307947263755E-05; distribution[19] = 1.47307947263755E-05; distribution[20] = 1.47307947263755E-05; distribution[21] = 1.47307947263755E-05; distribution[22] = 1.47307947263755E-05; distribution[23] = 1.47307947263755E-05; distribution[24] = 1.47307947263755E-05; distribution[25] = 1.47307947263755E-05; distribution[26] = 1.47307947263755E-05; distribution[27] = 1.47307947263755E-05; distribution[28] = 1.47307947263755E-05; distribution[29] = 1.47307947263755E-05; distribution[30] = 1.47307947263755E-05; distribution[31] = 1.47307947263755E-05; distribution[32] = 1.47307947263755E-05; distribution[33] = 1.47307947263755E-05; distribution[34] = 2.9461589452751E-05; distribution[35] = 2.9461589452751E-05; distribution[36] = 0.000220961920895632; distribution[37] = 0.00315239007144435; distribution[38] = 0.00334389040288724; distribution[39] = 0.00519997053841055; distribution[40] = 0.00599543345363482; distribution[41] = 0.0044487000073654; distribution[42] = 0.0060101642483612; distribution[43] = 0.00206231126169257; distribution[44] = 0.00131104073064742; distribution[45] = 0.00101642483611991; distribution[46] = 0.000928040067761656; distribution[47] = 0.000869116888856154; distribution[48] = 0.00175296457243868; distribution[49] = 0.00170877218825956; distribution[50] = 0.00075127053104515; distribution[51] = 0.000972232451940782; distribution[52] = 0.00104588642557266; distribution[53] = 0.000692347352139648; distribution[54] = 0.000869116888856154; distribution[55] = 0.000427193047064889; distribution[56] = 0.000662885762686897; distribution[57] = 0.00187081093024969; distribution[58] = 0.00131104073064742; distribution[59] = 0.00166457980408043; distribution[60] = 0.00195919569860794; distribution[61] = 0.000869116888856154; distribution[62] = 0.00151727185681668; distribution[63] = 0.00259261987184209; distribution[64] = 0.00119319437283641; distribution[65] = 0.00297562053472785; distribution[66] = 0.00964867054577595; distribution[67] = 0.00472858510716653; distribution[68] = 0.00718862782647124; distribution[69] = 0.0147013331369227; distribution[70] = 0.0102673639242837; distribution[71] = 0.0169846063195109; distribution[72] = 0.0385062974147455; distribution[73] = 0.0176769536716506; distribution[74] = 0.0242910805037932; distribution[75] = 0.0597922957943581; distribution[76] = 0.0245267732194152; distribution[77] = 0.0240995801723503; distribution[78] = 0.0322899020402151; distribution[79] = 0.00720335862119761; distribution[80] = 0.00446343080209177; distribution[81] = 0.00340281358179274; distribution[82] = 0.000324077483980261; distribution[83] = 0.000103115563084628; distribution[84] = 1.47307947263755E-05; distribution[85] = 1.47307947263755E-05; distribution[86] = 1.47307947263755E-05; distribution[87] = 1.47307947263755E-05; distribution[88] = 1.47307947263755E-05; distribution[89] = 1.47307947263755E-05; distribution[90] = 1.47307947263755E-05; distribution[91] = 1.47307947263755E-05; distribution[92] = 1.47307947263755E-05; distribution[93] = 1.47307947263755E-05; distribution[94] = 1.47307947263755E-05; distribution[95] = 1.47307947263755E-05; distribution[96] = 1.47307947263755E-05; distribution[97] = 1.47307947263755E-05; distribution[98] = 1.47307947263755E-05; distribution[99] = 1.47307947263755E-05; 
        return normalizeDistribution(distribution);
    }
    private static double[] fifty(){
        double[] distribution = new double[100];
        for(int i = 0; i < distribution.length; i ++)
            distribution[i] = minimalValue;
        distribution[0] = 0.263209840170877; distribution[1] = 1.47307947263755E-05; distribution[2] = 1.47307947263755E-05; distribution[3] = 1.47307947263755E-05; distribution[4] = 1.47307947263755E-05; distribution[5] = 1.47307947263755E-05; distribution[6] = 1.47307947263755E-05; distribution[7] = 1.47307947263755E-05; distribution[8] = 1.47307947263755E-05; distribution[9] = 1.47307947263755E-05; distribution[10] = 1.47307947263755E-05; distribution[11] = 1.47307947263755E-05; distribution[12] = 1.47307947263755E-05; distribution[13] = 1.47307947263755E-05; distribution[14] = 1.47307947263755E-05; distribution[15] = 1.47307947263755E-05; distribution[16] = 1.47307947263755E-05; distribution[17] = 1.47307947263755E-05; distribution[18] = 1.47307947263755E-05; distribution[19] = 1.47307947263755E-05; distribution[20] = 1.47307947263755E-05; distribution[21] = 1.47307947263755E-05; distribution[22] = 1.47307947263755E-05; distribution[23] = 1.47307947263755E-05; distribution[24] = 1.47307947263755E-05; distribution[25] = 1.47307947263755E-05; distribution[26] = 1.47307947263755E-05; distribution[27] = 1.47307947263755E-05; distribution[28] = 1.47307947263755E-05; distribution[29] = 1.47307947263755E-05; distribution[30] = 1.47307947263755E-05; distribution[31] = 1.47307947263755E-05; distribution[32] = 1.47307947263755E-05; distribution[33] = 1.47307947263755E-05; distribution[34] = 1.47307947263755E-05; distribution[35] = 1.47307947263755E-05; distribution[36] = 1.47307947263755E-05; distribution[37] = 1.47307947263755E-05; distribution[38] = 1.47307947263755E-05; distribution[39] = 1.47307947263755E-05; distribution[40] = 1.47307947263755E-05; distribution[41] = 1.47307947263755E-05; distribution[42] = 1.47307947263755E-05; distribution[43] = 1.47307947263755E-05; distribution[44] = 1.47307947263755E-05; distribution[45] = 1.47307947263755E-05; distribution[46] = 2.9461589452751E-05; distribution[47] = 2.9461589452751E-05; distribution[48] = 2.9461589452751E-05; distribution[49] = 4.41923841791265E-05; distribution[50] = 7.36539736318774E-05; distribution[51] = 0.000250423510348383; distribution[52] = 0.000854386094129778; distribution[53] = 0.00134050232010017; distribution[54] = 0.0037858142446785; distribution[55] = 0.00838182219930765; distribution[56] = 0.00253369669293658; distribution[57] = 0.00316712086617073; distribution[58] = 0.00508212418059954; distribution[59] = 0.00960447816159682; distribution[60] = 0.0159239890992119; distribution[61] = 0.014554025189659; distribution[62] = 0.03052220667305; distribution[63] = 0.0539883626721662; distribution[64] = 0.0360757162848936; distribution[65] = 0.059394564336746; distribution[66] = 0.0775429034396406; distribution[67] = 0.0431759593430066; distribution[68] = 0.0689990424983428; distribution[69] = 0.103601679310599; distribution[70] = 0.0383000662885763; distribution[71] = 0.0458127715990278; distribution[72] = 0.0640494954702806; distribution[73] = 0.0148781026736392; distribution[74] = 0.0129336377697577; distribution[75] = 0.0157472195624954; distribution[76] = 0.00235692715622008; distribution[77] = 0.00151727185681668; distribution[78] = 0.000957501657214407; distribution[79] = 0.000132577152537379; distribution[80] = 0.000117846357811004; distribution[81] = 7.36539736318774E-05; distribution[82] = 1.47307947263755E-05; distribution[83] = 1.47307947263755E-05; distribution[84] = 1.47307947263755E-05; distribution[85] = 1.47307947263755E-05; distribution[86] = 1.47307947263755E-05; distribution[87] = 1.47307947263755E-05; distribution[88] = 1.47307947263755E-05; distribution[89] = 1.47307947263755E-05; distribution[90] = 1.47307947263755E-05; distribution[91] = 1.47307947263755E-05; distribution[92] = 1.47307947263755E-05; distribution[93] = 1.47307947263755E-05; distribution[94] = 1.47307947263755E-05; distribution[95] = 1.47307947263755E-05; distribution[96] = 1.47307947263755E-05; distribution[97] = 1.47307947263755E-05; distribution[98] = 1.47307947263755E-05; distribution[99] = 1.47307947263755E-05; 
        return normalizeDistribution(distribution);
    }
    private static double[] hundredTwentySix(){
        double[] distribution = new double[100];
        for(int i = 0; i < distribution.length; i ++)
            distribution[i] = minimalValue;
        distribution[0] = 0.211372173528762; distribution[1] = 1.47307947263755E-05; distribution[2] = 1.47307947263755E-05; distribution[3] = 1.47307947263755E-05; distribution[4] = 1.47307947263755E-05; distribution[5] = 1.47307947263755E-05; distribution[6] = 1.47307947263755E-05; distribution[7] = 1.47307947263755E-05; distribution[8] = 1.47307947263755E-05; distribution[9] = 1.47307947263755E-05; distribution[10] = 1.47307947263755E-05; distribution[11] = 1.47307947263755E-05; distribution[12] = 1.47307947263755E-05; distribution[13] = 1.47307947263755E-05; distribution[14] = 1.47307947263755E-05; distribution[15] = 1.47307947263755E-05; distribution[16] = 1.47307947263755E-05; distribution[17] = 1.47307947263755E-05; distribution[18] = 1.47307947263755E-05; distribution[19] = 1.47307947263755E-05; distribution[20] = 1.47307947263755E-05; distribution[21] = 1.47307947263755E-05; distribution[22] = 1.47307947263755E-05; distribution[23] = 1.47307947263755E-05; distribution[24] = 1.47307947263755E-05; distribution[25] = 1.47307947263755E-05; distribution[26] = 1.47307947263755E-05; distribution[27] = 1.47307947263755E-05; distribution[28] = 1.47307947263755E-05; distribution[29] = 1.47307947263755E-05; distribution[30] = 2.9461589452751E-05; distribution[31] = 0.000117846357811004; distribution[32] = 7.36539736318774E-05; distribution[33] = 0.000545039404875893; distribution[34] = 0.00114900198865729; distribution[35] = 0.000957501657214407; distribution[36] = 0.00502320100169404; distribution[37] = 0.0280474331590189; distribution[38] = 0.0209324593061796; distribution[39] = 0.0262944685865802; distribution[40] = 0.0412020328496722; distribution[41] = 0.0381969507254916; distribution[42] = 0.0522206673050011; distribution[43] = 0.0487441997495765; distribution[44] = 0.0404949547028062; distribution[45] = 0.0564778669809236; distribution[46] = 0.056728290491272; distribution[47] = 0.049510201075348; distribution[48] = 0.0573911762539589; distribution[49] = 0.046608234514252; distribution[50] = 0.0335862119761361; distribution[51] = 0.0350887530382264; distribution[52] = 0.0213154599690653; distribution[53] = 0.0184282242026957; distribution[54] = 0.0199160344700597; distribution[55] = 0.012241290417618; distribution[56] = 0.00945717021433306; distribution[57] = 0.00953082418796494; distribution[58] = 0.00500847020696767; distribution[59] = 0.00502320100169404; distribution[60] = 0.00587758709582382; distribution[61] = 0.00260735066656846; distribution[62] = 0.00441923841791265; distribution[63] = 0.00774839802607351; distribution[64] = 0.00176769536716506; distribution[65] = 0.0016203874199013; distribution[66] = 0.00275465861383222; distribution[67] = 0.00181188775134419; distribution[68] = 0.00213596523532445; distribution[69] = 0.00371216027104662; distribution[70] = 0.00131104073064742; distribution[71] = 0.0014436178831848; distribution[72] = 0.00486116225970391; distribution[73] = 0.00226854238786183; distribution[74] = 0.00147307947263755; distribution[75] = 0.0014436178831848; distribution[76] = 0.000220961920895632; distribution[77] = 5.89231789055019E-05; distribution[78] = 1.47307947263755E-05; distribution[79] = 1.47307947263755E-05; distribution[80] = 1.47307947263755E-05; distribution[81] = 1.47307947263755E-05; distribution[82] = 1.47307947263755E-05; distribution[83] = 1.47307947263755E-05; distribution[84] = 1.47307947263755E-05; distribution[85] = 1.47307947263755E-05; distribution[86] = 1.47307947263755E-05; distribution[87] = 1.47307947263755E-05; distribution[88] = 1.47307947263755E-05; distribution[89] = 1.47307947263755E-05; distribution[90] = 1.47307947263755E-05; distribution[91] = 1.47307947263755E-05; distribution[92] = 1.47307947263755E-05; distribution[93] = 1.47307947263755E-05; distribution[94] = 1.47307947263755E-05; distribution[95] = 1.47307947263755E-05; distribution[96] = 1.47307947263755E-05; distribution[97] = 1.47307947263755E-05; distribution[98] = 1.47307947263755E-05; distribution[99] = 1.47307947263755E-05; 
        return normalizeDistribution(distribution);
    }
//    private static double[] getDistribution(double probabilityOfZero, double probabilityOfMostFrequent ,
//    double probabilityOfMax, double probabilityOfMin,int mostFrequent, int minimum, int maximum){

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
				//info about row
				int row = 0;
                if (round++ == 0) {
                    //don't have context yet
                    context = new int[303][1000];
                    //return dist over first column for each vector
                    for (double[] vector : myGuess) {
                        if (Thread.currentThread().isInterrupted()) {
                            throw new InterruptedException();
                        }
                        vector = getDistribution(row);
						row ++;
                    }
                } else {
                    //read context
                    context[round-2] = getValues(s.nextLine());
                    //now calculate based on context
                    for (int i = 0; i<1000; i++) {
                        myGuess[i] = getDistribution(row);
						
						row ++;
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
