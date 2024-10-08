package org.src.crawler.util;

import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

public class JsoupXpathUtil {

    private JsoupXpathUtil(){}

    public static String readNodeValueBasedOnTag(Element element){
        final Tag tag = element.tag();
        return switch (tag.getName()) {
            case "a" -> element.attr("href");
            default -> element.text();
        };
    }


}
