package app.pradeep.FileTalker.controller;


import app.pradeep.FileTalker.service.TextExtractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/files")
public class FileUploadController {

    @Autowired
    TextExtractService fileService;

    @GetMapping
    public String uploadForm() {
        return "uploadForm";
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            String contentType = file.getContentType();
            String extractedText = switch (contentType) {
                case "application/pdf" -> fileService.extractTextFromPdf(file);
                case "application/vnd.openxmlformats-officedocument.presentationml.presentation" ->
                        fileService.extractTextFromPptx(file);
                case "text/plain" -> fileService.extractTextFromTextFile(file);
                default -> "Unsupported file type. Please upload a PDF, PPTX, or text file.";
            };

            // Save the extracted text to sample.txt in the data folder
            fileService.saveTextToFile(extractedText);

            return "redirect:/files/chat";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/files/error";
        }
    }

    @GetMapping("/chat")
    public String chat() {
        return "chat";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }
}
