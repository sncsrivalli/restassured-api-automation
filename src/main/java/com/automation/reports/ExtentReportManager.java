package com.automation.reports;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Objects;
import com.automation.constants.FrameworkConstants;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExtentReportManager {

	private static ExtentReports extentReports;
	private static final ExtentSparkReporter extentSparkReporter = new ExtentSparkReporter(FrameworkConstants.getExtentReportPath());
	private static final ThreadLocal<ExtentTest> threadLocalExtentTest = new ThreadLocal<>();

	/**
	 * This method is to initialize the Extent Report
	 */
	public static void initExtentReport() {
		try {
			if (Objects.isNull(extentReports)) {
				extentReports = new ExtentReports();
				extentReports.attachReporter(extentSparkReporter);
				InetAddress ip = InetAddress.getLocalHost();
				String hostname = ip.getHostName();
				extentReports.setSystemInfo("Host Name", hostname);
				extentReports.setSystemInfo("Environment", "API Automation - RestAssured");
				extentReports.setSystemInfo("User Name", System.getProperty("user.name"));
				extentSparkReporter.config().setDocumentTitle("HTML Report");
				extentSparkReporter.config().setReportName("API Automation Test");
				extentSparkReporter.config().setTheme(Theme.DARK);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createTest(String testCaseName) {
		setExtentTest(extentReports.createTest(testCaseName));
	}

	public static void flushExtentReport() {
		if (Objects.nonNull(extentReports)) {
			extentReports.flush();
		}
		unload();
		try {
			Desktop.getDesktop().browse(new File(FrameworkConstants.getExtentReportPath()).toURI());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ExtentTest getExtentTest() {
		return threadLocalExtentTest.get();
	}

	static void setExtentTest(ExtentTest test) {
		threadLocalExtentTest.set(test);
	}

	static void unload() {
		threadLocalExtentTest.remove();
	}
}