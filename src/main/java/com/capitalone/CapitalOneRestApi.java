package com.capitalone;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("resources")
public class CapitalOneRestApi extends ResourceConfig {
	public CapitalOneRestApi() {
		//delimeter is ';'
        packages("com.capitalone.mymoney");
    }
}
