package com.xafero.dynkt.core;

import static org.jetbrains.kotlin.cli.jvm.config.JvmContentRootsKt.addJvmClasspathRoots;
import static org.jetbrains.kotlin.config.ContentRootsKt.addKotlinSourceRoot;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.jetbrains.kotlin.builtins.DefaultBuiltIns;
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys;
import org.jetbrains.kotlin.cli.common.arguments.K2JVMCompilerArguments;
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageLocation;
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity;
import org.jetbrains.kotlin.cli.common.messages.MessageCollector;
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler;
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles;
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment;
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinToJVMBytecodeCompiler;
import org.jetbrains.kotlin.cli.jvm.config.JVMConfigurationKeys;
import org.jetbrains.kotlin.config.CommonConfigurationKeys;
import org.jetbrains.kotlin.config.CompilerConfiguration;
import org.jetbrains.kotlin.name.Name;
import org.jetbrains.kotlin.script.KotlinScriptDefinition;
import org.jetbrains.kotlin.script.ScriptParameter;
import org.jetbrains.kotlin.script.StandardScriptDefinition;
import org.jetbrains.kotlin.types.KotlinType;
import org.jetbrains.kotlin.utils.KotlinPaths;
import org.jetbrains.kotlin.utils.PathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intellij.openapi.Disposable;
import com.xafero.dynkt.util.ReflUtils;

public class KotlinCompiler implements MessageCollector, Disposable {

	private static final Logger log = LoggerFactory.getLogger("kc");

	private final KotlinPaths paths;
	private final List<String> configPaths;

	public KotlinCompiler() {
		this.paths = PathUtil.getKotlinPathsForCompiler();
		this.configPaths = EnvironmentConfigFiles.JVM_CONFIG_FILES;
	}

	public Class<?> compileScript(File file) {
		log.info("Compiling '{}'...", file);
		CompilerConfiguration config = createCompilerConfig(file);
		config = addCurrentClassPath(config);
		KotlinCoreEnvironment env = KotlinCoreEnvironment.createForProduction(this, config, configPaths);
		return KotlinToJVMBytecodeCompiler.INSTANCE.compileScript(config, paths, env);
	}

	@SuppressWarnings("unchecked")
	private CompilerConfiguration addCurrentClassPath(CompilerConfiguration config) {
		K2JVMCompilerArguments cmpArgs = new K2JVMCompilerArguments();
		cmpArgs.classpath = System.getProperty("java.class.path");
		cmpArgs.noStdlib = true;
		addJvmClasspathRoots(config, (List<File>) ReflUtils.invoke(K2JVMCompiler.Companion.class, null,
				"access$getClasspath", K2JVMCompiler.Companion, paths, cmpArgs));
		return config;
	}

	private CompilerConfiguration createCompilerConfig(File file) {
		CompilerConfiguration config = new CompilerConfiguration();
		config.put(CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY, this);
		// Put arguments as field
		MemoryScriptDefinition memDef = new MemoryScriptDefinition();
		memDef.inject(StandardScriptDefinition.INSTANCE);
		// Bundle injection
		List<ScriptParameter> scriptParams = new LinkedList<ScriptParameter>();
		KotlinType type = DefaultBuiltIns.getInstance().getMutableMap().getDefaultType();
		Name ctxName = Name.identifier("ctx");
		scriptParams.add(new ScriptParameter(ctxName, type));
		memDef.inject(scriptParams);
		// Set definitions
		List<KotlinScriptDefinition> scriptDefs = new LinkedList<KotlinScriptDefinition>();
		scriptDefs.add(memDef);
		// Finish configuration
		config.put(JVMConfigurationKeys.MODULE_NAME, "dynamic");
		config.put(CommonConfigurationKeys.SCRIPT_DEFINITIONS_KEY, scriptDefs);
		addJvmClasspathRoots(config, PathUtil.getJdkClassesRoots());
		addKotlinSourceRoot(config, file.getAbsolutePath());
		return config;
	}

	@Override
	public void report(CompilerMessageSeverity severity, String message, CompilerMessageLocation location) {
		switch (severity) {
		case ERROR:
		case EXCEPTION:
			log.error(message + " " + location);
			break;
		case INFO:
			log.info(message + " " + location);
			break;
		case WARNING:
			log.warn(message + " " + location);
			break;
		default:
			log.debug(message + " " + location);
			break;
		}
	}

	@Override
	public void dispose() {
		log.info("Disposed.");
	}
}