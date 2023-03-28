package com.jinjin.jintranet.common;

import java.io.File;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.jinjin.jintranet.model.NoticeAttach;


public class FileUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    private static final String ROOT_PATH = System.getProperty("user.dir");
    private static final String FILE_PATH = "/files/";


    public static List<NoticeAttach> upload(MultipartHttpServletRequest request, String dirName) throws Exception {
        List<NoticeAttach> list = new ArrayList<>();
        try {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter df = DateTimeFormat.forPattern("yyyyMMddHHmmss");

            List<MultipartFile> files = new ArrayList<>();
            Map<String, MultipartFile> map = request.getFileMap();
            for (MultipartFile multipartFile : map.values()) {
                files.add(multipartFile);
            }

            StringBuffer sb = new StringBuffer(96);
            sb.append(ROOT_PATH).append(FILE_PATH).append(dirName).append("/")
                    .append(now.getYear()).append("/")
                    .append(now.getMonthOfYear()).append("/")
                    .append(now.getDayOfMonth()).append("/");

            String path = sb.toString();
            File dir = new File(path);

            if (!dir.exists())
                dir.mkdirs();

            sb.delete(0, ROOT_PATH.length());

            String time = now.toString(df);
            for (MultipartFile mf : files) {
                String originalFileName = mf.getOriginalFilename();


                String uuid = UUID.randomUUID().toString();
                String ext = originalFileName.substring(originalFileName.lastIndexOf("."));

                String fileName = String.format("%s_%s%s", time, uuid, ext);

                mf.transferTo(new File(path, fileName));

                NoticeAttach attach = new NoticeAttach();
                attach.setPath(sb.toString());
                attach.setOriginalFileName(originalFileName);
                attach.setStoredFileName(fileName);
                attach.setFileSize(mf.getSize());

                list.add(attach);
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void download(NoticeAttach attach, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String fileName = attach.getOriginalFileName();
            String browser = getBrowser(request);

            StringBuffer sb = new StringBuffer(96)
                    .append(ROOT_PATH).append(attach.getPath())
                    .append(attach.getStoredFileName());

            File file = new File(sb.toString());

            String mimeType = URLConnection.guessContentTypeFromName(fileName);
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }
            byte[] fileByte = org.apache.commons.io.FileUtils.readFileToByteArray(file);

            response.setContentType(mimeType);
            response.setHeader("Content-Disposition", "attachment; filename=\"".concat(getFileNm(browser, fileName)).concat("\""));
            response.setContentLength(fileByte.length);
            response.getOutputStream().write(fileByte);
            response.getOutputStream().flush();
            response.getOutputStream().close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static  String getBrowser(HttpServletRequest req) {
        String userAgent = req.getHeader("User-Agent");
        if (userAgent.indexOf("MSIE") > -1 || userAgent.indexOf("Trident") > -1 || userAgent.indexOf("Edge") > -1) {
            return "MSIE";
        } else if (userAgent.indexOf("Chrome") > -1) {
            return "Chrome";
        } else if (userAgent.indexOf("Opera") > -1) {
            return "Opera";
        } else if (userAgent.indexOf("Safari") > -1) {
            return "Safari";
        } else if (userAgent.indexOf("Firefox") > -1) {
            return "Firefox";
        } else {
            return null;
        }
    }

    private static String getFileNm(String browser, String fileNm) {
        String reFileNm = null;
        try {
            if (browser.equals("MSIE") || browser.equals("Trident") || browser.equals("Edge")) {
                reFileNm = URLEncoder.encode(fileNm, "UTF-8").replaceAll("\\+", "%20");
            } else {
                if (browser.equals("Chrome")) {
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < fileNm.length(); i++) {
                        char c = fileNm.charAt(i);
                        if (c > '~') {
                            sb.append(URLEncoder.encode(Character.toString(c), "UTF-8"));
                        } else {
                            sb.append(c);
                        }
                    }
                    reFileNm = sb.toString();
                } else {
                    reFileNm = new String(fileNm.getBytes("UTF-8"), "ISO-8859-1");
                }
                if (browser.equals("Safari") || browser.equals("Firefox")) reFileNm = URLDecoder.decode(reFileNm);
            }
        } catch (Exception e) {
        }
        return reFileNm;
    }
}
