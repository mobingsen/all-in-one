package com.drop.leaves.hdp.config;

import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiStyle;

import java.io.PrintStream;

/**
 * http://patorjk.com/software/taag
 * @author by mobingsen on 2021/5/25 20:31
 */
public class HdpBanner {

	private static final String[] BANNER = {
			"    .___                    .__                                    ",
					"  __| _/______  ____ ______ |  |   ____ _____ ___  __ ____   ______",
					" / __ |\\_  __ \\/  _ \\\\____ \\|  | _/ __ \\\\__  \\\\  \\/ // __ \\ /  ___/",
					"/ /_/ | |  | \\(  <_> )  |_> >  |_\\  ___/ / __ \\\\   /\\  ___/ \\___ \\ ",
					"\\____ | |__|   \\____/|   __/|____/\\___  >____  /\\_/  \\___  >____  >",
					"     \\/              |__|             \\/     \\/          \\/     \\/ "
	};

	private static final String SPRING_BOOT = " :: hdp boot :: ";

	private static final int STRAP_LINE_SIZE = 42;

	public void printBanner() {
		PrintStream printStream = System.out;
		for (String line : BANNER) {
			printStream.println(line);
		}
		String version = SpringBootVersion.getVersion();
		version = (version != null) ? " (v" + version + ")" : "";
		StringBuilder padding = new StringBuilder();
		while (padding.length() < STRAP_LINE_SIZE - (version.length() + SPRING_BOOT.length())) {
			padding.append(" ");
		}

		printStream.println(AnsiOutput.toString(AnsiColor.GREEN, SPRING_BOOT, AnsiColor.DEFAULT, padding.toString(),
				AnsiStyle.FAINT, version));
		printStream.println();
	}
}
