import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, Spersh Goyal
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // YOUR CODE HERE — Call your recursive method!
        makeWords("", letters);
    }

    public void makeWords(String current, String remaining)
    {
        if (remaining.length() == 0)
        {
            words.add(current);
            return;
        }
        makeWords(current + remaining.charAt(0), remaining.substring(1));
        makeWords(current, remaining.substring(1));
    }


    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // YOUR CODE HERE
        breakMergeSort(words);


//        for (int i = 0; i < words.size(); i++)
//        {
//            removeDuplicates();
//        }

    }

    public ArrayList<String> breakMergeSort(ArrayList<String> currentWords)
    {
        if (currentWords.size() <= 1)
        {
            return currentWords;
        }
        ArrayList<String> l1 = new ArrayList<>();
        ArrayList<String> l2 = new ArrayList<>();
        for (int i = 0; i < words.size(); i++)
        {
            if (i < words.size() / 2)
            {
                l1.add(words.get(i));
            }
            l2.add(words.get(i));
        }
        l1 = breakMergeSort(l1);
        l2 = breakMergeSort(l2);
        return buildMergeSort(l1,l2);
    }
    public ArrayList<String> buildMergeSort(ArrayList<String> l1, ArrayList<String> l2)
    {
        ArrayList<String> currentWords = new ArrayList<String>();
//        for (int i = 0, j = 0; i < l1.size() && j < l2.size(); i++, j++)
        int i = 0, j = 0;
        while (i < l1.size() && j < l2.size())
        {
             if (l1.get(i).compareTo(l2.get(j)) < 0)
             {
                 currentWords.add(l1.get(i++));
             }
             else
             {
                 currentWords.add(l2.get(j++));
             }


        }
        for (int k = 0; k < l1.size(); k++)
        {
            currentWords.add(l1.get(k));
        }
        for (int k = 0; k < l2.size(); k++)
        {
            currentWords.add(l2.get(k));
        }
        return currentWords;
    }
//    public boolean isFound(String str)
//    {
//           return (binarySearch(str, 0, DICTIONARY_SIZE) == false);
//    }
    public boolean binarySearch(String target, int left, int right)
    {
        if (left == right)
        {
            return (DICTIONARY[left].equals(target));
        }
        int mid = left + ((right - left) / 2);

        if (target.compareTo(DICTIONARY[mid]) == 0)
        {
            return true;
        }

        if (target.compareTo(DICTIONARY[mid]) < 0)
        {
            return binarySearch(target, left, mid);
        }
        return binarySearch(target, mid+1, right);


    }
    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        // YOUR CODE HERE

        for (int i = 0; i < words.size(); i++)
        {
            if (binarySearch(words.get(i), 0, DICTIONARY_SIZE) == false)
            {
                words.remove(i);
            }
//            if (isFound(words.get(i)) == false)
//            {
//                words.remove(i);
//            }
        }

    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
