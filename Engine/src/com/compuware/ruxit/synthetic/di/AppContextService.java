package com.compuware.ruxit.synthetic.di;

import java.util.Collection;

public interface AppContextService {

	public Object getBean (String name);
	public <T> T getBean (String name, Class<T> c);
	public <T> T getBean (Class<T> c);
	public <T> Collection<T> getBeansOfType(Class<T> c);	
}
