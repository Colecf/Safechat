package colecf.safechat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.StringTranslate;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatEvents
{
	static String replaceText;
	static String[] badWords;
	static String deleteMessage;
	static String deleteWholeWordString;
	static String[][] customReplaces;
	
	public static void init()
	{
		Properties swearingConfigFile = Safechat.swearingConfigFile;
		replaceText = swearingConfigFile.getProperty("replaceText");
		badWords = swearingConfigFile.getProperty("words").split(",");
		deleteMessage = swearingConfigFile.getProperty("deleteMessage");
		deleteWholeWordString = swearingConfigFile.getProperty("deleteWholeWord");
		if(swearingConfigFile.getProperty("customReplace")==null)
		{
			System.out.println("Generating customReplace in old config file");
			appendToConfig("\n# Phrases to replace with other phrases:\n"
						+ "customReplace=a safechat test:replaced,another safechat test:replaced2\n\n");
		}
		String[] temp = swearingConfigFile.getProperty("customReplace").split(",");
		ArrayList temp2 = new ArrayList<String[]>();
		for(int i=0; i<temp.length; i++)
		{
			temp2.add(temp[i].split(":"));
		}
		customReplaces = new String[temp2.size()][];
		for(int i=0; i<temp2.size(); i++)
		{
			customReplaces[i] = (String[]) temp2.get(i);
		}
	}
	
	@SubscribeEvent
	public void chatReceived(ClientChatReceivedEvent event)
	{/*
		String s = ChatMessageComponent.createFromJson(event.message).toStringWithFormatting(true);
		
		boolean deleteWholeWord = false;
        if(deleteWholeWordString.equals("true"))
        {
        	deleteWholeWord = true;
        }
		boolean abort = false;

		if (deleteMessage.toLowerCase().equals("delete"))
		{
			if ((s.charAt(0) == '<') && (s.indexOf('>') != -1))
			{
				for (int i = 0; i < badWords.length; i++)
				{
					Pattern p = Pattern.compile("(?i)" + badWords[i]);
					Matcher m = p.matcher(s + " ");
					System.out.println(badWords[i]);
					if (m.find())
					{
						s = s.substring(0, s.indexOf('>') + 1) + " (Message has been deleted by SafeChat)";
						break;
					}
				}
			}
			else
				for (int i = 0; i < badWords.length; i++)
				{
					Pattern p = Pattern.compile("(?i)" + badWords[i]);
					Matcher m = p.matcher(s + " ");
					if (m.find())
					{
						s = "(Message has been deleted by SafeChat)";
						break;
					}
				}
		}
		else if (deleteMessage.toLowerCase().equals("hide"))
		{
			for (int i = 0; i < badWords.length; i++)
			{
				Pattern p = Pattern.compile("(?i)" + badWords[i]);
				Matcher m = p.matcher(s + " ");
				if (m.find())
				{
					abort = true;
				}
			}
		}
		else if (deleteMessage.toLowerCase().equals("nochat"))
		{
			abort = true;
		}

		for (int i = 0; i < badWords.length; i++)
        {
            String regex = "(?i)" + badWords[i];
            if(!deleteWholeWord)
            {
                s = (s+" ").replaceAll(regex, replaceText);
                if(s.charAt(s.length()-1)==' ')
                {
                	s = s.substring(0, s.length()-1);
                }
            } else
            {
            	Matcher m = Pattern.compile(regex).matcher(s+" ");
            	while(m.find())
            	{
            		int start = findSpaceBefore(s, m.start());
            		int end = findSpaceAfter(s+" ", m.end());
            		s = s.substring(0, start+1) + replaceText + s.substring(end);
            	}
            }
        }
		
		for(int i=0; i<customReplaces.length; i++)
		{
			String regex = "(?i)"+customReplaces[i][0];
			s = s.replaceAll(regex, customReplaces[i][1]);
		}

		event.setCanceled(true);
		if (!abort)
		{
			
			Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(s);
		}*/
	}
	
	private int findSpaceBefore(String s, int index)
    {
    	while(index >= 0 && s.charAt(index)!=' ')
    	{
    		index--;
    	}
		return index;
    }
    private int findSpaceAfter(String s, int index)
    {
    	while(index < s.length() && s.charAt(index)!=' ')
    	{
    		index++;
    	}
		return index;
    }
    
    public static void appendToConfig(String toAppend)
    {
    	String path = Minecraft.getMinecraft().mcDataDir.toString() + "/config/safechat.properties";

		FileOutputStream fop = null;
		try
		{
			FileWriter fw = new FileWriter(path,true);
			fw.write(toAppend);
			fw.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("ERROR: Couldn't open config file when it's supposed to be generated!");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}

