package com.example.androiderp.pull;

import com.example.androiderp.adaper.DataStructure;

import org.litepal.crud.DataSupport;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingtan on 2017/5/4.
 */

public class XmlPullParser {
    private List<DataStructure> dataStructures = new ArrayList<DataStructure>();

    public    List<DataStructure> parseXMLWithPull(String xmlData) {
        if (xmlData != null){

            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                org.xmlpull.v1.XmlPullParser xmlPullParser = factory.newPullParser();
                xmlPullParser.setInput(new StringReader(xmlData));
                int eventType = xmlPullParser.getEventType();
                String artist="";
                String name = "";
                String duration="";
                String thumb_url="";
                while (eventType != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) {
                    String nodeName = xmlPullParser.getName();
                    switch (eventType) {
                        // 开始解析某个结点
                        case org.xmlpull.v1.XmlPullParser.START_TAG: {
                             if ("title".equals(nodeName)) {
                                name = xmlPullParser.nextText();
                            }else if("artist".equals(nodeName)) {
                                 artist=xmlPullParser.nextText();
                             }else if("duration".equals(nodeName)) {
                                 duration=xmlPullParser.nextText();
                             }else if("thumb_url".equals(nodeName)) {
                                 thumb_url=xmlPullParser.nextText();
                             }

                            break;
                        }
                        // 完成解析某个结点
                        case org.xmlpull.v1.XmlPullParser.END_TAG: {
                            if ("song".equals(nodeName)) {

                                DataStructure lpu=new DataStructure();
                                lpu.setName(name);
                                lpu.setArtist(artist);
                                lpu.setDuration(duration);
                                lpu.setThumb_url(thumb_url);
                                lpu.save();
                                //DataStructure apple = new DataStructure(name,artist,duration, thumb_url);
                                //dataStructures.add(apple);


                            }
                            break;
                        }
                        default:
                            break;
                    }
                    eventType = xmlPullParser.next();
                }
                dataStructures= DataSupport.findAll(DataStructure.class);
                return dataStructures;
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
        return dataStructures;
    }


}
