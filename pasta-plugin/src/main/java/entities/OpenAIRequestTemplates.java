package entities;

import chat.ChatRequest;
import chat.Message;

import java.util.ArrayList;
import java.util.List;

public class OpenAIRequestTemplates {

    private static final String MODEL_NAME = "gpt-4o";
    private static final String OS_NAME = System.getProperty("os.name");

    public static ChatRequest createSummaryRequest(String selectedCode, String fileContext) {
        List<Message> messages = new ArrayList<>();
        messages.add(new Message("system", """
                You are a helpful assistant tasked with providing a concise summary of a selected code snippet within a file.
                - The summary should describe the functionality of the code in a step-by-step manner, following the execution order as closely as possible.
                - Keep the summary brief (preferably 2-3 sentences, but 1 sentence is acceptable if sufficient).
                - Each sentence should be on a separate line for readability, beginning with a verb.
                
                Here are some examples of good summaries:
                
                Example 1:
                Code:
                ```java
                public void processData(List<String> data) {
                    for (String item : data) {
                        if (item.startsWith("A")) {
                            System.out.println(item);
                        }
                    }
                }
                ```
                Summary:
                Processes a list of strings by iterating through each item.
                Prints items that start with the letter 'A'.
                
                Example 2:
                Code:
                ```java
                public int calculateSum(int[] numbers) {
                    int sum = 0;
                    for (int num : numbers) {
                        sum += num;
                    }
                    return sum;
                }
                ```
                Summary:
                Initializes a sum variable to zero.
                Iterates through the array of numbers.
                Returns the total sum of all numbers.
                
                I will begin by presenting the entire file to establish context, followed by the specific code snippet for summarization.
                """));
        messages.add(new Message("user", """
                Below is the context of the file:
                
                %s
                
                Below is the selected code snippet:
                
                %s
                
                Please provide a concise summary of the functionality of this snippet in at 1-3 sentences. Write each sentence on a separate line, beginning with a verb.
                """.formatted(fileContext, selectedCode)));
        return new ChatRequest(MODEL_NAME, messages);
    }

    public static ChatRequest createProceduralModificationRequest(String selectedCode, String fileContext, String originalSummary, String modifiedSummary) {
        List<Message> messages = new ArrayList<>();
        messages.add(new Message("system", """
                As a helpful assistant, your task is to revise the selected code snippet to reflect the changes specified in its modified summary. The operating system of my environment is %s.
                
                Here are some examples of good modifications:
                
                Example 1:
                Original Code:
                ```java
                public void processData(List<String> data) {
                    for (String item : data) {
                        if (item.startsWith("A")) {
                            System.out.println(item);
                        }
                    }
                }
                ```
                Original Summary:
                Processes a list of strings by iterating through each item.
                Prints items that start with the letter 'A'.
                
                Modified Summary:
                Processes a list of strings by iterating through each item.
                Prints items that start with the letter 'B'.
                
                Modified Code:
                ```java
                public void processData(List<String> data) {
                    for (String item : data) {
                        if (item.startsWith("B")) {
                            System.out.println(item);
                        }
                    }
                }
                ```
                
                Example 2:
                Original Code:
                ```java
                public int calculateSum(int[] numbers) {
                    int sum = 0;
                    for (int num : numbers) {
                        sum += num;
                    }
                    return sum;
                }
                ```
                Original Summary:
                Initializes a sum variable to zero.
                Iterates through the array of numbers.
                Returns the total sum of all numbers.
                
                Modified Summary:
                Initializes a sum variable to zero.
                Iterates through the array of numbers.
                Returns the average of all numbers.
                
                Modified Code:
                ```java
                public double calculateAverage(int[] numbers) {
                    int sum = 0;
                    for (int num : numbers) {
                        sum += num;
                    }
                    return (double) sum / numbers.length;
                }
                ```
                
                Initially, I will present the entire file to set the context. Then, I will provide both the specific code snippet and its original summary,
                followed by the modified summary which indicates the desired changes.
                """.formatted(OS_NAME)));

        messages.add(new Message("user", """
                Below is the context of the entire file:
                
                %s
                
                Below is the selected code snippet:
                
                %s
                
                Original summary of this snippet:
                
                %s
                
                Modified summary (pay attention to the changes compared to the original):
                
                %s
                
                Please revise the code snippet to reflect the changes outlined in the modified summary.
                Focus only on modifying the selected snippet—do not add any additional code.
                Begin your response with ``` and conclude with ``` in separate lines.
                """.formatted(fileContext, selectedCode, originalSummary, modifiedSummary)));
        return new ChatRequest(MODEL_NAME, messages);
    }

    public static ChatRequest createDeclarativeModificationRequest(String selectedCode, String fileContext, String prompt) {
        List<Message> messages = new ArrayList<>();
        messages.add(new Message("system", """
                You are a helpful assistant tasked with modifying the selected code snippet based on the provided prompt. The operating system of my environment is %s.
                
                Here are some examples of good modifications:
                
                Example 1:
                Original Code:
                ```java
                public void processData(List<String> data) {
                    for (String item : data) {
                        if (item.startsWith("A")) {
                            System.out.println(item);
                        }
                    }
                }
                ```
                Prompt: "Modify the code to print items that start with 'B' instead of 'A'"
                
                Modified Code:
                ```java
                public void processData(List<String> data) {
                    for (String item : data) {
                        if (item.startsWith("B")) {
                            System.out.println(item);
                        }
                    }
                }
                ```
                
                Example 2:
                Original Code:
                ```java
                public int calculateSum(int[] numbers) {
                    int sum = 0;
                    for (int num : numbers) {
                        sum += num;
                    }
                    return sum;
                }
                ```
                Prompt: "Change the method to calculate the average instead of sum"
                
                Modified Code:
                ```java
                public double calculateAverage(int[] numbers) {
                    int sum = 0;
                    for (int num : numbers) {
                        sum += num;
                    }
                    return (double) sum / numbers.length;
                }
                ```
                
                Initially, I will present the entire file to establish context. Then, I will provide the specific code snippet along with the modification prompt.
                """.formatted(OS_NAME)));
        messages.add(new Message("user", """
                Below is the context of the entire file:
                
                %s
                
                Below is the selected code snippet:
                
                %s
                
                Prompt for modification:
                
                %s
                
                Please modify the code snippet based on the provided prompt.
                Focus only on modifying the selected snippet—do not add any additional code.
                Begin your response with ``` and conclude with ``` in separate lines.
                """.formatted(fileContext, selectedCode, prompt)));
        return new ChatRequest(MODEL_NAME, messages);
    }
}
