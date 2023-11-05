package com.verafied;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.function.Predicate;

public class InputHandler {


    /**
     * 
     * @param reader The input reader handling the input
     * @param prompt The initial message the user should be asked
     * @param rePrompt The message to send to the user duringa re-prompt
     * @param f should give false when it should re-prompt the user
     * @return
     * @throws IOException
     */
    public String loopingPromptUserInput(BufferedReader reader, String prompt, String rePrompt, Predicate<String> f) throws IOException {
        String input = promptUserInput(reader, prompt);
        while(!f.test(input)){
            System.out.println(input);
            input = promptUserInput(reader, rePrompt);
        }
        return input;
    }


    public String promptUserInput(BufferedReader reader, String prompt) throws IOException {
        System.out.print(prompt);
        return reader.readLine();
    }
}
