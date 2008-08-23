/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *

 *******************************************************************************/
package org.eclipse.dltk.ruby.core.tests.rewriter;

import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.core.tests.model.AbstractModelTests;
import org.eclipse.dltk.ruby.core.tests.Activator;
import org.eclipse.dltk.ruby.internal.parsers.jruby.ASTUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author mhowe
 * 
 * The tests in this test case are intended to test class def's, type ref's, method def's, method ref's, block's, variable def's, variable ref's, hash
 * expressions (very common in Ruby), expressions or statement. The tests don't go into detail for specific AST's, such as for, if etc. The intent here is to
 * define the common top level AST's required for a majority of work with a rewriter, such as fix ups, re-factorings, renames, insertions/removal of high level
 * elements at various points etc.
 * 
 * The tests are intended to exercise the ability to locate a specific AST (such as a block, method def, statement etc) and then modify that AST or insert a new
 * AST before or after the discovered AST. These are common use cases for modifying code from an AST perspective.
 * 
 * The first pass of this test case uses ModuleDeclaration as a starting point but this is for demonstration only, no restriction on the API is suggested by
 * this. ModuleDeclaration should be replaced with whatever is appropriate from the real API once created. These tests are fairly simple either using ruby files
 * defined in the associated workspace or if the test is simple enough from strings in the test itself. Also many of tests so far are just method stubs with
 * descriptive names to be filled in as the rewriter evolves. All tests require the rewriter API to be filled in. The checkResults method always fails, this
 * requires converting an AST to a string and then comparing with the expected result.
 * 
 * Once this stage of the rewriter is complete more test cases should be written which test the specific detailed AST's not covered by the tests in this test
 * case.
 */
public class RewriterTests extends AbstractModelTests {

	private static final String PATH_PREFIX = "/workspace/rewriter/";
	private static final String SRC_PROJECT = "rewriter";

	public RewriterTests(String name) {
		super(Activator.PLUGIN_ID, name);
	}

	public void setUpSuite() throws Exception {
		setUpScriptProject(SRC_PROJECT);
		super.setUpSuite();
		waitUntilIndexesReady();
		ResourcesPlugin.getWorkspace().build(IncrementalProjectBuilder.FULL_BUILD, new NullProgressMonitor());
	}

	public void tearDownSuite() throws Exception {
		deleteProject(SRC_PROJECT);
		super.tearDownSuite();
	}

	///////////////////////////////////////////////////////
	//Series of tests for adding various types of AST's
	///////////////////////////////////////////////////////

	/**
	 * Start with empty file and a header comment
	 * 
	 */
	public void testAddCommentToEmptyScript() throws IOException {
		String content = loadContent(PATH_PREFIX + "empty_script.rb");
		ModuleDeclaration ast = ASTUtils.getAST(content.toCharArray());

		String[] header = new String[] {
				"###############################################################################\n",
				"# Copyright (c) 2005, 2007 IBM Corporation and others.\n",
				"# All rights reserved. This program and the accompanying materials\n",
				"# are made available under the terms of the Eclipse Public License v1.0\n",
				"# which accompanies this distribution, and is available at\n",
				"# http://www.eclipse.org/legal/epl-v10.html\n",
				"#\n",
				"###############################################################################\n"
		};
		//add header to to ast

		checkResults(ast, loadContent(PATH_PREFIX + "empty_script_with_header.rb"));
	}

	/**
	 * Start with empty file and add class declaration
	 * 
	 */
	public void testAddSimpleClassToEmptyScript() throws IOException {
		String content = loadContent(PATH_PREFIX + "empty_script.rb");
		ModuleDeclaration ast = ASTUtils.getAST(content.toCharArray());
		//modify ast
		//add class "Simple" after comment
		checkResults(ast, loadContent(PATH_PREFIX + "simple_class.rb"));
	}

	/**
	 * Start with empty file and add class declaration
	 * 
	 */
	public void testAddClassAfterHeader() throws IOException {
		String content = loadContent(PATH_PREFIX + "empty_script_with_header.rb");
		ModuleDeclaration ast = ASTUtils.getAST(content.toCharArray());
		//modify ast
		//add class "Simple" after comment
		checkResults(ast, loadContent(PATH_PREFIX + "simple_class_with_header.rb"));
	}

	/**
	 */
	public void testAddInnerClass() throws IOException {
		String content = loadContent(PATH_PREFIX + "simple_class.rb");
		ModuleDeclaration ast = ASTUtils.getAST(content.toCharArray());
		//modify ast
		//classAst.newClass("Inner");
		checkResults(ast, loadContent(PATH_PREFIX + "simple_nested_class.rb"));
	}

	/**
	 * Insert new method before first method
	 * 
	 * @throws IOException
	 */
	public void testInsertMethodBeforeFirstMethod() throws IOException {
		String content = loadContent(PATH_PREFIX + "simple1.rb");
		ModuleDeclaration ast = ASTUtils.getAST(content.toCharArray());
		//modify ast
		//insert new_method as first method
		checkResults(ast, loadContent(PATH_PREFIX + "simple1a.result"));
	}

	/**
	 * Insert method after specific method
	 * 
	 * @throws IOException
	 */
	public void testInsertMethodAfterMethod() throws IOException {
		String content = loadContent(PATH_PREFIX + "simple1.rb");
		ModuleDeclaration ast = ASTUtils.getAST(content.toCharArray());
		//modify ast
		//insert new_method after method m1
		checkResults(ast, loadContent(PATH_PREFIX + "simple1b.result"));
	}

	/**
	 * Insert method before specific method
	 * 
	 * @throws IOException
	 */
	public void testInsertMethodBeforeMethod() throws IOException {
		String content = loadContent(PATH_PREFIX + "simple1.rb");
		ModuleDeclaration ast = ASTUtils.getAST(content.toCharArray());
		//modify ast
		//insert new_method before method m2
		checkResults(ast, loadContent(PATH_PREFIX + "simple1b.result"));
	}

	/**
	 * Append method after last method
	 * 
	 * @throws IOException
	 */
	public void testAppendMethod() throws IOException {
		String content = loadContent(PATH_PREFIX + "simple1.rb");
		ModuleDeclaration ast = ASTUtils.getAST(content.toCharArray());
		//modify ast
		//append new_method after last method
		checkResults(ast, loadContent(PATH_PREFIX + "simple1c.result"));
	}

	/**
	 * Tests discovering a block and then inserting a method call as the first call in that block
	 */
	public void testInsertAsFirstMethodCallInBlock() {
		assertTrue(false);
	}

	/**
	 * Tests discovering a block and then inserting a method call as the last call in that block
	 */
	public void testInsertAsLastMethodCallInBlock() {
		assertTrue(false);
	}

	/**
	 * Tests discovering a method definition and then inserting a method call as the first call in that block
	 */
	public void testInsertAsFirstMethodCallInMethodDef() {
		assertTrue(false);
	}

	/**
	 * Tests discovering a method definition and then inserting a method call as the last call in that block
	 */
	public void testInsertAsLastMethodCallInMetohdDef() {
		assertTrue(false);
	}

	/**
	 * Tests discovering a specific statement and then inserting a method call before that statement
	 */
	public void testMethodCallBeforeStatement() {
		assertTrue(false);
	}

	/**
	 * Tests discovering a specific statement and then inserting a method call after that statement
	 */
	public void testMethodCallAfterStatement() {
		assertTrue(false);
	}

	/**
	 * Tests adding a key/value pair to a hash expression
	 */
	public void testAddKeyValuePairToHash() {
		assertTrue(false);
	}

	/**
	 * Tests inserting a new parameter as the first parameter to a method def
	 */
	public void testInsertFirstParameterToMethodDef() {
		assertTrue(false);
	}

	/**
	 * Tests inserting a new parameter as the last parameter to a method def
	 */
	public void testInsertLastParameterToMethodDef() {
		assertTrue(false);
	}

	/**
	 * Tests inserting a before a block in a method def
	 */
	public void testInsertBeforeBlockInMethodDef() {
		assertTrue(false);
	}

	/**
	 * Tests inserting a parameter as a specific parameter in a method def, i.e. test adding a parameter between parameter 0 and parameter 1
	 */
	public void testInsertParameterInSpecificPosInMethodDef() {
		assertTrue(false);
	}

	/**
	 * Tests inserting a parameter as the first parameter in a method call
	 */
	public void testInsertFirstParameterToMethodCall() {
		assertTrue(false);
	}

	/**
	 * Tests inserting a parameter as the last parameter in a method call
	 */
	public void testInsertLastParameterToMethodCall() {
		assertTrue(false);
	}

	/**
	 * Tests inserting a parameter before the block in a method call (similar to appending a parameter as the last parameter but specifically tests the case where
	 * a block is used)
	 */
	public void testInsertBeforeBlockInMethodCall() {
		assertTrue(false);
	}

	/**
	 * Tests inserting a parameter as a specific parameter in a method call, i.e. test adding a parameter between parameter 0 and parameter 1
	 */
	public void testInsertParameterInSpecificPosInMethodCall() {
		assertTrue(false);
	}

	///////////////////////////////////////////////////////
	//Series of tests for modifying references
	///////////////////////////////////////////////////////
	/**
	 * Change variable reference
	 */
	public void testChangeClassVariableReference() {
		String content = "@@class_var = 'hello'";
		ModuleDeclaration ast = ASTUtils.getAST(content.toCharArray());
		//change @var to @new_var
		checkResults(ast, "@@new_class_var = 'hello')");
	}

	/**
	 * Change variable reference
	 */
	public void testChangeInstanceVariableReference() {
		String content = "puts(@instance_var = 'hello')";
		ModuleDeclaration ast = ASTUtils.getAST(content.toCharArray());
		//change @var to @new_var
		checkResults(ast, "puts(@new_instance_var = 'hello')");
	}

	/**
	 * Change variable reference
	 */
	public void testChangeVariableReference() {
		String content = "puts(var = 'hello')";
		ModuleDeclaration ast = ASTUtils.getAST(content.toCharArray());
		//change @var to @new_var
		checkResults(ast, "puts(new_var = 'hello')");
	}

	/**
	 * Change type reference
	 */
	public void testChangeTypeReference() {
		String content = "@var = Stringx.new";
		ModuleDeclaration ast = ASTUtils.getAST(content.toCharArray());
		//change type reference Stringx to String 
		checkResults(ast, "@var = String.new");
	}

	/**
	 * Change method reference
	 */
	public void testChangeMethodReference() {
		String content = "@var = String.newx";
		ModuleDeclaration ast = ASTUtils.getAST(content.toCharArray());
		//change method reference newx to new
		checkResults(ast, "@var = String.new");
	}

	/**
	 * Tests changing a symbol reference
	 */
	public void testChangeSymbolReference() {
		String content = "var = :my_symbol";
		ModuleDeclaration ast = ASTUtils.getAST(content.toCharArray());
		//change symbol ref from :my_symbol to :your_symbol
		checkResults(ast, "var = :your_symbol");
	}

	/**
	 * Tests changing a string literal reference
	 */
	public void testChangeStringLiteral() {
		String content = "var = 'my_string'";
		ModuleDeclaration ast = ASTUtils.getAST(content.toCharArray());
		//modify ast
		checkResults(ast, "var = 'your_string'");
	}

	//TODO
	public void testChangeNumericLiteral() throws IOException {
		String content = "var = 123";
		ModuleDeclaration ast = ASTUtils.getAST(content.toCharArray());
		//modify ast
		checkResults(ast, "var = 4567");
	}

	/**
	 * Test adding parenthesis around a method call (tests finding all the parameters to a call)
	 */
	public void testAddParenthesisAroundMethodCall() {
		assertTrue(false);
	}

	///////////////////////////////////////////////////////
	//Series of tests for modifying whitespace and delimeters
	///////////////////////////////////////////////////////
	public void testRemoveLeadingWhiteSpace() {
		assertTrue(false);
	}

	public void testRemoveTrailingWhiteSpace() {
		assertTrue(false);
	}

	public void testModifyLeadingWhiteSpace() {
		assertTrue(false);
	}

	public void testModifyTrailingWhiteSpace() {
		assertTrue(false);
	}

	public void testCovertBlockFromBraceToDoEnd() {
		assertTrue(false);
	}

	public void testConvertBlockFromDoEndToBrace() {
		assertTrue(false);
	}

	///////////////////////////////////////////////////////
	//Series of tests for removing various types of AST's
	///////////////////////////////////////////////////////

	/**
	 * Tests discovering and removing a class
	 */
	public void testRemoveClass() {
		assertTrue(false);
	}

	/**
	 * Tests discovering and removing a method def
	 */
	public void testRemoveMethodDef() {
		assertTrue(false);
	}

	/**
	 * Tests discovering and removing a block
	 */
	public void testRemoveBlock() {
		assertTrue(false);
	}

	/**
	 * Tests discovering and removing a statement
	 */
	public void testRemoveStatement() {
		assertTrue(false);
	}

	/**
	 * Tests discovering and removing a parameter from a method call
	 */
	public void testRemoveParameterFromMethodCall() {
		assertTrue(false);
	}

	/**
	 * Tests discovering and removing a parameter from a method def
	 */
	public void testRemoveParameterFromMethodDef() {
		assertTrue(false);
	}

	/**
	 * Tests discovering and removing a hash parameter from a method call
	 */
	public void testRemoveHashParameterFromMethodCall() {
		assertTrue(false);
	}

	private String getContent(ModuleDeclaration ast) {
		//TODO - get string from AST.
		return null;
	}

	//This assumes some way of resolving the formatted text from an AST
	private void checkResults(ModuleDeclaration ast, String contents) {
		assertTrue(false);
	}

	private String loadContent(String path) throws IOException {
		StringBuffer buffer = new StringBuffer();
		InputStream input = null;
		try {
			input = Activator.openResource(path);
			InputStreamReader reader = new InputStreamReader(input);

			char[] cbuf = new char[1024 * 16];
			while (reader.ready() == true) {
				int read = reader.read(cbuf);
				buffer.append(cbuf, 0, read);
			}
		}
		finally {
			if (input != null) {
				input.close();
			}
		}
		String content = buffer.toString();
		return content;
	}

}
