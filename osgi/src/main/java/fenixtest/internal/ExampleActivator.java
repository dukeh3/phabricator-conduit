package fenixtest.internal;

import org.osgi.framework.BundleActivator;

public class ExampleActivator implements BundleActivator {
	public void start(org.osgi.framework.BundleContext context) throws Exception {
	    // do something at startup
		System.out.println("ExampleActivator.start()");
	  }
	  public void stop(org.osgi.framework.BundleContext context) throws Exception {
		  
		  System.out.println("ExampleActivator.stop()");
	  }
}
