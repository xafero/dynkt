package com.xafero.dynkt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStreamReader;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

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

	private InputStreamReader getStream(String name) {
		return new InputStreamReader(getClass().getResourceAsStream(name));
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