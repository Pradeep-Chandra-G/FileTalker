package app.pradeep.FileTalker.controller;

import app.pradeep.FileTalker.service.AIService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class AIController {

    @Autowired
    private AIService aiService;

    @Autowired
    private ChatClient chatClient;



    @GetMapping("/chat")
    public String chatInterface() {
        return "chat"; // Returns the Thymeleaf chat interface
    }

    @PostMapping("/response")
    public String getResponse(@RequestParam String message, Model model) {
        try {
            // Add data to the AI service vector store
            VectorStore vectorStore = aiService.addDataInVectorStore();

            // Get AI response
            String response = chatClient.prompt()
                    .advisors(new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults()))
                    .user(message)
                    .call()
                    .content();

            // Add response to the model for Thymeleaf
            model.addAttribute("response", response);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("response", "An error occurred while processing your question.");
        }

        model.addAttribute("message", message);
        return "chat"; // Reload the chat page with the response
    }
}
