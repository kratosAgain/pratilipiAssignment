package pratilipiSug;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class MainHash {

	private HashMap<String,ArrayList<String>> consonantMap = null;
	private HashMap<String,String> vowelMap = null;
	private HashMap<String,String> vowelMatra = null;
	private HashMap<String,String> numberMap = null;
	private String halant = "";
	private HashMap<String,String> halfsMap = null;
	private String skip = "skip";
	
	
	public MainHash(){
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
	
	public ArrayList<String> allPossible(String word,boolean afterConsonant,boolean start){
		
		if(word.length()<1){
			//System.out.println("I am zero");
			return new ArrayList<String>();
			
		}
		ArrayList<String> finalReturnStr = new ArrayList<String>();
		
		int check = 3;
		if(word.length()<3){
			check = word.length();
		}
		for(int i=1;i<=check;i++){
			ArrayList<String> operatedStr = new ArrayList<String>();
			ArrayList<String> returnFromRec = new ArrayList<String>();			
			String firstHalf = word.substring(0,i);
			String secondHalf = word.substring(i,word.length());
			boolean consonant = false;
			//System.out.println(firstHalf+"  "+secondHalf+"   "+operatedStr);
			if(!this.consonantMap.containsKey(firstHalf)){
				consonant = false;
			}else{
				consonant = true;
			}
			ArrayList<String> list = operate(firstHalf,afterConsonant,start);
			if(list.size()>0){
				operatedStr.addAll(operate(firstHalf,afterConsonant,start));
				returnFromRec.addAll(allPossible(secondHalf,consonant,false)); //change 
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
	
	public ArrayList<String> operate(String word,boolean afterConsonant,boolean start){
		ArrayList<String> operatedList = new ArrayList<String>();
		if(this.vowelMap.containsKey(word)){
			if(!start){
				operatedList.add(this.vowelMatra.get(word));
			}else{
			operatedList.add(this.vowelMap.get(word));
			}
			if(word.equals("a") && !start){
				operatedList.add(new String(""));
				
			}
			return operatedList;
		}else
		if(this.consonantMap.containsKey(word)){
			ArrayList<String> list = this.consonantMap.get(word);
			if(afterConsonant){
				for(String str:list){
					String s =  str+this.halant;
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

