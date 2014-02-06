package com.compuware.ruxit.synthetic.dispatcher;

import javax.servlet.AsyncContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestDispatchServiceFactory {
    @Autowired
    TestDispatchServiceConfig config;
    
    
    public TestDispatchServiceConfig getConfig () {
    	return config;
    }
    
    public TestDispatchService create (AsyncContext asyncContext) {
    	return new TestDispatchService(config, asyncContext);
    }
}
