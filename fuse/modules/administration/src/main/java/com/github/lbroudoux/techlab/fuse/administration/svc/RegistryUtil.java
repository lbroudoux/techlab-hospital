package com.github.lbroudoux.techlab.fuse.administration.svc;

import java.util.ArrayList;
/**
 * 
 * @author laurent
 */
public class RegistryUtil {
		
	static ArrayList<String> addresses = new ArrayList<String>();
	static ArrayList<String> names = new ArrayList<String>();
	
	
	public static String genUDSCode(){
		if (Math.random() < 0.5)
			return "ABDO";
		else 
			return "ORTHO";
	}
	
	public static String genRandomBirthday(){
		int randomYear = 1940 + (int)(Math.random() * 70);
		int randomMonth = 1 + (int)(Math.random() * 12); 
		int randomday = 1 + (int)(Math.random() * 28); 
		
		return randomYear+""+randomMonth+""+randomday;
	}

	public static String genRandomGender(){
		if (Math.random() < 0.5)
			return "M";
		else 
			return "F";
	}
	
	public static String genAddress(){
		int randomInt = 0 + (int)(Math.random() * addresses.size()); 	
		return addresses.get(randomInt);
	}
	
	public static String genName(){
		int randomInt = 0 + (int)(Math.random() * names.size()); 	
		return names.get(randomInt);
	}
	
	static{
		addresses.add("293 Harvest Lake Edge^^Gualala^GA^32937");
		addresses.add("3483 Tawny Sky Heights^^Lick^GA^93824");
		addresses.add("446 Wishing Nectar Gate^^Planet^GA^58268");
		addresses.add("4356 Heather Deer Mountain^^Cabinet^NY^15739");
		addresses.add("8704 Misty Hills Common^^Tingle^NM^93726");
		addresses.add("6439 3rd Street^^Memphis^TN^74927");
		addresses.add("7521 Devon Road^^Battle Ground^WA^38256");
		addresses.add("9308 Country Club^^Road Nazareth^PA^31728");
		addresses.add("93 Cooper Street^^Stroudsburg^PA^");
		addresses.add("6483 3rd Street^^East Fall River^MA^59263");
		addresses.add("8561 Hanover Court^^Cartersville^GA^21037");
		addresses.add("339 Essex Court^^Burlington^MA^31729");
		addresses.add("497 Smith Street^^Perrysburg^OH^10927");
		addresses.add("703 Durham Court^^New Bern^NC^12646");
		addresses.add("2 Bridge Street^^Bolingbrook^IL^23917");
		addresses.add("110 Bay Street^^Fargo^ND^38163");
		addresses.add("848 Valley View Road^^Rockford^MI^36272");
		addresses.add("988 3rd Street^^West Centreville^VA^64839");
		addresses.add("918 Rosewood Drive^^Ypsilanti^MI^091739");
		addresses.add("192 Laurel Lane^^Smyrna^GA^293934");
		addresses.add("902 Windsor Drive^^New Kensington^PA^39292");
		addresses.add("565 Olive Street^^Troy^NY^12180");
		
		names.add("Alex Ambram");
		names.add("Cnaeus Anissa");
		names.add("Tatiana Kajetán");
		names.add("Jachin Neirin");
		names.add("Radoslav Clotho");
		names.add("Marijana Refik");
		names.add("Johnathon Linus");
		names.add("Thokozani Helmfrid");
		names.add("Kaan Barbara");
		names.add("Malene Pamphilos");
		names.add("Njáll Lise");
		names.add("Philomena Evgeniya");
		names.add("Dua Philo");
		names.add("Alf Sheelagh");
		names.add("Bet Hilaria");
	}
}
