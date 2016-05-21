package com.xafero.dynkt;

import java.util.logging.Logger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private final Logger log = Logger.getLogger(Activator.class + "");

	private static BundleContext context;

	@Override
	public void start(BundleContext ctx) throws Exception {
		log.info("Starting Kotlin script engine bundle...");
		context = ctx;
	}

	@Override
	public void stop(BundleContext ctx) throws Exception {
		log.info("Stopping Kotlin script engine bundle...");
		context = null;
	}

	public static BundleContext getContext() {
		return context;
	}
}