package com.xaxc.teqin.common.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.lang3.StringUtils;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.operation.buffer.BufferOp;
import org.locationtech.jts.util.GeometricShapeFactory;
import org.locationtech.proj4j.*;
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;

public class Utils {

    public static boolean verifyISO8601Time(String time) {
        try {
            DateTimeFormatter.ISO_DATE_TIME.parse(time);
            return true;
        } catch (Exception e) {
            throw new IllegalArgumentException(time + "不是ISO 8601格式的日期");
        }
    }

    public static String getTimeData(LocalDate localDate) {
        int value = localDate.getDayOfWeek().getValue();
        String week = "";
        switch (value) {
            case 1:
                week = "星期一";
                break;
            case 2:
                week = "星期二";
                break;
            case 3:
                week = "星期三";
                break;
            case 4:
                week = "星期四";
                break;
            case 5:
                week = "星期五";
                break;
            case 6:
                week = "星期六";
                break;
            case 7:
                week = "星期日";
                break;
            default:
                break;
        }
        return localDate.getMonthValue() + "月" + localDate.getDayOfMonth() + "日（" + week + ")";
    }

    public static Double object2Object(Object o) {
        return ObjectUtils.isEmpty(o) ? null : Double.valueOf(o.toString());
    }

    public static String getPointBuffer(double longitude, double latitude, double radiusInMeters) {
        GeometricShapeFactory shapeFactory = new GeometricShapeFactory();
        shapeFactory.setNumPoints(64); // adjustable
        Coordinate coordinate = new Coordinate(longitude, latitude);
        shapeFactory.setCentre(coordinate);
        // Length in meters of 1° of latitude = always 111.32 km
        shapeFactory.setHeight(radiusInMeters * 2 / 111320d);
        // Length in meters of 1° of longitude = 40075 km * cos( latitude ) / 360
        shapeFactory.setWidth(radiusInMeters * 2 / (40075000 * Math.cos(Math.toRadians(latitude)) / 360));
        Polygon circle = shapeFactory.createCircle();
        return circle.toText();
    }

    public static String getPointBuffer(String wkt, double radiusInMeters) throws ParseException {
        GeometricShapeFactory shapeFactory = new GeometricShapeFactory();
        shapeFactory.setNumPoints(64); // adjustable
        GeometryFactory geometryFactory = new GeometryFactory();
        WKTReader wktReader = new WKTReader(geometryFactory);
        Geometry pointString = wktReader.read(wkt);
        Coordinate coordinate = pointString.getCoordinate();
        shapeFactory.setCentre(coordinate);
        // Length in meters of 1° of latitude = always 111.32 km
        shapeFactory.setHeight(radiusInMeters * 2 / 111320d);
        // Length in meters of 1° of longitude = 40075 km * cos( latitude ) / 360
        shapeFactory.setWidth(radiusInMeters * 2 / (40075000 * Math.cos(Math.toRadians(coordinate.getY())) / 360));
        Polygon circle = shapeFactory.createCircle();
        return circle.toText();
    }

    public static String getLineBuffer(String wkt, double radiusInMeters) throws ParseException {
        return getLineBuffer(wkt, radiusInMeters, BufferOp.CAP_FLAT);//  CAP_ROUND
    }

    public static String getLineBuffer(String wkt, double radiusInMeters, int endCapStyle) throws ParseException {
        GeometryFactory geometryFactory = new GeometryFactory();
        WKTReader wktReader = new WKTReader(geometryFactory);
        Geometry gfLineString = wktReader.read(wkt);
        double degree = radiusInMeters * 2 / (2 * Math.PI * 6371004) * 360;
        //缓冲区建立
        BufferOp bufOp = new BufferOp(gfLineString);
        bufOp.setEndCapStyle(endCapStyle); //BufferOp.CAP_BUTT
        Geometry bg = bufOp.getResultGeometry(degree);
        return bg.toText();
    }

    public static String transterJson(Object object){
        if(object == null){
            return null;
        }
        // 对于旧的脏数据的处理
        if (object instanceof JSONObject) {
           JSONObject ll =  (JSONObject) object;
           if(ll.containsKey("$numberInt")){
               return ll.get("$numberInt").toString();
           }else {
               return ll.values().stream().findFirst().get().toString();
           }
        }else{
             return object.toString();
        }
    }

    /**
     * 2000转84坐标转换
     *
     * @param x
     * @param y
     * @return
     */
    public static ProjCoordinate cgcs2000ToWgs84(double x, double y) {
        //定义大地2000（116）坐标系投影
        String projString = "+proj=tmerc +lat_0=0 +lon_0=116 +k=1 +x_0=500000 +y_0=0 +ellps=GRS80 +units=m +no_defs";
        CRSFactory crsFactory = new CRSFactory();
        //创建大地2000 坐标系
        CoordinateReferenceSystem sourceCRS = crsFactory.createFromParameters("CGCS2000116E", projString);
        //创建经纬度坐标系
        CoordinateReferenceSystem targetCRS = crsFactory.createFromName("EPSG:4326");
        //创建坐标转换器
        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
        CoordinateTransform transform = ctFactory.createTransform(sourceCRS, targetCRS);
        ProjCoordinate srcCord = new ProjCoordinate(x, y);
        ProjCoordinate targetCord = new ProjCoordinate();
        //进行坐标转换
        transform.transform(srcCord, targetCord);
        return targetCord;
    }

    /**
     * 84转2000坐标转换
     *
     * @param x
     * @param y
     * @return
     */
    public static ProjCoordinate wgs84ToCgcs2000(double x, double y) {
        //定义大地2000（116）坐标系投影
        String projString = "+proj=tmerc +lat_0=0 +lon_0=116 +k=1 +x_0=500000 +y_0=0 +ellps=GRS80 +units=m +no_defs";
        CRSFactory crsFactory = new CRSFactory();
        //创建经纬度坐标系
        CoordinateReferenceSystem sourceCRS = crsFactory.createFromName("EPSG:4326");
        //创建大地2000 坐标系
        CoordinateReferenceSystem targetCRS = crsFactory.createFromParameters("CGCS2000116E", projString);

        //创建坐标转换器
        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
        CoordinateTransform transform = ctFactory.createTransform(sourceCRS, targetCRS);
        ProjCoordinate srcCord = new ProjCoordinate(x, y);
        ProjCoordinate targetCord = new ProjCoordinate();
        //进行坐标转换
        transform.transform(srcCord, targetCord);
        return targetCord;
    }

    private static final double PI = 3.14159265358979324D;
    private static final double A = 6378245.0D;
    private static final double EE = 0.00669342162296594323D;

    public static double[] gcj02ToWgs84(double lat, double lon) {
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * PI;
        double magic = Math.sin(radLat);
        magic = 1 - EE * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((A * (1 - EE)) / (magic * sqrtMagic) * PI);
        dLon = (dLon * 180.0) / (A / sqrtMagic * Math.cos(radLat) * PI);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        double[] wgs84 = {(lat * 2 - mgLat), (lon * 2 - mgLon)};
        return wgs84;
    }

    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * PI) + 40.0 * Math.sin(y / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * PI) + 320 * Math.sin(y * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * PI) + 40.0 * Math.sin(x / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * PI) + 300.0 * Math.sin(x / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }

    private static final char[] ENCRYPT_CHARS = {'*'};

    public static String encryptName(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        char[] chars = name.toCharArray();
        int encryptCharIndex = 0;

        for (int i = 1; i < chars.length; i++) {
            if (Character.isLetter(chars[i])) {
                if (chars.length > 2 && i == chars.length - 1) {
                    break;
                }
                chars[i] = ENCRYPT_CHARS[encryptCharIndex];
                encryptCharIndex = (encryptCharIndex + 1) % ENCRYPT_CHARS.length;
            }
        }
        return new String(chars);
    }

    public static String encryptCard(String idCard, int front, int end) {
        if (StringUtils.isEmpty(idCard)) return idCard;
        if ((front + end) > idCard.length()) return idCard;
        if (front < 0 || end < 0) return idCard;
        //计算 ‘*’ 号数量
        int asteriskCount = idCard.length() - (front + end);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < asteriskCount; i++) {
            stringBuilder.append("*");
        }
        String regex = "(\\w{" + String.valueOf(front) + "})(\\w+)(\\w{" + String.valueOf(end) + "})";
        return idCard.replaceAll(regex, "$1" + stringBuilder + "$3");
    }

    public static String encryptPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return phone;
        }
        if (phone.length() == 11) {
            return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        } else if (phone.length() == 13) {
            return phone.replaceAll("(\\d{3})-\\d{4}-(\\d{4})", "$1-****-$2");
        } else {
            return phone.replaceAll("(\\w{3})\\w{4}(\\w{2,})", "$1****$2");
        }
    }

    public static void unzip(String inputFilePath, String outputDirectoryPath) throws Exception {
        try (InputStream is = new FileInputStream(inputFilePath);
             BufferedInputStream bis = new BufferedInputStream(is);
             ArchiveInputStream ais = new ArchiveStreamFactory()
                     .createArchiveInputStream(bis)) {
            new File(outputDirectoryPath).mkdir();
            ArchiveEntry entry;
            while ((entry = ais.getNextEntry()) != null) {
                if (!ais.canReadEntryData(entry)) {
                    continue;
                }
                File outputFile = new File(outputDirectoryPath, entry.getName());
                String entryCanonicalPath = outputFile.getCanonicalPath();
                if (!entryCanonicalPath.startsWith(outputDirectoryPath + File.separator)) {
                    throw new IOException("Detected path traversal attack: " + entry.getName());
                }

                if (entry.isDirectory()) {
                    if (!outputFile.exists() && !outputFile.mkdirs()) {
                        throw new IOException("Failed to create directory: " + outputFile);
                    }
                } else {
                    try (OutputStream os = new FileOutputStream(outputFile);
                         BufferedOutputStream bos = new BufferedOutputStream(os)) {
                        byte[] buffer = new byte[8192];
                        int len;
                        while ((len = ais.read(buffer)) != -1) {
                            bos.write(buffer, 0, len);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("文件解压失败: " + e.getMessage());
        }
    }

    public static Object trimStrings(Object obj) throws IllegalAccessException {
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true); // 设置可访问性

            if (field.getType() == String.class) {
                String originalValue = (String) field.get(obj);
                if (originalValue != null) {
                    String trimmedValue = originalValue.trim();
                    field.set(obj, trimmedValue);
                }
            }
        }
        return obj;
    }

    private static final char[] chineseNums = {'零', '一', '二', '三', '四', '五', '六', '七', '八', '九'};
    private static final String[] units = {"", "十", "百", "千", "万", "亿"};

    public static String numberToChinese(int number) {
        if (number == 0) {
            return "零";
        }
        StringBuilder chinese = new StringBuilder();
        int unitIndex = 0; // 单位索引
        boolean zero = false; // 前一位是否为0
        while (number > 0) {
            int part = number % 10; // 取出当前位的数字
            if (part == 0) {
                if (!zero) {
                    chinese.insert(0, chineseNums[part]);
                    zero = true;
                }
            } else {
                chinese.insert(0, units[unitIndex]);
                chinese.insert(0, chineseNums[part]);
                zero = false;
            }
            unitIndex++;
            number /= 10;
        }
        return chinese.toString();
    }
}
