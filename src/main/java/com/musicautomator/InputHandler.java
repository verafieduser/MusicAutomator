package com.musicautomator;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class InputHandler {


    public String loopingPromptUserInput(BufferedReader reader, String prompt, String rePrompt, Predicate<String> f) throws IOException {
        String input = promptUserInput(reader, prompt);
        while(f.test(input)){
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
