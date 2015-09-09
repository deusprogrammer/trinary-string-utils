package com.trinary.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	protected static String replacementFormatString = "\\{%s\\}";
	protected static String extrapolationString = "\\{(.*?)\\}";
	protected static String interpolationString = String.format(replacementFormatString, "(.*?)");
	protected static Pattern interpolationPattern = Pattern.compile(interpolationString);
	protected static Pattern extrapolationPattern = Pattern.compile(extrapolationString);
	
	public static String interpolate(String formatString, Map<String, Object> values) {
		Matcher interpolationMatcher = interpolationPattern.matcher(formatString);
		String modifiedString = formatString;
		
		while (interpolationMatcher.find()) {
			int blanks = interpolationMatcher.groupCount();
			for (int i = 0; i < blanks; i++) {
				String blank = interpolationMatcher.group(i + 1);
				
				Object o = values.get(blank);
				
				if (o != null) {
					String blankRegex = String.format(replacementFormatString, blank);
					modifiedString = modifiedString.replaceAll(blankRegex, o.toString());
				}
			}
		}
		
		return modifiedString;
	}
	
	public static Map<String, String> extrapolateString(String formatString, String sourceString) {
		List<String> positionMap = new ArrayList<String>();
		Map<String, String> parsedData = new HashMap<String, String>();
		
		Matcher extrapolationMatcher = extrapolationPattern.matcher(formatString);
		while (extrapolationMatcher.find()) {
			for (int i = 0; i < extrapolationMatcher.groupCount(); i++) {
				String match = extrapolationMatcher.group(i + 1);
				positionMap.add(match);
				parsedData.put(match, "");
			}
		}
		
		String searchString = formatString.replaceAll(extrapolationString, "(.*)");
		Pattern searchPattern = Pattern.compile(searchString);
		Matcher searchMatcher = searchPattern.matcher(sourceString);
		
		while (searchMatcher.find()) {
			for (int i = 0; i < searchMatcher.groupCount(); i++) {
				String match = searchMatcher.group(i + 1);
				String key = positionMap.get(i);
				parsedData.put(key, match.trim());
			}
		}
		
		return parsedData;
	}
}