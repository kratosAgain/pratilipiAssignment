package pratilipiSug;

import java.io.BufferedReader;
import java.io.FileReader;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;



public class MongoHelper {
	private MongoClient mongoClient = null;
	private DB database = null;
	private String databaseName = "WORDS";
	private String fileName = "dataset1.txt";
	private String mainCollection = "stock_words";
	private String userCollection = "user_words";
	
	public MongoHelper(){
		this.mongoClient = new MongoClient( "localhost" , 27017);
		this.database = this.mongoClient.getDB(this.databaseName);
		System.out.println("Database connected");
		boolean exists = this.database.collectionExists(this.mainCollection);
		boolean exists1 = this.database.collectionExists(this.userCollection);
		this.fillDatabase();
	}
	
	private void fillDatabase(){
		BufferedReader br = null;
		
		boolean exists = this.database.collectionExists(this.mainCollection);
		boolean exists1 = this.database.collectionExists(this.userCollection);
		if(exists && exists1){
			System.out.println("Already Filled");
			return ;
		}else if(exists && !exists1){
			DBCollection userCollection = database.getCollection(this.userCollection);
			return;
		}
		try{
			DBCollection stockCollection = database.getCollection(this.mainCollection);
			DBCollection userCollection = database.getCollection(this.userCollection);
			
			br = new BufferedReader(new FileReader(this.fileName));
			String current = "";
			while((current = br.readLine())!=null){				
				String[] strs = current.split("\t");	
				BasicDBObject document = new BasicDBObject();
				document.put("english",strs[0]);
				document.put("hindi",strs[1]); 
				stockCollection.insert(document);
			}
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	public String search(String eng){
		BasicDBObject query = new BasicDBObject();
		BasicDBObject field = new BasicDBObject();
		field.put("english", eng);
		field.put("hindi", "*");
		boolean exists = this.database.collectionExists(this.userCollection);
		DBCursor cursor = null;
		if(exists){
		cursor = this.database.getCollection(this.userCollection).find(query,field);
		System.out.println("user cursor count "+cursor.count());
		if(cursor.count()>0){
			System.out.println(eng+" not found in me");
			BasicDBObject bdb = (BasicDBObject) cursor.next();
			System.out.println("I am in user db " + bdb.getString("hindi"));
			return bdb.getString("hindi");
		}
		}
		cursor = this.database.getCollection(this.mainCollection).find(query,field);
		System.out.println("main cursor count "+cursor.count());
		if(cursor.count()>=0){
			System.out.println("I am in main db");
			BasicDBObject bdb = (BasicDBObject) cursor.next();
			String res =  bdb.getString("hindi");
			BasicDBObject document = new BasicDBObject();
			document.put("english",eng);
			document.put("hindi", res);
			this.database.getCollection(this.userCollection).insert(document);
			return res;
		}
		
//		Transliterate trans = new Transliterate();
//		String hindiRes = trans.transliterate(eng);
//		BasicDBObject document = new BasicDBObject();
//		document.put("english",eng);
//		document.put("hindi", hindiRes);
//		this.database.getCollection(this.userCollection).insert(document);
//		this.database.getCollection(this.mainCollection).insert(document);
		return "";
	}
	
	
	public void insert(String eng,String hin){
		BasicDBObject document = new BasicDBObject();
		document.put("english",eng);
		document.put("hindi", hin);
		this.database.getCollection(this.userCollection).insert(document);
		this.database.getCollection(this.mainCollection).insert(document);
	}
	
	
	
	public void emptyDatabase(){
		
		database.dropDatabase();
	}
	
	public void emptyCollection(String collectionName){
		DBCollection collection = this.database.getCollection(collectionName);
		collection.drop();
	}
}
