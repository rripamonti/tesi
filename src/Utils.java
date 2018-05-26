import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

public class Utils {
    public static boolean isType(String s){
        if (s.contains("fixed")) {
            //è un fixed point number

            return true;
        };
        if (s.contains("int")) {
            //è un int number

            return true;
        };
        if (s.contains("address")) {
            //è un address

            return true;
        };
        if (s.contains("byte")) {
            //è un array byte

            return true;
        };
        if (s.contains("string")) {
            //è un string

            return true;
        };
        return false;
    }

    public static boolean isEvent(String s) {
        if (s.contains("event")) {
            //è un event

            return true;
        };
        return false;
    }

    public static boolean isFunction(String s) {
        if (s.contains("function")) {
            //è una funzione

            return true;
        };
        return false;
    }

    public static boolean isQuery(String s) {
        if (s.startsWith("query")) {
            //è una query

            return true;
        };
        return false;
    }

    public static boolean isCallback(String s) {
        if (s.startsWith("_callback")) {
            //è una callback

            return true;
        };
        return false;
    }

    public static boolean isStoreKeyword(String s) {
        if (s.equalsIgnoreCase("memory")||s.equalsIgnoreCase("storage")) {
            //è una keyword

            return true;
        };
        return false;
    }

    public static String removeLastChar(String s) {
        return s.substring(0, s.length()-1);
    }

    public static String extractMethodName(String s) {
        String[] token = s.trim().split("\\(");
        return token[0];
    }

    public static ArrayList<String> extractParam(String[] s) {
        ArrayList<String> arrayList = new ArrayList<String>();
        boolean flag = true;
        for(String string: s) {
            String[] token = string.trim().split("\\(");
            for (String string2 : token) {
                if (string2.equals("function")) {
                    continue;
                }
                if (string2.startsWith("_callback")) {
                    continue;
                }
                if (string2.startsWith("query")) {
                    continue;
                }
                if (string2.endsWith(",")) {
                    string2 = removeLastChar(string2);
                } else if (string2.endsWith(")")) {
                    arrayList.add(removeLastChar(string2));
                    flag = false;
                    break;
                } else if (string2.endsWith("){")) {
                    arrayList.add(string2.substring(0, string2.length()-2));
                    flag = false;
                    break;
                } else if (string2.endsWith(");")) {
                    arrayList.add(string2.substring(0, string2.length()-2));
                    flag = false;
                    break;
                }
                arrayList.add(string2);
            }
            if (!flag) {
                break;
            }
        }
        return arrayList;
    }

    public static boolean isVariableInit(Set<String> var, String[] token) {
        if (var.contains(token[0]) && token[1].equals("=")) {
            //init della variabile
            return true;

        }

        return false;
    }

    public static boolean isInitByComputation(String[] token) {

        //vedere se è computata o ritornata da un invokedServices
        if (token[2].startsWith("_") && token[2].endsWith(";")){
            return false;
        }

        return true;
    }

    public static ArrayList<String> extractVariablesExploited(Set<String> var, String[] token) {
        ArrayList<String> arrayList = new ArrayList<String>();
        for(int i = 1; i<token.length; i++) {
            String s = token[i];
            if (s.endsWith(";")) {
                s = removeLastChar(s);
            }
            if (var.contains(s)) {
                arrayList.add(s);
            }
        }
        return arrayList;
    }

    public static boolean isQueryCall(HashMap<String, Integer> invokedServices, String[] token) {
        String s = extractMethodName(token[0]);
        if (invokedServices.containsKey(s)) {
            return true;
        }
        return false;
    }

    public static boolean isTestCondition(String[] token) {
        //if for while
        if (token[0].startsWith("if")||token[0].startsWith("for")||token[0].startsWith("while")) {
            return true;
        }
        int i = token.length;
        //else if ?
        if (i>2 && (token[0].contains("else")||token[1].contains("else")) && (token[1].contains("if") || token[2].contains("if"))) {
            return true;
        }

        return false;

    }

    public static ArrayList<String> extractTestVar(Set<String> strings, String[] token) {
        ArrayList<String> arrayList = new ArrayList<String>();
        for(String string: token) {
            string = string.replaceAll("[^\\w\\s]"," ");
            String[] s = string.trim().split(" ");
            for (String string2 : s) {
                if (string2.equals("")) {
                    continue;
                }
                if (string2.equals("if")) {
                    continue;
                }
                if (string2.equals("while")) {
                    continue;
                }
                if (string2.contains("if")) {
                    continue;
                }
                if (strings.contains(string2)) {
                    arrayList.add(string2);
                }
            }
        }
        return arrayList;
    }

    public static boolean canSkip(String s) {
        //TODO: ci sono altre linee da poter skippare?
        switch (s) {
            case "":;
            case "pragma":;
            case "import": return true;
        }
        return false;
    }
}