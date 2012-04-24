package net.klu2.logview.config;

import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import net.bull.javamelody.MonitoringFilter;
import net.bull.javamelody.SessionListener;

import org.springframework.web.WebApplicationInitializer;

public class MonitoringInitializer implements WebApplicationInitializer {
	@Override
	public void onStartup(ServletContext servlet) throws ServletException {
		servlet.addListener(SessionListener.class);
		Dynamic filter = servlet.addFilter("monitoring", MonitoringFilter.class);
		filter.addMappingForUrlPatterns(null, true, "/*");
	}
}
