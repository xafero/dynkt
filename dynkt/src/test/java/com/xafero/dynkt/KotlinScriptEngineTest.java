package com.xafero.dynkt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class KotlinScriptEngineTest {

	private static ScriptEngine engine;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ScriptEngineManager mgr = new ScriptEngineManager();
		engine = mgr.getEngineByExtension("kts");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		engine = null;
	}

	private InputStreamReader getStream(String name) throws FileNotFoundException {
		InputStream res = getClass().getResourceAsStream(name);
		if (res == null)
			res = new FileInputStream("src/test/java/" + "com/xafero/dynkt/" + name);
		return new InputStreamReader(res);
	}

	@Test
	public void testEvalFromFile() throws Exception {
		Bindings bnd = engine.createBindings();
		bnd.put(ScriptEngine.ARGV, new String[] { "." });
		InputStreamReader code = getStream("script.kts");
		Object result = engine.eval(code, bnd);
		assertNotNull(result);
		assertEquals("getFolders", result.getClass().getDeclaredMethods()[0].getName());
	}

	@Test
	public void testEvalFromFile2() throws FileNotFoundException, ScriptException {
		InputStreamReader code = getStream("hello1.kt");
		StringWriter out;
		Bindings bnd = engine.createBindings();
		bnd.put("out", new PrintWriter(out = new StringWriter()));
		Object result = engine.eval(code, bnd);
		assertNotNull(result);
		assertEquals("main", result.getClass().getDeclaredMethods()[0].getName());
		assertEquals("Hello, World!", out.toString().trim());
	}

	@Test
	public void testCompileFromFile() throws Exception {
		InputStreamReader code = getStream("tree.kts");
		CompiledScript script = ((Compilable) engine).compile(code);
		Bindings bnd = engine.createBindings();
		Object result = script.eval(bnd);
		assertNotNull(result);
		assertEquals("tree", result.getClass().getDeclaredMethods()[0].getName());
	}

	@Test
	public void testGetFactory() {
		ScriptEngineFactory factory;
		assertNotNull(factory = engine.getFactory());
		assertTrue(factory instanceof KotlinScriptEngineFactory);
	}

	@Test
	public void testCreateBindings() {
		assertNotNull(engine.createBindings());
	}
}