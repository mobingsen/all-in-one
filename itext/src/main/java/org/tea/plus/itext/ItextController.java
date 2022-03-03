package org.tea.plus.itext;

import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.rtf.RtfWriter2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * https://blog.csdn.net/xygg0801/article/details/53286502
 * https://www.cnblogs.com/ldl326308/p/10961616.html
 * @author mobingsen
 */
@Slf4j
@RestController
public class ItextController {

    private static final String DOC = "doc";

    @GetMapping("/download")
    public void download(String type, HttpServletResponse response) throws Exception {
        ExamScore examScore = new ExamScore();
        Document document = new Document(PageSize.A4);
        String fileName = "学生考试成绩-" + examScore.getStuName() + (DOC.equals(type) ? ".doc" : ".pdf");
        // 设置编码
        response.setCharacterEncoding("utf-8");
        //设置响头部
        response.setContentType("application/octet-stream;charset=UTF-8");
        //设置文件下载的默认名称
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        if (DOC.equals(type)) {
            RtfWriter2.getInstance(document, response.getOutputStream());
            // RtfWriter2.getInstance(document, new FileOutputStream("C:\\dev\\IdeaCodes\\java-make-topics\\Itext\\src\\main\\resources\\" + fileName));
        } else {
            PdfWriter.getInstance(document, response.getOutputStream());
            // PdfWriter.getInstance(document, new FileOutputStream("C:\\dev\\IdeaCodes\\java-make-topics\\Itext\\src\\main\\resources\\" + fileName));
        }
        // 设置中文字体
        BaseFont bfChinese = getBaseFont();
        Font boldFont = new Font(bfChinese);
        boldFont.setStyle(FontFactory.HELVETICA);
        boldFont.setStyle(Font.BOLD);
        boldFont.setColor(new Color(0, 0, 0));

        Font normalFont = new Font(bfChinese);
        normalFont.setStyle(FontFactory.HELVETICA);
        normalFont.setColor(new Color(0, 0, 0));

        document.open();
        //创建段落
        Font bigFont = new Font(bfChinese);
        bigFont.setSize(18);
        bigFont.setStyle(Font.BOLD);
        bigFont.setStyle(FontFactory.HELVETICA);
        bigFont.setColor(new Color(0, 0, 0));
        Paragraph p = new Paragraph("学生考试成绩_" + examScore.getStuName(), bigFont);
        //设置段落为居中对齐
        p.setAlignment(Element.ALIGN_CENTER);
        document.add(p);
        document.add(new Paragraph("\n"));
        Paragraph companyName = getParagraph("班级名称：", examScore.getGradeName(), bfChinese);
        companyName.setSpacingBefore(5);
        companyName.setSpacingAfter(5);
        document.add(companyName);
        Paragraph dbType = getParagraph("班级类型：", examScore.getClassType(), bfChinese);
        dbType.setSpacingBefore(5);
        dbType.setSpacingAfter(5);
        document.add(dbType);
        Paragraph dbName = getParagraph("班主任：", examScore.getTeacherName(), bfChinese);
        dbName.setSpacingBefore(5);
        dbName.setSpacingAfter(5);
        document.add(dbName);
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("考试评价", boldFont));
        Paragraph dataCollectionResult = new Paragraph(examScore.getEval(), new Font(bfChinese));
        dataCollectionResult.setSpacingBefore(5);
        dataCollectionResult.setSpacingAfter(5);
        document.add(dataCollectionResult);

        document.add(new Paragraph("\n"));
        document.add(new Paragraph("考试成绩", boldFont));
        Paragraph tableParagraph = new Paragraph();
        Table table = getTable(examScore, bfChinese);
        tableParagraph.add(table);
        tableParagraph.setAlignment(Element.ALIGN_LEFT);
        document.add(tableParagraph);
        document.close();

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.flush();
        outputStream.close();
    }

    private Table getTable(ExamScore examScore, BaseFont bfChinese) throws BadElementException {
        Table table = new Table(2, examScore.getScore().size() + 1);
        table.setWidth(100);
        table.setPadding(5);

        Font tableFont = new Font(bfChinese);
        tableFont.setStyle(FontFactory.HELVETICA);
        tableFont.setStyle(Font.BOLD);
        tableFont.setColor(new Color(0, 0, 0));

        Cell cellName = new Cell();
        cellName.setBackgroundColor(Color.LIGHT_GRAY);
        cellName.add(new Paragraph("学科", tableFont));
        table.addCell(cellName, new Point(0, 0));
        Cell cellData = new Cell();
        cellData.setBackgroundColor(Color.LIGHT_GRAY);
        cellData.add(new Paragraph("成绩", tableFont));
        table.addCell(cellData, new Point(0, 1));
        Font tableColumnFont = new Font(bfChinese);
        int i = 1;
        int total = 0;
        for (Map.Entry<String, String> entry : examScore.getScore().entrySet()) {
            table.addCell(new Paragraph(entry.getKey(), tableColumnFont), new Point(i, 0));
            table.addCell(new Paragraph(entry.getValue(), tableColumnFont), new Point(i, 1));
            total += Integer.parseInt(entry.getValue());
            i++;
        }
        table.addCell(new Paragraph("总分", tableFont), new Point(i, 0));
        table.addCell(new Paragraph(String.valueOf(total), tableFont), new Point(i, 1));
        return table;
    }

    private BaseFont getBaseFont() throws DocumentException, IOException {
        //第三种：使用资源字体，也就是自己下载的字体
        try {
            BaseFont bfChinese = BaseFont.createFont("", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            if (bfChinese != null) {
                return bfChinese;
            }
        } catch (Exception e) {
            log.info("自定义字体不存在");
        }
        //第一种：使用iTextAsian.jar包中的字体
        try {
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            if (bfChinese != null) {
                return bfChinese;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("STSong-Light 字体不存在");
        }
        //第二种：使用Windows系统字体
        try {
            BaseFont bfChinese = BaseFont.createFont("C:\\Windows\\Fonts\\simfang.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            if (bfChinese != null) {
                return bfChinese;
            }
        } catch (Exception e) {
            log.info("Windows系统字体不存在");
        }
        return BaseFont.createFont();
    }

    private static Paragraph getParagraph(String name, String value, BaseFont bfChinese) {
        Paragraph paragraph = new Paragraph();
        Font boldFont = new Font(bfChinese);
        boldFont.setStyle(Font.BOLD);
        Font normalFont = new Font(bfChinese);
        Chunk chunk1 = new Chunk(name, boldFont);
        Chunk chunk2 = new Chunk(value, normalFont);
        paragraph.addAll(Arrays.asList(chunk1, chunk2));
        paragraph.setAlignment(Element.ALIGN_LEFT);
        return paragraph;
    }
}