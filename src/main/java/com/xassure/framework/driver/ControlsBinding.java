package com.xassure.framework.driver;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class ControlsBinding extends AbstractModule {

	@Override
	protected void configure() {
		bind(Controls.class).annotatedWith(Names.named("web")).toProvider(ControlsWebProvider.class);
		bind(Controls.class).annotatedWith(Names.named("mobile")).toProvider(ControlsMobileProvider.class);
	}

}
