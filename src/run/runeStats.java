package run;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class runeStats 
{
	public static void main (String [] args)
	{
		//tree choice (primary+secondary)
		//rune stats for that tree choice
		String [] thingies = {"Keystone","Heroism","Legend","Combat","Keystone","Malice","Tracking","Hunter","Keystone","Artefact","Excellence","Power","Keystone","Strength","Resistance","Vitality","Keystone","Contraption","Tomorrow","Beyond"};
		String [] ids = {"16","37","40","43","117","267"};
		String [] trees = {"8100","8300","8000","8400","8200"};
		String [] runes = {"8112","8124","8128","8126","8139","8143","8136","8120","8138","8135","8134","8105","8326","8351","8359","8306","8345","8313","8304","8321","8316","8347","8410","8339","8005","8008","8021","9101","9111","8009","9104","9105","9103","8014","8017","8299","8437","8439","8465","8242","8446","8463","8430","8435","8429","8451","8453","8444","8214","8229","8230","8224","8226","8243","8210","8234","8233","8237","8232","8236"};
		String [] runeNames = {"Electrocute","Predator","Dark Harvest","Cheap Shot","Taste of Blood","Sudden Impact","Zombie Ward","Ghost Poro","Eyeball Collection","Ravenous Hunter","Ingenious Hunter","Relentless Hunter","Unsealed Spellbook","Glacial Augment","Kleptomancy","Hextech Flashtraption","Biscuit Delivery","Perfect Timing","Magical Footwear","Future's Market","Minion Dematerializer","Cosmic Insight","Approach Velocity","Celestial Body","Press the Attack","Lethal Tempo","Fleet Footwork","Overheal","Triumph","Presence of Mind","Legend: Alacrity","Legend: Tenacity","Legend: Bloodline","Coup de Grace","Cut Down","Last Stand","Grasp of the Undying","Aftershock","Guardian","Unflinching","Demolish","Font of Life","Iron Skin","Mirror Shell","Conditioning","Overgrowth","Revitalize","Second Wind","Summon Aery","Arcane Comet","Phase Rush","Nullifying Orb","Manaflow Band","The Ultimate Hat","Transcendence","Celerity","Absolute Focus","Scorch","Waterwalking","Gathering Storm"};
		String [] idkwhylower = {"Electrocute","Predator","Dark Harvest","Cheap Shot","Taste of Blood","Sudden Impact","Zombie Ward","Ghost Poro","Eyeball Collection","Ravenous Hunter","Ingenious Hunter","Relentless Hunter","Unsealed Spellbook","Glacial Augment","Kleptomancy","Hextech Flashtraption","Biscuit Delivery","Perfect Timing","Magical Footwear","Future's Market","Minion Dematerializer","Cosmic Insight","Approach Velocity","Celestial Body","Press the Attack","Lethal Tempo","Fleet Footwork","Overheal","Triumph","Presence of Mind","Legend: Alacrity","Legend: Tenacity","Legend: Bloodline","Coup de Grace","Cut Down","Last Stand","Grasp of the Undying","Aftershock","Guardian","Unflinching","Demolish","Font of Life","Iron Skin","Mirror Shell","Conditioning","Overgrowth","Revitalize","Second Wind","Summon Aery","Arcane Comet","Phase Rush","Nullifying Orb","Manaflow Band","The Ultimate Hat","Transcendence","Celerity","Absolute Focus","Scorch","Waterwalking","Gathering Storm"};
		String [] champions = {"Soraka","Sona","Janna","Karma","Lulu","Nami"};
		String [] paths = {"Domination","Inspiration","Precision","Resolve","Sorcery"}; 			 //21 runes in two trees-pk,p1,p2,p3,s1,s2,s3 1-3 each
		double [][][][][] treeStats = new double [6][5][5][21][19]; //dmg, healing, cc, vision, kills, deaths, assists, win, g10, g20, lvl10, lvl20, win10, loss10, win20, loss20, win30, loss30
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
									for(int s=3;s<9;s++) //runes taken
									{
										for(int rune=0;rune<60;rune++) //individual runes
										{
											if(stats[s].equals(runes[rune]))
											{
												int inTree = rune%12;
												if(s>6)
												{
													inTree = inTree+9; //can't have any of 3 keystones
												}
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
												treeStats[champ][primary][secondary][inTree][0] += dmg;
												treeStats[champ][primary][secondary][inTree][1] += heal;
												treeStats[champ][primary][secondary][inTree][2] += cc;
												treeStats[champ][primary][secondary][inTree][3] += vision;
												treeStats[champ][primary][secondary][inTree][4] += win;
												treeStats[champ][primary][secondary][inTree][5] += lvl10;
												treeStats[champ][primary][secondary][inTree][6] += lvl20;
												treeStats[champ][primary][secondary][inTree][7] += gold10;
												treeStats[champ][primary][secondary][inTree][8] += gold20;
												treeStats[champ][primary][secondary][inTree][9] += kills;
												treeStats[champ][primary][secondary][inTree][10] += deaths;
												treeStats[champ][primary][secondary][inTree][11] += assists;
												treeStats[champ][primary][secondary][inTree][12] ++;
												treeStats[champ][primary][secondary][inTree][13] += win10;
												treeStats[champ][primary][secondary][inTree][14] += loss10;
												treeStats[champ][primary][secondary][inTree][15] += win20;
												treeStats[champ][primary][secondary][inTree][16] += loss20;
												treeStats[champ][primary][secondary][inTree][17] += win30;
												treeStats[champ][primary][secondary][inTree][18] += loss30;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		String [][] popularSetups = new String [30][3];
		String [][][] runeStats = new String [30][21][16];
		int [] times = new int [30];
		for(int i=0;i<6;i++)
		{
			for(int j=0;j<5;j++)
			{
				for(int k=0;k<5;k++)
				{
					int n = 0;
					for(int lol=0;lol<21;lol++)
					{
						n = (int) (n+treeStats[i][j][k][lol][12]);
					}
					n = n/6;
					
					for(int t=0;t<30;t++)
					{
						if(n>times[t])
						{
							for(int r=29;r>t;r--)
							{
								times[r]=times[r-1];
								for(int p=0;p<3;p++)
								{
									popularSetups[r][p]=popularSetups[r-1][p];
								}
								for(int rune=0;rune<21;rune++)
								{
									for(int zzz=0;zzz<16;zzz++)
									{
										runeStats[r][rune][zzz]=runeStats[r-1][rune][zzz];										
									}
								}
							}
							times[t]=n;
							popularSetups[t][0]=champions[i];
							popularSetups[t][1]=paths[j];
							popularSetups[t][2]=paths[k];
							for(int rune=0;rune<21;rune++)
							{
								double games = treeStats[i][j][k][rune][12];
								for(int d=0;d<12;d++)
								{
									treeStats[i][j][k][rune][d] = treeStats[i][j][k][rune][d]/games;
								}
								runeStats[t][rune][0] = ""+Round(treeStats[i][j][k][rune][0]); //damage
								runeStats[t][rune][1] = ""+Round(treeStats[i][j][k][rune][1]); //healing
								runeStats[t][rune][2] = ""+Round(treeStats[i][j][k][rune][2]); //cc
								runeStats[t][rune][3] = ""+Round(treeStats[i][j][k][rune][3]); //vision
								runeStats[t][rune][4] = ""+Round(treeStats[i][j][k][rune][9]); //k
								runeStats[t][rune][5] = ""+Round(treeStats[i][j][k][rune][10]); //d
								runeStats[t][rune][6] = ""+Round(treeStats[i][j][k][rune][11]); //a
								runeStats[t][rune][7] = ""+Round(treeStats[i][j][k][rune][4]*100); //wr
								runeStats[t][rune][8] = ""+Round(treeStats[i][j][k][rune][13]/(treeStats[i][j][k][rune][13]+treeStats[i][j][k][rune][14])*100); //wr10-20
								runeStats[t][rune][9] = ""+Round(treeStats[i][j][k][rune][15]/(treeStats[i][j][k][rune][15]+treeStats[i][j][k][rune][16])*100); //wr20-30
								runeStats[t][rune][10] = ""+Round(treeStats[i][j][k][rune][17]/(treeStats[i][j][k][rune][17]+treeStats[i][j][k][rune][18])*100); //wr30+
								runeStats[t][rune][11] = ""+Round(treeStats[i][j][k][rune][7]); //g10
								runeStats[t][rune][12] = ""+Round(treeStats[i][j][k][rune][8]); //g20
								runeStats[t][rune][13] = ""+Round(treeStats[i][j][k][rune][5]); //lvl10
								runeStats[t][rune][14] = ""+Round(treeStats[i][j][k][rune][6]); //lvl20
								runeStats[t][rune][15] = ""+games;
								}
							t=30;
						}
					}
				}
			}
		}
		inputStream.close();
		PrintWriter runeStream=null;
		try
		{
			runeStream=new PrintWriter(new FileOutputStream(("runes.txt"),false));
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Goodbye Cruel World");
			System.exit(0);
		}
		for(int i=0;i<60;i++)
		{
			runeNames[i]=runeNames[i].toLowerCase();
			runeNames[i]=runeNames[i].replace(" ", "");
		}
		int link = 0;
		for(int i=0;i<16;i++) //stop at a bit over 250 games to get inspiration/sorcery sona in
		{//popularSetups[i][1].toLowerCase()
			String before = 
					"<!DOCTYPE html>\n" + 
					"<html>\n" + 
					"  <head>\n" + 
					"    <meta content=\"text/html; charset=UTF-8\" http-equiv=\"content-type\">\n" + 
					"    <link rel=\"icon\" href=\"./images/favicenser.png\">\n" + 
					"    <title></title>\n" + 
					"    <style>\n" + 
					"body {\n" + 
					"          color:white;\n" + 
					"  text-shadow:2px 2px black; \n" + 
					"background-image: url(./images/"+popularSetups[i][1].toLowerCase()+".jpg);\n" + 
					"      background-size:100% 100vh;\n" + 
					"    background-attachment: fixed;\n" + 
					"    background-position: top;\n" + 
					"   background-repeat: no-repeat;\n" + 
					"  background-color:black;\n" + 
					"  }\n" + 
					"        a:link {\n" + 
					"color: white;\n" + 
					"text-decoration: none;\n" + 
					"}\n" + 
					"a:visited {\n" + 
					"color: white;\n" + 
					"  text-decoration: none;\n" + 
					"}\n" + 
					"}"+
					"  </head>\n" + 
					"  <body>\n" + 
					"    <h1 style=\"text-align: center;\">Choose Your Runes</h1>\n" + 
					"    <p style=\"text-align: center;\"> </p>\n" + 
					"    <p style=\"text-align: center;\"> </p>\n" + 
					"    <br>"+
					"    </style>\n";
			String after = 
					"<div align=\"center\"> <br>\n" + 
					"      <br>\n" + 
					"      <br>\n" + 
					"      <br>\n" + 
					"      text goes here\n" + 
					"    </div>\n" + 
					"  </body>\n" + 
					"</html>\n";
		//	System.out.println(times[i]+" "+popularSetups[i][0]+" "+popularSetups[i][1]+" "+popularSetups[i][2]);
			runeStream.println(times[i]+" "+popularSetups[i][0]+" "+popularSetups[i][1]+" "+popularSetups[i][2]);
			runeStream.print(before);
			int addP = 0;
			int addS = 0;
			int addOne = 0;
			int addTwo = 0;
			String [] names = {"Precision","Domination","Sorcery","Resolve","Inspiration"}; //cba
			for(int j=0;j<5;j++)
			{
				if(popularSetups[i][1].equals(paths[j]))
				{
					addP = j*12;
				}
				if(popularSetups[i][2].equals(paths[j]))
				{
					addS = j*12;
				}
				if(popularSetups[i][1].equals(names[j]))
				{
					addOne = j*4;
				}
				if(popularSetups[i][2].equals(names[j]))
				{
					addTwo = j*4;
				}
			}
			for(int rune=0;rune<21;rune=rune+3)
			{
				if(rune==12)
				{
					addP = addS-9;
					addOne = addTwo;
				}
				for(int t=0;t<3;t++)
				{
					if(Double.parseDouble(runeStats[i][rune+t][15])*10<times[i])
					{
						for(int e=1;e<16;e++)
						{
							runeStats[i][rune+t][e]="";
						}
						runeStats[i][rune+t][0] = "not meta";
					}	
				}
				int thingy = addOne+(rune%12)/3;
				if(rune>=12)thingy++;
				//this could probably be generated with a loop or something but why not just take forever to type it all out haha
				String table =
						"  <table style=\"width: 100%; text-align: center;\" align=\"center\">\n" + 
						"      <tbody>\n" + 
						"        <tr>\n" + 
						"          <th style=\"text-align: center;\">"+thingies[thingy]+" <br>\n" + 
						"          </th>\n" + 
						"          <th style=\"text-align: center;\"> <img src=\"./images/"+runeNames[rune+addP]+".png\"\n" + 
						"              alt=\""+runeNames[rune+addP]+"\">\n" + 
						"          </th>\n" + 
						"          <th style=\"text-align: center;\"><img src=\"./images/"+runeNames[rune+addP+1]+".png\" alt=\""+runeNames[rune+addP+1]+"\">\n" + 
						"          </th>\n" + 
						"          <td><img src=\"./images/"+runeNames[rune+addP+2]+".png\" alt=\""+runeNames[rune+addP+2]+"\"> </td>\n" + 
						"        </tr>\n" + 
						"        <tr>\n" + 
						"          <td style=\"text-align: center;\"><a href=\"./graphs/"+(link)+".png\">Damage dealt</a></td>\n" + 
						"          <td style=\"text-align: center;\">"+runeStats[i][rune][0]+"</td>\n" + 
						"          <td style=\"text-align: center;\">"+runeStats[i][rune+1][0]+"</td>\n" + 
						"          <td>"+runeStats[i][rune+2][0]+"<br>\n" + 
						"          </td>\n" + 
						"        </tr>\n" + 
						"        <tr>\n" + 
						"          <td style=\"text-align: center;\"><a href=\"./graphs/"+(link+1)+".png\">HP healed</a></td>\n" + 
						"          <td style=\"text-align: center;\">"+runeStats[i][rune][1]+"</td>\n" + 
						"          <td style=\"text-align: center;\">"+runeStats[i][rune+1][1]+"</td>\n" + 
						"          <td>"+runeStats[i][rune+2][1]+"<br>\n" + 
						"          </td>\n" + 
						"        </tr>\n" + 
						"        <tr>\n" + 
						"          <td style=\"text-align: center;\"><a href=\"./graphs/"+(link+2)+".png\">Time CCed</a></td>\n" + 
						"          <td style=\"text-align: center;\">"+runeStats[i][rune][2]+"</td>\n" + 
						"          <td style=\"text-align: center;\">"+runeStats[i][rune+1][2]+"</td>\n" + 
						"          <td>"+runeStats[i][rune+2][2]+"<br>\n" + 
						"          </td>\n" + 
						"        </tr>\n" + 
						"        <tr>\n" + 
						"          <td style=\"text-align: center;\"><a href=\"./graphs/"+(link+3)+".png\">Vision score</a></td>\n" + 
						"          <td style=\"text-align: center;\">"+runeStats[i][rune][3]+"</td>\n" + 
						"          <td style=\"text-align: center;\">"+runeStats[i][rune+1][3]+"</td>\n" + 
						"          <td>"+runeStats[i][rune+2][3]+"<br>\n" + 
						"          </td>\n" + 
						"        </tr>\n" + 
						"        <tr>\n" + 
						"          <td style=\"text-align: center;\"><a href=\"./graphs/"+(link+4)+".png\">Kills</a></td>\n" + 
						"          </td>\n" + 
						"          <td style=\"text-align: center;\">"+runeStats[i][rune][4]+"</td>\n" + 
						"          <td style=\"text-align: center;\">"+runeStats[i][rune+1][4]+"</td>\n" + 
						"          <td>"+runeStats[i][rune+2][4]+"<br>\n" + 
						"          </td>\n" + 
						"        </tr>\n" + 
						"        <tr>\n" + 
						"          <td style=\"text-align: center; width: 25%;\"><a href=\"./graphs/"+(link+5)+".png\">Deaths</a></td>\n" + 
						"          </td>\n" + 
						"          <td style=\"text-align: center; width: 25%;\">"+runeStats[i][rune][5]+"</td>\n" + 
						"          <td style=\"text-align: center; width: 25%;\">"+runeStats[i][rune+1][5]+"</td>\n" + 
						"          <td>"+runeStats[i][rune+2][5]+"<br>\n" + 
						"          </td>\n" + 
						"        </tr>\n" + 
						"        <tr>\n" + 
						"          <td style=\"text-align: center;\"><a href=\"./graphs/"+(link+6)+".png\">Assists</a></td>\n" + 
						"          </td>\n" + 
						"          <td style=\"text-align: center;\">"+runeStats[i][rune][6]+"</td>\n" + 
						"          <td style=\"text-align: center;\">"+runeStats[i][rune+1][6]+"</td>\n" + 
						"          <td>"+runeStats[i][rune+2][6]+"<br>\n" + 
						"          </td>\n" + 
						"        </tr>\n" + 
						"        <tr>\n" + 
						"          <td style=\"text-align: center;\"><a href=\"./graphs/"+(link+7)+".png\">Winrate</a></td>\n" + 
						"          </td>\n" + 
						"          <td style=\"text-align: center;\">"+runeStats[i][rune][7]+"</td>\n" + 
						"          <td style=\"text-align: center;\">"+runeStats[i][rune+1][7]+"</td>\n" + 
						"          <td>"+runeStats[i][rune+2][7]+"<br>\n" + 
						"          </td>\n" + 
						"        </tr>\n" + 
						"        <tr>\n" + 
						"          <td style=\"text-align: center;\"><a href=\"./graphs/"+(link+8)+".png\">Winrate 10-20</a></td>\n" + 
						"          <td style=\"text-align: center;\">"+runeStats[i][rune][8]+"</td>\n" + 
						"          <td style=\"text-align: center;\">"+runeStats[i][rune+1][8]+"</td>\n" + 
						"          <td>"+runeStats[i][rune+2][8]+"<br>\n" + 
						"          </td>\n" + 
						"        </tr>\n" + 
						"        <tr>\n" + 
						"          <td style=\"text-align: center;\"><a href=\"./graphs/"+(link+9)+".png\">Winrate 20-30</a></td>\n" + 
						"          <td style=\"text-align: center;\">"+runeStats[i][rune][9]+"</td>\n" + 
						"          <td style=\"text-align: center;\">"+runeStats[i][rune+1][9]+"</td>\n" + 
						"          <td>"+runeStats[i][rune+2][9]+"<br>\n" + 
						"          </td>\n" + 
						"        </tr>\n" + 
						"        <tr>\n" + 
						"          <td style=\"text-align: center;\"><a href=\"./graphs/"+(link+10)+".png\">Winrate 30+</a></td>\n" + 
						"          <td style=\"text-align: center;\">"+runeStats[i][rune][10]+"</td>\n" + 
						"          <td style=\"text-align: center;\">"+runeStats[i][rune+1][10]+"</td>\n" + 
						"          <td>"+runeStats[i][rune+2][10]+"<br>\n" + 
						"          </td>\n" + 
						"        </tr>\n" + 
						"        <tr>\n" + 
						"          <td style=\"text-align: center;\"><a href=\"./graphs/"+(link+11)+".png\">Gold at 10</a></td>\n" + 
						"          <td style=\"text-align: center;\">"+runeStats[i][rune][11]+"</td>\n" + 
						"          <td style=\"text-align: center;\">"+runeStats[i][rune+1][11]+"</td>\n" + 
						"          <td>"+runeStats[i][rune+2][11]+"<br>\n" + 
						"          </td>\n" + 
						"        </tr>\n" + 
						"        <tr>\n" + 
						"          <td style=\"text-align: center;\"><a href=\"./graphs/"+(link+12)+".png\">Gold at 20</a></td>\n" + 
						"          </td>\n" + 
						"          <td style=\"text-align: center;\">"+runeStats[i][rune][12]+"</td>\n" + 
						"          <td style=\"text-align: center;\">"+runeStats[i][rune+1][12]+"</td>\n" + 
						"          <td>"+runeStats[i][rune+2][12]+"<br>\n" + 
						"          </td>\n" + 
						"        </tr>\n" + 
						"        <tr>\n" + 
						"          <td style=\"text-align: center;\"><a href=\"./graphs/"+(link+13)+".png\">Level at 10</a></td>\n" + 
						"          </td>\n" + 
						"          <td style=\"text-align: center;\">"+runeStats[i][rune][13]+"</td>\n" + 
						"          <td style=\"text-align: center;\">"+runeStats[i][rune+1][13]+"</td>\n" + 
						"          <td>"+runeStats[i][rune+2][13]+"<br>\n" + 
						"          </td>\n" + 
						"        </tr>\n" + 
						"        <tr>\n" + 
						"          <td style=\"text-align: center;\"><a href=\"./graphs/"+(link+14)+".png\">Level at 20</a></td>\n" + 
						"          </td>\n" + 
						"          <td style=\"text-align: center;\">"+runeStats[i][rune][14]+"</td>\n" + 
						"          <td style=\"text-align: center;\">"+runeStats[i][rune+1][14]+"</td>\n" + 
						"          <td>"+runeStats[i][rune+2][14]+"<br>\n" + 
						"          </td>\n" + 
						"        </tr>\n" + 
						"      </tbody>\n" + 
						"    </table>\n" +
						" <br>";
				String [] statNames = {"Damage dealt","HP healed","Time CCed","Vision Score","Kills","Deaths","Assists","Winrate","Winrate 10-20","Winrate 20-30","Winrate 30+","Gold at 10","Gold at 20","Level at 10","Level at 20"};
				System.out.print("names=c(");
				for(int j=0;j<3;j++)
				{
					System.out.print("\""+idkwhylower[rune+addP+j]+"\"");
					if(j<2)
					{
						System.out.print(",");
					}
				}
				System.out.println(")");
				for(int j=0;j<15;j++)
				{
					System.out.println("png(file='"+link+".png',width=800,height=600)");
					link++;
					System.out.print("text(barplot(c(");
					for(int k=0;k<3;k++)
					{
						if(!runeStats[i][rune+k][j].equals("not meta"))
						{
							System.out.print(runeStats[i][rune+k][j]);
						}						
						if(runeStats[i][rune+k][j].equals("")||runeStats[i][rune+k][j].equals("not meta")) //am i ugly
						{
							System.out.print("0");
						}
						if(k<2)System.out.print(",");
					}
					String add = "";
					for(int r=0;r<3;r++)
					{
						add = add+popularSetups[i][r];
						if(r<2)
						{
							add = add+" ";
						}
					}
					System.out.print("),main=\""+statNames[j]+"\",sub=\""+add+"\",names.arg=names), 0, c(");
					for(int k=0;k<3;k++)
					{
						if(!runeStats[i][rune+k][j].equals("not meta"))
						{
							System.out.print(runeStats[i][rune+k][j]);
						}
						if(runeStats[i][rune+k][j].equals("")||runeStats[i][rune+k][j].equals("not meta"))
						{
							System.out.print("\"not meta\"");
						}
						if(k<2)System.out.print(",");
					}
					System.out.println("),cex=1,pos=3)");
					System.out.println("dev.off()");
				}
				runeStream.println(table);
			}
			runeStream.println(after);
		}
		runeStream.close();
	}

	private static double Round(double d) 
	{
		return (Math.round(d*10.0)/10.0);
	}
}
