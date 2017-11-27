package com.company;

import sun.security.krb5.Config;

import javax.security.auth.x500.X500Principal;
import java.io.*;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.*;

public class Main {

    public static final String FILE_NAMES_MALE = "male_names.txt";
    public static final String FILE_NAMES_FEMALE = "female_names.txt";
    public static final String FILE_NAMES_LAST = "last_names.txt";
    public static final String FILE_KEY_WORDS = "key_words.txt";
    public static final String FILE_DICTIONARY = "words_alpha.txt";
    public static final String FILE_TECH_WORDS = "tech_words.txt";

    public static final String[] mas = {"AddOn", "Algorithm", "Architect", "Array", "Asynchronous", "Avatar", "Band", "Base", "Beta", "Binary", "Blog", "Board", "Boolean", "Boot", "Bot", "Browser", "Bug", "Cache", "Character", "Checksum", "Chip", "Circuit", "Client", "Cloud", "Cluster", "Code", "Codec", "Coder", "Column", "Command", "Compile", "Compression", "Computing", "Console", "Constant", "Control", "Cookie", "Core", "Cyber", "Default", "Deprecated", "Dev", "Developer", "Development", "Device", "Digital", "Domain", "Dynamic", "Emulation", "Encryption", "Engine", "Error", "Exception", "Exploit", "Export", "Extension", "File", "Font", "Fragment", "Frame", "Function", "Group", "Hacker", "Hard", "HTTP", "Icon", "Input", "IT", "Kernel", "Key", "Leak", "Link", "Load", "Logic", "Mail", "Mashup", "Mega", "Meme", "Memory", "Meta", "Mount", "Navigation", "Net", "Node", "Open", "OS", "Output", "Over", "Packet", "Page", "Parallel", "Parse", "Path", "Phone", "Ping", "Pixel", "Port", "Power", "Programmers", "Programs", "Protocol", "Push", "Query", "Queue", "Raw", "Real", "Repository", "Restore", "Root", "Router", "Run", "Safe", "Sample", "Scalable", "Script", "Server", "Session", "Shell", "Smart", "Socket", "Soft", "Solid", "Sound", "Source", "Streaming", "Symfony", "Syntax", "System", "Tag", "Tape", "Task", "Template", "Thread", "Token", "Tool", "Tweak", "URL", "Utility", "Viral", "Volume", "Ware", "Web", "Wiki", "Window", "Wire"};

    private static HashSet<String> sKeyWords;
    private static List<String> sDictionary, sMaleNames, sFemaleNames, sLastNames, sTechWords;

    private static Random sRandom = new Random();
    private static Properties sProperties = new Properties();

    private static String sProjectPath, sClonePath;
    private static int sNumberOfClones;
    private static List<String> sClonePaths = new ArrayList<>();

    public static void main(String[] args) {

        System.out.println("Start");
        sKeyWords = new HashSet<>(loadWordsToList(FILE_KEY_WORDS));
        sDictionary = loadWordsToList(FILE_DICTIONARY);
        sMaleNames = loadWordsToList(FILE_NAMES_MALE);
        sFemaleNames = loadWordsToList(FILE_NAMES_FEMALE);
        sLastNames = loadWordsToList(FILE_NAMES_LAST);
        sTechWords = loadWordsToList(FILE_TECH_WORDS);

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("config.properties");
            sProperties.load(inputStream);
        } catch (IOException e) {
            System.out.println("Can't load config.properties file.");
            e.printStackTrace();
        }

        sClonePath = sProperties.getProperty("CLONE_FOLDER");
        String[] temp = sClonePath.split("\\\\");
        sProjectPath = sClonePath.replace(temp[temp.length - 1], "");
        if (sProperties.getProperty("NUMBER_OF_CLONES") != null)
            sNumberOfClones = Integer.valueOf(sProperties.getProperty("NUMBER_OF_CLONES"));
        else {
            sNumberOfClones = 0;
            File directory = new File(sProjectPath);
            File[] fList = directory.listFiles();
            for (File file : fList){
                if (file.getPath().length() == sClonePath.length() &&
                        file.getPath().contains(sClonePath.substring(0, sClonePath.length() - 1))) {
                    sClonePaths.add(file.getPath());
                    sNumberOfClones++;
                }
            }
        }

        if (Boolean.valueOf(sProperties.getProperty("GENERATE_URL_CLASS"))) {
            String urls[] = sProperties.getProperty("URLS").split("#");
            int i = 0;
            for (String path : sClonePaths) {
                generateUrlClass(path, urls[i]);
                i++;
            }
        }

        if (Boolean.valueOf(sProperties.getProperty("GENERATE_DICTIONARY"))) {
            String urls[] = sProperties.getProperty("URLS").split("#");
            for (String path : sClonePaths) {
                generateDictionary(path);
            }
        }

        if (Boolean.valueOf(sProperties.getProperty("GENERATE_KEYSTORE"))) {
            String keystoreName = sProperties.getProperty("KEYSTORE_NAME");
            String path = sProjectPath;
            if (sProperties.getProperty("NUMBER_OF_CLONES") != null && sProperties.getProperty("NUMBER_OF_CLONES").equals("1")) {
                path = sProperties.getProperty("KEYSTORE_PATH");
                generateKeystore(path, keystoreName + ".jks");
            }
            else {
                sNumberOfClones = 5; ///delete
                for (int i = 1; i <= sNumberOfClones; i++) {
                    generateKeystore(path, keystoreName + i + ".jks");
                }
            }
        }
    }

    public static void generateUrlClass(String path, String url) {
        String currentPath = path + sProperties.getProperty("PATH_TO_SRC") + "\\src\\main\\java";
        String name = sProperties.getProperty("URL_CLASS_NAME");
        File directory;
        File[] fList;
        do {
            System.out.println(currentPath);
            directory = new File(currentPath);
            fList = directory.listFiles();
            currentPath = fList[0].getPath();
        } while (fList[0].isDirectory());
        currentPath = fList[0].getParent();


        FileWriter fout = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        HashSet<String> mySet = new HashSet<>();
        String ch;

        for (int i=0; i < url.length(); i++) {
            ch = url.substring(i,i+1);
            mySet.add(ch);
        }

        char q = 'j';

        for (int i=0; i < 6 ; i+=3) {   //ненужные буквы
            mySet.add(Character.toString((char)(q+i)));
        }

        String targetUrl = "";

        try {
            fout = new FileWriter(currentPath + "\\" + name + ".java");
        } catch (IOException e) {
            System.out.println(""); ///////////добавить вывод сообщения
            e.printStackTrace();
        }
        try {
            fout.write("public class " + name + " {\n");

            Iterator<String> iterator = mySet.iterator();
            q = 'a';
            while (iterator.hasNext()) {
                fout.write("\tpublic static String " + q + " = \"" + iterator.next() + "\";\n");
                q++;
            }

            fout.write("}\n//");

            for (int i=0; i < url.length(); i++) {  // формируем строку запроса
                ch = url.substring(i,i+1);
                q = 'a';
                iterator = mySet.iterator();
                while (iterator.hasNext()) {
                    if (ch.equals(iterator.next()))
                        break;
                    q++;
                }
                fout.write(name + "." + q + " + ");
                targetUrl = targetUrl + name + "." + q + " + ";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (fout != null)
                fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        targetUrl = targetUrl.substring(0, targetUrl.length() - 3);
        System.out.println(targetUrl);

    }

    public static void generateDictionary(String path) {
        String name = sProperties.getProperty("DICTIONARY_NAME");
        String folder = sProperties.getProperty("DICTIONARY_FOLDER");
        int dictionarySize = Integer.valueOf(sProperties.getProperty("NUMBER_OF_WORDS"));
        List<String> mySuperDic = new ArrayList<>();
        for (int j=0; j < dictionarySize; j++) {  // формируем словарь
            String goodWord = sDictionary.get(sRandom.nextInt(sDictionary.size()));
            while (sKeyWords.contains(goodWord)) {
                goodWord = sDictionary.get(sRandom.nextInt(sDictionary.size()));
            }
            mySuperDic.add(goodWord);
        }

        FileWriter fout = null;
        try {
            fout = new FileWriter(path + folder + "\\" + name);
        } catch (IOException e) {
            System.out.println("File wasn't created: " + path + folder + "\\" + name);
            e.printStackTrace();
        }

        for (int j=0; j < dictionarySize; j++) {  // формируем словарь
            try {
                fout.write(mySuperDic.get(j));
                fout.write("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateKeystore(String path, String keystoreName) {
        Runtime rt = Runtime.getRuntime();
        String command = "";
        String storePass = sProperties.getProperty("KEYSTORE_PASSWORD");
        String alias = sProperties.getProperty("ALIAS");
        String keyPass = sProperties.getProperty("KEY_PASSWORD");
        String commonName;
        if (sProperties.getProperty("USER_GENDER").toUpperCase().equals("MALE"))
            commonName = getFullName(Gender.MALE);
        else if (sProperties.getProperty("USER_GENDER").toUpperCase().equals("FEMALE"))
            commonName = getFullName(Gender.FEMALE);
        else
            commonName = getFullName(Gender.NONE);
        String organizationName = getCompanyName();

        command = "cmd.exe /c keytool -genkey -keystore " + path + "\\" + keystoreName +
                " -storepass " + storePass + " -alias " + alias + " -keypass " + keyPass +
                " -dname \"CN=" + commonName + " O=" + organizationName + " C=RU\" -validity 10000";

        System.out.println(command);

        try {
            Process pr = rt.exec(command);
        } catch (IOException e) {
            System.out.println("Can't generate keystore");
            e.printStackTrace();
        }
    }

    private static List<String> loadWordsToList(String fileName) {
        FileInputStream fstream = null;
        try {
            fstream = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("File \"" + fileName + "\" not found.");
        }
        BufferedReader bufr = new BufferedReader(new InputStreamReader(fstream));

        List<String> wordsList = new ArrayList<>();
        String word;
        try {
            while ((word = bufr.readLine()) != null){
                wordsList.add(word);
            }
        } catch (IOException e) {
            System.out.println("Something wrong with file \"" + fileName + "\"");
        }
        return wordsList;
    }

    public static String getFullName(Gender gender) {
        if (gender == Gender.NONE)
            gender = Gender.getRandomGender();
        if (gender == Gender.MALE)
            return getMaleName() + " " + getLastName();
        else
            return getFemaleName() + " " + getLastName();
    }

    public static String getCompanyName() {
        String temp = sDictionary.get(sRandom.nextInt(sDictionary.size()));
        temp = temp.toUpperCase().substring(0, 1) + temp.substring(1);
        return temp + sTechWords.get(sRandom.nextInt(sTechWords.size()));
    }

    public static String getMaleName() {
        return sMaleNames.get(sRandom.nextInt(sMaleNames.size()));
    }

    public static String getFemaleName() {
        return sFemaleNames.get(sRandom.nextInt(sFemaleNames.size()));
    }

    public static String getLastName() {
        return sLastNames.get(sRandom.nextInt(sLastNames.size()));
    }

    private void loadProperties() {

    }
}
