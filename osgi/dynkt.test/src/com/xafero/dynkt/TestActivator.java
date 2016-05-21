package com.xafero.dynkt;

import java.util.logging.Logger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class TestActivator implements BundleActivator {

	private final Logger log = Logger.getLogger(TestActivator.class + "");

	private static BundleContext context;

	@Override
	public void start(BundleContext ctx) throws Exception {
		log.info("Starting script engine test bundle...");
		context = ctx;
	}

	@Override
	public void stop(BundleContext ctx) throws Exception {
		log.info("Stopping script engine test bundle...");
		context = null;
	}

	public static BundleContext getContext() {
		return context;
	}
}