package net.klu2.logview.web;

import java.io.File;
import java.util.Date;

public class LogFile {
	private final String name;
	private long size;
	private Date lastModified;

	public LogFile(File file) {
		this.name = file.getName();
		this.size = file.length();
		this.lastModified = new Date(file.lastModified());
	}

	public String getName() {
		return name;
	}
	public long getSize() {
		return size;
	}
	public Date getLastModified() {
		return lastModified;
	}
}
