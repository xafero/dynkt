package com.xafero.dynkt.core;

import java.util.LinkedList;
import java.util.List;

import org.jetbrains.kotlin.descriptors.ScriptDescriptor;
import org.jetbrains.kotlin.name.Name;
import org.jetbrains.kotlin.psi.KtScript;
import org.jetbrains.kotlin.script.KotlinScriptDefinition;
import org.jetbrains.kotlin.script.ScriptParameter;
import org.jetbrains.kotlin.script.StandardScriptDefinition;

import com.intellij.psi.PsiFile;

public class MemoryScriptDefinition implements KotlinScriptDefinition {

	private KotlinScriptDefinition base;
	private List<ScriptParameter> params;

	public MemoryScriptDefinition() {
	}

	@Override
	public Name getScriptName(KtScript script) {
		return StandardScriptDefinition.INSTANCE.getScriptName(script);
	}

	@Override
	public boolean isScript(PsiFile file) {
		return StandardScriptDefinition.INSTANCE.isScript(file);
	}

	@Override
	public List<ScriptParameter> getScriptParameters(ScriptDescriptor desc) {
		List<ScriptParameter> tmp = new LinkedList<ScriptParameter>();
		tmp.addAll(base.getScriptParameters(desc));
		tmp.addAll(params);
		return tmp;
	}

	public void inject(KotlinScriptDefinition base) {
		this.base = base;
	}

	public void inject(List<ScriptParameter> params) {
		this.params = params;
	}
}