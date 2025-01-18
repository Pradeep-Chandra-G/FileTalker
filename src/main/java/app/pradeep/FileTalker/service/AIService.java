package app.pradeep.FileTalker.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AIService {


    //    "C:/Users/prade/OneDrive/Desktop/Java/AI/Spring AI/FileTalker/src/main/resources/data/sample.txt"
    @Value("classpath:/data/sample.txt")
    private Resource fileResource;

    @Autowired
    VectorStore vectorStore;

    public VectorStore addDataInVectorStore() {
        TextReader textReader = new TextReader(fileResource);

        textReader.getCustomMetadata().put("filename", "sample.txt");

        List<Document> documents0 = textReader.read();

        List<Document> documents = new TokenTextSplitter().apply(documents0);

        vectorStore.add(documents);

        return vectorStore;
    }



}

