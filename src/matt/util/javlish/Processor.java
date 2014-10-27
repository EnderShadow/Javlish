package matt.util.javlish;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Processor
{
	public static String toJava(String english)
	{
		List<String> strings = new ArrayList<String>();
		int i = 0;
		while(i < english.length())
		{
			if(english.charAt(i) == '\"' && (i < 1 || english.charAt(i - 1) != '\\'))
			{
				int j = i + 1;
				while(j < english.length())
				{
					if(english.charAt(j) == '\"' && (j < 1 || english.charAt(j - 1) != '\\'))
					{
						strings.add(english.substring(i, j + 1));
						i = j;
						break;
					}
					j++;
				}
			}
			i++;
		}
		
		english = english.replace("integer ", "int ");
	    english = english.replace("is less than ", "< ");
	    english = english.replace("is greater than ", "> ");
	    english = english.replace(" or equal to ", "= ");
	    english = english.replace("is not equal to ", "!= ");
	    english = english.replace(" is ", "== ");
	    english = english.replace("otherwise if ", "else if( ");
	    english = english.replace("if ", "if( ");
	    english = english.replace(" then", " ){");
	    english = english.replace(" otherwise", " else{");
	    english = english.replace("begins here.", "{");
	    english = english.replace("end", "}");
	    english = english.replace(" bitwise and ", " & ");
	    english = english.replace(" bitwise or ", " | ");
	    english = english.replace(" and ", " && ");
	    english = english.replace(" or ", " || ");
	    english = english.replace(" not ", "!");
	    english = english.replace(".", ";");
	    english = english.replace(" to string", " + \"\"");
	    english = english.replace("gets ", "= ");
	    english = english.replace("^", ".");
	    english = english.replace("minus ", "- ");
	    english = english.replace("plus ", "+ ");
	    english = english.replace("times ", "* ");
	    english = english.replace("divided by ", "/ ");
	    english = english.replace(" modulus ", " % ");
	    english = parsePostfixIncrements(english);
	    english = parsePostfixDecrements(english);
	    english = english.replace("increment", "++");
	    english = english.replace("decrement", "--");
	    english = english.replace("shift bit right with no carry", ">>>");
	    english = english.replace("shift bit right", ">>");
	    english = english.replace("shift bit left", "<<");
	    english = english.replace(" invert ", " ~");
	    english = english.replace(" bitwise exclusive or ", " ^");

		english = parseErrorConsolePrints(english);
		english = parseConsolePrints(english);
		english = parseEnhancedForLoops(english);
		english = parseForLoops(english);
		english = parseWhileLoops(english);
		english = parseMethods(english);
		english = parseConstructors(english);
		english = parseMethodCalls(english);
		while(english.contains(" )")) english = english.replace(" )", ")");
		while(english.contains("( ")) english = english.replace("( ", "(");
		while(english.contains(" }")) english = english.replace(" }", "}");
		while(english.contains("} ")) english = english.replace("} ", "}");
		while(english.contains("{ ")) english = english.replace("{ ", "{");
		while(english.contains(" {")) english = english.replace(" {", "{");
		while(english.contains(" ;")) english = english.replace(" ;", ";");
		while(english.contains("; ")) english = english.replace("; ", ";");
		while(english.contains(" ++")) english = english.replace(" ++", "++");
		while(english.contains("++ ")) english = english.replace("++ ", "++");
		english = english.replace("==", " == ");
		while(english.contains("  ")) english = english.replace("  ", " ");
		
		i = 0;
		while(i < english.length())
		{
			if(english.charAt(i) == '\"' && (i < 1 || english.charAt(i - 1) != '\\'))
			{
				int j = i + 1;
				while(j < english.length())
				{
					if(english.charAt(j) == '\"' && (j < 1 || english.charAt(j - 1) != '\\') && strings.size() > 0)
					{
						String codePt1 = english.substring(0, i);
						String codePt2 = english.substring(j + 1, english.length());
						String quote = english.substring(i, j + 1);
						String newQuote = strings.remove(0);
						english = codePt1 + newQuote + codePt2;
						int lengthDif = newQuote.length() - quote.length();
						i = j + lengthDif;
						break;
					}
					j++;
				}
			}
			i++;
		}
		
		english = splitLines(english);
		english = makeHumanReadable(english);
		
		return english;
	}
	
	private static String parsePostfixIncrements(String english)
	{
		int i = 0;
		while(!english.startsWith("postfix increment ", i) && i < english.length() - 18)
		{
			i++;
		}
		int j = i + 18;
		while(!english.startsWith(" ", j) && !english.startsWith(";", j) && j < english.length() - 1)
		{
			j++;
		}
		
		if(j + 1 < english.length() && (english.substring(i, i + 18) + english.substring(j, j + 1)).equals("postfix increment  "))
		{
			String codePt1 = english.substring(0, i);
			String codePt2 = english.substring(j + 1, english.length());
			String increment = english.substring(i + 18, j) + "++" + english.substring(j, j + 1);
			
			return parsePostfixIncrements(codePt1 + increment + codePt2);
		}
		
		if(j + 1 < english.length() && (english.substring(i, i + 18) + english.substring(j, j + 1)).equals("postfix increment ;"))
		{
			String codePt1 = english.substring(0, i);
			String codePt2 = english.substring(j + 1, english.length());
			String increment = english.substring(i + 18, j) + "++" + english.substring(j, j + 1);
			
			return parsePostfixIncrements(codePt1 + increment + codePt2);
		}
		
		return english;
	}
	
	private static String parsePostfixDecrements(String english)
	{
		int i = 0;
		while(!english.startsWith("postfix decrement ", i) && i < english.length() - 18)
		{
			i++;
		}
		int j = i + 18;
		while(!english.startsWith(" ", j) && !english.startsWith(";", j) && j < english.length() - 1)
		{
			j++;
		}
		
		if(j + 1 < english.length() && (english.substring(i, i + 18) + english.substring(j, j + 1)).equals("postfix decrement  "))
		{
			String codePt1 = english.substring(0, i);
			String codePt2 = english.substring(j + 1, english.length());
			String increment = english.substring(i + 18, j) + "--" + english.substring(j, j + 1);
			
			return parsePostfixDecrements(codePt1 + increment + codePt2);
		}
		
		if(j + 1 < english.length() && (english.substring(i, i + 18) + english.substring(j, j + 1)).equals("postfix decrement ."))
		{
			String codePt1 = english.substring(0, i);
			String codePt2 = english.substring(j + 1, english.length());
			String increment = english.substring(i + 18, j) + "--" + english.substring(j, j + 1);
			
			return parsePostfixDecrements(codePt1 + increment + codePt2);
		}
		
		return english;
	}
	
	private static String parseErrorConsolePrints(String english)
	{
		int i = 0;
		while(!english.startsWith("print ", i) && i < english.length() - 6)
		{
			i++;
		}
		int j = i + 6;
		while(!english.startsWith("to error console", j) && j < english.length() - 16)
		{
			j++;
		}
		
		if(j + 16 < english.length() && (english.substring(i, i + 6) + english.substring(j, j + 16)).equals("print to error console"))
		{
			String codePt1 = english.substring(0, i);
			String codePt2 = english.substring(j + 16, english.length());
			String loop = english.substring(i, j + 16);
			loop = loop.replace("print ", "System.err.println( ").replace("to error console", ") ");
			
			return parseConsolePrints(codePt1 + loop + codePt2);
		}
		
		return english;
	}
	
	private static String parseConsolePrints(String english)
	{
		int i = 0;
		while(!english.startsWith("print ", i) && i < english.length() - 6)
		{
			i++;
		}
		int j = i + 6;
		while(!english.startsWith("to console", j) && j < english.length() - 10)
		{
			j++;
		}
		
		if(j + 10 < english.length() && (english.substring(i, i + 6) + english.substring(j, j + 10)).equals("print to console"))
		{
			String codePt1 = english.substring(0, i);
			String codePt2 = english.substring(j + 10, english.length());
			String loop = english.substring(i, j + 10);
			loop = loop.replace("print ", "System.out.println( ").replace("to console", ") ");
			
			return parseConsolePrints(codePt1 + loop + codePt2);
		}
		
		return english;
	}
	
	private static String parseEnhancedForLoops(String english)
	{
		int i = 0;
		while(!english.startsWith("for each ", i) && i < english.length() - 9)
		{
			i++;
		}
		int j = i + 9;
		while(!english.startsWith(" in ", j) && j < english.length() - 4)
		{
			j++;
		}
		int k = j + 4;
		while(!english.startsWith(" do", k) && k < english.length() - 3)
		{
			k++;
		}
		
		if(k + 3 < english.length() && (english.substring(i, i + 9) + english.substring(j, j + 4) + english.substring(k, k + 3)).equals("for each  in  do"))
		{
			String codePt1 = english.substring(0, i);
			String codePt2 = english.substring(k + 3, english.length());
			String loop = english.substring(i, k + 3);
			loop = loop.replace("for each", "for(").replace("in", ":").replace("do", "){");
			
			return parseForLoops(codePt1 + loop + codePt2);
		}
		
		return english;
	}
	
	private static String parseForLoops(String english)
	{
		int i = 0;
		while(!english.startsWith("for ", i) && i < english.length() - 4)
		{
			i++;
		}
		int j = i + 4;
		while(!english.startsWith(" do", j) && j < english.length() - 3)
		{
			j++;
		}
		
		if(j + 3 < english.length() && (english.substring(i, i + 4) + english.substring(j, j + 3)).equals("for  do"))
		{
			String codePt1 = english.substring(0, i);
			String codePt2 = english.substring(j + 3, english.length());
			String loop = english.substring(i, j + 3);
			loop = loop.replace("for", "for(").replace("do", "){");
			
			return parseForLoops(codePt1 + loop + codePt2);
		}
		
		return english;
	}
	
	private static String parseWhileLoops(String english)
	{
		int i = 0;
		while(!english.startsWith("while ", i) && i < english.length() - 6)
		{
			i++;
		}
		int j = i + 6;
		while(!english.startsWith(" do", j) && j < english.length() - 3)
		{
			j++;
		}
		
		if(j + 3 < english.length() && (english.substring(i, i + 6) + english.substring(j, j + 3)).equals("while  do"))
		{
			String codePt1 = english.substring(0, i);
			String codePt2 = english.substring(j + 3, english.length());
			String loop = english.substring(i, j + 3);
			loop = loop.replace("while ", "while(").replace(" do", "){");
			
			return parseForLoops(codePt1 + loop + codePt2);
		}
		
		return english;
	}
	
	private static String parseMethods(String english)
	{
		int i = 0;
		while(!english.startsWith("method ", i) && i < english.length() - 7)
		{
			i++;
		}
		int j = i + 7;
		while(!english.startsWith(" returns ", j) && j < english.length() - 9)
		{
			j++;
		}
		int k = j + 9;
		while(!english.startsWith(" with parameters ", k) && k < english.length() - 17)
		{
			k++;
		}
		int l = k + 17;
		while(!english.startsWith("{", l) && l < english.length() - 1)
		{
			l++;
		}
		
		if(l + 1 < english.length() && (english.substring(i, i + 7) + english.substring(j, j + 9) + english.substring(k, k + 17) + english.substring(l, l + 1)).equals("method  returns  with parameters {"))
		{
			String codePt1 = english.substring(0, i);
			String codePt2 = english.substring(l, english.length());
			
			String methodName = english.substring(i + 7, j);
			String returnType = english.substring(j + 9, k);
			String[] parameters = english.substring(k + 17, l).split(" && ");
			
			String method = returnType + " " + methodName + "(";
			
			for(int num = 0; num < parameters.length; num++)
			{
				method += parameters[num];
				if(num < parameters.length - 1)
					method += ", ";
			}
			
			method += ")";
			
			return parseMethods(codePt1 + method + codePt2);
		}
		
		return english;
	}
	
	private static String parseMethodCalls(String english)
	{
		int u = 0;
		while(!english.startsWith("call ", u) && u < english.length() - 5)
		{
			u++;
		}
		int i = u + 5;
		while(!english.startsWith(" with parameters ", i) && i < english.length() - 17)
		{
			i++;
		}
		int j = i + 17;
		while(!english.startsWith(";", j) && j < english.length() - 1)
		{
			j++;
		}
		
		if(j + 1 < english.length() && (english.substring(u, u + 5) + english.substring(i, i + 17) + english.substring(j, j + 1)).equals("call  with parameters ;"))
		{
			String codePt1 = english.substring(0, u);
			String codePt2 = english.substring(j, english.length());
			
			String[] parameters = english.substring(i + 17, j).split(" && ");
			
			String methodCall = english.substring(u + 5, i) + "(";
			
			for(int num = 0; num < parameters.length; num++)
			{
				methodCall += parameters[num];
				if(num < parameters.length - 1)
					methodCall += ", ";
			}
			
			methodCall += ")";
			
			return parseMethodCalls(codePt1 + methodCall + codePt2);
		}
		
		return english;
	}
	
	private static String parseConstructors(String english)
	{
		int i = 0;
		while(!english.startsWith("constructor ", i) && i < english.length() - 12)
		{
			i++;
		}
		int j = i + 12;
		while(!english.startsWith(" with parameters ", j) && j < english.length() - 17)
		{
			j++;
		}
		int k = j + 17;
		while(!english.startsWith("{", k) && k < english.length() - 1)
		{
			k++;
		}
		
		if(k + 1 < english.length() && (english.substring(i, i + 12) + english.substring(j, j + 17) + english.substring(k, k + 1)).equals("constructor  with parameters {"))
		{
			String codePt1 = english.substring(0, i);
			String codePt2 = english.substring(k, english.length());
			
			String constructorName = english.substring(i + 12, j);
			String[] parameters = english.substring(j + 17, k).split(" && ");
			
			String constructor = constructorName + "(";
			
			for(int num = 0; num < parameters.length; num++)
			{
				constructor += parameters[num];
				if(num < parameters.length - 1)
					constructor += ", ";
			}
			
			constructor += ")";
			
			return parseConstructors(codePt1 + constructor + codePt2);
		}
		
		return english;
	}
	
	private static String splitLines(String s)
	{
		List<String> list = new ArrayList<String>();
		list.addAll(Arrays.asList(s.split("\n")));
		
		s = "";
		for(int i = 0; i < list.size(); i++)
		{
			if(list.get(i).contains(";") && !list.get(i).endsWith(";"))
			{
				int index = list.get(i).indexOf(";") + 1;
				String s1 = list.get(i).substring(0, index);
				String s2 = list.get(i).substring(index);
				list.set(i, s1);
				list.add(i + 1, s2);
			}
			s += list.get(i) + "\n";
		}
		
		
		
		return s;
	}
	
	private static String makeHumanReadable(String s)
	{
		s = s.replace("{", "\n{\n");
		s = s.replace("}", "\n}\n");
		
		String[] sa = s.split("\n");
		s = "";
		
		int numIndent = 0;
		for(int i = 0; i < sa.length; i++)
		{
			if(sa[i].isEmpty())
				continue;
			if(sa[i].equals("{"))
				numIndent++;
			if(sa[i].equals("}"))
				numIndent--;
			for(int j = 0; j < numIndent; j++)
				sa[i] = "\t" + sa[i];
			s += sa[i] + "\n";
		}
		
		s = s.replace("\t{", "{");
		s = s.substring(0, s.length() - 1);
		
		
		
		return s;
	}
}