package net.klu2.logview.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LogViewController {

	private final Resource rootDirectory;

	@Autowired
	public LogViewController(
			@Value("#{systemProperties.rootdir}") Resource rootDirectory) {
		this.rootDirectory = rootDirectory;
		if (!rootDirectory.exists()) {
			throw new IllegalArgumentException(rootDirectory.toString()
					+ " does not exist");
		}
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView webapps() throws IOException {
		return new ModelAndView("webapps", "apps", Arrays.asList(rootDirectory
				.getFile().list(directoryFilter)));
	}

	@RequestMapping(value = "/{webapp}", method = RequestMethod.GET)
	public ModelAndView logFiles(@PathVariable("webapp") String webApp)
			throws IOException {
		ModelAndView mav = new ModelAndView("logfiles", "app", webApp);
		List<LogFile> list = new ArrayList<LogFile>();
		for (File f : rootDirectory.createRelative(webApp + "/")
				.createRelative("logs/").getFile().listFiles()) {
			list.add(new LogFile(f));
		}
		mav.addObject("files", list);
		return mav;
	}

	@RequestMapping(value = "/{webapp}/{logfile}", method = RequestMethod.GET)
	public void logFile(@PathVariable("webapp") String webApp,
			@PathVariable("logfile") String logFile,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/plain");
		InputStream stream = new FileInputStream(rootDirectory
				.createRelative(webApp + "/").createRelative("logs/")
				.createRelative(logFile).getFile());
		IOUtils.copy(stream, response.getOutputStream());
		stream.close();
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}

	private FilenameFilter directoryFilter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return new File(dir + "/" + name).isDirectory();
		}
	};
}