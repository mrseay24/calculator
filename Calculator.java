package calculator;

/*
CSC3410 - Fall 2015
Sidney Seay -  sseay5@student.gsu.edu

Assignment: #5

calculator class

File(s): Calculator.java

Purpose: Evaluate an infix expression entered by the user. The program
         will convert the expression to postfix (RPN) form and display
         the converted expression.
         The program will repeatedly prompt the user for the value of x,
         displaying the result from evaluating the expression each time.
         When the user enter the letter q instead of a number the program
         will terminate. 
         
*/

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class Calculator {

	private static String infixExpression = "";
    private static boolean isOperand = false;
    private static boolean isOperator = false;
    
    final static ScriptEngineManager engineManager = new ScriptEngineManager();
    final static ScriptEngine engine = engineManager.getEngineByName("JavaScript");

    // constructor
    public Calculator() {

    }

   public static class stackConversion {
	   char conversionStack[] = new char[50];
	   int top;

	   /*
	    *  validate expression format
	    *  check for duplicate operator, unbalance '(' or ')'
	    *  
	    */
	   boolean validateExpressionFormat(String expression) {
           char prevChar = ' ';
		   int countLeftP = 0;
		   int countRightP = 0;
		   
    	   for (int i = 0; i < expression.length(); i++) {
    		   if (expression.charAt(i) == '+' || expression.charAt(i) == '-' || expression.charAt(i) == '*' || expression.charAt(i) == '/' || expression.charAt(i) == '%' || expression.charAt(i) == '^') {
    			   if (expression.charAt(i) == prevChar) {
    				   return false;
    			   }
    		   }
    		   if (expression.charAt(i) == '(' || expression.charAt(i) == ')') {
    			   if (expression.charAt(i) == '(') {
    				   countLeftP++;
    			   }
    			   if (expression.charAt(i) == ')') {
    				   countRightP++;
    			   }    			   
    		   }    		   
    		   prevChar = expression.charAt(i);
    	   }

    	   if (countLeftP != countRightP) {
    		   return false;
    	   }
    	   
   		   return true;

	   }
       /*
        * format error message
        */
       String formatErrorMessage(String message) {
    	   String formattedMessage = "";
    	   String xValue = "";
    	   StringBuffer sb = new StringBuffer();
    	   sb.append(" ");
    	   boolean foundColon = false;
    	   boolean foundPeriod = false;    	   
           int iposition = 0;
    	   
    	   for (int i = 0; i < message.length(); i++) {
    		   if ((message.charAt(i) != ':') && (foundColon == false)) {
    			   // skip character
    		   }
    		   else {
    			   foundColon = true;
    			   sb.append(message.charAt(i));
    			   }    			   
    	   }
    	   formattedMessage = sb.toString().replaceFirst(" : ", "");
    	   sb = new StringBuffer();
           sb.append(" ");
           
    	   for (int i = 0; i < formattedMessage.length(); i++) {
    		   if ((formattedMessage.charAt(i) != '.') && (foundPeriod == false)) {
    			   sb.append(formattedMessage.charAt(i));
    		   }
    		   else {
    			   foundPeriod = true;
    			   }    			   
    	   }
    	   formattedMessage = sb.toString().replaceFirst(" ", "");

       	   iposition = formattedMessage.indexOf("(<");
       	   xValue = "";
       	   if (iposition > 0) {
           	   xValue = formattedMessage.substring(iposition, formattedMessage.length());       	        	   
           	   formattedMessage = formattedMessage.replace(xValue, "");
       	   }
    	   
    	   return formattedMessage;
       }	   
	   /*
	    *  push character in the stack
	    */
	   void push(char convertChar) {
		   top++;
		   conversionStack[top] = convertChar;
	   }
	   /*
	    *  pop character from stack
	    */
	   char pop() {
		   char convertChar;
		   convertChar = conversionStack[top];
		   top--;
		   return convertChar;
	   }
	   /*
	    * get operator precedence
	    */
	   int precedence(char convertChar) {
		   switch(convertChar) {
		   case '(': return 0;
		   case '-': return 1;		   
		   case '+': return 1;
		   case '*': return 2;
		   case '/': return 2;
		   case '%': return 2;		   
		   case '^': return 3;		   
		   }
		   return 4;
	   }
	   /*
	    * Checks if character 2 has same or higher precedence than character 1
	    */
	   boolean checkPrecedence(char convertChar1, char convertChar2){
	    if((convertChar2 == '+' || convertChar2 == '-') && (convertChar1 == '+' || convertChar1 == '-'))
	     return true;
	    else if((convertChar2 == '*' || convertChar2 == '/') && (convertChar1 == '+' || convertChar1 == '-' || convertChar1 == '*' || convertChar1 == '/'))
	     return true;
	    else if((convertChar2 == '^') && (convertChar1 == '+' || convertChar1 == '-' || convertChar1 == '*' || convertChar1 == '/'))
	     return true;
	    else
	     return false;
	   }
	   
	   /*
	    * return boolean value true or false
	    * if given operator found
	    */
       boolean findOperator(char convertChar) {
    	   if (convertChar == '/' || convertChar == '*' || convertChar == '%' || convertChar == '^'  || convertChar == '+' || convertChar == '-') {
    		   return true;
    	   }
    	   return false;
       }
       /*
        * validate user input expression contain character 'x'
        * variable
        */
       boolean checkXParm(String expression) {
    	   expression = expression.toLowerCase();
    	   
    	   int counter = 0;
    	   for (int i = 0; i < expression.length(); i++) {
    		   if (expression.charAt(i) >= 'a' && expression.charAt(i) <= 'z') {
    			   if (expression.charAt(i) != 'x') {
    				   return false;
    			   }
    			   else {
    				   counter++;
    			   }
    		   }
    	   }
    	   if (counter > 0) {
    		   return true;
    	   }
    	   return false;
       }
       /*
        * check for alpha or numeric character
        */
       boolean isAlpha(char convertChar) {
           // convert character to lower case
    	   String sChar = String.valueOf(convertChar);
           sChar = sChar.toLowerCase();
           convertChar = sChar.charAt(0);
           
    	   if ((convertChar >= 'a' && convertChar <= 'z') || (convertChar >= '0' && convertChar <= '9')) {
    		   return true;
    	   }
    	   return false;
       }       
       /*
        * convert infix expression to postfix
        */
       void convertInfixPostfix(String convertString) {
           // define variables
    	   // such as stack array to hold the expression
    	   char stackChar[] = new char[convertString.length()];
    	   char convertChar;
    	   int position = 0;
    	   int iArray = 0;
    	   
    	   // initialize stackChar to blank
    	   for (int i = 0; i < convertString.length(); i++) {
    		   stackChar[i] = ' ';
    	   }
           // routine to load the stack
    	   // method push
    	   // method pop 
    	   for (iArray = 0; iArray < convertString.length(); iArray++) {
    		convertChar = convertString.charAt(iArray);
   		
    		if (convertChar == '(') {
                // push '(' to the current top stack position
    			push(convertChar);	
    		}
    		else if (isAlpha(convertChar)) {
    			// add character to the stack array
    			stackChar[position++] = convertChar;
    		}
    		else if (findOperator(convertChar)) {
 			          if ((conversionStack[top] == 0) || (precedence(convertChar) >= precedence(conversionStack[top])) || (conversionStack[top] == '(')) {
                          // push character to the current top stack position
 			        	  push(convertChar);
    			   }
    		}
    		else if (precedence(convertChar) <= precedence(conversionStack[top])) {
                // get current character in stack and move it
    			// down 1 position in the stack
    			stackChar[position++] = pop();
    			push(convertChar);
    		}
    		else if (convertChar == ')') {
    			    while ((convertChar = pop()) != '(') {
                        // add character to the stack array
    			    	stackChar[position++] = convertChar;
    			    }
    		}
    	   }
    	   /*
    	    *  display postfix value on console 
    	    */
           while (top != 0) {
        	   stackChar[position++] = pop();
    	   }
           System.out.println("Converted expression: ");           
           for (int k = 0; k < convertString.length(); k++) {
        	   if ((stackChar[k] != '(') && (stackChar[k] != ')')) {
            	   System.out.print(stackChar[k]);        		   
        	   }
           }
       }
   }
   
   /*
    * M A I N
   */
   public static void main(String[] args){
	   
       String expressionTranslated = "";
       String xValidateExpression = "";
       String xReplaceExpression = "";       
       String xExpression = "";
       String xValue = "";
       String exceptionMessage = "";
       int expressionAnswer = 0;
       int iposition1 = 0;
       int iposition2 = 0;       
       boolean validExpression = true;
       
       Scanner input = new Scanner(System.in);
	   
	   System.out.println("Enter infix expression: ");
       // define console scanner
	   input = new Scanner(System.in);
	   infixExpression = input.nextLine();
	   
	   // create instance of class stackConversion
	   stackConversion stack = new stackConversion();
       
	   xExpression = infixExpression.replaceAll(" ", "");
	   xValidateExpression = xExpression;
	   //
       // expression error handling processes
	   //
	   if (xExpression.isEmpty()) {
		   System.out.println("No expression was entered ");		   
	   }
	   else if (!stack.checkXParm(xValidateExpression)) {
		   System.out.println("Infix expression syntax not valid.\nFor example enter, (x + 2) * (x - 5) / x\nUser will be prompt for value of 'x'.");		   
	   }
	   else {
	        try {
        	    xValidateExpression = xValidateExpression.replaceAll("x", "1");	        	
	        	engine.eval(xValidateExpression);
	        	//
                // call method convertInfixPostfix
	        	// convert user input expression to postfix
	 		    stack.convertInfixPostfix(xExpression);
               /*
                * display message 'Enter value of x'
                * until user enter 'q' to exit class calculation
                */
               while (!xValue.equalsIgnoreCase("q")) {
           	        System.out.println("\nEnter value of x: ");
                    input = new Scanner(System.in);
            	    xValue = input.nextLine();
            	    if (!xValue.equalsIgnoreCase("q")) {
                	   xReplaceExpression = xExpression.replaceAll("x", xValue);
                	   expressionTranslated = engine.eval(xReplaceExpression).toString();
       	        	   iposition2 = expressionTranslated.indexOf(".");
       	        	   expressionTranslated = expressionTranslated.substring(iposition1, iposition2);

                	   System.out.println("Answer to expression: " + expressionTranslated); // prints arithmetic expression result
            	    }
               }	 		   
	        } catch (Exception exp3) {
	        	exceptionMessage = exp3.getMessage();
	        	// call method formatErrorMessage
	        	//exceptionMessage = stack.formatErrorMessage(exceptionMessage);

	        	System.out.println("Error in expression!! " + infixExpression + " is not a valid expression");
	        }		   
	   }
   }
}