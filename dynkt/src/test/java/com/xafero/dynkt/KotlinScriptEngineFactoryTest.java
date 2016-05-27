package com.xafero.dynkt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class KotlinScriptEngineFactoryTest {

	private static ScriptEngineFactory factory;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ScriptEngineManager mgr = new ScriptEngineManager();
		factory = mgr.getEngineByExtension("kts").getFactory();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		factory = null;
	}

	@Test
	public void testGetEngineName() {
		assertEquals("Kotlin Compiler", factory.getEngineName());
	}

	@Test
	public void testGetEngineVersion() {
		assertEquals("1.0.2", factory.getEngineVersion());
	}

	@Test
	public void testGetLanguageName() {
		assertEquals("Kotlin", factory.getLanguageName());
	}

	@Test
	public void testGetLanguageVersion() {
		assertEquals("1.0.2", factory.getLanguageVersion());
	}

	@Test
	public void testGetNames() {
		assertEquals("[kotlin]", factory.getNames() + "");
	}

	@Test
	public void testGetMimeTypes() {
		assertEquals("[text/x-kotlin]", factory.getMimeTypes() + "");
	}

	@Test
	public void testGetExtensions() {
		assertEquals("[kts, kt]", factory.getExtensions() + "");
	}

	@Test
	public void testGetMethodCallSyntax() {
		assertEquals("adder.add(42, b)", factory.getMethodCallSyntax("adder", "add", "42", "b"));
	}

	@Test
	public void testGetOutputStatement() {
		assertEquals("print(\"Hello\")", factory.getOutputStatement("Hello"));
	}

	@Test
	public void testGetParameter() {
		assertEquals("1.0.2", factory.getParameter(ScriptEngine.LANGUAGE_VERSION));
	}

	@Test
	public void testGetProgram() {
		assertEquals("int c = a + b|float d = 42.0|", factory.getProgram("int c = a + b", "float d = 42.0")
				.replace(System.getProperty("line.separator"), '|' + "").trim());
	}

	@Test
	public void testGetScriptEngine() {
		ScriptEngine engine;
		assertNotNull(engine = factory.getScriptEngine());
		assertTrue(engine instanceof KotlinScriptEngine);
	}
}