package com.xafero.dynkt.core;

import java.io.File;
import java.io.FileWriter;

import javax.script.Bindings;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.apache.commons.io.IOUtils;

import com.xafero.dynkt.KotlinScriptEngine;

public class KotlinCompiledScript extends CompiledScript {

	private final KotlinScriptEngine engine;
	private final String script;
	private final File file;

	private Class<?> cachedClazz;

	public KotlinCompiledScript(KotlinScriptEngine engine, String script, File file) {
		this.engine = engine;
		this.script = script;
		this.file = file;
	}

	@Override
	public Object eval(ScriptContext context) throws ScriptException {
		// Only compile if necessary (should be only the first time!)
		if (cachedClazz == null) {
			try {
				FileWriter out;
				IOUtils.write(script, out = new FileWriter(file));
				out.flush();
				out.close();
				cachedClazz = engine.compileScript(file);
			} catch (Exception e) {
				throw new ScriptException(e);
			}
		}
		// Evaluate it
		Bindings bnd = context.getBindings(ScriptContext.ENGINE_SCOPE);
		bnd.put(ScriptEngine.FILENAME, file.getAbsolutePath());
		return engine.evalClass(cachedClazz, bnd);
	}

	@Override
	public ScriptEngine getEngine() {
		return engine;
	}
}