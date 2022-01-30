package br.com.netdeal.service;

import br.com.netdeal.espec.PasswordValidationInterface;
import br.com.netdeal.model.Password;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PasswordValidationService implements PasswordValidationInterface {

    private int getLength(String text) {
        if (text != null) {
            return text.length();
        }
        return 0;
    }

    private boolean isNumber(String text) {
        if (text != null) {
            String regex = "[0-9]+";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);
            return matcher.find();
        }
        return false;
    }

    private boolean isLetters(String text) {
        if (text != null) {
            String regex = "[a-zA-Z]+";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);
            return matcher.find();
        }
        return false;
    }

    private boolean isUppercaseLetters(String text) {
        if (text != null) {
            String regex = "[A-Z]+";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);
            return matcher.find();
        }
        return false;
    }

    private boolean isLowerCaseLetters(String text) {
        if (text != null) {
            String regex = "[a-z]+";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);
            return matcher.find();
        }
        return false;
    }

    private boolean isSymbol(String text) {
        if (text != null) {
            return !this.isNumber(text) && !this.isLetters(text);
        }
        return false;
    }

    private String getSymbols(String text) {
        StringBuilder result = new StringBuilder();
        if (text != null) {
            String[] splitted = text.split("");
            for (String s : splitted) {
                if (this.isSymbol(s))
                    result.append(s);
            }
        }
        return result.toString();
    }

    private int getLengthScore(String text) {
        if (text != null) {
            // +(n*4)
            return this.getLength(text) * 4;
        }
        return 0;
    }

    private int getUpperCaseLettersScore(String text) {
        if (text != null) {
            // +((len-n)*2)
            int ratio = 2;
            String[] splitted = text.split("");

            int n = Arrays.stream(splitted).map(x -> {
                if (this.isUppercaseLetters(x)) {
                    return 1;
                }else{
                    return 0;
                }
            }).reduce(Integer::sum).orElse(0);

            if (n == 0) {
                return 0;
            }
            return (this.getLength(text) - n) * ratio;
        }
        return 0;
    }
    private int getLowerCaseLettersScore(String text) {
        if (text != null) {
            // +((len-n)*2)
            int ratio = 2;
            String[] splitted = text.split("");

            int n = Arrays.stream(splitted).map(x -> {
                if (this.isLowerCaseLetters(x)) {
                    return 1;
                }else{
                    return 0;
                }
            }).reduce(Integer::sum).orElse(0);

            if (n == 0) {
                return 0;
            }
            return (this.getLength(text) - n) * ratio;
        }
        return 0;
    }
    private int getNumbersScore(String text) {
        if (text != null) {
            // +((len-n)*2)
            int ratio = 2;
            String[] splitted = text.split("");

            int n = Arrays.stream(splitted).map(x -> {
                if (this.isNumber(x)) {
                    return 1;
                }else{
                    return 0;
                }
            }).reduce(Integer::sum).orElse(0);

            if (n == 0) {
                return 0;
            }
            return (this.getLength(text) - n) * ratio;
        }
        return 0;
    }
    private int getSymbolsScore(String text) {
        if (text != null) {
            // +((len-n)*2)
            int ratio = 2;
            String[] splitted = text.split("");

            int n = Arrays.stream(splitted).map(x -> {
                if (this.isSymbol(x)) {
                    return 1;
                }else{
                    return 0;
                }
            }).reduce(Integer::sum).orElse(0);

            if (n == 0) {
                return 0;
            }
            return (this.getLength(text) - n) * ratio;
        }
        return 0;
    }

    private int getLettersOnlyScore(String text) {
        if (text != null) {
            // -n
            int ratio = -1;
            if (this.isLetters(text)) {
                return this.getLength(text) * ratio;
            }
        }
        return 0;
    }

    private int getNumbersOnlyScore(String text) {
        if (text != null) {
            // -n
            int ratio = -1;
            if (this.isNumber(text)) {
                return this.getLength(text) * ratio;
            }
        }
        return 0;
    }

    private int getConsecutiveUppercaseLetterssScore(String text) {
        return getConsecutiveScore(text, "[A-Z]+");
    }
    private int getConsecutiveLowercaseLetterssScore(String text) {
        return getConsecutiveScore(text, "[a-z]+");
    }
    private int getConsecutiveNumbersScore(String text) {
        return getConsecutiveScore(text, "[0-9]+");
    }

    private int getConsecutiveScore(String text, String regex){
        if (text != null) {
            // -(n*2)
            int ratio = 2;
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);
            if(!matcher.matches()){
                return 0;
            }
            int grupo = 1;
            int score = 0;
            while(matcher.find()){
                String cons = matcher.group(grupo);
                score += (this.getLength(cons) - 1) * ratio;
                grupo++;
            }
            return score;
        }
        return 0;
    }

    private String reverseString(String str) {
        return new StringBuilder(str).reverse().toString();
    }

    private String[] chunkString(String str, int len) {
        int size = str.length() / len;
        String[] ret = new String[size];
        int offset = 0;
        for (int i = 0; i < size; i++) {
            offset = i * len;
            ret[i] = str.substring(offset, offset + len);
        }
        return ret;
    }

    private List<String> distinctArray(String[] arr) {
        List<String> a = new ArrayList<String>();
        for (String s : arr)
            if (!a.contains(s) && !"".equals(s))
                a.add(s);
        return a;
    }

    private String[] sequentialBuilder(String text, int minChunk) {
        if (text != null) {
            List<String> list = new ArrayList<String>();
            int len = text.split("").length - minChunk;
            for (int i = 0; i < len; i++) {
                for (int index = 0; index < len; index++) {
                    String newText = text.substring(index, text.length());
                    String[] arr = this.chunkString(newText, i + minChunk);
                    for (String s : arr) {
                        list.add(s);
                        list.add(this.reverseString(s));
                    }
                }
            }
            List<String> result = this.distinctArray(list.toArray(new String[list.size()]));

            return result.toArray(new String[result.size()]);
        }
        return new String[0];
    }

    private int getSequentialLettersScore(String text) {
        int minChunk = 3;
        if (text != null) {
            String uppercaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            String[] uStr = this.sequentialBuilder(uppercaseLetters, minChunk);
            String lowercaseLetters = "abcdefghijklmnopqrstuvwxyz";
            String[] lStr = this.sequentialBuilder(lowercaseLetters, minChunk);

            int score = getScoreSequential(uStr, text, minChunk);
            score += getScoreSequential(lStr, text, minChunk);
            // -(n*3)
            int ratio = -3;
            return score * ratio;
        }
        return 0;
    }

    private int getScoreSequential(String[] str, String text, int minChunk){
        int score = 0;
        String aux = text;
        for(String x : str){
            if (aux.contains(x)) {
                score += x.length() - (minChunk - 1);
                aux = aux.replace(x, "");
            }
        }
        return score;
    }

    private int getSequentialNumbersScore(String text) {
        int minChunk = 3;
        if (text != null) {
            String numbers = "1234567890";
            String[] num = this.sequentialBuilder(numbers, minChunk);
            return getSequentialScore(num, text, minChunk);
        }
        return 0;
    }

    private int getSequentialSymbolsScore(String text) {
        int minChunk = 3;
        String sym = this.getSymbols(text);
        if (text != null && !sym.equals("")) {
            String[] num = this.sequentialBuilder(sym, minChunk);
            return getSequentialScore(num, text, minChunk);
        }
        return 0;
    }

    private int getSequentialScore(String[] str, String text, int minChunk){
        int score = 0;
        String txt = text;
        for(String x : str) {
            if (txt.contains(x)) {
                score += x.length() - (minChunk - 1);
                txt = txt.replace(x, "");
            }
        }
        // -(n*3)
        int ratio = -3;
        return score * ratio;
    }

    private int getRepeatCharactersScore(String text) {
        if (text != null) {
            String regex = "(\\w)\\1+";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);
            int maxResultLength = 0;
            while(matcher.find()){
                maxResultLength += matcher.end()-matcher.start();
            }
            if(maxResultLength == 0){
                return 0;
            }
            int ratio = 0;
            if (maxResultLength >= 1 && maxResultLength <= 5) ratio = -8;
            if (maxResultLength >= 6 && maxResultLength <= 10) ratio = -5;
            if (maxResultLength >= 11) ratio = -2;
            return ratio * maxResultLength + (text.length() - maxResultLength * 2);
        }
        return 0;
    }


    @Override
    public Map<String, String> validarSenha(Password password) {
        String senha = password.getSenha();

        int len = this.getLengthScore(senha);
        int upper = this.getUpperCaseLettersScore(senha);
        int lower = this.getLowerCaseLettersScore(senha);
        int num = this.getNumbersScore(senha);
        int symbol = this.getSymbolsScore(senha);
        // Deductions
        int letterOnly = this.getLettersOnlyScore(senha);
        int numberOnly = this.getNumbersOnlyScore(senha);
        int consecutiveUpper = this.getConsecutiveUppercaseLetterssScore(senha);
        int consecutiveLower = this.getConsecutiveLowercaseLetterssScore(senha);
        int consecutiveNumber = this.getConsecutiveNumbersScore(senha);
        int seqLetters = this.getSequentialLettersScore(senha);
        int seqNumbers = this.getSequentialNumbersScore(senha);
        int seqSymbols = this.getSequentialSymbolsScore(senha);
        int repCharacters= this.getRepeatCharactersScore(senha);

        int score = len + upper + lower + num + symbol + letterOnly + numberOnly +
            consecutiveUpper + consecutiveLower + consecutiveNumber + seqLetters +
            seqNumbers + seqSymbols + repCharacters;

        Map<String, String> mapa = new HashMap<>();
        mapa.put("complexity",getComplexity(score));
        mapa.put("score", getScorePorcentagem(score));
        return mapa;
    }

    public String getScorePorcentagem(int score){
        if(score>100){
            return "100";
        }else if(score<0){
            return "0";
        }

        return ""+score;
    }

    public String getComplexity(int score){
        String complexity = "Too Short";
        if(score>0 && score < 20){
            complexity = "Very Weak";
        }else if(score>0 && score < 40){
            complexity = "Weak";
        }else if(score>0 && score < 60){
            complexity = "Good";
        }else if(score>0 && score < 80){
            complexity = "Strong";
        }else if(score>0 && score < 100){
            complexity = "Very Strong";
        }else if(score >= 100){
            complexity = "Perfect";
        }

        return complexity;
    }
}
