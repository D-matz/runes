package run;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class enchanterTradeOffs 
{
	/*
	Champs:
		Soraka 16
		Sona 37
		Janna 40
		Karma 43
		Lulu 117
		Nami 267
	Statistics:
		Damage dealt
		HP healed
		Time CCed
		Vision score
		Winrate 10-20? 20-30,30+
		Gold/XP at 10/20?
	Plan:
		Gather data
		Sort by champ/rune
		Present site
	 */
	static String key = "RGAPI-42acf9ef-ca1b-48da-80ca-3f4824c5afb2";
	public static void main (String [] args)
	{
		//getLeagues();
		//getMatchHistories();
		//String [] tiers = {"bronze","gold","silver","bronze,diamond"};
		for(int i=0;i<4;i++)
		{
		//	getStats(tiers[i]); //gonna just use diamond but maybe other leagues matter
		}
		getStats("diamond");
	}

	private static void getStats(String tier) 
	{
		ArrayList <String> usedGames = new ArrayList();
		Scanner inputStream=null;
		try
		{
			inputStream=new Scanner(new FileInputStream(tier));
		}
		catch(FileNotFoundException e)
		{
			System.out.println("dun goofed");
		}
		while(inputStream.hasNext())
		{
			String line = inputStream.nextLine();
			int spaceAt = line.indexOf(" ");
			while(spaceAt>0)
			{
				String matchId = line.substring(0,spaceAt);
				if(!usedGames.contains(matchId)&&matchId.charAt(0)=='2') //3462124875 not found
				{
					usedGames.add(matchId);
					System.out.println(matchId);
					String game = getRequest("https://na1.api.riotgames.com/lol/match/v3/matches/"+matchId+"?api_key=");
					String [] ids = {"16","37","40","43","117","267"};
					String gameSeconds = between(game,"\"gameDuration\":",",\"queueI");
					game = game.substring(game.indexOf("participants"));
					int seconds = Integer.parseInt(gameSeconds);
					int gameMinutes = seconds/60;
					for(int i=0;i<6;i++)
					{
						int champId = game.indexOf("championId\":"+ids[i]+",");
						if(gameMinutes>6&&champId>0)
						{
							String ss = game.substring(champId);
							int end = ss.indexOf("\"role\":\"DUO_SUPPORT\"");
							if(end>0)
							{
								ss = ss.substring(0,end);
								String kills = between(ss,"\"kills\":",",\"deaths\"");
								String deaths = between(ss,",\"deaths\":",",\"assists");
								String assists = between(ss,",\"assists\":",",\"largestKillingS");
								String dmg = between(ss,"totalDamageDealtToChampions\":",",\"magicDamageDealtT");
								String heal = between(ss,"totalHeal\":",",\"totalUnitsHea");
								String cc = between(ss,"timeCCingOthers\":",",\"totalDamageTaken");
								String vision = between(ss,"visionScore\":",",\"timeCCingO");
								String win = between(ss,"win\":",",\"item0");
								String xpdelta = ss.substring(ss.indexOf("xpPerMinDeltas\":{"));
								xpdelta = between(xpdelta,"xpPerMinDeltas\":{","},\"goldPerMinDelta")+",";
								String golddelta = ss.substring(ss.indexOf("ldPerMinDeltas\":{"));
								golddelta = between(golddelta,"\"ldPerMinDeltas\":{\"","}")+",";
								double lvl0 = -1;
								double lvl10 = -1;
								double gold0 = -1;
								double gold1 = -1;
								if(seconds>600)
								{
									String xps0 = xpdelta.substring(xpdelta.indexOf("0-10"));
									double xp0 = Double.parseDouble(between(xps0, ":", ","))*10;
									lvl0 = calcLevel(xp0);
									String golds0 = golddelta.substring(golddelta.indexOf("0-10"));
									gold0 = Double.parseDouble(between(golds0, ":", ","))*10;
									if(seconds>1200) //use seconds instead of minutes to get more matches because if it's exactly 20m it doesn't record 10-20 deltas
									{
										String xps1 = xpdelta.substring(xpdelta.indexOf("10-20"));
										double xp10 = Double.parseDouble(between(xps1, ":", ","))*10;
										lvl10 = calcLevel(xp10+xp0);
										String golds1 = golddelta.substring(golddelta.indexOf("10-20"));
										gold1 = Double.parseDouble(between(golds1, ":", ","))*10+gold0;										
									}
								}
								String statList = dmg+" "+heal+" "+cc+" "+vision+" "+win+" "+gameMinutes+" "+lvl0+" "+lvl10+" "+gold0+" "+gold1+" "+kills+" "+deaths+" "+assists+" ";
								String runes = between(ss,"playerScore9",",\"timeline"); //get primary/secondary tree, then runes within that tree
								String primary = between(runes,"perkPrimaryStyle\":",",\"perkSubStyle");
								String secondary = between(runes,",\"perkSubStyle\":","}");
								String runeList = primary+" "+secondary+" ";
								for(int rune=0;rune<6;rune++) //6 runes chosen
								{
									String before = "perk"+rune+"\":";
									String after = ",\"perk"+rune+"Var1";
									runeList = runeList+between(runes,before,after)+" ";
								}
						//		System.out.println(ids[i]+" "+runeList+" "+statList);
								PrintWriter statStream=null;
								try
								{
									statStream=new PrintWriter(new FileOutputStream((tier+"Stats.txt"),true));
								}
								catch(FileNotFoundException e)
								{
									System.out.println("Goodbye Cruel World");
									System.exit(0);
								}
								statStream.println(ids[i]+" "+runeList+statList);
								statStream.flush(); //I still don't know what this does
							}
						}
					}
				}
				line = line.substring(spaceAt+1);
				spaceAt = line.indexOf(" ");
			}
		}
		
	}

	private static void getMatchHistories()
	{
		int on = 302;
		Scanner inputStream=null;
		try
		{
			inputStream=new Scanner(new FileInputStream("leagues"));
		}
		catch(FileNotFoundException e)
		{
			System.out.println("dun goofed");
		}
		for(int i=0;i<on;i++)
		{
			String skipped = inputStream.nextLine();
		}
		while(inputStream.hasNext())
		{
			String line = inputStream.nextLine();
			getGameInfo(line);
			on++;
			System.out.println(on);
		}
		inputStream.close();
	}
	
	private static double calcLevel(double xp0)
	{
		if(xp0==0)return 1;//save afk 0 xp headache
		int [] levelUp = {280,380,480,580,680,780,880,980,1080,1180,1280,1380,1480,1580,1680,1780,1880};
		int lvl10 = 0; //actually has nothing to do with level 10 but oh well
		while(xp0>0)
		{
			xp0 = xp0 - levelUp[lvl10];
			lvl10++;
		}
		lvl10--;
		xp0 = xp0+levelUp[lvl10];
		double lvl = 1+lvl10 + xp0/levelUp[lvl10];
		//lvl = Math.round(lvl*10.0)/10.0;
		if(lvl>18) lvl=18;
		return lvl;
	}

	private static void getGameInfo(String league)
	{
		int space = league.indexOf(" ");
		String tier = league.substring(0, space);
		System.out.println(tier);
		if(tier.equals("DIAMOND"))
		{
		PrintWriter rankStream=null;
		try
		{
			rankStream=new PrintWriter(new FileOutputStream((tier+".txt"),true));
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Goodbye Cruel World");
			System.exit(0);
		}
		league = league.substring(space+1);
		space = league.indexOf(" ");
		while(space>0)
		{
			String acc = league.substring(0, space);
		//	System.out.println(acc);
			String summonerInfo = getRequest("https://na1.api.riotgames.com/lol/summoner/v3/summoners/"+acc+"?api_key=");
			String accId = between(summonerInfo,"accountId\":",",\"name");
			String matchlist = canFailRequest("https://na1.api.riotgames.com/lol/match/v3/matchlists/by-account/"+accId+"?queue=420&beginTime=1513352071000&api_key=");
			if(!matchlist.equals("none"))
			{
				String print = "";
				while(matchlist.contains("gameId\":"))
				{
					String game = between(matchlist,"gameId\":",",\"champion");
					print = print+game+" ";
					matchlist = matchlist.substring(matchlist.indexOf(",\"champion")+30);
				}
				rankStream.println(print);
				rankStream.flush(); //what is this? I don't know. But it seems necessary
			}
			league = league.substring(space+1);
			space = league.indexOf(" ");
		}
		String summonerInfo = getRequest("https://na1.api.riotgames.com/lol/summoner/v3/summoners/"+league+"?api_key=");
		String accId = between(summonerInfo,"accountId\":",",\"name");
		String matchlist = canFailRequest("https://na1.api.riotgames.com/lol/match/v3/matchlists/by-account/"+accId+"?queue=420&beginTime=1513352071000&api_key=");
		if(!matchlist.equals("none"))
		{
			String print = "";
			while(matchlist.contains("gameId\":"))
			{
				String game = between(matchlist,"gameId\":",",\"champion");
				print = print+game+" ";
				matchlist = matchlist.substring(matchlist.indexOf(",\"champion")+30);
			}
			rankStream.println(print);
			rankStream.flush();
		}
		}
	}

	private static String canFailRequest(String s)
	{
		try
		{
		 Thread.sleep(900);
		 URL myurl = new URL(s+key);
		 HttpsURLConnection con = (HttpsURLConnection)myurl.openConnection();
		 InputStream ins = con.getInputStream();
		 InputStreamReader isr = new InputStreamReader(ins);
		 BufferedReader in = new BufferedReader(isr);
		 String info;
		 String r="";
		 while ((info = in.readLine()) != null)
		 {
			 r=r+info;
		 }
		 return r;
		}catch(Exception e)
		{
			return "none";
		}
	}

	private static void getLeagues() 
	{
		int on = 16;
		Scanner inputStream=null;
		try
		{
			inputStream=new Scanner(new FileInputStream("summonerIDs"));
		}
		catch(FileNotFoundException e)
		{
			System.out.println("dun goofed");
		}
		for(int i=0;i<on;i++)
		{
			String alreadyDone = inputStream.nextLine();
		}
		while(inputStream.hasNext())
		{
			String line = inputStream.nextLine();
			String league = getRequest("https://na1.api.riotgames.com/lol/league/v3/leagues/by-summoner/"+line+"?api_key=");
			printLeagueSummoners(league);
			on++;
			System.out.println(on);
		}
	/*	String master = getRequest("https://na1.api.riotgames.com/lol/league/v3/masterleagues/by-queue/RANKED_SOLO_5x5?api_key=");
		String challenger = getRequest("https://na1.api.riotgames.com/lol/league/v3/challengerleagues/by-queue/RANKED_SOLO_5x5?api_key=");
		printLeagueSummoners(master);
		printLeagueSummoners(challenger);*/	
	}

	private static void printLeagueSummoners(String league)
	{
		PrintWriter leagueStream=null;
		try
		{
			leagueStream=new PrintWriter(new FileOutputStream("leagues.txt",true));
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Goodbye Cruel World");
			System.exit(0);
		}
		int soloq = league.indexOf("queue\":\"RANKED_SOLO_5x5");
		if(soloq!=-1)
		{
			String tierIn = between(league,"tier\":\"","\",\"queue\":\"RANKED_SOLO_5x5");
			league = league.substring(soloq+1);
			int other = league.indexOf("\"queue\"");
			if(other!=-1)
			{
				league = league.substring(0, other);
			}
			String print = tierIn;
			while(league.contains("playerOrTeamId\":\""))
			{
				league = league.substring(league.indexOf("playerOrTeamId\":\""));
				String id = between(league,"playerOrTeamId\":\"","\",\"playerOrTeamName");
				print = print+" "+id;
				league = league.substring(60);
			}
			leagueStream.println(print);
		}
		leagueStream.close();
	}

/*	public static String getRequest (String s)
	{
		try
		{
		 Thread.sleep(900);
		 URL myurl = new URL(s+key);
		 HttpsURLConnection con = (HttpsURLConnection)myurl.openConnection();
		 InputStream ins = con.getInputStream();
		 InputStreamReader isr = new InputStreamReader(ins);
		 BufferedReader in = new BufferedReader(isr);
		 String info;
		 String r="";
		 while ((info = in.readLine()) != null)
		 {
			 r=r+info;
		 }
		 return r;
		}catch(Exception e)
		{
			System.out.println("retrying: "+s);
			return getRequest(s);
		}
	}*/
	
	public static String getRequest (String s)
	{
		try
		{
		 Thread.sleep(150);
		 URL myurl = new URL(s+key);
		 HttpsURLConnection con = (HttpsURLConnection)myurl.openConnection();
		 InputStream ins = con.getInputStream();
		 InputStreamReader isr = new InputStreamReader(ins);
		 BufferedReader in = new BufferedReader(isr);
		 String info;
		 String r="";
		 while ((info = in.readLine()) != null)
		 {
			 r=r+info;
		 }
		 return r;
		}catch(Exception e)
		{
			System.out.println("retrying: "+s);
			return getRequest(s);
		}
	}
	
	private static String between(String in, String before, String after)
	{
		int b = in.indexOf(before)+before.length();
		int a = in.indexOf(after);
		return(in.substring(b, a));
	}
}
