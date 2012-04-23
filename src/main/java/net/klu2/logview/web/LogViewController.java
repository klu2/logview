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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * This controller serves all requests to the logview application via a REST interface
 * 
 * @author Klaus Lehner
 */
@Controller
public class LogViewController {

	private final String rootDirectory;
	private File rootDirectoryFile;

	@Autowired
	public LogViewController(@Value("#{systemProperties.rootdir}") String rootDirectory) {
		this.rootDirectory = rootDirectory;
		this.rootDirectoryFile = new File(rootDirectory);
		if (!rootDirectoryFile.exists() || !rootDirectoryFile.isDirectory()) {
			throw new IllegalArgumentException(rootDirectory + " does not exist or is no directory");
		}
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView webapps() {
		return new ModelAndView("webapps", "apps", Arrays.asList(rootDirectoryFile.list(directoryFilter)));
	}

	@RequestMapping(value = "/{webapp}", method = RequestMethod.GET)
	public ModelAndView logFiles(@PathVariable("webapp") String webApp) {
		ModelAndView mav = new ModelAndView("logfiles", "app", webApp);
		List<LogFile> list = new ArrayList<LogFile>();
		// TODO check if file exists, return 404 instead
		File directory = new File(rootDirectory + webApp + "/logs/");
		for (File f : directory.listFiles()) {
			list.add(new LogFile(f));
		}
		mav.addObject("files", list);
		return mav;
	}

	@RequestMapping(value = "/{webapp}/{logfile}", method = RequestMethod.GET)
	public void logFile(@PathVariable("webapp") String webApp, @PathVariable("logfile") String logFile, HttpServletResponse response)
			throws IOException {
		response.setContentType("text/plain");
		File file = new File(rootDirectory + webApp + "/logs/" + logFile);
		// TODO check if file exists, return 404 instead
		InputStream stream = new FileInputStream(file);
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