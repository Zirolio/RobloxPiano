package com.zirolio.robloxpiano;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Objects;

public class MelodyDownloader {
    public static Config.MelodyConfig load(String url) throws Exception {
        if (!url.startsWith("https://virtualpiano.net/music-sheet")) throw new Exception("Bad url: " + url);

        String tabs = null, name = null, author = null, date = null, playTime = null;
        Integer tempo = null, trans = null;

        Document dom = Jsoup.connect(url)
                .userAgent("Mozilla/5.0")
                .timeout(5000)
                .get();


        // Parsing
        tabs = Objects.requireNonNull(Objects.requireNonNull(dom.getElementById("sheet-content"))
                .select("> p")
                .first()).text().strip()
                .replaceAll("\\s+", "~").replaceAll("\\|", "-").replaceAll("\\s{2,}", "");;

        Element sheetInfo = dom.getElementsByClass("sheet__info").first();
        if (sheetInfo != null) {
            Element tempoIcon = sheetInfo.getElementsByClass("tempo-icon").first();
            if (tempoIcon != null) {
                Element parent = tempoIcon.parent();
                if (parent != null) {
                    String text = parent.select("> span").text().strip();
                    if (!text.isEmpty()) {
                        tempo = Integer.parseInt(text);
                    }
                }
            }

            Element transIcon = sheetInfo.getElementsByClass("trans-icon").first();
            if (transIcon != null) {
                Element parent = transIcon.parent();
                if (parent != null) {
                    String text = parent.select("> span").text().strip();
                    if (!text.isEmpty()) {
                        trans = Integer.parseInt(text);
                    }
                }
            }

            Element timeIcon = sheetInfo.getElementsByClass("time-icon").first();
            if (timeIcon != null) {
                Element parent = timeIcon.parent();
                if (parent != null) {
                    String text = parent.select("> span").text().strip();
                    if (!text.isEmpty()) {
                        playTime = text;
                    }
                }
            }
        }

        Element melodyInfo = dom.getElementsByClass("sheet__head-wrapper").first();
        if (melodyInfo != null) {
            String n = melodyInfo.select("> .verified").text().strip();
            if (!n.isEmpty()) name = n;

            String a = melodyInfo.select("> .sheet__author").text().strip();
            if (!a.isEmpty()) author = a;

            String d = melodyInfo.select("> .sheet__date").text().strip();
            if (!d.isEmpty()) date = d;
        }

        /*if (tempo == -1) {
            tabs.replaceAll("\\\\[.+\\\\]", "a").
        }*/

        Config.MelodyConfig config = new Config.MelodyConfig(name, author, date, playTime, url, tabs, tempo, trans);
        System.out.println("DOWNLOADED: " + config.name);
        System.out.println("Url: " + config.url);
        System.out.println("Author: " + config.author);
        System.out.println("Date: " + config.date);
        System.out.println("Tabs: " + config.tabs);
        System.out.println("Play time: " + config.playTime);
        System.out.println("Tempo: " + config.tempo);
        System.out.println("Transpose: " + config.trans);
        System.out.println("\n");

        return config;
    }
}
