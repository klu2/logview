package net.klu2.logview.web;

import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class LogViewControllerTest {

	private LogViewController controller;

	@Before
	public void setup() {
		controller = new LogViewController(new FileSystemResource("./src/test/resources/WEB-INF/"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void webapps() throws IOException {
		ModelAndView webapps = controller.webapps();
		Assert.assertEquals("webapps", webapps.getViewName());
		List<String> list = (List<String>) webapps.getModel().get("apps");
		Assert.assertNotNull(list);
		Assert.assertEquals(2, list.size());
		Assert.assertEquals("cc.catalysts.spm", list.get(0));
		Assert.assertEquals("local.catalysts.catcoder", list.get(1));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void logfiles() throws IOException {
		ModelAndView logFiles = controller.logFiles("local.catalysts.catcoder");
		Assert.assertEquals("logfiles", logFiles.getViewName());
		List<LogFile> files = (List<LogFile>) logFiles.getModel().get("files");
		Assert.assertNotNull(files);
		Assert.assertEquals(2, files.size());
		Assert.assertEquals("catalina.out", files.get(0).getName());
		Assert.assertEquals("catcoder.log", files.get(1).getName());
	}

	@Test
	public void logfile() throws IOException {
		MockHttpServletResponse response = new MockHttpServletResponse();
		controller.logFile("local.catalysts.catcoder", "catalina.out", response);
		Assert.assertEquals("Catcoder CatalinaOut", response.getContentAsString());
	}
}