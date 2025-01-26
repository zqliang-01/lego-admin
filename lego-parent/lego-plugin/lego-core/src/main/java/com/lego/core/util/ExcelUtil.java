package com.lego.core.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.lego.core.exception.CoreException;
import com.lego.core.module.gen.converter.ExcelBigNumberConvert;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Excel相关处理
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExcelUtil {

    /**
     * 导出excel
     *
     * @param list      导出数据集合
     * @param sheetName 工作表的名称
     * @param clazz     实体类
     * @param response  响应体
     */
    public static <T> void exportExcel(List<T> list, String sheetName, Class<T> clazz, HttpServletResponse response) {
        try {
            resetResponse(sheetName, response);
            ServletOutputStream os = response.getOutputStream();
            EasyExcel.write(os, clazz)
                .autoCloseStream(false)
                // 自动适配
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                // 大数值自动转换 防止失真
                .registerConverter(new ExcelBigNumberConvert())
                .sheet(sheetName)
                .doWrite(list);
        } catch (IOException e) {
            throw new CoreException("导出Excel异常", e);
        }
    }

    /**
     * @param head      表头
     * @param data      导出数据集合
     * @param sheetName 工作表的名称
     * @param response  响应体
     */
    public static <T> void exportExcel(List<List<String>> head, List<List<String>> data, String sheetName, HttpServletResponse response) {
        try {
            resetResponse(sheetName, response);
            OutputStream os = response.getOutputStream();
            EasyExcel.write(os)
                .autoCloseStream(false)
                // 自动适配
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                // 大数值自动转换 防止失真
                .registerConverter(new ExcelBigNumberConvert())
                .sheet()
                .head(head)
                .doWrite(data);
        } catch (IOException e) {
            throw new CoreException("导出Excel异常", e);
        }
    }

    /**
     * 重置响应体
     */
    public static void resetResponse(String sheetName, HttpServletResponse response) throws UnsupportedEncodingException {
        String filename = encodingFilename(sheetName);
        String percentEncodedFileName = StringUtil.encodeUrl(filename);
        StringBuilder contentDisposition = new StringBuilder();
        contentDisposition.append("attachment;filename=");
        contentDisposition.append(percentEncodedFileName);

        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition,download-filename");
        response.setHeader("Content-disposition", contentDisposition.toString());
        response.setHeader("download-filename", percentEncodedFileName);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
    }

    /**
     * 编码文件名
     */
    public static String encodingFilename(String filename) {
        return DateUtil.toDateString(DateUtil.currentDateTime(), "yyyyMMdd_HHmmss") + "_" + filename + ".xlsx";
    }
}
