package com.lego.system.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.TagProcessorFactory;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.itextpdf.tool.xml.pipeline.html.ImageProvider;
import com.lego.core.exception.CoreException;
import com.lego.core.util.StringUtil;
import com.lego.core.web.upload.FileHandler;
import com.lego.system.dao.ISysFileDao;
import com.lego.system.entity.SysFile;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
public class PrintUtil {

    private static TagProcessorFactory tpf;

    public static String buildPrintContent(String content, Map<String, String> params) {
        if (StringUtil.isBlank(content)) {
            return content;
        }
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = StrUtil.isNotEmpty(entry.getValue()) ? entry.getValue() : "";
            if (isJsonObject(value)) {
                JSONObject jsonObject = JSON.parseObject(value);
                value = jsonObject.getString("name");
            }
            content = content.replaceAll("(<span((?!/span>).)*data-lego-tag=\"" + key + "\".*?)(\\{.*?\\})(</span>)", "$1" + value + "$4");
        }
        return content;
    }

    private static boolean isJsonObject(String value) {
        if (StringUtil.isBlank(value)) {
            return false;
        }
        return Pattern.compile("^\\{.*\\}$").matcher(value).matches();
    }

    public static String buildTempFile(String content, String fileType, ISysFileDao fileDao, FileHandler fileHandler) {
        if (StrUtil.isEmpty(content)) {
            content = "<br/>";
        }
        String html = "<html>\n" +
            "<head>\n" +
            "<style>\n" +
            "/**\n" +
            "* Copyright (c) Tiny Technologies, Inc. All rights reserved.\n" +
            "* Licensed under the LGPL or a commercial license.\n" +
            "* For LGPL see License.txt in the project root for license information.\n" +
            "* For commercial licenses see https://www.tiny.cloud/\n" +
            "*/\n" +
            "body {\n" +
            "  font-family:  simsun, serif,-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;\n" +
            "  line-height: 1.4;\n" +
            "  padding: 60px;\n" +
            "  width: 595px;\n" +
            "  margin: 0 auto;\n" +
            "  border-radius: 4px;\n" +
            "  background: white;\n" +
            "  min-height: 100%;\n" +
            "}\n" +
            "p { margin: 5px 0;\n" +
            "  line-height: 1.5;\n" +
            "}\n" +
            "table {\n" +
            "  border-collapse: collapse;\n" +
            "}\n" +
            "table th,\n" +
            "table td {\n" +
            "  border: 1px solid #ccc;\n" +
            "  padding: 0.4rem;\n" +
            "}\n" +
            "*{\n" +
            "     font-family:  simsun, serif,-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;\n" +
            "}\n" +
            "figure {\n" +
            "  display: table;\n" +
            "  margin: 1rem auto;\n" +
            "}\n" +
            "figure figcaption {\n" +
            "  color: #999;\n" +
            "  display: block;\n" +
            "  margin-top: 0.25rem;\n" +
            "  text-align: center;\n" +
            "}\n" +
            "hr {\n" +
            "  border-color: #ccc;\n" +
            "  border-style: solid;\n" +
            "  border-width: 1px 0 0 0;\n" +
            "}\n" +
            "code {\n" +
            "  background-color: #e8e8e8;\n" +
            "  border-radius: 3px;\n" +
            "  padding: 0.1rem 0.2rem;\n" +
            "}\n" +
            ".mce-content-body:not([dir=rtl]) blockquote {\n" +
            "  border-left: 2px solid #ccc;\n" +
            "  margin-left: 1.5rem;\n" +
            "  padding-left: 1rem;\n" +
            "}\n" +
            ".mce-content-body[dir=rtl] blockquote {\n" +
            "  border-right: 2px solid #ccc;\n" +
            "  margin-right: 1.5rem;\n" +
            "  padding-right: 1rem;\n" +
            "}\n" +
            "\n" +
            "</style>\n" +
            "</head>\n" +
            "<body>\n" +
            content +
            "</body>\n" +
            "</html>";
        String date = DateUtil.format(new Date(), "yyyyMMdd");
        String folderPath = FileUtil.getTmpDirPath() + File.separator + "print" + File.separator + date;
        String uuid = IdUtil.simpleUUID();
        String fileName = uuid + ".pdf";
        FileUtil.mkdir(folderPath + File.separator);
        String path = folderPath + File.separator + fileName;
        if ("pdf".equals(fileType)) {
            createPdfFile(path, html, fileDao, fileHandler);
        } else if ("word".equals(fileType)) {
            ByteArrayInputStream bais = null;
            FileOutputStream ostream = null;
            try {
                bais = IoUtil.toUtf8Stream(html);
                POIFSFileSystem poifs = new POIFSFileSystem();
                DirectoryEntry directoryEntry = poifs.getRoot();
                directoryEntry.createDocument("WordDocument", bais);
                File file = FileUtil.file(folderPath + File.separator + uuid + ".doc");
                ostream = new FileOutputStream(file);
                poifs.writeFilesystem(ostream);
            } catch (Exception e) {
                throw new CoreException("创建临时Word文件异常", e);
            } finally {
                IoUtil.close(bais);
                IoUtil.close(ostream);
            }
            createPdfFile(path, html, fileDao, fileHandler);
        }
        return path;
    }

    @SneakyThrows
    private static void createPdfFile(String path, String html, ISysFileDao fileDao, FileHandler fileHandler) {
        Document document = null;
        PdfWriter pdfWriter = null;
        try {
            document = new Document(PageSize.A4);
            File file = FileUtil.file(path);
            pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(html.getBytes());
            CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
            HtmlPipelineContext htmlContext = new HtmlPipelineContext(new CssAppliersImpl(new FontProvider()));
            htmlContext.setAcceptUnknown(true).autoBookmark(true).setTagFactory(getDefaultTagProcessorFactory()).setResourcesRootPath(null);
            htmlContext.setImageProvider(new ImageProvider() {
                @Override
                public Image retrieve(String src) {
                    try {
                        String code = src.substring(src.lastIndexOf("/") + 1);
                        SysFile localFile = fileDao.findByCode(code);
                        InputStream input = fileHandler.download(localFile.getPath());
                        return Image.getInstance(IoUtil.readBytes(input));
                    } catch (Exception e) {
                        log.error("打印图片{}失败", src);
                        return null;
                    }
                }

                @Override
                public String getImageRootPath() {
                    return null;
                }

                @Override
                public void store(String src, Image img) {
                }

                @Override
                public void reset() {
                }
            });
            HtmlPipeline htmlPipeline = new HtmlPipeline(htmlContext, new PdfWriterPipeline(document, pdfWriter));
            Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, htmlPipeline);
            XMLWorker worker = new XMLWorker(pipeline, true);
            XMLParser p = new XMLParser(true, worker, StandardCharsets.UTF_8);
            p.parse(byteArrayInputStream, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new CoreException("创建临时PDF文件异常", e);
        } finally {
            if (document != null) {
                document.close();
            }
            if (pdfWriter != null) {
                pdfWriter.close();
            }
        }
    }

    private static synchronized TagProcessorFactory getDefaultTagProcessorFactory() {
        if (null == tpf) {
            tpf = Tags.getHtmlTagProcessorFactory();
        }
        return tpf;
    }

    public static class FontProvider extends XMLWorkerFontProvider {

        @Override
        public Font getFont(String fontname, String encoding, boolean embedded, float size, int style, BaseColor color) {
            try {
                BaseFont baseFont = BaseFont.createFont("/fonts/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                return new Font(baseFont, size, style, color);
            } catch (Exception e) {
                log.error("本地字体引用出错:" + e.getMessage());
            }
            return super.getFont(fontname, encoding, embedded, size, style, color);
        }

        @Override
        public Font getFont(String fontname, String encoding, float size, int style) {
            try {
                BaseFont baseFont = BaseFont.createFont("/fonts/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                return new Font(baseFont, size, style);
            } catch (Exception e) {
                log.error("本地字体引用出错:" + e.getMessage());
            }
            return super.getFont(fontname, encoding, size, style);
        }
    }

}
