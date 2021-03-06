package run;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class runePathStats 
{
	public static void main (String [] args)
	{
		String [] thingies = {"Keystone","Heroism","Legend","Combat","Keystone","Malice","Tracking","Hunter","Keystone","Artefact","Excellence","Power","Keystone","Strength","Resistance","Vitality","Keystone","Contraption","Tomorrow","Beyond"};
		String [] ids = {"16","37","40","43","117","267"};
		String [] trees = {"8100","8300","8000","8400","8200"};
		String [] runes = {"8112","8124","8128","8126","8139","8143","8136","8120","8138","8135","8134","8105","8326","8351","8359","8306","8345","8313","8304","8321","8316","8347","8410","8339","8005","8008","8021","9101","9111","8009","9104","9105","9103","8014","8017","8299","8437","8439","8465","8242","8446","8463","8430","8435","8429","8451","8453","8444","8214","8229","8230","8224","8226","8243","8210","8234","8233","8237","8232","8236"};
		String [] runeNames = {"Electrocute","Predator","Dark Harvest","Cheap Shot","Taste of Blood","Sudden Impact","Zombie Ward","Ghost Poro","Eyeball Collection","Ravenous Hunter","Ingenious Hunter","Relentless Hunter","Unsealed Spellbook","Glacial Augment","Kleptomancy","Hextech Flashtraption","Biscuit Delivery","Perfect Timing","Magical Footwear","Future's Market","Minion Dematerializer","Cosmic Insight","Approach Velocity","Celestial Body","Press the Attack","Lethal Tempo","Fleet Footwork","Overheal","Triumph","Presence of Mind","Legend: Alacrity","Legend: Tenacity","Legend: Bloodline","Coup de Grace","Cut Down","Last Stand","Grasp of the Undying","Aftershock","Guardian","Unflinching","Demolish","Font of Life","Iron Skin","Mirror Shell","Conditioning","Overgrowth","Revitalize","Second Wind","Summon Aery","Arcane Comet","Phase Rush","Nullifying Orb","Manaflow Band","The Ultimate Hat","Transcendence","Celerity","Absolute Focus","Scorch","Waterwalking","Gathering Storm"};
		String [] champions = {"Soraka","Sona","Janna","Karma","Lulu","Nami"};
		String [] paths = {"Domination","Inspiration","Precision","Resolve","Sorcery"};
		double [][][][] treeStats = new double [6][5][5][21]; //dmg, healing, cc, vision, kills, deaths, assists, win, g10, g20, lvl10, lvl20, stats[13], stats[14], stats[15], stats[16], stats[17], stats[18]
		Scanner inputStream=null;
		try
		{
			inputStream=new Scanner(new FileInputStream("diamondStats"));
		}
		catch(FileNotFoundException e)
		{
			System.out.println("dun goofed");
		}
		while(inputStream.hasNext())
		{
			String line = inputStream.nextLine();
			String [] stats = new String [22];
			for(int i=0;i<22;i++)
			{
				int space = line.indexOf(" ");
				String stat = line.substring(0, space);
				stats[i] = stat;
				line = line.substring(space+1);
			}
			for(int champ=0;champ<6;champ++) //champ
			{
				if(stats[0].equals(ids[champ]))
				{
					for(int primary=0;primary<5;primary++) //primary tree
					{
						if(stats[1].equals(trees[primary]))
						{
							for(int secondary=0;secondary<5;secondary++) //secondary tree
							{
								if(stats[2].equals(trees[secondary]))
								{
							//		System.out.println(champions[champ]+" "+paths[primary]+" "+paths[secondary]);
									int dmg = Integer.parseInt(stats[9]);
									int heal = Integer.parseInt(stats[10]);
									int cc = Integer.parseInt(stats[11]);
									int vision = Integer.parseInt(stats[12]);
									int win = 1;
									if(stats[13].equals("false"))
									{
										win = 0;
									}
									int minutes = Integer.parseInt(stats[14]);
									double lvl10 = Double.parseDouble(stats[15]);
									double lvl20 = Double.parseDouble(stats[16]);
									double gold10 = Double.parseDouble(stats[17]);
									double gold20 = Double.parseDouble(stats[18]);
									int kills = Integer.parseInt(stats[19]);
									int deaths = Integer.parseInt(stats[20]);	
									int assists = Integer.parseInt(stats[21]);
									int win10 = 0;
									int loss10 = 0; 
									int win20 = 0;
									int loss20 = 0;
									int win30 = 0;
									int loss30 = 0;
									if(minutes<20)
									{
										if(win==1)
										{
											win10 = 1;
										}
										else
										{
											loss10 = 1;
										}
									}
									else if(minutes<30)
									{
										if(win==1)
										{
											win20 = 1;
										}
										else
										{
											loss20 = 1;
										}
									}
									else
									{
										if(win==1)
										{
											win30 = 1;
										}
										else
										{
											loss30 = 1;
										}
									}
									treeStats[champ][primary][secondary][0] += dmg;
									treeStats[champ][primary][secondary][1] += heal;
									treeStats[champ][primary][secondary][2] += cc;
									treeStats[champ][primary][secondary][3] += vision;
									treeStats[champ][primary][secondary][4] += win;
									treeStats[champ][primary][secondary][5] += lvl10;
									treeStats[champ][primary][secondary][6] += lvl20;
									treeStats[champ][primary][secondary][7] += gold10;
									treeStats[champ][primary][secondary][8] += gold20;
									treeStats[champ][primary][secondary][9] += kills;
									treeStats[champ][primary][secondary][10] += deaths;
									treeStats[champ][primary][secondary][11] += assists;
									treeStats[champ][primary][secondary][12] ++;
									treeStats[champ][primary][secondary][13] += win10;
									treeStats[champ][primary][secondary][14] += loss10;
									treeStats[champ][primary][secondary][15] += win20;
									treeStats[champ][primary][secondary][16] += loss20;
									treeStats[champ][primary][secondary][17] += win30;
									treeStats[champ][primary][secondary][18] += loss30;
								}
							}
						}
					}
				}
			}
		}
		inputStream.close();
		String [][][][] runeStats = new String [6][5][5][16];
		for(int i=0;i<6;i++)
		{
			for(int j=0;j<5;j++)
			{
				for(int k=0;k<5;k++)
				{
					double games = treeStats[i][j][k][12];
					for(int d=0;d<12;d++)
					{
						treeStats[i][j][k][d] = treeStats[i][j][k][d]/games;
					}
					runeStats[i][j][k][0] = ""+Round(treeStats[i][j][k][0]); //damage
					runeStats[i][j][k][1] = ""+Round(treeStats[i][j][k][1]); //healing
					runeStats[i][j][k][2] = ""+Round(treeStats[i][j][k][2]); //cc
					runeStats[i][j][k][3] = ""+Round(treeStats[i][j][k][3]); //vision
					runeStats[i][j][k][4] = ""+Round(treeStats[i][j][k][9]); //k
					runeStats[i][j][k][5] = ""+Round(treeStats[i][j][k][10]); //d
					runeStats[i][j][k][6] = ""+Round(treeStats[i][j][k][11]); //a
					runeStats[i][j][k][7] = ""+Round(treeStats[i][j][k][4]*100); //wr
					runeStats[i][j][k][8] = ""+Round(treeStats[i][j][k][13]/(treeStats[i][j][k][13]+treeStats[i][j][k][14])*100); //wr10-20
					runeStats[i][j][k][9] = ""+Round(treeStats[i][j][k][15]/(treeStats[i][j][k][15]+treeStats[i][j][k][16])*100); //wr20-30
					runeStats[i][j][k][10] = ""+Round(treeStats[i][j][k][17]/(treeStats[i][j][k][17]+treeStats[i][j][k][18])*100); //wr30+
					runeStats[i][j][k][11] = ""+Round(treeStats[i][j][k][7]); //g10
					runeStats[i][j][k][12] = ""+Round(treeStats[i][j][k][8]); //g20
					runeStats[i][j][k][13] = ""+Round(treeStats[i][j][k][5]); //lvl10
					runeStats[i][j][k][14] = ""+Round(treeStats[i][j][k][6]); //lvl20
					runeStats[i][j][k][15] = ""+games;
				}
			}
		}
		int link = 0;
		String [] statNames = {"Damage dealt","HP healed","Time CCed","Vision Score","Kills","Deaths","Assists","Winrate","Winrate 10-20","Winrate 20-30","Winrate 30+","Gold at 10","Gold at 20","Level at 10","Level at 20"};
		for(int i=0;i<6;i++)
		{
			String names = "";
			for(int j=0;j<5;j++)
			{
				for(int k=0;k<5;k++)
				{
					if(Double.parseDouble(runeStats[i][j][k][15])>250)
					{
						names = names+"\""+paths[j]+" "+paths[k]+"\",";
					}
				}
			}
			names = names.substring(0,names.length()-1);
			System.out.println("names=c("+names+")");
			String [] stats = new String [15];
			for(int whyisitnull=0;whyisitnull<15;whyisitnull++)
			{
				stats[whyisitnull] = "";
			}
			for(int j=0;j<5;j++)
			{
				for(int k=0;k<5;k++)
				{
					if(Double.parseDouble(runeStats[i][j][k][15])>250)
					{
					/*	String print = "";
						for(int p=0;p<16;p++)
						{
							print = print+" "+runeStats[i][j][k][p];
						}
						System.out.println(champions[i]+" "+paths[j]+" "+paths[k]+" "+print);*/
						for(int p=0;p<15;p++)
						{
							stats[p] = stats[p]+runeStats[i][j][k][p]+",";
						}
					}
				}
			}
			for(int g=0;g<15;g++)
			{
				stats[g] = stats[g].substring(0, stats[g].length()-1);
				System.out.println("png(file='"+link+".png',width=800,height=600)");
				link++;
				System.out.println("text(barplot(c("+stats[g]+"),main=\""+statNames[g]+"\",sub=\""+champions[i]+"\",names.arg=names), 0, c("+stats[g]+"),cex=1,pos=3)");
				System.out.println("dev.off()");
			}
		}
	}

	private static double Round(double d) 
	{
		return (Math.round(d*10.0)/10.0);
	}
}
