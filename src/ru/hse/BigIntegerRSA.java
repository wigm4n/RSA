package ru.hse;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class BigIntegerRSA {
    final private static BigIntegerRSA ZERO = new BigIntegerRSA("0");
    final private static BigIntegerRSA ONE = new BigIntegerRSA("1");
    final private static BigIntegerRSA TWO = new BigIntegerRSA("2");

    private List<Integer> cache = new ArrayList<>();
    private boolean sign = true;

    BigIntegerRSA(String input, boolean isPositive) {
        this.setSign(isPositive);
        for (int i = 0; i < input.length(); ++i) {
            cache.add(Integer.parseInt(String.valueOf(input.charAt(i))));
        }
    }

    BigIntegerRSA(String input) {
        for (int i = 0; i < input.length(); ++i) {
            cache.add(Integer.parseInt(String.valueOf(input.charAt(i))));
        }
    }

    BigIntegerRSA() {
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!this.getSign())
            sb.append("-");
        for (Integer numeral : cache) {
            sb.append(numeral);
        }
        return sb.toString();
    }

    private String toStringWithOutSign() {
        StringBuilder sb = new StringBuilder();
        for (Integer numeral : cache) {
            sb.append(numeral);
        }
        return sb.toString();
    }

    int size(){
        return cache.size();
    }

    private Integer get(int index) {
        return cache.get(index);
    }

    private void add(int numeral) {
        cache.add(numeral);
    }

    private BigIntegerRSA reverse() {
        BigIntegerRSA result = new BigIntegerRSA();
        boolean flag = false;
        for (int i = this.cache.size() - 1; i >= 0; --i) {
            if (!cache.get(i).equals(0))
                flag = true;
            if (flag) {
                result.add(cache.get(i));
            }
        }
        result.setSign(this.getSign());
        return result;
    }

    private BigIntegerRSA reverseWithOutZeros() {
        BigIntegerRSA result = new BigIntegerRSA();
        for (int i = this.cache.size() - 1; i >= 0; --i) {
            result.add(cache.get(i));
        }
        result.setSign(this.getSign());
        return result;
    }

    BigIntegerRSA sum(BigIntegerRSA second) {
        BigIntegerRSA result;

        if (this.getSign() && second.getSign()) {
            result = subSum(this, second);
        } else if (!this.getSign() && !second.getSign()) {
            result = subSum(this, second);
            result.setSign(false);
        } else if (this.getSign() && !second.getSign()) {
            result = subSubtraction(this, second);
        } else {
            result = subSubtraction(this, second);
        }

        return result;
    }

    private BigIntegerRSA subSum(BigIntegerRSA first, BigIntegerRSA second) {
        BigIntegerRSA result = new BigIntegerRSA();
        int discharge = 0;
        if (first.size() == second.size()) {
            for (int i = first.size() - 1; i >= 0; --i) {
                int sum = first.get(i) + second.get(i) + discharge;
                discharge = 0;
                if (sum > 9) {
                    discharge = 1;
                    sum -= 10;
                }
                result.add(sum);
            }
            if (discharge > 0) {
                result.add(discharge);
            }
        }

        if (first.size() > second.size()) {
            int diff = first.size() - second.size();

            for (int i = second.size() - 1; i >= 0; --i) {
                int sum = first.get(i + diff) + second.get(i) + discharge;
                discharge = 0;
                if (sum > 9) {
                    discharge = 1;
                    sum -= 10;
                }
                result.add(sum);
            }
            for (int i = diff - 1; i >= 0; --i) {
                if (first.get(i) + discharge > 9) {
                    result.add(first.get(i) + discharge - 10);
                    discharge = 1;
                } else {
                    result.add(first.get(i) + discharge);
                    discharge = 0;
                }
            }
            if (discharge > 0) {
                result.add(discharge);
            }
        }

        if (first.size() < second.size()) {
            int diff = second.size() - first.size();

            for (int i = first.size() - 1; i >= 0; --i) {
                int sum = second.get(i + diff) + first.get(i) + discharge;
                discharge = 0;
                if (sum > 9) {
                    discharge = 1;
                    sum -= 10;
                }
                result.add(sum);
            }
            for (int i = diff - 1; i >= 0; --i) {
                if (second.get(i) + discharge > 9) {
                    result.add(second.get(i) + discharge - 10);
                    discharge = 1;
                } else {
                    result.add(second.get(i) + discharge);
                    discharge = 0;
                }
            }
            if (discharge > 0) {
                result.add(discharge);
            }
        }
        if(!first.getSign() && !second.getSign())
            result.setSign(false);
        return result.reverse();
    }

    BigIntegerRSA subtraction(BigIntegerRSA number) {
        BigIntegerRSA result;

        if (this.equals(number))
            return new BigIntegerRSA("0");

        if (this.getSign() && number.getSign())
            result = subSubtraction(this, number);
        else if (!this.getSign() && !number.getSign())
            result = subSubtraction(this, number);
        else if (this.getSign() && !number.getSign())
            result = subSum(this, number);
        else {
            number.setSign(false);
            result = subSum(this, number);
            number.setSign(true);
        }
        return result;
    }

    private BigIntegerRSA subSubtraction(BigIntegerRSA first, BigIntegerRSA second) {
        BigIntegerRSA result = new BigIntegerRSA();
        int discharge = 0;
        boolean isPositive = false;

        if (first.isLargerThanModule(second)) {
            int diff = first.size() - second.size();
            for (int i = second.size() - 1; i >= 0; --i) {
                if (first.get(i + diff) - discharge >= second.get(i)) {
                    result.add(first.get(i + diff) - discharge - second.get(i));
                    discharge = 0;
                }
                else {
                    result.add(10 + first.get(i + diff) - second.get(i) - discharge);
                    discharge = 1;
                }
            }
            for (int i = diff - 1; i >= 0; --i) {
                if (first.get(i) == 0 && discharge == 1) {
                    result.add(9);
                    discharge = 1;
                } else if (first.get(i) == 0 && discharge == 0) {
                    result.add(first.get(i));
                }
                else {
                    result.add(first.get(i) - discharge);
                    discharge = 0;
                }
            }
            result.setSign(first.getSign());
        } else {
            if (!first.getSign() && second.getSign())
                isPositive = true;
            if (!first.getSign() && !second.getSign())
                isPositive = true;
            int diff = second.size() - first.size();
            for (int i = first.size() - 1; i >= 0; --i) {
                if (second.get(i + diff) - discharge >= first.get(i)) {
                    result.add(second.get(i + diff) - discharge - first.get(i));
                    discharge = 0;
                }
                else {
                    result.add(10 + second.get(i + diff) - first.get(i) - discharge);
                    discharge = 1;
                }
            }
            for (int i = diff - 1; i >= 0; --i) {
                result.add(second.get(i) - discharge);
                discharge = 0;
            }
            result.setSign(isPositive);
        }
        return result.reverse();
    }

    BigIntegerRSA multiply(BigIntegerRSA number) {
        String num1 = this.reverseWithOutZeros().toStringWithOutSign();
        String num2 = number.reverseWithOutZeros().toStringWithOutSign();

        int[] m = new int[num1.length() + num2.length()];

        for (int i = 0; i < num1.length(); i++) {
            for (int j = 0; j < num2.length(); j++) {
                m[i + j] = m[i + j] + (num1.charAt(i) - '0') * (num2.charAt(j) - '0');
            }
        }

        StringBuilder product = new StringBuilder();
        for (int i = 0; i < m.length; i++) {
            int digit = m[i] % 10;
            int carry = m[i] / 10;
            if (i + 1 < m.length) {
                m[i + 1] = m[i + 1] + carry;
            }
            product.insert(0, digit);
        }

        while (product.length() > 1 && product.charAt(0) == '0') {
            product = new StringBuilder(product.substring(1));
        }

        if (!this.getSign() && !number.getSign())
            return new BigIntegerRSA(product.toString(), true);
        else if (!this.getSign() || !number.getSign())
            return new BigIntegerRSA(product.toString(), false);
        return new BigIntegerRSA(product.toString(), true);
    }

    BigIntegerRSA divisionV2(BigIntegerRSA number) {
        if (!this.isLargerThan(number))
            return ZERO;

        ArrayList<Integer> left = new ArrayList<>();
        for (int i = 0; i < this.size(); ++i) {
            left.add(this.get(i));
        }
        ArrayList<Integer> right = new ArrayList<>();
        for (int i = 0; i < number.size(); ++i) {
            right.add(number.get(i));
        }
        ArrayList<Integer> result = new ArrayList<>();
        StringBuilder currentSetString = new StringBuilder();


        ArrayList<Integer> currentSet = new ArrayList<>(); // лист цифр делимого длиной делителя
        for (int i = 0; i < number.size(); ++i) {
            currentSet.add(this.get(i));
            currentSetString.append(String.valueOf(this.get(i)));
        }

        BigIntegerRSA currentNumber = new BigIntegerRSA(currentSetString.toString());  // число в делимом длиной делителя

        ArrayList<Integer> temp = new ArrayList<>();
        for (int i = this.size() - 1; i >= number.size(); --i) {
            temp.add(left.get(i));
        }
//        temp.
//        for (int i = )
        return null;
    }

    BigIntegerRSA division(BigIntegerRSA number) {
        StringBuilder ans = new StringBuilder();
        String numberStr = createShift();
        int divisor = 2;

        int idx = 0;
        char[] num = numberStr.toCharArray();
        int temp = num[0] - '0';

        while (temp < divisor)
            temp = temp * 10 + (num[++idx] - '0');

        idx += 1;

        while (num.length > idx) {
            ans.append(temp / divisor);
            temp = (temp % divisor) * 10 + num[idx++] - '0';
        }

        BigIntegerRSA result = convert(this.toStringWithOutSign(), number.toStringWithOutSign(), ans.toString());

        if (!this.getSign() && !number.getSign()) {
            result.setSign(true);
            return result;
        } else if (!this.getSign() || !number.getSign()) {
            result.setSign(false);
            return result;
        }
        result.setSign(true);
        return result;
    }

    BigIntegerRSA mod(BigIntegerRSA number) {
        BigIntegerRSA div = this.division(number);
        return this.subtraction(div.multiply(number));
    }

    BigIntegerRSA powerMod(BigIntegerRSA power, BigIntegerRSA mod) {
        if (!mod.isLargerThan(ONE))
            return ZERO;

        BigIntegerRSA result = new BigIntegerRSA("1");
        BigIntegerRSA number = power;
        BigIntegerRSA base = this.mod(mod);

        while(number.isLargerThan(ZERO)) {
            if (number.mod(TWO).equals(ONE)) {
                result = result.multiply(base).mod(mod);
            }
            number = number.division(TWO);
            base = base.multiply(base).mod(mod);
        }
        return result;
    }

    BigIntegerRSA gcd(BigIntegerRSA number) {
        //int gcd (int a, int b) {
        //	while (b) {
        //		a %= b;
        //		swap (a, b);
        //	}
        //	return a;
        //}

        BigIntegerRSA result = this;
        BigIntegerRSA zero = new BigIntegerRSA("0");
        BigIntegerRSA temp;

        while (!number.equals(zero)) {
            result = result.mod(number);
            temp = result;
            result = number;
            number = temp;
        }

        return result;
    }

    boolean isLargerThan(BigIntegerRSA second) {
        int lenFirst = cache.size();
        int lenSecond = second.size();

        if ((this.getSign()) && (second.getSign())) {
            if (lenFirst > lenSecond)
                return true;
            else if (lenFirst < lenSecond)
                return false;
            for (int i = 0; i < lenFirst; ++i) {
                if (cache.get(i) > second.get(i))
                    return true;
                else if (cache.get(i) < second.get(i))
                    return false;
            }
            return false;
        } else if ((!this.getSign()) && (!second.getSign())) {
            if (lenFirst > lenSecond)
                return false;
            else if (lenFirst < lenSecond)
                return true;
            for (int i = 0; i < lenFirst; ++i) {
                if (cache.get(i) > second.get(i))
                    return false;
                else if (cache.get(i) < second.get(i))
                    return true;
            }
            return false;
        } else return this.getSign();
    }

    private boolean isLargerThanModule(BigIntegerRSA second) {
        int lenFirst = cache.size();
        int lenSecond = second.size();

        if (lenFirst > lenSecond)
            return true;
        else if (lenFirst < lenSecond)
            return false;
        for (int i = 0; i < lenFirst; ++i) {
            if (cache.get(i) > second.get(i))
                return true;
            else if (cache.get(i) < second.get(i))
                return false;
        }
        return false;
    }

    boolean equals(BigIntegerRSA number) {
        int lenFirst = this.cache.size();
        int lenSecond = number.size();

        if (this.getSign() != number.getSign())
            return false;
        if (lenFirst != lenSecond)
            return false;
        for (int i = 0; i < lenFirst; ++i) {
            if (!cache.get(i).equals(number.get(i)))
                return false;
        }
        return true;
    }

    private BigIntegerRSA convert(String f, String s, String fa) {
        BigInteger first = new BigInteger(f);
        BigInteger second = new BigInteger(s);
        BigInteger res = first.divide(second);
        return new BigIntegerRSA(res.toString());
    }

    private String createShift() {
        return "s";
    }

    private void setSign(boolean sign) {
        this.sign = sign;
    }

    private boolean getSign() {
        return sign;
    }
}
