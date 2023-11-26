package com.verafied;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Function;
import java.util.function.Predicate;

public class InputHandler {

    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    private boolean demo; 

    public InputHandler(){
        demo = false;
    }
    public InputHandler(boolean demo){
        this.demo = demo;
    }

    /**
     * 
     * @param reader The input reader handling the input
     * @param prompt The initial message the user should be asked
     * @param rePrompt The message to send to the user duringa re-prompt
     * @param f should give false when it should re-prompt the user
     * @return
     * @throws IOException
     */
    public String loopingPromptUserInput(String prompt, String rePrompt, Predicate<String> f) throws IOException {
        String input = promptUserInput(reader, prompt);
        while(!f.test(input) && input.equals("Q")){
            if(demo){
                System.out.println(input + "\nPredicate resulted in: " + f.test(input) + "\n");
                System.out.print("Please try again, or enter \"Q\" to exit: ");
            }
            input = promptUserInput(reader, rePrompt);
        }
        return input;
    }

    /**
     * This method takes a function instead of a predicate,
     * while object returned by function is null, retry (not found)
     * @param reader
     * @param prompt
     * @param rePrompt
     * @param f
     * @return
     * @throws IOException
     */
    public Object loopingPromptUserInput(String prompt, String rePrompt, Function<String, Object> f) throws IOException{
        String input = promptUserInput(reader, prompt);
        Object result = f.apply(input);
        while(result==null){
            if(demo){
                System.out.println("\nFunction resulted in null, enter 0 to exit");
            }
            input = promptUserInput(reader, rePrompt);
            if(input.equalsIgnoreCase("0")){
                break;
            }
            result = f.apply(input);
        }
        return result;   
    }


    public String promptUserInput(BufferedReader reader, String prompt) throws IOException {
        System.out.print(prompt);
        return reader.readLine();
    }
}
