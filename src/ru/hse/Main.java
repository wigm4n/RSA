package ru.hse;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    private static final int N = 64;
    private static final BigIntegerRSA ZERO = new BigIntegerRSA("0");
    private static final BigIntegerRSA ONE = new BigIntegerRSA("1");
    private static final BigIntegerRSA TWO = new BigIntegerRSA("2");


    public static void main(String[] args) {

        String input = "11987";

        BigIntegerRSA randBigInteger;
        BigIntegerRSA p = new BigIntegerRSA();
        BigIntegerRSA q = new BigIntegerRSA();


        while (p.size() == 0) {
            randBigInteger = rndBigIntegerRSA(N);
            randBigInteger = makeOdd(randBigInteger);
            if (isPrimeCheck256(randBigInteger))
                if (isPrimeRabinMiller(randBigInteger, getRounds(randBigInteger)))
                    p = randBigInteger;
        }

        while (q.size() == 0) {
            randBigInteger = rndBigIntegerRSA(N);
            if (isPrimeCheck256(randBigInteger))
                if (isPrimeRabinMiller(randBigInteger, getRounds(randBigInteger)))
                    q = randBigInteger;
        }

        System.out.println("Генерируем ключи длиной " + N + "-бит");
        System.out.println("\np: " + p.toString() + "\nq: " + q.toString());

        BigIntegerRSA n = p.multiply(q);
        //euler
        BigIntegerRSA fi = new BigIntegerRSA(p.subtraction(ONE).multiply(q.subtraction(ONE)).toString());
        //exp
        BigIntegerRSA e = rndBigIntegerRSA(N);

        while ((e.isLargerThan(fi) || e.equals(fi)) || !e.gcd(fi).equals(ONE)) {
            e = rndBigIntegerRSA(N);
        }

        System.out.println("fi: " + fi.toString() + "\ne: " + e.toString());

        BigIntegerRSA secretExp = gcdWide(new BigIntegerRSA(String.valueOf(e)), new BigIntegerRSA(String.valueOf(fi))).x;
        secretExp = (secretExp.mod(fi).sum(fi)).mod(fi);

        System.out.println("d: " + secretExp.toString());
        System.out.println("\nОткрытый ключ: {" + e + ", " + n.toString() + "}");
        System.out.println("Закрытый ключ: {" + secretExp + ", " + n.toString() + "}");
        System.out.println("\nКодируем сообщение: " + input);

        BigIntegerRSA encoded = new BigIntegerRSA(input).powerMod(e, n);
        System.out.println("\nЗашифрованное сообщение: " + encoded.toString());

        //Расшифрование
        System.out.println("\nПроцесс раскодировки...");
        BigIntegerRSA decoded = encoded.powerMod(secretExp, n);

        System.out.println("\nРезультат: " + decoded.toString());
    }

    private static BigIntegerRSA makeOdd(BigIntegerRSA number) {
        if (number.mod(TWO).equals(ZERO))
            number = number.sum(ONE);
        return number;
    }

    private static boolean isPrimeCheck256(BigIntegerRSA number) {
        if (number.mod(new BigIntegerRSA(String.valueOf(2))).equals(ZERO))
            return false;
        for (int i = 3; i < 256; i += 2) {
            if (number.mod(new BigIntegerRSA(String.valueOf(i))).equals(ZERO))
                return false;
        }
        return true;
    }

    public static BigIntegerRSA rndBigIntegerRSA(int dimension) {
        Random randomNum = new Random();
        int defDimension = 8;
        StringBuilder accumulatedNumber = new StringBuilder();
        for (int i=0; i < dimension / defDimension; ++i) {
            for (int j = 0; j < defDimension; ++j) {
                accumulatedNumber.append(String.valueOf(1 + randomNum.nextInt(9)));
            }
        }
       return new BigIntegerRSA(accumulatedNumber.toString());
    }


    private static long getRounds(BigIntegerRSA n) {
        int res = 0;
        while (!n.division(TWO).equals(ZERO)) {
            n = n.division(TWO);
            res++;
        }
        return res;
    }

    private static boolean isPrimeRabinMiller(BigIntegerRSA number, long rounds) {
        BigIntegerRSA prev = number.subtraction(new BigIntegerRSA("1"));
        List<BigIntegerRSA> params = findRabinMillerParam(prev);
        //s, t

        bf:
        for (int i = 0; i < rounds; ++i) {
            BigIntegerRSA curRand = rndBigIntegerRSA(N);
            BigIntegerRSA modRes = curRand.powerMod(params.get(1), number);
            if (modRes.equals(ONE) || modRes.equals(prev)) {
                continue;
            }

            //s-1
            BigIntegerRSA cond = new BigIntegerRSA("1");
            while (!cond.isLargerThan(params.get(0).subtraction(ONE)) || cond.equals(params.get(0).subtraction(ONE))) {
                cond = cond.sum(ONE);
                modRes = modRes.powerMod(TWO, number);
                if (modRes.equals(ONE)) {
                    return false;
                }

                if (modRes.equals(prev))
                    continue bf;
            }
            return false;
        }
        return true;

    }

    private static List<BigIntegerRSA> findRabinMillerParam(BigIntegerRSA n) {
        BigIntegerRSA s = new BigIntegerRSA("0");
        BigIntegerRSA temp = n;

        while (temp.mod(TWO).equals(ZERO)) {
            temp = temp.division(TWO);
            s = s.sum(ONE);
        }

        List<BigIntegerRSA> result = new ArrayList<>();
        result.add(s);
        result.add(temp);
        return result;
    }

    static class gcdWideParams {
        //ax+by=gcd(a+b) || (d*e)=1 mod n
        BigIntegerRSA d;  //nod
        BigIntegerRSA x;  //x
        BigIntegerRSA y;  //y

        gcdWideParams(BigIntegerRSA one, BigIntegerRSA two, BigIntegerRSA three) {
            d = one;
            x = two;
            y = three;
        }

        gcdWideParams() {}
    }

    private static gcdWideParams gcdWide(BigIntegerRSA a, BigIntegerRSA b) {
        gcdWideParams temphere = new gcdWideParams(a, new BigIntegerRSA("1"), new BigIntegerRSA("0"));
        gcdWideParams temphere2;
        if (b.equals(ZERO)) {
            return temphere;
        }
        temphere2 = gcdWide(b, a.mod(b));
        temphere = new gcdWideParams();
        temphere.d = temphere2.d;
        temphere.x = temphere2.y;

        temphere.y = temphere2.x.subtraction(a.division(b).multiply(temphere2.y));

        return temphere;
    }
}
