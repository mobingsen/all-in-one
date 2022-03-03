package org.tea.plus.hdp.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiStyle;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * http://patorjk.com/software/taag
 * @author by mobingsen on 2021/5/25 20:31
 */
@Slf4j
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
	private static final String DOWN_ARROW = "↓";
	private static final String BLANK = " ";

	public void printBanner() {
		int maxLen = Arrays.stream(BANNER)
				.map(String::length)
				.max(Comparator.comparingInt(Integer::intValue))
				.orElse(0);
		String prefix = DOWN_ARROW + DOWN_ARROW + "  ";
		String suffix = "  " + DOWN_ARROW + DOWN_ARROW;
		int width = maxLen + prefix.length() + suffix.length();

		int fillLen = width - SPRING_BOOT.length() - prefix.length() - suffix.length();
		StringBuilder padding = new StringBuilder();
		while (padding.length() < fillLen / 2) {
			padding.append(BLANK);
		}

		String bannerLine = IntStream.range(0, width).boxed().map(i -> DOWN_ARROW).collect(Collectors.joining());
		log.info(bannerLine);
		for (String line : BANNER) {
			log.info(prefix + line + suffix);
		}
		log.info(prefix +
				AnsiOutput.toString(
						AnsiColor.DEFAULT, padding.toString(),
						AnsiColor.GREEN, SPRING_BOOT,
						AnsiColor.DEFAULT, padding.toString()) +
				" " +
				suffix);
		log.info(bannerLine);
	}
}
