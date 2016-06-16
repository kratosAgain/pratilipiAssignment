package pratilipiSug;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class MainHash {
    
	private HashMap<String,ArrayList<String>> consonantMap = null;    //map from english to hindi consonants
	private HashMap<String,String> vowelMap = null;  //map from english to hindi vowels
	private HashMap<String,String> vowelMatra = null; //map from english vowel to hindi matras
	private HashMap<String,String> numberMap = null; //map from english number to hindi numbers
	private String halant = "";  //to make aadha hindi consonant 
	
	
	
	public MainHash(){
		//initializing all variables/structures 
		this.consonantMap = new HashMap<String,ArrayList<String>>();
		this.vowelMap = new HashMap<String,String>();
		this.vowelMatra = new HashMap<String,String>();
		this.numberMap = new HashMap<String,String>();
		this.halant = "्";
		this.makeMap();
	}
	
	public void makeMap(){
		String[] symbolsArr = "० १ २ ३ ४ ५ ६ ७ ८ ९ ".split(" ");
		//String[] hinConsoList = "क ख ग घ ङ च छ ज झ ञ ट ठ ड ढ ण त थ द ध न प फ ब भ म य र ल व श ष स ह ळ क्ष ज्ञ ऋ".split(" ");
		String[] crspondingHin = "क ख ग घ च छ ज झ ट ठ ड ढ ण त थ द ध न प फ फ ब भ म य र ल व व श ष स ह क्ष ज्ञ ".split(" ");
		String[] crspondingEng = "k kh g gh ch ch j jh t th d dh n t th d dh n p f ph b bh m y r l w v sh sh s h ksh gy ".split(" ");
		//System.out.println("क"+"ी");
		String[] hinVowelList = "अ आ इ ई उ ऊ ऎ ए ऒ औ ऋ ऋ".split(" ");
		String[] crspondingVolEng = "a aa i ee u oo e ai o au ri ra".split(" ");
		String[] vowelMatras = "ा ा ि ी ु ू ॆ ै ॊ ौ ृ ृ".split(" ");
		
		//putting values in english to hindi hashMap for consonants
		for(int i=0;i<crspondingEng.length;i++){
			if(this.consonantMap.containsKey(crspondingEng[i])){
				ArrayList<String> l = this.consonantMap.get(crspondingEng[i]);
				l.add(crspondingHin[i]);
				this.consonantMap.put(crspondingEng[i], l);
			}else{
				ArrayList<String> l = new ArrayList();
				l.add(crspondingHin[i]);
				this.consonantMap.put(crspondingEng[i], l);
			}
		}
		//System.out.println(Arrays.asList(crspondingVolEng));
		
		//putting values in english to hindi-vowels and matras
		for(int i=0;i<crspondingVolEng.length;i++){			
				this.vowelMap.put(crspondingVolEng[i], hinVowelList[i]);
				this.vowelMatra.put(crspondingVolEng[i],vowelMatras[i] );			
		}
		
		//putting numbers
		for(int i=0;i<10;i++){
			this.numberMap.put(Integer.toString(i), symbolsArr[i]);
		}
		
//		System.out.println(this.consonantMap);
//		System.out.println(this.vowelMap);
//		System.out.println(this.numberMap);
		
		//String[] comboAccentArr = "ः॑ ः॒ ं॑ ं॒".split(" ");
		//System.out.println(Arrays.asList(comboAccentArr));
		//StringBuilder s = new StringBuilder();
		//String k = "्क";
		//k="य"+k;
		//System.out.println("checking "+k);
		//System.out.println(s.reverse().reverse().toString());
		
		
	}
	//recursive program to find all possible hindi variation possible from english string
	public ArrayList<String> allPossible(String word,boolean afterConsonant,boolean start){
		
		if(word.length()<1){
			
			return new ArrayList<String>();
			
		}
		ArrayList<String> finalReturnStr = new ArrayList<String>();
		//maximum length of a hindi char in english is 3 i.e ksh so we need to check maximum for length 3 in it 
		int check = 3;
		//if length of string is less than 3 then we will not check for next 3 chars
		if(word.length()<3){
			check = word.length();
		}
		for(int i=1;i<=check;i++){
			ArrayList<String> operatedStr = new ArrayList<String>();
			ArrayList<String> returnFromRec = new ArrayList<String>();			
			String firstHalf = word.substring(0,i);  //first part of string
			String secondHalf = word.substring(i,word.length()); //second part of string
			boolean consonant = false;  //see if the current first word is consonant or not
			//System.out.println(firstHalf+"  "+secondHalf+"   "+operatedStr);
			if(!this.consonantMap.containsKey(firstHalf)){
				consonant = false;
			}else{
				consonant = true;
			}
			//make a list of all probables with first string
			ArrayList<String> list = operate(firstHalf,afterConsonant,start);
			if(list.size()>0){
				operatedStr.addAll(operate(firstHalf,afterConsonant,start)); //adding values to the list which are returned my operate function
				returnFromRec.addAll(allPossible(secondHalf,consonant,false)); //adding the values to the list which are returned by recursion 
				afterConsonant = false;
			}
			if(returnFromRec.size()>=1){
				for(String op:operatedStr){
					// if(op.equals(this.skip)){
//		 				continue;
//		 			}
					for(String ret:returnFromRec){
						finalReturnStr.add(ret+op);
					}
				}
				}
				else{
					for(String op:operatedStr){
						
						finalReturnStr.add(op);
					}
				}
			
			//System.out.println(returnFromRec);
		}
		
		//System.out.println(finalReturnStr);
		return finalReturnStr;
	}
	
	//function to return mapping to word
	public ArrayList<String> operate(String word,boolean afterConsonant,boolean start){
		ArrayList<String> operatedList = new ArrayList<String>();
		//if it is a vowel
		if(this.vowelMap.containsKey(word)){
			//vowel at word starting get normal char printed while those in between gets matras
			if(!start){
				operatedList.add(this.vowelMatra.get(word));
			}else{
			operatedList.add(this.vowelMap.get(word));
			}
			//if it "a" in the word, we assume that both with matra and without matra solution may exist
			if(word.equals("a") && !start){
				operatedList.add(new String(""));
				
			}
			return operatedList;
		}
		//if it is a consonant 
		else
		if(this.consonantMap.containsKey(word)){
			ArrayList<String> list = this.consonantMap.get(word);
			//if it is a consonant after a consonant then it must be an "aadha" consonant
			if(afterConsonant){
				for(String str:list){
					String s =  str+this.halant; //adding halant for aadha consonant
					//System.out.println("I am here");
					//s = s.substring(0,s.length());
					//System.out.println(s);
					operatedList.add(s);
				}
			}else{
				operatedList.addAll(list);
			}
			return operatedList;
		}
		return operatedList;
	}
	
	
	//function pulls all possible values for a english word also checks in the file whether that contains the word
	//if it is contained, that word will be flashed on the top of the list.
	//return an Arraylist of all possible strings
	public ArrayList<String> suggest(String word){
		ArrayList<String> list = this.allPossible(word, false, true);
		ArrayList<String> listR = new ArrayList();
		for(String s:list){
			//if(s.length()>s1.length()){
			StringBuilder sb = new StringBuilder(s);
			listR.add(sb.reverse().toString());
			//System.out.println(sb.toString());
			//System.out.println(sb.reverse().toString());
			//
		}
		//System.out.println(listR+" inside hashmain");
		BufferedReader user_words = null;
		HashMap<String,String> userMap = new HashMap<String,String>();
		
		try{
			user_words = new BufferedReader(new FileReader("user_words.txt"));
			String current = "";
			String value = "";
			while((current = user_words.readLine())!=null){
				String[] data = current.split(" ");
				if(data[0].equals(word)){
					value = data[1];
					
					listR.remove(value);
					listR.set(0, value);
					System.out.println(data[1]+" suggested");
					break;
				}
			}						
		}catch(Exception e){
			System.out.println(e);
		}
		
		return listR;
	}
	
	public static void main(String[] args){
		MainHash m = new MainHash();
		
		//System.out.println(m.consonantMap);
		String s1 = "ayush";
		ArrayList<String> list = m.suggest(s1);
//		Collections.reverse(list);
//		for(String s:list){
//			//if(s.length()>s1.length()){
//			StringBuilder sb = new StringBuilder(s);
//			System.out.println(sb.reverse().toString());
//			//
//		}
		System.out.println(list);
		}
	}

