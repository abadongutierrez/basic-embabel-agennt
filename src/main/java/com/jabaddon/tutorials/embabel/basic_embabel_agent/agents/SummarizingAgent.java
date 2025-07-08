package com.jabaddon.tutorials.embabel.basic_embabel_agent.agents;

import com.embabel.agent.api.annotation.AchievesGoal;
import com.embabel.agent.api.annotation.Action;
import com.embabel.agent.api.annotation.Agent;
import com.embabel.agent.api.common.OperationContext;
import com.embabel.agent.api.common.PromptRunner;
import com.embabel.agent.domain.io.UserInput;
import com.embabel.common.ai.model.LlmOptions;
import com.embabel.common.ai.model.ModelSelectionCriteria;
import com.jabaddon.tutorials.embabel.basic_embabel_agent.domain.SummarizedPage;
import com.jabaddon.tutorials.embabel.basic_embabel_agent.domain.SummarizedPages;
import com.jabaddon.tutorials.embabel.basic_embabel_agent.domain.WebPageLinks;
import com.jabaddon.tutorials.embabel.basic_embabel_agent.tools.JSoupTool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Agent(description = "Agent to summarize content of web pages")
public class SummarizingAgent {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(SummarizingAgent.class);

    private final JSoupTool jSoupTool;
    private final boolean useOpenAI;
    private final int maxWordsInSummary;

    public SummarizingAgent(JSoupTool jSoupTool,
                           @Value("${app.useOpenAI:false}") boolean useOpenAI,
                           @Value("${app.max-words-in-summary:500}") int maxWordsInSummary) {
        this.jSoupTool = jSoupTool;
        this.useOpenAI = useOpenAI;
        this.maxWordsInSummary = maxWordsInSummary;
    }

    @Action
    public WebPageLinks extractWebPagesLinks(UserInput userInput) {
        return PromptRunner.usingLlm().createObjectIfPossible(String.format("""
                Extracts the urls from the provided user input.
                
                <user-input>
                %s
                </user-input>
                
                Extract only the links mentioned in the user input, dont add any other links.
                """.trim(), userInput.getContent()), WebPageLinks.class);
    }

    @Action
    public SummarizedPages summarizeWebPages(WebPageLinks webPageLinks, OperationContext operationContext) {
        if (this.useOpenAI) {
            return getSummarizedPagesUsingOpenAI(webPageLinks);
        }
        return getSummarizedPagesUsingLocalModels(webPageLinks, operationContext);
    }

    @Nullable
    private SummarizedPages getSummarizedPagesUsingOpenAI(WebPageLinks webPageLinks) {
        // Llama3.2 is not a great model, if you were using OpenAI you could use the following code instead
        // and summarize the content of each web page using the jsoup tool in a single prompt and request
        return PromptRunner.usingLlm(LlmOptions.fromCriteria(
                ModelSelectionCriteria.byName("gpt-4.1-mini")
        )).withToolObject(jSoupTool).createObjectIfPossible(
                String.format("""
                        Summarize the content of each web page.
                        
                        <links>
                        %s
                        </links>
                        
                        For each link, use the jsoup tool to get the text content and then summarize the content using maximum of %d words.
                        Return the summaries as a list of summarized pages with their URLs.
                        DO NOT include any additional information or links, just the summaries.
                        DO NOT ask the user for any additional information.
                        """.trim(), "- " + String.join("\n- ", webPageLinks.links()), maxWordsInSummary),
                SummarizedPages.class);
    }

    @NotNull
    private SummarizedPages getSummarizedPagesUsingLocalModels(WebPageLinks webPageLinks, OperationContext operationContext) {
        // This is probably not the best way since it will create a separate prompt/request for each link
        // but since we are working with local Llama3.2 model it is not a big deal.
        try {
            logger.info("Trying to summarize web pages using the jsoup tool");
            return new SummarizedPages(
                    webPageLinks.links().stream()
                            .map(link -> operationContext.promptRunner().withToolObject(jSoupTool).createObjectIfPossible(String.format("""
                                    Summarize the content of the web page: %s.
                                    You MUST use the jsoup tool to get the text content of the page.
                                    DO NOT provide information from your own knowledge or any other sources USE THE PROVIDED TOOL.
                                    Use a maximum of %d words for the summary.
                                    """.trim(), link, maxWordsInSummary), SummarizedPage.class))
                            .toList()
            );
        } catch (Exception ex) {
            logger.warn("Model wasn't able to summarize the content of the web pages using the jsoup tool, trying a different approach.", ex);
            // Model wasn't able to summarize the content of the web pages using the previous method
            // lets try to make it easier for the model by just asking it to summarize the text
            return new SummarizedPages(
                    webPageLinks.links().stream()
                            .map(link -> {
                                try {
                                    // calling the tool directly to get the page text
                                    String pageText = jSoupTool.getPageText(link);
                                    // ask the model just to summarize the text and return text as a result
                                    String summarizedPageText = operationContext.promptRunner().generateText(String.format("""
                                            Summarize the following text:
                                            
                                            <text>%s</text>
                                            
                                            Use a maximum of %d words. 
                                            """.trim(), pageText, maxWordsInSummary));
                                    return new SummarizedPage(link, summarizedPageText);
                                } catch (IOException e) {
                                    return new SummarizedPage(link, "No content available due to: " + e.getMessage());
                                }
                            })
                            .toList()
            );
        }
    }

    @AchievesGoal(description = "Show summarized content of the web pages to the user")
    @Action
    public SummarizedPages showSummarization(SummarizedPages summarizedPages) {
        return summarizedPages;
    }
}
