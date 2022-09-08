import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Iterator;
import java.util.List;
//import java.util.Map;

//import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CompilationUnit;
//import org.eclipse.jdt.core.dom.MemberRef;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
//import org.eclipse.jdt.core.dom.MethodRef;
//import org.eclipse.jdt.core.dom.MethodRefParameter;
//import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
//import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
//import org.eclipse.jdt.core.dom.VariableDeclarationExpression;

public class Test {

	public void run() throws IOException {

		// Reading the content of the file and storing it in a string. And, replacing the path of the file.
		String content = FileUtils
				.readFileToString(new File("C:\\Users\\jaish\\eclipse-workspace\\JDT-SE\\src\\Person.java")); 

		// Creating a parser object and entering the version of java that is to be used.
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		
		// Setting options- 
		// The content of the source file is given to the parser.
		parser.setSource(content.toCharArray());
		// Telling JDT that the file that is going to be parsed is a class file.(or what is going to be parsed)
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		// Creating the compilation unit.
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		// Question 1, new visitor
		System.out.print("Output 1,\n");
		// Using a visitor pattern for visiting the tree.
		// Creating a visitor class.
		cu.accept(new ASTVisitor() {
			
			// Visiting all the methods declarations.
			@Override
			public boolean visit(MethodDeclaration node) {
				String parameter_name = node.parameters().toString();
				int n = parameter_name.length();
				char[] param_char = parameter_name.toCharArray();
				param_char[0] = '(';
				param_char[n - 1] = ')';
				String method_declaration = String.valueOf(param_char);
				// Using getReturnType2() method to get the return type of the method declaration.
				// And, using getName() method to get the name of the method declaration.
				System.out.println("Method Declaration: " + node.getReturnType2().toString() + " "
						+ node.getName().getFullyQualifiedName() + " " + method_declaration);
				System.out.print("Method calls and Constructors: ");

				node.accept(new ASTVisitor() {

					@Override
					public boolean visit(MethodInvocation mi) {
						// TODO Auto-generated method stub

						System.out.print(mi.getName().getIdentifier().toString() + ","); // +" Receiver expression:
																				// "+node.getExpression());
						return true;
					}

					//@Override
					//public boolean visit(ClassInstanceCreation cic) {
						// TODO Auto-generated method stub
						//System.out.print(cic.getType() + ","); 
						//return true;
					//}

				});

				return true;

			}// End of Method Declaration function
		});// Question 1, end of new visitor
		

		// Question 2, new Visitor
		System.out.print("\n\nOutput 2,");
		cu.accept(new ASTVisitor() {

			@Override
			public boolean visit(MethodDeclaration node) {
				// TODO Auto-generated method stub
				String parameter_name = node.parameters().toString();
				int n = parameter_name.length();
				char[] param_char = parameter_name.toCharArray();
				param_char[0] = '(';
				param_char[n - 1] = ')';
				String method_declaration = String.valueOf(param_char);
				System.out.println("\n\nMethod Declaration: " + node.getReturnType2().toString() + " "
						+ node.getName().getFullyQualifiedName() + " " + method_declaration);
				// System.out.print("Variables: ");

				node.accept(new ASTVisitor() {

					@Override
					public boolean visit(VariableDeclarationFragment var) {
						// TODO Auto-generated method stub
						System.out.print("Variable: " + var.getName().getIdentifier());
						System.out.println(", Line Number: " + (cu.getLineNumber(var.getStartPosition())));
						return true;
					}

				});

				return true;
			}

		});// Question 2, end of new Visitor

		// Question3, start new visitor
		System.out.print("\n\nOutput 3,");
		HashMap<String, List<String>> Methods_Called = new HashMap<String, List<String>>(); 
		List<String> all_variables = new ArrayList<String>(); 
		cu.accept(new ASTVisitor() {

			@Override
			public boolean visit(MethodDeclaration node) {
				// TODO Auto-generated method stub
				String parameter_name = node.parameters().toString();
				int n = parameter_name.length();
				char[] param_char = parameter_name.toCharArray();
				param_char[0] = '(';
				param_char[n - 1] = ')';
				String method_declaration = String.valueOf(param_char);
				System.out.println("\n\nMethod Declaration: " + node.getReturnType2().toString() + " "
						+ node.getName().getFullyQualifiedName() + " " + method_declaration);
				
				node.accept(new ASTVisitor( ) {

					@Override
					public boolean visit(VariableDeclarationFragment node) {
						// TODO Auto-generated method stub
						String get_variable=node.getName().toString(); 
						all_variables.add(get_variable);
						return true;
					}
				
					@Override
					public boolean visit(MethodInvocation node) {
						// TODO Auto-generated method stub
						
						System.out.println("Method Call: "+node.toString( )+", Receiver Variable: "+node.getExpression());
						
						String invoked_method=node.getName().toString();          
						
						String receiver_variable = node.getExpression().toString();          
						List<String> list_of_methodcalls = new ArrayList<String>();
						System.out.println("Methods called on "+receiver_variable+" :"+Methods_Called.get(receiver_variable));
						if(all_variables.contains(receiver_variable))                           
						{
							if(Methods_Called.containsKey(receiver_variable))                     
								//getting all the methods previously invoked by this variable 
								list_of_methodcalls=Methods_Called.get(receiver_variable);             
							list_of_methodcalls.add(invoked_method);                      
							Methods_Called.put(receiver_variable, list_of_methodcalls);       
						}
						System.out.println("\n");
						return true;
					}
					
				});
				
				return true;
			}

		});

		// Question 4, new visitor
		System.out.print("\n\nOutput 4,");
		cu.accept(new ASTVisitor() {

			@Override
			public boolean visit(MethodDeclaration node) {
				String parameter_name = node.parameters().toString();
				int n = parameter_name.length();
				char[] param_char = parameter_name.toCharArray();
				param_char[0] = '(';
				param_char[n - 1] = ')';
				String method_declaration = String.valueOf(param_char);
				System.out.println("\nMethod Declaration: " + node.getReturnType2().toString() + " "
						+ node.getName().getFullyQualifiedName() + " " + method_declaration);

				return true;
			}

			@Override
			public void endVisit(CatchClause node) {
				System.out.println("Exceptions: " + node.getException().getType());
			}

		});// Question 4, end of new visitor

		// we need to parse the content
	}// end of run()

	public void run1() throws IOException {

		// we need to read the content of the file
		String content = FileUtils
				.readFileToString(new File("C:\\Users\\jaish\\eclipse-workspace\\JDT-SE\\src\\Excep.java")); 

		// we need to create a parser object
		ASTParser parser = ASTParser.newParser(AST.JLS8);

		// we need to give the content to the parser
		parser.setSource(content.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		cu.accept(new ASTVisitor() {

			@Override
			public boolean visit(MethodDeclaration node) {
				String parameter_name = node.parameters().toString();
				int n = parameter_name.length();
				char[] param_char = parameter_name.toCharArray();
				param_char[0] = '(';
				param_char[n - 1] = ')';
				String method_declaration = String.valueOf(param_char);
				System.out.println("\n\nMethod Declaration: " + node.getReturnType2().toString() + " "
						+ node.getName().getFullyQualifiedName() + " " + method_declaration);

				return true;
			}

			@Override
			public void endVisit(CatchClause node) {
				System.out.println("Exceptions: " + node.getException().getType());
			}

		});

		// we need to parse the content
	}

	public void run2() throws IOException {

		// we need to read the content of the file
		String content = FileUtils
				.readFileToString(new File("C:\\Users\\jaish\\eclipse-workspace\\JDT-SE\\src\\Excep.java")); // replace
																												// the
																												// path
																												// of
																												// the
																												// file
																												// here

		// we need to create a parser object
		ASTParser parser = ASTParser.newParser(AST.JLS8);

		// we need to give the content to the parser
		parser.setSource(content.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		cu.accept(new ASTVisitor() {
		});

		// we need to parse the content
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Test driver = new Test();
		try {
			driver.run();
			// driver.run2();
			// driver.run1();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
