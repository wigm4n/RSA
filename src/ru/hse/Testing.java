package ru.hse;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class Testing {
    static private List<BigIntegerRSA> list1 = Arrays.asList(
            new BigIntegerRSA("99999", true),
            new BigIntegerRSA("22399999900", true),
            new BigIntegerRSA("10000", false),
            new BigIntegerRSA("2399", false),
            new BigIntegerRSA("1111", true),
            new BigIntegerRSA("9", false),
            new BigIntegerRSA("0", true),
            new BigIntegerRSA( "41317", true),
            new BigIntegerRSA("95", true),
            new BigIntegerRSA("111", true)
    );

    static private List<BigIntegerRSA> list2 = Arrays.asList(
            new BigIntegerRSA("77", true),
            new BigIntegerRSA("31231231111", false),
            new BigIntegerRSA("99999", true),
            new BigIntegerRSA("991", false),
            new BigIntegerRSA("9111", false),
            new BigIntegerRSA("71", true),
            new BigIntegerRSA("90909009", true),
            new BigIntegerRSA("99509", true),
            new BigIntegerRSA("111", true),
            new BigIntegerRSA("999", true)
    );

    static private List<BigIntegerRSA> res = Arrays.asList(
            new BigIntegerRSA("100076", true),
            new BigIntegerRSA("8831231211", false),
            new BigIntegerRSA( "89999", true),
            new BigIntegerRSA("3390", false),
            new BigIntegerRSA("8000", false),
            new BigIntegerRSA("62", true),
            new BigIntegerRSA("90909009", true),
            new BigIntegerRSA("140826", true),
            new BigIntegerRSA("206", true),
            new BigIntegerRSA("1110", true)
    );

    static private List<BigIntegerRSA> list12 = Arrays.asList(
            new BigIntegerRSA("99999", true),
            new BigIntegerRSA("22399999900", true),
            new BigIntegerRSA("1000012", false),
            new BigIntegerRSA("2399", false),
            new BigIntegerRSA("1111", true),
            new BigIntegerRSA("9", false),
            new BigIntegerRSA("100", true),
            new BigIntegerRSA("101", true)
    );

    static private List<BigIntegerRSA> list22 = Arrays.asList(
            new BigIntegerRSA("46128", true),
            new BigIntegerRSA("31231231", false),
            new BigIntegerRSA("99999", true),
            new BigIntegerRSA("991", false),
            new BigIntegerRSA("9111", true),
            new BigIntegerRSA("71", false),
            new BigIntegerRSA("1", true),
            new BigIntegerRSA("1", true)
    );

    static private List<BigIntegerRSA> res2 = Arrays.asList(
            new BigIntegerRSA("53871", true),
            new BigIntegerRSA("22431231131", true),
            new BigIntegerRSA( "1100011", false),
            new BigIntegerRSA("1408", false),
            new BigIntegerRSA("8000", false),
            new BigIntegerRSA("62", true),
            new BigIntegerRSA("99", true),
            new BigIntegerRSA("100", true)
    );

    static void runTests() {
        System.out.println("Сложение:");
        for (int i = 0; i < list1.size(); ++i) {
            BigIntegerRSA sum = list1.get(i).sum(list2.get(i));

            System.out.println(String.format("%s + %s = %s => %s", list1.get(i).toString(), list2.get(i).toString(),
                    sum.toString(), sum.equals(res.get(i))));
        }

        System.out.println("\nВычитание:");
        for (int i = 0; i < list12.size(); ++i) {
            BigIntegerRSA sub = list12.get(i).subtraction(list22.get(i));

            System.out.println(String.format("%s - %s = %s => %s", list12.get(i).toString(), list22.get(i).toString(),
                    sub.toString(), sub.equals(res2.get(i))));
        }

        BigInteger s = BigInteger.valueOf(3);
        BigInteger sd = BigInteger.valueOf(3);
        for (int i = 0; i < 500; i++)
            s = s.multiply(sd);

        BigInteger s2 = BigInteger.valueOf(4);
        BigInteger sd2 = BigInteger.valueOf(4);
        for (int i = 0; i < 300; i++)
            s2 = s2.multiply(sd2);



        BigIntegerRSA input1 = new BigIntegerRSA(s.toString(), true);
        BigIntegerRSA input2 = new BigIntegerRSA(s2.toString(), false);

        s2 = s2.multiply(BigInteger.valueOf(-1));
        System.out.println(s.subtract(s2));
        System.out.println(input1.subtraction(input2).toString());


        System.out.println("\nУмножение");
        BigIntegerRSA inputMult = new BigIntegerRSA("60", true);
        BigIntegerRSA inputMult2 = new BigIntegerRSA("3", true);
        System.out.println(inputMult.toString() + " * " + inputMult2.toString() + " = " + inputMult.multiply(inputMult2).toString());

        System.out.println("\nДеление");
        BigIntegerRSA inputDev = new BigIntegerRSA("361", true);
        BigIntegerRSA inputDev2 = new BigIntegerRSA("6", true);
        System.out.println(inputDev.toString() + " / " + inputDev2.toString() + " = " + inputDev.division(inputDev2).toString());

        System.out.println("\nMod");
        BigIntegerRSA inputDev3 = new BigIntegerRSA("123", true);
        BigIntegerRSA inputDev4 = new BigIntegerRSA("6", true);
        System.out.println(inputDev3.toString() + " % " + inputDev4.toString() + " = " + inputDev3.mod(inputDev4).toString());

        System.out.println("\nPowerMod");
        BigIntegerRSA inputDev5 = new BigIntegerRSA("4", true);
        BigIntegerRSA inputDev6 = new BigIntegerRSA("13", true);
        BigIntegerRSA inputDev7 = new BigIntegerRSA("497", true);
        System.out.println("|" + inputDev5.toString() + " ^ " + inputDev6.toString() + "| = " + inputDev5.powerMod(inputDev6, inputDev7).toString());

        System.out.println("\nGCD");
        BigIntegerRSA inputDev8 = new BigIntegerRSA("234", true);
        BigIntegerRSA inputDev9 = new BigIntegerRSA("9813", true);
        System.out.println(inputDev8.toString() + ", " + inputDev9.toString() + ": " + inputDev8.gcd(inputDev9).toString());

    }
}
