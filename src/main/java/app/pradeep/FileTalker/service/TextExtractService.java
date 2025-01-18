package app.pradeep.FileTalker.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;

@Service
public class TextExtractService {
    public String extractTextFromPdf(MultipartFile file) throws Exception {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            return new PDFTextStripper().getText(document);
        }
    }

    public String extractTextFromPptx(MultipartFile file) throws Exception {
        StringBuilder sb = new StringBuilder();
        try (XMLSlideShow pptx = new XMLSlideShow(file.getInputStream())) {
            for (XSLFSlide slide : pptx.getSlides()) {
                for (XSLFShape shape : slide.getShapes()) {
                    if (shape instanceof org.apache.poi.xslf.usermodel.XSLFTextShape) {
                        sb.append(((org.apache.poi.xslf.usermodel.XSLFTextShape) shape).getText()).append("\n");
                    }
                }
            }
        }
        return sb.toString();
    }

    public String extractTextFromTextFile(MultipartFile file) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            return reader.lines().reduce("", (acc, line) -> acc + line + "\n");
        }
    }

    public void saveTextToFile(String text) throws Exception {
        // Define the path to the data folder inside resources
        String filePath = "src/main/resources/data/sample.txt";

        // Create a file object and write the extracted text to it
        File file = new File(filePath);
        try (FileWriter writer = new FileWriter(file, false)) { // false ensures the file is overwritten
            writer.write(text);
        }
    }
}
