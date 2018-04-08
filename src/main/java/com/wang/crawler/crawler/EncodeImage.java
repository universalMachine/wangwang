package com.wang.crawler.crawler;

public class EncodeImage {

    public static String encodeImg(Object thumb,Object full){
        return encodeImg(thumb.toString(),full.toString());
    }

    public static String encodeImg(String thumb,String full){
        return encodeDesc(encodeComposition(thumb,full),"who is the best beauty asian girl,cute asian girl,hot asian girl ,where are  is  beauty asian girl cute asian girl hot asia girl" +
                "Beauty Asian Girl  Hot Asian girl Cute Asian Girl - welcome friends,there are many cute asian girl,beauty asian girl and hot asian girls here,so many beautiful photo,so many beauty asian girls womans,so cute and hot");
    }
    public static String encodeComposition(String thumb,String full){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("composition:");
        stringBuilder.append("thumb:").append(thumb).append(";");
        stringBuilder.append("full:").append(full).append(";");
        return stringBuilder.toString();
    }

    public static String encodeDesc(String content ,String desc){
        StringBuilder stringBuilder = new StringBuilder();
       stringBuilder.append(content);
       stringBuilder.append("desc:").append(desc).append(";");
       return stringBuilder.toString();
    }




}
