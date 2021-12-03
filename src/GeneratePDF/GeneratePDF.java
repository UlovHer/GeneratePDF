package GeneratePDF;

import java.io.ByteArrayOutputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class GeneratePDF {
    public static void main(String[] args) {
        System.out.println("****test****");
        String pdfTemplatePath = "E:/VSCodeWorkSpace/GeneratePDF/resource/template.pdf";
        String pdfSavePath = "E:/VSCodeWorkSpace/GeneratePDF/resource/gen_test.pdf";
        // String filedName="image_1";
        // String imageFile="E:/VSCodeWorkSpace/GeneratePDF/resource/image_1.jpg";
        
        Map<String, Object> preData = preData();
        Map<String, String> preImage = new HashMap<>();
        preImage.put("image_1", "E:/VSCodeWorkSpace/GeneratePDF/resource/image_1.jpg");
        preImage.put("image_2", "E:/VSCodeWorkSpace/GeneratePDF/resource/image_2.jpg");
        generatePDF(pdfTemplatePath, pdfSavePath, preData, preImage);
    }

    /**
     * 填充pdf模板
     * @param pdfTemplatePath
     * @param pdfSavePath
     * @param preData
     * @param preImage
     */
    public static void generatePDF(String pdfTemplatePath, String pdfSavePath, Map<String, Object> preData,
            Map<String, String> preImage) {
        // pdf模板
        System.out.println("****generate pdf****");
        // 模板pdf的路径,创建pdf reader
        try {
            PdfReader pr = new PdfReader(pdfTemplatePath);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            // 生成的pdf目标文件
            PdfStamper ps = new PdfStamper(pr, bos);
            // PdfContentByte under = ps.getUnderContent(1);
            // 字体
            BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            ArrayList<BaseFont> list = new ArrayList<BaseFont>();
            list.add(bf);
            AcroFields fields = ps.getAcroFields();
            fields.setSubstitutionFonts(list);
            fillData(fields, preData);
            // 设置属性生成文档

            for (Map.Entry<String, String> entry : preImage.entrySet()) {
                // String mapKey = entry.getKey();
                // String mapValue = entry.getValue();
                // System.out.println(mapKey+":"+mapValue);
                addImage(ps, fields, entry.getKey(), entry.getValue());
                // 填充图片
            }

            ps.setFormFlattening(true);
            ps.close();

            // 生成的pdf的文件路径
            OutputStream os = new FileOutputStream(pdfSavePath);
            os.write(bos.toByteArray());
            os.flush();
            os.close();
            bos.close();
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }

    }

    /**
     * 填充文本数据
     * 
     * @param fields
     * @param preData
     */
    public static void fillData(AcroFields fields, Map<String, Object> preData) {
        System.out.println("****fill data****");
        // 将map中的数据填充至fields中
        for (Object key : preData.keySet()) {
            try {
                fields.setField(key.toString(), preData.get(key).toString());
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 准备文本数据
     * 
     * @return
     */
    public static Map<String, Object> preData() {
        System.out.println("****pre data****");
        Map<String, Object> fillDataMap = new HashMap<>();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy.MM.dd-HH:mm:ss");
        Date currentTime = new Date();
        fillDataMap.put("report_name", "TEST检测报告");
        fillDataMap.put("name", "TEST");
        fillDataMap.put("time", dateFormatter.format(currentTime));
        fillDataMap.put("total_num", "500");
        fillDataMap.put("accuracy", "90%");
        fillDataMap.put("result_1", "500");
        fillDataMap.put("result_2", "90%");
        fillDataMap.put("location", "HongKong");
        fillDataMap.put("woker", "John");
        return fillDataMap;
    }

    /**
     * 添加图像
     * 
     * @param stamper
     * @param form
     * @param field
     * @param fieldValue
     */
    public static void addImage(PdfStamper stamper, AcroFields form, String field, String fieldValue) {
        System.out.println("****add image****");
        try {
            java.util.List<AcroFields.FieldPosition> photograph = form.getFieldPositions(field);
            if (photograph != null && photograph.size() > 0) {
                Rectangle rect = photograph.get(0).position;
                Image img = Image.getInstance(fieldValue);
                img.scaleToFit(rect.getWidth(), rect.getHeight());
                img.setBorder(2);
                img.setAbsolutePosition(
                        photograph.get(0).position.getLeft() + (rect.getWidth() - img.getScaledWidth()),
                        photograph.get(0).position.getTop() - (rect.getHeight()));
                PdfContentByte cb = stamper.getOverContent((int) photograph.get(0).page);
                cb.addImage(img);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
