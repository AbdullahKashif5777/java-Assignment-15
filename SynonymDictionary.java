package wordsynonymdictionary;
import java.io.*;
import java.util.*;

public class SynonymDictionary {
    private Map<String, List<String>> dictionary;

    public SynonymDictionary() {
        dictionary = new HashMap<>();
    }

    public void insertWord(String word, List<String> synonyms) {
        dictionary.putIfAbsent(word, new ArrayList<>());
        List<String> existingSynonyms = dictionary.get(word);
        for (String synonym : synonyms) {
            if (!existingSynonyms.contains(synonym)) {
                existingSynonyms.add(synonym);
            }
        }
    }

    public List<String> searchWord(String word) {
        return dictionary.getOrDefault(word, null);
    }

    public void deleteWord(String word) {
        dictionary.remove(word);
    }

    public void updateWord(String word, List<String> newSynonyms) {
        if (dictionary.containsKey(word)) {
            List<String> existingSynonyms = dictionary.get(word);
            existingSynonyms.clear();
            existingSynonyms.addAll(newSynonyms);
        }
    }


    public void saveToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Map.Entry<String, List<String>> entry : dictionary.entrySet()) {
                writer.println(entry.getKey() + ":" + String.join(",", entry.getValue()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void loadFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                String word = parts[0];
                List<String> synonyms = Arrays.asList(parts[1].split(","));
                insertWord(word, new ArrayList<>(synonyms));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object[][] getAllEntries() {
        Object[][] entries = new Object[dictionary.size()][2];
        int index = 0;
        for (Map.Entry<String, List<String>> entry : dictionary.entrySet()) {
            entries[index][0] = entry.getKey();
            entries[index][1] = String.join(", ", entry.getValue());
            index++;
        }
        return entries;
    }

    public int size() {
        return dictionary.size();
    }
}





