package com.khoadonguyen.java_music_streaming.Util;

import com.khoadonguyen.java_music_streaming.Model.Lyric;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.*;

public class LyricParser {

    public static List<Lyric> parseLyricsFromXml(String xmlContent) {
        List<Lyric> lyrics = new ArrayList<>();

        try {
            // Parse XML content
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true); // Needed because of XML namespaces
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xmlContent.getBytes("UTF-8")));

            // Get all <p> tags in the document
            NodeList pTags = doc.getElementsByTagNameNS("*", "p");

            for (int i = 0; i < pTags.getLength(); i++) {
                Element p = (Element) pTags.item(i);

                String begin = p.getAttribute("begin");
                String end = p.getAttribute("end");

                // Gather all text inside the <p> (including <br/> if needed)
                StringBuilder textBuilder = new StringBuilder();
                NodeList children = p.getChildNodes();
                for (int j = 0; j < children.getLength(); j++) {
                    Node child = children.item(j);
                    if (child.getNodeType() == Node.TEXT_NODE) {
                        textBuilder.append(child.getTextContent().trim());
                    } else if (child.getNodeName().equals("br")) {
                        textBuilder.append("\n");
                    } else {
                        textBuilder.append(child.getTextContent().trim());
                    }
                }

                lyrics.add(new Lyric(begin, end, textBuilder.toString().trim()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lyrics;
    }
}
