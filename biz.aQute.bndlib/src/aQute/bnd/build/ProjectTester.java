package aQute.bnd.build;

import java.io.*;
import java.util.*;

import aQute.bnd.osgi.*;

public abstract class ProjectTester {
	final Project				project;
	final Collection<Container>	testbundles;
	final ProjectLauncher		launcher;
	final List<String>			tests		= new ArrayList<String>();
	File						reportDir;
	boolean						continuous	= true;

	public ProjectTester(Project project) throws Exception {
		this.project = project;
		launcher = project.getProjectLauncher();
		launcher.setCwd(project.getBase());
		launcher.addRunVM("-ea");
		testbundles = project.getTestpath();
		continuous = project.is(Constants.TESTCONTINUOUS);
		
		for (Container c : testbundles) {
			launcher.addClasspath(c);
		}
		reportDir = new File(project.getTarget(), project.getProperty("test-reports", "test-reports"));
	}

	public ProjectLauncher getProjectLauncher() {
		return launcher;
	}

	public void addTest(String test) {
		tests.add(test);
	}

	public Collection<String> getTests() {
		return tests;
	}

	public Collection<File> getReports() {
		List<File> reports = new ArrayList<File>();
		for (File report : reportDir.listFiles()) {
			if (report.isFile())
				reports.add(report);
		}
		return reports;
	}

	public File getReportDir() {
		return reportDir;
	}

	public void setReportDir(File reportDir) {
		this.reportDir = reportDir;
	}

	public Project getProject() {
		return project;
	}

	public boolean getContinuous() {
		return continuous;
	}

	public void setContinuous(boolean b) {
		this.continuous = b;
	}
	
	public File getCwd() {
		return launcher.getCwd();
	}
	
	public void setCwd(File dir) {
		launcher.setCwd(dir);
	}

	public boolean prepare() throws Exception {
		if (!reportDir.exists() && !reportDir.mkdirs()) {
			throw new IOException("Could not create directory " + reportDir);
		}
		for (File file : reportDir.listFiles()) {
			file.delete();
		}
		return true;
	}

	public abstract int test() throws Exception;
}
