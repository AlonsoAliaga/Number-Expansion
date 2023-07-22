package com.alonsoaliaga.numberexpansion;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.Cacheable;
import me.clip.placeholderapi.expansion.Configurable;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

public class NumberExpansion extends PlaceholderExpansion implements Configurable, Cacheable {
    private boolean debug = false;
    private final boolean maxBoundInclusive;
    private final String toRomanZeroChar, toRomanFailChar, toRomanNegativeFormat;
    private final String toHexDisplay, toHexFail;
    private final String fromHexDisplay, fromHexFail;
    private final Pattern splitPattern = Pattern.compile("_(?=[^\\}]*(?:\\{|$))");
    HashMap<String, FormatData> formatterMap = new HashMap<>();
    HashMap<Character, Integer> romanValues = new HashMap<Character, Integer>(){{
        put('I', 1);
        put('V', 5);
        put('X', 10);
        put('L', 50);
        put('C', 100);
        put('D', 500);
        put('M', 1000);
    }};
    private String lastRandom = null;
    private String lastRandomDouble = null;
    public NumberExpansion() {
        try {
            debug = getPlaceholderAPI().getPlaceholderAPIConfig().isDebugMode();
        } catch (Throwable ignored) {}
        toRomanZeroChar = getString("to-roman.zero","");
        toRomanFailChar = getString("to-roman.fail","");
        toRomanNegativeFormat = getString("to-roman.negative-format","-{NUMBER}");
        maxBoundInclusive = getBoolean("max-bound-inclusive",true);

        toHexDisplay = getString("to-hex.display","#{NUMBER}");
        toHexFail = getString("to-hex.fail","");
        fromHexDisplay = getString("from-hex.display","{NUMBER}");
        fromHexFail = getString("from-hex.fail","");
        ConfigurationSection formatterSection = getConfigSection("number-formatter");
        if(formatterSection != null) {
            for (String identifier : formatterSection.getKeys(false)) {
                try{
                    ConfigurationSection section = formatterSection.getConfigurationSection(identifier);
                    if(section != null) {
                        String f = section.getString("format");
                        if(f != null) {
                            String f2 = section.getString("display");
                            String fail = section.getString("fail","");
                            DecimalFormat df = new DecimalFormat(f);
                            FormatData fd = new FormatData(df, f2, fail.equals("null") ? null : fail);
                            formatterMap.put(identifier, fd);
                        }
                    }
                }catch (Throwable e) {
                    if(debug) {
                        Bukkit.getConsoleSender().sendMessage("[Number-Expansion] (DEBUG) Error loading '"+identifier+"' formatter type: "+e.getMessage());
                    }
                }
            }
        }
        if(debug) {
            if (formatterMap.isEmpty()) Bukkit.getConsoleSender().sendMessage("[Number-Expansion] (DEBUG) Couldn't load any number formats.");
            else Bukkit.getConsoleSender().sendMessage("[Number-Expansion] (DEBUG) Loaded "+formatterMap.size()+" number formats!");
        }
    }
    @Override
    public void clear() {

    }
    @Override
    public Map<String, Object> getDefaults() {
        final Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("max-bound-inclusive",true);
        defaults.put("to-roman.zero","");
        defaults.put("to-roman.fail","");
        defaults.put("to-roman.negative-format","-{NUMBER}");
        defaults.put("to-hex.display","#{NUMBER}");
        defaults.put("to-hex.fail","");
        defaults.put("from-hex.display","{NUMBER}");
        defaults.put("from-hex.fail","");
        defaults.put("number-formatter.round-int.format","#");
        defaults.put("number-formatter.round-int.display","{NUMBER}");
        defaults.put("number-formatter.round-two-decimals.format","#.##");
        defaults.put("number-formatter.round-two-decimals.display","{NUMBER}");
        defaults.put("number-formatter.round-force-two-decimals.format","#.##");
        defaults.put("number-formatter.round-force-two-decimals.display","{NUMBER}");
        defaults.put("number-formatter.thousand-separator-two-decimals.format","#,##0.##");
        defaults.put("number-formatter.thousand-separator-two-decimals.display","{NUMBER}");
        defaults.put("number-formatter.dollar-two-decimals.format","#,##0.##");
        defaults.put("number-formatter.dollar-two-decimals.display","${NUMBER}");
        defaults.put("number-formatter.percentage-two-decimals.format","0.##%");
        defaults.put("number-formatter.percentage-two-decimals.display","{NUMBER}");
        return defaults;
    }
    @Override
    public String onPlaceholderRequest(Player p, String params) {
        if (params.equalsIgnoreCase("version")) {
            return getVersion();
        }
        if (params.equalsIgnoreCase("author")) {
            return getAuthor();
        }
        if(params.startsWith("toroman_")) {
            String toParse = PlaceholderAPI.setBracketPlaceholders(p,PlaceholderAPI.setPlaceholders(p,params.substring(8).replace("<percent>","%")));
            try{
                return intToRoman(Integer.parseInt(toParse));
            }catch (Throwable e) {
                if(debug) {
                    Bukkit.getConsoleSender().sendMessage("[Number-Expansion] (DEBUG) Error converting '"+params.substring(8)+"'("+toParse+") to roman: "+e.getMessage());
                }
                return toRomanFailChar;
            }
        }
        if(params.startsWith("fromroman_")) {
            String toParse = PlaceholderAPI.setBracketPlaceholders(p,PlaceholderAPI.setPlaceholders(p,params.substring(10).replace("<percent>","%")));
            try{
                return String.valueOf(convertFromRoman(toParse));
            }catch (Throwable e) {
                if(debug) {
                    Bukkit.getConsoleSender().sendMessage("[Number-Expansion] (DEBUG) Error converting '"+params.substring(10)+"'("+toParse+") from roman: "+e.getMessage());
                }
                return toRomanFailChar;
            }
        }
        if(params.startsWith("tohex_")) {
            String toParse = PlaceholderAPI.setBracketPlaceholders(p,PlaceholderAPI.setPlaceholders(p,params.substring(6).replace("<percent>","%")));
            try{
                String result = Integer.toHexString(Integer.parseInt(toParse));
                if(result.length() < 6) {
                    result = String.join("", Collections.nCopies(6 - result.length(), "0")) + result;
                }
                return toHexDisplay.replace("{NUMBER}",result);
            }catch (Throwable e) {
                if(debug) {
                    Bukkit.getConsoleSender().sendMessage("[Number-Expansion] (DEBUG) Error converting '"+params.substring(6)+"'("+toParse+") to roman: "+e.getMessage());
                }
                return toHexFail.replace("{INPUT}",toParse);
            }
        }
        if(params.startsWith("fromhex_")) {
            String toParse = PlaceholderAPI.setBracketPlaceholders(p,PlaceholderAPI.setPlaceholders(p,params.substring(8).replace("<percent>","%")));
            try{
                String output = String.valueOf(Integer.parseInt(toParse.replace("0x","").replace("#",""),16));
                return fromHexDisplay.replace("{NUMBER}",output);
            }catch (Throwable e) {
                if(debug) {
                    Bukkit.getConsoleSender().sendMessage("[Number-Expansion] (DEBUG) Error converting '"+params.substring(8)+"'("+toParse+") from roman: "+e.getMessage());
                }
                return fromHexFail.replace("{INPUT}",toParse);
            }
        }
        if(params.startsWith("format_")) {
            String[] parts = params.substring(7).split("_",2);
            if(parts.length == 2) {
                if(formatterMap.containsKey(parts[0])) {
                    FormatData formatter = formatterMap.get(parts[0]);
                    String toParse = PlaceholderAPI.setBracketPlaceholders(p,PlaceholderAPI.setPlaceholders(p,parts[1].replace("<percent>","%")));
                    try{
                        double i = Double.parseDouble(toParse);
                        String f = formatter.getDecimalFormat().format(i);
                        return formatter.getDisplayFormat(f);
                    }catch (Throwable e) {
                        if(debug) {
                            Bukkit.getConsoleSender().sendMessage("[Number-Expansion] (DEBUG) Error formatting '"+parts[1]+"'("+toParse+") to '"+parts[0]+"' format: "+e.getMessage());
                        }
                        return formatter.getFail(toParse);
                    }
                }
            }
            return null;
        }
        if(params.startsWith("random_")) {
            //%number_random_1_10%
            String[] parts = splitPattern.split(params.substring(7),2);
            if(parts.length >= 2) {
                try{
                    String xToParse = PlaceholderAPI.setBracketPlaceholders(p,parts[0]);
                    String yToParse = PlaceholderAPI.setBracketPlaceholders(p,parts[1]);
                    double xDouble = Double.parseDouble(xToParse);
                    double yDouble = Double.parseDouble(yToParse);
                    double[] values = getMinMax(xDouble,yDouble);
                    int x = (int) Math.ceil(values[0]);
                    int y = (int) Math.floor(values[1]);
                    if(x == y) {
                        lastRandom = String.valueOf(x);
                        return lastRandom;
                    }
                    lastRandom = String.valueOf((int) ((Math.random() * (y + (maxBoundInclusive?1:0) - x)) + x));
                    return lastRandom;
                }catch (Throwable e) {
                    if(debug) {
                        Bukkit.getConsoleSender().sendMessage("[Number-Expansion] (DEBUG) Error '"+params+"' :");
                        e.printStackTrace();
                        Bukkit.getConsoleSender().sendMessage("[Number-Expansion] (DEBUG) Error above is because debug mode is enabled in /PlaceholderAPI/config.yml'");
                    }
                    return "0";
                }
            }
            return null;
        }
        if(params.startsWith("lastrandom_")) {
            if(lastRandom == null) {
                return PlaceholderAPI.setBracketPlaceholders(p,PlaceholderAPI.setPlaceholders(p,params.substring(11)));
            }
            return lastRandom;
        }
        if(params.startsWith("randomdouble_")) {
            //%number_randomdouble_1_10%
            String[] parts = splitPattern.split(params.substring(13),3);
            if(parts.length >= 2) {
                try{
                    String xToParse = PlaceholderAPI.setBracketPlaceholders(p,parts[0]);
                    String yToParse = PlaceholderAPI.setBracketPlaceholders(p,parts[1]);
                    int decimalPrecision = parts.length >= 3 ? Integer.parseInt(PlaceholderAPI.setBracketPlaceholders(p,parts[2])) : 0;
                    double multiplier = Math.pow(10, decimalPrecision);
                    double xDouble = Double.parseDouble(xToParse);
                    double yDouble = Double.parseDouble(yToParse);
                    double[] values = getMinMax(xDouble,yDouble);
                    double min = Math.ceil(values[0] * multiplier) / multiplier;
                    double max = Math.floor(values[1] * multiplier) / multiplier;
                    if(min == max) {
                        lastRandomDouble = String.valueOf(min);
                        return lastRandomDouble;
                    }
                    double additional = decimalPrecision == 0 ? 1 : 1 / multiplier;
                    double randomValue = min + (max + (maxBoundInclusive?additional:0) - min) * ThreadLocalRandom.current().nextDouble();
                    lastRandomDouble = String.valueOf(Math.round(randomValue * multiplier) / multiplier);
                    return lastRandomDouble;
                }catch (Throwable e) {
                    if(debug) {
                        Bukkit.getConsoleSender().sendMessage("[Number-Expansion] (DEBUG) Error '"+params+"' :");;
                        e.printStackTrace();
                        Bukkit.getConsoleSender().sendMessage("[Number-Expansion] (DEBUG) Error above is because debug mode is enabled in /PlaceholderAPI/config.yml'");
                    }
                    return "0";
                }
            }
        }
        if(params.startsWith("lastrandomdouble_")) {
            if(lastRandomDouble == null) {
                return PlaceholderAPI.setBracketPlaceholders(p,PlaceholderAPI.setPlaceholders(p,params.substring(17)));
            }
            return lastRandomDouble;
        }
        return null;
    }
    private String intToRoman(int originalNum) {
        if(originalNum == 0) return toRomanZeroChar;
        int num = originalNum;
        StringBuilder result = new StringBuilder();
        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] romanNumerals = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        for (int i = 0; i < values.length; i++) {
            while (num >= values[i]) {
                result.append(romanNumerals[i]);
                num -= values[i];
            }
        }
        return originalNum > 0 ? result.toString() : toRomanNegativeFormat.replace("{NUMBER}",result.toString());
    }
    public int convertFromRoman(String roman) {
        int result = 0;
        for (int i = 0; i < roman.length(); i++) {
            char current = roman.charAt(i);
            int currentValue = romanValues.get(current);
            if (i < roman.length() - 1) {
                char next = roman.charAt(i + 1);
                int nextValue = romanValues.get(next);
                if (currentValue < nextValue) {
                    result -= currentValue;
                } else {
                    result += currentValue;
                }
            } else {
                result += currentValue;
            }
        }
        return result;
    }
    @Override
    public @NotNull String getIdentifier() {
        return "number";
    }
    @Override
    public @NotNull String getAuthor() {
        return "AlonsoAliaga";
    }
    @Override
    public @NotNull String getVersion() {
        return "0.1-BETA";
    }
    @Override
    public boolean persist() {
        return true;
    }
    @Override
    public boolean canRegister() {
        return true;
    }
    public class FormatData {
        private DecimalFormat decimalFormat;
        private String displayFormat;
        private String fail;
        public FormatData(DecimalFormat decimalFormat, String displayFormat, String fail) {
            this.decimalFormat = decimalFormat;
            this.displayFormat = displayFormat;
            this.fail = fail;
        }
        public DecimalFormat getDecimalFormat() {
            return decimalFormat;
        }
        public String getDisplayFormat(String parsed) {
            return displayFormat == null ? parsed : displayFormat.replace("{NUMBER}",parsed);
        }
        public String getFail(String input) {
            return fail == null ? null : fail.replace("{INPUT}",input);
        }
    }
    public int[] getMinMax(int num1, int num2) {
        int min = Math.min(num1, num2);
        int max = Math.max(num1, num2);
        return new int[] { min, max };
    }
    public double[] getMinMax(double num1, double num2) {
        double min = Math.min(num1, num2);
        double max = Math.max(num1, num2);
        return new double[] { min, max };
    }
}