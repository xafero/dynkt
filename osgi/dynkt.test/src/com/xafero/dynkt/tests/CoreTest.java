package com.xafero.dynkt.tests;

import static com.xafero.dynkt.tests.OsgiUtil.find;
import static com.xafero.dynkt.tests.OsgiUtil.getClassloader;
import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.xafero.dynkt.TestActivator;

public class CoreTest {

	private static ClassLoader loader;
	private static Bundle engBundle;

	@BeforeClass
	public static void setup() {
		BundleContext ctx = TestActivator.getContext();
		loader = getClassloader(ctx);
		engBundle = find(ctx.getBundles(), "com.xafero.dynkt");
	}

	@Test
	public void testScriptEngineBundle() {
		assertEquals(4, engBundle.getState());
	}

	@Test
	public void testScriptEngineWithoutLoader() {
		ScriptEngineManager mgr = new ScriptEngineManager();
		List<ScriptEngineFactory> factories = mgr.getEngineFactories();
		assertEquals(1, factories.size());
		assertEquals("ECMAScript", factories.get(0).getLanguageName());
	}

	@Test
	public void testScriptEngineWithLoader() {
		ScriptEngineManager mgr = new ScriptEngineManager(loader);
		assertEquals(0, mgr.getEngineFactories().size());
	}

	@AfterClass
	public static void teardown() {
		loader = null;
		engBundle = null;
	}
}