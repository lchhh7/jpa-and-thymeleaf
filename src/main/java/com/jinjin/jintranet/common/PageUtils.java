package com.jinjin.jintranet.common;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;

public class PageUtils {
    public static String page(Page paging, String methodName, HttpServletRequest request) {
        String contextPath = request.getContextPath();
        StringBuffer sb = new StringBuffer(200);

        int page = paging.getPageable().getPageNumber()+1;
        int firstPage = ((page-1) /10) * 10 + 1;
        //int lastPage = (((page-1)/10)+1 ) * 10;
        int lastPage = paging.getTotalPages();

        if (paging.getTotalElements() == 0) return "";

            sb.append("<a role='button' class='pfirst' onclick='" + methodName +"(\"1\")'><img src='" + contextPath +"/common/img/pfirst.png' alt='첫페이지'></a>");
            sb.append("<a role='button' class='pprev' onclick='" + methodName +"(\"" + (page-1) +"\")'><img src='" + contextPath +"/common/img/pprev.png' alt='앞페이지'></a>");

        for (int i = firstPage; i <= lastPage; i++) {
            sb.append("<a role='button' class='pnum");
            if (page == i) {
                sb.append(" active");
            }
            sb.append("' onclick='" + methodName +"(\"" + i + "\")'>").append(i).append("</a>");
        }

            sb.append("<a role='button' class='pnext' onclick='" + methodName +"(\"" + (page == lastPage ? page : (page+1)) +"\")'><img src='" + contextPath +"/common/img/pnext.png' alt='뒤페이지'></a>");
            sb.append("<a role='button' class='plast' onclick='" + methodName +"(\"" + lastPage +"\")'><img src='" + contextPath +"/common/img/plast.png' alt='마지막페이지'></a>");

        return sb.toString();
    }
}
