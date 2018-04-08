package com.wang.crawler.crawler;

import org.junit.Test;

import static org.junit.Assert.*;

public class EncodeImageTest {

    @Test
    public void encodeComposition() {
        assertEquals("composition:thumb:thumb.jpg;full:full.jpg",EncodeImage.encodeImg("thumb.jpg","full.jpg"));
    }

    @Test
    public void encodeDesc() {
        assertEquals("composition:thumb:thumb.jpg;full:full.jpg",EncodeImage.encodeDesc(EncodeImage.encodeImg("thumb.jpg","full.jpg"),
                "who is the best beauty asian girl,cute asian girl,hot asian girl ,where are  is  beauty asian girl cute asian girl hot asia girl\n" +
                        "Beauty Asian Girl  Hot Asian girl Cute Asian Girl - welcome friends,there are many cute asian girl,beauty asian girl and hot asian girls here,so many beautiful photo,so many beauty asian girls womans,so cute and hot"));
    }
}