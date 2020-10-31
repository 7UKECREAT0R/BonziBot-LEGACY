package com.lukecreator.BonziBot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;

/*
 * Holds an expression
 */
public class Expression {

	// I wrote this source code for an evaluator a while back and am only now getting around to actually porting it over to java.
	
	String equation;
	public Expression(String equation) {
		this.equation = equation;
	}
	public double solve() throws Exception {
		
        String m = equation; // Allow modification of the string.
        m = m.replace(" ", "");
        int openers = GetCharOccurrence(m, '(');
        int closers = GetCharOccurrence(m, ')');
        if(openers != closers)
        {
            throw new Exception("Malformed parenthesis. Expression: " + m);
        }
        boolean finished = false;
        do {
            // Parenthesis check
            int open = -1;
            int close = -1;
            HashMap<Integer, Integer> out = FindParenthesis(m, open, close);
            open = (int) out.keySet().toArray()[0];
            close = (int) out.values().toArray()[0];
            if(open != -1 && close != -1)
            {
                String sub = m.substring(open + 1, close - open - 1);
                Expression equ = new Expression(sub);
                double res = equ.solve();
                String replace = m.substring(open, close - open + 1);
                m = m.replace(replace, String.valueOf(res));
                continue;
            }

            // Exponent check
            int _power = m.indexOf('^');
            if (_power != -1) {
                // Gather characters left and right of the symbol.
                double[] args = GetSymbolArguments(m, _power);
                double a = args[0];
                double b = args[1];
                double result = Math.pow(a, b);
                m = m.replace(a + "^" + b, String.valueOf(result));
                continue;
            }

            // Division/Multiplication check
            int _mult = m.indexOf('*');
            int _div = m.indexOf('/');
            if (_div == -1 && _mult != -1 || _div != -1 && _mult != -1 && _mult < _div) {
                double[] args = GetSymbolArguments(m, _mult);
                double a = args[0];
                double b = args[1];
                double result = a * b;
                m = m.replace(a + "*" + b, String.valueOf(result));
                continue;
            } else if (_div != -1 && _mult == -1 || _div != -1 && _mult != -1 && _mult > _div)
            {
                double[] args = GetSymbolArguments(m, _div);
                double a = args[0];
                double b = args[1];
                double result = a / b;
                m = m.replace(a + "/" + b, String.valueOf(result));
                continue;
            }

            // Subtraction/Addition check
            int _sub = m.indexOf('-');
            int _add = m.indexOf('+');
            if (_add == -1 && _sub != -1 || _add != -1 && _sub != -1 && _sub < _add)
            {
                double[] args = GetSymbolArguments(m, _sub);
                double a = args[0];
                double b = args[1];
                double result = a - b;
                m = m.replace(a + "-" + b, String.valueOf(result));
                continue;
            } else if (_add != -1 && _sub == -1 || _add != -1 && _sub != -1 && _sub > _add)
            {
                double[] args = GetSymbolArguments(m, _add);
                double a = args[0];
                double b = args[1];
                double result = a + b;
                m = m.replace(a + "+" + b, String.valueOf(result));
                continue;
            }
            // If it reaches this far, the equation is finished.
            finished = true;
            break;
        } while (!finished);
        double d = Double.parseDouble(m);
        if(d == -1)
        {
            return d;
        }
        return d;
	}
	
    private static double[] GetSymbolArguments(String s, int symbol)
    {
    	ArrayList<Character> charsBefore = new ArrayList<Character>();
    	ArrayList<Character> charsAfter = new ArrayList<Character>();
        for (int i = symbol-1; i <= symbol; i--)
        {
            try
            {
                // Get the character at i.
                String atI = String.valueOf(s.charAt(i));
                if (IsInt(atI) || atI.equals("."))
                {
                    charsBefore.add(atI.charAt(0));
                }
                else
                {
                    break;
                }
            } catch(IndexOutOfBoundsException exc)
            {
                break;
            }
        }
        for (int i = symbol + 1; i >= symbol; i++)
        {
            try {
                // Get the character at i.
                String atI = String.valueOf(s.charAt(i));
                if (IsInt(atI) || atI.equals("."))
                {
                    charsAfter.add(atI.charAt(0));
                }
                else
                {
                    break;
                }
            } catch (IndexOutOfBoundsException exc) {
                break;
            }
        }
       // charsBefore.reverse();
        Collections.reverse(charsBefore);
        double a = Double.parseDouble(charsBefore.stream().map(String::valueOf).collect(Collectors.joining()));
        double b = Double.parseDouble(charsAfter.stream().map(String::valueOf).collect(Collectors.joining()));
        return new double[] { a, b };
    }
    private static boolean IsInt(String s) {
    	try {
    		Integer.parseInt(s);
    		return true;
    	} catch(Exception exc) {
    		return false;
    	}
    }
    private static int GetCharOccurrence(String s, char c) {
        return (int) s.chars().filter(ch -> ch == c).count();
    }
    private static HashMap<Integer, Integer> FindParenthesis(String m, int open, int close)
    {
        int a, b;
        boolean assigned = false;
        do
        {
            a = m.indexOf('(');
            b = m.indexOf(')');
            if(b < a) { break; }
            if(a == -1 || b == -1) { break; }
            String sub = m.substring(a + 1, b - a - 1);
            if(sub.contains("("))
            {
                b = m.indexOf(')', b + 1);
                if(b == -1) { break; }
            }
            assigned = true;
            break;
        } while (true);
        if(!assigned)
        {
            open = -1;
            close = -1;
        } else
        {
            open = a;
            close = b;
        }
        HashMap<Integer, Integer> base = new HashMap<Integer, Integer>();
        base.put(open, close);
        return base;
    }
}
