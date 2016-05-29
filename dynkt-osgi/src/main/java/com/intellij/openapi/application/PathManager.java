/*
 * Copyright 2000-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.openapi.application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.SystemProperties;
import com.intellij.util.io.URLUtil;

public class PathManager {
	private static final String PATHS_SELECTOR = System.getProperty("idea.paths.selector");
	private static final Pattern PROPERTY_REF = Pattern.compile("\\$\\{(.+?)}");
	private static String ourHomePath;
	private static String ourConfigPath;
	private static String ourSystemPath;
	private static String ourPluginsPath;

	@NotNull
	public static String getHomePath() {
		if (ourHomePath != null) {
			String tmp9_6 = ourHomePath;
			if (tmp9_6 == null) {
				throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null",
						new Object[] { "com/intellij/openapi/application/PathManager", "getHomePath" }));
			}
			return tmp9_6;
		}
		String fromProperty = System.getProperty("idea.home.path", System.getProperty("idea.home"));
		if (fromProperty != null) {
			ourHomePath = getAbsolutePath(fromProperty);
			if (!new File(ourHomePath).isDirectory()) {
				throw new RuntimeException("Invalid home path '" + ourHomePath + "'");
			}
		} else {
			ourHomePath = getHomePathFor(PathManager.class);
			if (ourHomePath == null) {
				String advice = SystemInfo.isMac ? "reinstall the software."
						: "make sure bin/idea.properties is present in the installation directory.";

				throw new RuntimeException("Could not find installation home path. Please " + advice);
			}
		}
		if (SystemInfo.isWindows) {
			try {
				ourHomePath = new File(ourHomePath).getCanonicalPath();
			} catch (IOException localIOException) {
			}
		}
		String tmp200_197 = ourHomePath;
		if (tmp200_197 == null) {
			throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null",
					new Object[] { "com/intellij/openapi/application/PathManager", "getHomePath" }));
		}
		return tmp200_197;
	}

	@Nullable
	public static String getHomePathFor(@NotNull Class aClass) {
		if (aClass == null) {
			throw new IllegalArgumentException(String.format(
					"Argument for @NotNull parameter '%s' of %s.%s must not be null",
					new Object[] { "aClass", "com/intellij/openapi/application/PathManager", "getHomePathFor" }));
		}
		String rootPath = getResourceRoot(aClass, "/" + aClass.getName().replace('.', '/') + ".class");
		if (rootPath == null) {
			return null;
		}
		File root = new File(rootPath).getAbsoluteFile();
		do {
			String parent = root.getParent();
			if (parent == null) {
				return null;
			}
			root = new File(parent).getAbsoluteFile();
		} while (!isIdeaHome(root));
		return root.getAbsolutePath();
	}

	private static boolean isIdeaHome(File root) {
		return (new File(root, FileUtil.toSystemDependentName("bin/idea.properties")).exists())
				|| (new File(root,
						FileUtil.toSystemDependentName("bin/" + getOSSpecificBinSubdir() + "/" + "idea.properties"))
								.exists())
				|| (new File(root, FileUtil.toSystemDependentName("community/bin/idea.properties")).exists());
	}

	@NotNull
	public static String getBinPath() {
		String tmp27_21 = (getHomePath() + File.separator + "bin");
		if (tmp27_21 == null) {
			throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null",
					new Object[] { "com/intellij/openapi/application/PathManager", "getBinPath" }));
		}
		return tmp27_21;
	}

	private static String getOSSpecificBinSubdir() {
		if (SystemInfo.isWindows) {
			return "win";
		}
		if (SystemInfo.isMac) {
			return "mac";
		}
		return "linux";
	}

	@NotNull
	public static String getLibPath() {
		String tmp27_21 = (getHomePath() + File.separator + "lib");
		if (tmp27_21 == null) {
			throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null",
					new Object[] { "com/intellij/openapi/application/PathManager", "getLibPath" }));
		}
		return tmp27_21;
	}

	@NotNull
	public static String getPreInstalledPluginsPath() {
		String tmp27_21 = (getHomePath() + File.separatorChar + "plugins");
		if (tmp27_21 == null) {
			throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null",
					new Object[] { "com/intellij/openapi/application/PathManager", "getPreInstalledPluginsPath" }));
		}
		return tmp27_21;
	}

	@NotNull
	public static String getConfigPath() {
		if (ourConfigPath != null) {
			String tmp9_6 = ourConfigPath;
			if (tmp9_6 == null) {
				throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null",
						new Object[] { "com/intellij/openapi/application/PathManager", "getConfigPath" }));
			}
			return tmp9_6;
		}
		if (System.getProperty("idea.config.path") != null) {
			ourConfigPath = getAbsolutePath(trimPathQuotes(System.getProperty("idea.config.path")));
		} else if (PATHS_SELECTOR != null) {
			ourConfigPath = getDefaultConfigPathFor(PATHS_SELECTOR);
		} else {
			ourConfigPath = getHomePath() + File.separator + "config";
		}
		String tmp120_117 = ourConfigPath;
		if (tmp120_117 == null) {
			throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null",
					new Object[] { "com/intellij/openapi/application/PathManager", "getConfigPath" }));
		}
		return tmp120_117;
	}

	@NotNull
	public static String getDefaultConfigPathFor(@NotNull String selector) {
		if (selector == null) {
			throw new IllegalArgumentException(
					String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] {
							"selector", "com/intellij/openapi/application/PathManager", "getDefaultConfigPathFor" }));
		}
		String tmp48_45 = platformPath(selector, "Library/Preferences", "config");
		if (tmp48_45 == null) {
			throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null",
					new Object[] { "com/intellij/openapi/application/PathManager", "getDefaultConfigPathFor" }));
		}
		return tmp48_45;
	}

	@NotNull
	public static String getPluginsPath() {
		if (ourPluginsPath != null) {
			String tmp9_6 = ourPluginsPath;
			if (tmp9_6 == null) {
				throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null",
						new Object[] { "com/intellij/openapi/application/PathManager", "getPluginsPath" }));
			}
			return tmp9_6;
		}
		if (System.getProperty("idea.plugins.path") != null) {
			ourPluginsPath = getAbsolutePath(trimPathQuotes(System.getProperty("idea.plugins.path")));
		} else if ((SystemInfo.isMac) && (PATHS_SELECTOR != null)) {
			ourPluginsPath = SystemProperties.getUserHome() + File.separator + "Library/Application Support"
					+ File.separator + PATHS_SELECTOR;
		} else {
			ourPluginsPath = getConfigPath() + File.separatorChar + "plugins";
		}
		String tmp159_156 = ourPluginsPath;
		if (tmp159_156 == null) {
			throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null",
					new Object[] { "com/intellij/openapi/application/PathManager", "getPluginsPath" }));
		}
		return tmp159_156;
	}

	@NotNull
	public static String getSystemPath() {
		if (ourSystemPath != null) {
			String tmp9_6 = ourSystemPath;
			if (tmp9_6 == null) {
				throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null",
						new Object[] { "com/intellij/openapi/application/PathManager", "getSystemPath" }));
			}
			return tmp9_6;
		}
		if (System.getProperty("idea.system.path") != null) {
			ourSystemPath = getAbsolutePath(trimPathQuotes(System.getProperty("idea.system.path")));
		} else if (PATHS_SELECTOR != null) {
			ourSystemPath = platformPath(PATHS_SELECTOR, "Library/Caches", "system");
		} else {
			ourSystemPath = getHomePath() + File.separator + "system";
		}
		checkAndCreate(ourSystemPath, true);
		String tmp132_129 = ourSystemPath;
		if (tmp132_129 == null) {
			throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null",
					new Object[] { "com/intellij/openapi/application/PathManager", "getSystemPath" }));
		}
		return tmp132_129;
	}

	@NotNull
	public static File getIndexRoot() {
		String indexRoot = System.getProperty("index_root_path", getSystemPath() + "/index");
		checkAndCreate(indexRoot, true);
		return new File(indexRoot);
	}

	@Nullable
	public static String getResourceRoot(@NotNull Class context, String path) {
		if (context == null) {
			throw new IllegalArgumentException(String.format(
					"Argument for @NotNull parameter '%s' of %s.%s must not be null",
					new Object[] { "context", "com/intellij/openapi/application/PathManager", "getResourceRoot" }));
		}
		URL url = context.getResource(path);
		if (url == null) {
			url = ClassLoader.getSystemResource(path.substring(1));
		}
		return url != null ? extractRoot(url, path) : null;
	}

	@Nullable
	private static String extractRoot(URL resourceURL, String resourcePath) {
		if ((!StringUtil.startsWithChar(resourcePath, '/')) && (!StringUtil.startsWithChar(resourcePath, '\\'))) {
			log("precondition failed: " + resourcePath);
			return null;
		}
		String resultPath = null;
		String protocol = resourceURL.getProtocol();
		if ("file".equals(protocol)) {
			String path = resourceURL.getFile();
			String testPath = path.replace('\\', '/');
			String testResourcePath = resourcePath.replace('\\', '/');
			if (StringUtil.endsWithIgnoreCase(testPath, testResourcePath)) {
				resultPath = path.substring(0, path.length() - resourcePath.length());
			}
		} else if ("jar".equals(protocol)) {
			Pair<String, String> paths = URLUtil.splitJarUrl(resourceURL.getFile());
			if (paths != null) {
				resultPath = paths.first;
			}
		} else if ("bundle".equals(protocol)) {
			resultPath = (new File(
					(PathManager.class.getProtectionDomain().getCodeSource().getLocation() + "").replace("file:/", "")))
							.getAbsolutePath().replace('\\', '/');
		} else {
			throw new UnsupportedOperationException(
					"No '" + protocol + "' known! (" + resourceURL + ", " + resourcePath + ")");
		}
		if (resultPath == null) {
			log("cannot extract: " + resourcePath + " from " + resourceURL);
			return null;
		}
		if ((SystemInfo.isWindows) && (resultPath.startsWith("/"))) {
			resultPath = resultPath.substring(1);
		}
		resultPath = StringUtil.trimEnd(resultPath, File.separator);
		resultPath = URLUtil.unescapePercentSequences(resultPath);

		return resultPath;
	}

	@Nullable
	public static String getJarPathForClass(@NotNull Class aClass) {
		if (aClass == null) {
			throw new IllegalArgumentException(String.format(
					"Argument for @NotNull parameter '%s' of %s.%s must not be null",
					new Object[] { "aClass", "com/intellij/openapi/application/PathManager", "getJarPathForClass" }));
		}
		String resourceRoot = getResourceRoot(aClass, "/" + aClass.getName().replace('.', '/') + ".class");
		return resourceRoot != null ? new File(resourceRoot).getAbsolutePath() : null;
	}

	private static void log(String x) {
		System.err.println(x);
	}

	private static String getAbsolutePath(String path) {
		path = FileUtil.expandUserHome(path);
		return FileUtil.toCanonicalPath(new File(path).getAbsolutePath());
	}

	private static String trimPathQuotes(String path) {
		if ((path != null) && (path.length() >= 3) && (StringUtil.startsWithChar(path, '"'))
				&& (StringUtil.endsWithChar(path, '"'))) {
			path = path.substring(1, path.length() - 1);
		}
		return path;
	}

	private static String platformPath(@NotNull String selector, @Nullable String macPart, @NotNull String fallback) {
		if (selector == null) {
			throw new IllegalArgumentException(String.format(
					"Argument for @NotNull parameter '%s' of %s.%s must not be null",
					new Object[] { "selector", "com/intellij/openapi/application/PathManager", "platformPath" }));
		}
		if (fallback == null) {
			throw new IllegalArgumentException(String.format(
					"Argument for @NotNull parameter '%s' of %s.%s must not be null",
					new Object[] { "fallback", "com/intellij/openapi/application/PathManager", "platformPath" }));
		}
		return platformPath(selector, macPart, null, null, null, fallback);
	}

	private static String platformPath(@NotNull String selector, @Nullable String macPart, @Nullable String winVar,
			@Nullable String xdgVar, @Nullable String xdgDir, @NotNull String fallback) {
		if (selector == null) {
			throw new IllegalArgumentException(String.format(
					"Argument for @NotNull parameter '%s' of %s.%s must not be null",
					new Object[] { "selector", "com/intellij/openapi/application/PathManager", "platformPath" }));
		}
		if (fallback == null) {
			throw new IllegalArgumentException(String.format(
					"Argument for @NotNull parameter '%s' of %s.%s must not be null",
					new Object[] { "fallback", "com/intellij/openapi/application/PathManager", "platformPath" }));
		}
		if ((macPart != null) && (SystemInfo.isMac)) {
			return SystemProperties.getUserHome() + File.separator + macPart + File.separator + selector;
		}
		if ((winVar != null) && (SystemInfo.isWindows)) {
			String dir = System.getenv(winVar);
			if (dir != null) {
				return dir + File.separator + selector;
			}
		}
		if ((xdgVar != null) && (xdgDir != null) && (SystemInfo.hasXdgOpen())) {
			String dir = System.getenv(xdgVar);
			if (dir == null) {
				dir = SystemProperties.getUserHome() + File.separator + xdgDir;
			}
			return dir + File.separator + selector;
		}
		return SystemProperties.getUserHome() + File.separator + "." + selector
				+ (!fallback.isEmpty() ? File.separator + fallback : "");
	}

	private static boolean checkAndCreate(String path, boolean createIfNotExists) {
		if (createIfNotExists) {
			File file = new File(path);
			if (!file.exists()) {
				return file.mkdirs();
			}
		}
		return false;
	}
}