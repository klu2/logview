package net.klu2.logview.config;

import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import net.klu2.logview.web.WebConfig;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * This is the entry point to the web app, instead of having a <code>web.xml</code>
 * 
 * @author Klaus Lehner
 */
public class LogViewWebAppInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext sc) throws ServletException {
		AnnotationConfigWebApplicationContext servletContext = new AnnotationConfigWebApplicationContext();
		servletContext.register(WebConfig.class);
		
		sc.getServletRegistration("default").addMapping("*.ico");

		ServletRegistration.Dynamic appServlet = sc.addServlet("appServlet", new DispatcherServlet(servletContext));

		appServlet.setLoadOnStartup(1);
		Set<String> mappingConflicts = appServlet.addMapping("/");
		if (!mappingConflicts.isEmpty()) {
			throw new IllegalStateException("'appServlet' could not be mapped to '/' due "
					+ "to an existing mapping. This is a known issue under Tomcat versions "
					+ "<= 7.0.14; see https://issues.apache.org/bugzilla/show_bug.cgi?id=51278");
		}
	}
}
