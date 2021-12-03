import java.util.HashMap;
import java.util.Map;
import GeneratePDF.GeneratePDF;
public class main {
    public static void main(String[] args) throws Exception {
        System.out.println("****test****");
        String pdfTemplatePath = "./resource/template.pdf";
        String pdfSavePath = "./resource/test_gen.pdf";
        // String filedName="image_1";
        // String imageFile="E:/VSCodeWorkSpace/GeneratePDF/resource/image_1.jpg";
        
        Map<String, Object> preData = GeneratePDF.preData();
        Map<String, String> preImage = new HashMap<>();
        preImage.put("image_1", "./resource/image_1.jpg");
        preImage.put("image_2", "./resource/image_2.jpg");
        GeneratePDF.generatePDF(pdfTemplatePath, pdfSavePath, preData, preImage);
    }
}
