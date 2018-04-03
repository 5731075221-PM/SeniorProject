package com.example.uefi.seniorproject.breakiterator;

import android.util.Log;

import com.example.uefi.seniorproject.breakiterator.LongParseTree;
import com.example.uefi.seniorproject.breakiterator.Trie;
import com.example.uefi.seniorproject.databases.DBHelperDAO;

import java.io.*;
import java.util.*;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.lang3.tuple.Pair;

public class LongLexTo {

    //Private variables
    private Trie dict;               //For storing words from dictionary
    private LongParseTree ptree;     //Parsing tree (for Thai words)

    //Returned variables
    private Vector indexList;  //List of word index positions
    private Vector lineList;   //List of line index positions
    private Vector typeList;   //List of word types (for word only)
    private Iterator iter;     //Iterator for indexList OR lineList (depends on the call)

    ArrayList<String> stopword = new ArrayList<String>();

    /*******************************************************************/
    /*********************** Return index list *************************/
    /*******************************************************************/
    public Vector getIndexList() {
        return indexList; }

    /*******************************************************************/
    /*********************** Return type list *************************/
    /*******************************************************************/
    public Vector getTypeList() {
        return typeList; }

    /*******************************************************************/
    /******************** Iterator for index list **********************/
    /*******************************************************************/
    //Return iterator's hasNext for index list
    public boolean hasNext() {
        if(!iter.hasNext())
            return false;
        return true;
    }

    //Return iterator's first index
    public int first() {
        return 0;
    }

    //Return iterator's next index
    public int next() {
        return((Integer)iter.next()).intValue();
    }

    /*******************************************************************/
    /********************** Constructor (default) **********************/
    /*******************************************************************/
    public LongLexTo(String input) throws IOException {
        System.out.println("Self dict");
        dict=new Trie();
//    File dictFile=new File("lexitron.txt");
//    if(dictFile.exists())
//      addDict(dictFile);
//    else
//      System.out.println(" !!! Error: Missing default dictionary file, lexitron.txt");
        indexList=new Vector();
        lineList=new Vector();
        typeList=new Vector();
        ptree=new LongParseTree(dict, indexList, typeList);
    }
//    public LongLexTo() throws IOException {
//
//        dict=new Trie();
////        addDict();
////        File dictFile=new File("assets/dictionary/lexitron.txt");
////        if(dictFile.exists())
////            addDict(dictFile);
////        else
////            System.out.println(" !!! Error: Missing default dictionary file, lexitron.txt");
//        indexList=new Vector();
//        lineList=new Vector();
//        typeList=new Vector();
//        ptree=new LongParseTree(dict, indexList, typeList);
//    } //Constructor

    /*******************************************************************/
    /************** Constructor (passing dictionary file ) *************/
    /**
     * @param dictList
     * @param stopwordList*****************************************************************/
    public LongLexTo(ArrayList<String> dictList, ArrayList<String> stopwordList) throws IOException {
//        System.out.println("INLEXTO");
        dict=new Trie();
        addDict(dictList,stopwordList);
//        if(dictFile.exists())
//        { addDict(dictFile); System.out.println("ADDED DICT"); }
//        else
//            System.out.println(" !!! Error: The dictionary file is not found, " + dictFile.getName());
        indexList=new Vector<String>();
        lineList=new Vector();
        typeList=new Vector();
        ptree=new LongParseTree(dict, indexList, typeList);
    } //Constructor

    /*******************************************************************/
    /**************************** addDict ******************************/
    /**
     * @param dictList
     * @param stopwordList*****************************************************************/
//    public void addDict(String line) throws IOException {
//        line=line.trim();
//        if(line.length()>0)
//            dict.add(line);
//    }
    public void addDict(ArrayList<String> dictList, ArrayList<String> stopwordList) throws IOException {

        //Read words from dictionary
        String line, word, word2;
        int index;
//        FileReader fr = new FileReader(dictFile);
//        BufferedReader br = new BufferedReader(new InputStreamReader(dictFile));
//        BufferedReader br = new BufferedReader(new InputStreamReader(dictFile));
        for(int i = 0; i < dictList.size();i++){
            line = dictList.get(i).trim();
            if(line.length()>0)
                dict.add(line);
        }
//        while((line=br.readLine())!=null) {
//            line=line.trim();
//            if(line.length()>0)
//                dict.add(line);
//        }
        stopword = stopwordList;
//        BufferedReader br2 = new BufferedReader(new FileReader("src/stopword.csv"));
//        while((line=br2.readLine())!=null) {
//            Log.d("myvalue = ","value");
//            line=line.trim();
//            if(line.length()>0)
//                stopword.add(line);
//        }

    } //addDict

    /****************************************************************/
    /************************** wordInstance ************************/
    /****************************************************************/
    public void wordInstance(String text) {
        System.out.println("I'm In wordInStance");
        indexList.clear();
        typeList.clear();
        int pos, index;
        String word;
        boolean found;
        char ch;

        pos=0;
        while(pos<text.length()) {

            //Check for special characters and English words/numbers
            ch=text.charAt(pos);

            //English
            if(((ch>='A')&&(ch<='Z'))||((ch>='a')&&(ch<='z'))) {
                while((pos<text.length())&&(((ch>='A')&&(ch<='Z'))||((ch>='a')&&(ch<='z'))))
                    ch=text.charAt(pos++);
                if(pos<text.length())
                    pos--;
                indexList.addElement(new Integer(pos));
                typeList.addElement(new Integer(3));
            }
            //Digits
            else if(((ch>='0')&&(ch<='9'))||((ch>='�')&&(ch<='�'))) {
                while((pos<text.length())&&(((ch>='0')&&(ch<='9'))||((ch>='�')&&(ch<='�'))||(ch==',')||(ch=='.')))
                    ch=text.charAt(pos++);
                if(pos<text.length())
                    pos--;
                indexList.addElement(new Integer(pos));
                typeList.addElement(new Integer(3));
            }
            //Special characters
            else if((ch<='~')||(ch=='�')||(ch=='�')||(ch=='�')||(ch=='�')||(ch==',')) {
                pos++;
                indexList.addElement(new Integer(pos));
                typeList.addElement(new Integer(4));
            }
            //Thai word (known/unknown/ambiguous)
            else
            {
                //  System.out.println(text);
                //System.out.println("I do in ELSE");

                pos=ptree.parseWordInstance(pos, text);

            }
        } //While all text length
        iter=indexList.iterator();
    } //wordInstance

    public String genOutput(String line, LongLexTo tokenizer) throws IOException {
        String ProcessedText="";
//        String COMMA_DELIMITER = ",";
//        String NEW_LINE_SEPARATOR = "\n";
//        String FILE_HEADER = "id,symptoms";
//
//        FileWriter fileWriter = null;
//        fileWriter = new FileWriter("src/output.csv");
//        fileWriter.append(FILE_HEADER.toString());
//        fileWriter.append(NEW_LINE_SEPARATOR);

//        LongLexTo tokenizer=new LongLexTo(new File("assets/dictionary/lexitron.txt"));
//        File unknownFile=new File("unknown.txt");
//        if(unknownFile.exists())
//            tokenizer.addDict(unknownFile);
        Vector typeList;
        String text="";
        char ch;
        int begin, end, type;

        File inFile, outFile;
        FileReader fr;
        BufferedReader br;
        FileWriter fw;

//        BufferedReader streamReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("\n\n*******************************");
        System.out.println("*** LexTo: Lexeme Tokenizer ***");
        System.out.println("*******************************");

        if(line.length()>0) {
            tokenizer.wordInstance(line);
            typeList=tokenizer.getTypeList();
            begin=tokenizer.first();
            int i=0;

            while(tokenizer.hasNext()) {
                end=tokenizer.next();
                type=((Integer)typeList.elementAt(i++)).intValue();

                for(int j = 0; j< stopword.size(); j++) {
                    if(line.substring(begin, end).equals(stopword.get(j))) {
                        type = -1;
                        break;
                    }
                }
                if(type==0) {
//                	System.out.println("0 = "+line.substring(begin, end));
//                    System.out.print("PRINT = "+"<font color=#ff0000>" + line.substring(begin, end) + "</font>");
//                    fw.write("<font color=#ff0000>" + line.substring(begin, end) + "</font>");
//                    ProcessedText+=line.substring(begin, end) + "-";
                } else if(type==1) {
                    System.out.println("1 = "+line.substring(begin, end));
//                    System.out.print("PRINT = "+"<font color=#00bb00>" + line.substring(begin, end) + "</font>");
//                    fw.write("<font color=#00bb00>" + line.substring(begin, end) + "</font>");
                    ProcessedText+=line.substring(begin, end) + "-";
                } else if(type==2) {
                    System.out.println("2 = "+line.substring(begin, end));
//                    System.out.print("PRINT = "+"<font color=#0000bb>" + line.substring(begin, end) + "</font>");
//                    fw.write("<font color=#0000bb>" + line.substring(begin, end) + "</font>");
                    ProcessedText+=line.substring(begin, end) + "-";
                } else if(type==3) {
//                	System.out.println("3 = "+line.substring(begin, end));
//                    System.out.print("PRINT = "+"<font color=#aa00aa>" + line.substring(begin, end) + "</font>");
//                    fw.write("<font color=#aa00aa>" + line.substring(begin, end) + "</font>");
//                    ProcessedText+=line.substring(begin, end) + "-";
                } else if(type==4) {
//                	System.out.println("4 = "+line.substring(begin, end));
//                    System.out.print("PRINT = "+"<font color=#00aaaa>" + line.substring(begin, end) + "</font>");
//                    fw.write("<font color=#00aaaa>" + line.substring(begin, end) + "</font>");
//                    ProcessedText+=line.substring(begin, end) + "-";
                }
//                fw.write("<font color=#000000>|</font>");
//                ProcessedText+= line.substring(begin, end) + "-";
                begin=end;
            }
//            System.out.println("PRINT = "+ProcessedText.split("-").length);
//            fileWriter.append(String.valueOf(0));
//            fileWriter.append(COMMA_DELIMITER);
//            fileWriter.append(String.valueOf(ProcessedText));
//            fileWriter.append(COMMA_DELIMITER);
//            fileWriter.append(String.valueOf(ProcessedText.split("-").length));
//            fileWriter.append(NEW_LINE_SEPARATOR);

//            ProcessedText+="<br>\n";
        }
//        fileWriter.flush();
//        fileWriter.close();
        return ProcessedText;
    } //main
}