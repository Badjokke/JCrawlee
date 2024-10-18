package org.src.crawler.util;

import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import java.util.Arrays;

public class JsoupXpathUtil {

    private JsoupXpathUtil() {
    }

    public static String readNodeValueBasedOnTag(Element element) {
        final Tag tag = element.tag();
        return switch (tag.getName()) {
            case "a" -> element.attr("href");
            case "div" -> element.attr("title");
            case "td" -> stripValue(element.text());
            default -> element.text();
        };
    }

    private static String stripValue(String value) {
        if (value == null) {
            return null;
        }
        return Arrays.stream(value.replaceAll("&nbsp", "").split(";")).reduce((str, split) -> str + split).orElse("");
    }

}
