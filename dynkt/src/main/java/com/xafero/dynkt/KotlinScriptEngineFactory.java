package com.xafero.dynkt;

import java.util.Arrays;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

import org.jetbrains.kotlin.cli.common.KotlinVersion;

public class KotlinScriptEngineFactory implements ScriptEngineFactory {

	private static final String engineName = "Kotlin Compiler";
	private static final String engineVer = KotlinVersion.VERSION;
	private static final String langName = "Kotlin";
	private static final String langVer = KotlinVersion.VERSION;
	private static final String shortName = "Kotlin";
	private static final List<String> names = Arrays.asList("kotlin");
	private static final List<String> mimeTypes = Arrays.asList("text/x-kotlin");
	private static final List<String> extensions = Arrays.asList("kts", "kt");

	@Override
	public String getEngineName() {
		return engineName;
	}

	@Override
	public String getEngineVersion() {
		return engineVer;
	}

	@Override
	public String getLanguageName() {
		return langName;
	}

	@Override
	public String getLanguageVersion() {
		return langVer;
	}

	@Override
	public List<String> getNames() {
		return names;
	}

	@Override
	public List<String> getMimeTypes() {
		return mimeTypes;
	}

	@Override
	public List<String> getExtensions() {
		return extensions;
	}

	@Override
	public ScriptEngine getScriptEngine() {
		return new KotlinScriptEngine(this);
	}

	@Override
	public Object getParameter(String key) {
		if (key.equals(ScriptEngine.ENGINE))
			return getEngineName();
		if (key.equals(ScriptEngine.ENGINE_VERSION))
			return getEngineVersion();
		if (key.equals(ScriptEngine.NAME))
			return shortName;
		if (key.equals(ScriptEngine.LANGUAGE))
			return getLanguageName();
		if (key.equals(ScriptEngine.LANGUAGE_VERSION))
			return getLanguageVersion();
		return null;
	}

	@Override
	public String getMethodCallSyntax(String obj, String m, String... args) {
		String argStr = Arrays.toString(args);
		argStr = argStr.substring(1, argStr.length() - 1);
		return obj + "." + m + "(" + argStr + ")";
	}

	@Override
	public String getOutputStatement(String toDisplay) {
		return "print(\"" + toDisplay + "\")";
	}

	@Override
	public String getProgram(String... statements) {
		String sep = System.getProperty("line.separator");
		StringBuilder bld = new StringBuilder();
		for (String statement : statements)
			bld.append(statement).append(sep);
		return bld.toString();
	}
}