package colecf.safechat;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatEvents {
    private final String replaceText;
    private final Pattern[] badWords;
    private final String deleteMessage;
    private final boolean deleteWholeWord;
    private final String[][] customReplaces;

    private boolean firstChatMessage = true;

    public ChatEvents(Properties swearingConfigFile) {
        replaceText = swearingConfigFile.getProperty("replaceText", "***");
        badWords = Arrays.stream(swearingConfigFile.getProperty("words", "").split(","))
                .filter(w -> w.length() > 0)
                .map(w -> Pattern.compile("(?i)" + w))
                .toArray(Pattern[]::new);
        deleteMessage = swearingConfigFile.getProperty("deleteMessage", "false");
        deleteWholeWord = "true".contentEquals(swearingConfigFile.getProperty("deleteWholeWord", "false"));
        if (swearingConfigFile.getProperty("customReplace") != null) {
            customReplaces = Arrays.stream(swearingConfigFile.getProperty("customReplace").split(","))
                    .map(s -> s.split(":"))
                    .toArray(String[][]::new);
        } else {
            customReplaces = new String[][]{};
        }
    }

    @SubscribeEvent
    public void chatReceived(ClientChatReceivedEvent event) {
        if (firstChatMessage) {
            firstChatMessage = false;
            if (badWords.length == 0) {
                Minecraft.getInstance().ingameGUI.getChatGUI()
                        .printChatMessage(new StringTextComponent("Error loading Safechat config"));
            }
        }

        String s = event.getMessage().getString();

        boolean abort = false;

        switch (deleteMessage.toLowerCase()) {
            case "delete":
                for (Pattern badWord : badWords) {
                    if (badWord.matcher(s).find()) {
                        Matcher m = Pattern.compile("(^<[^>]+> )?.*").matcher(s);
                        m.find();
                        s = Optional.ofNullable(m.group(1)).orElse("") + "(Message has been deleted by SafeChat)";
                        break;
                    }
                }
                break;
            case "hide":
                for (Pattern badWord : badWords) {
                    Matcher m = badWord.matcher(s);
                    if (m.find()) {
                        abort = true;
                        break;
                    }
                }
                break;
            case "nochat":
                abort = true;
                break;
            default:
                for (Pattern badWord : badWords) {
                    if (!deleteWholeWord) {
                        s = badWord.matcher(s).replaceAll(replaceText);
                    } else {
                        Matcher m = badWord.matcher(s);
                        while (m.find()) {
                            int start = s.lastIndexOf(' ', m.start());
                            int end = s.indexOf(' ', m.end());
                            s = s.substring(0, start + 1) + replaceText + s.substring(end);
                        }
                    }
                }
                break;
        }

        for (String[] customReplace : customReplaces) {
            s = s.replaceAll("(?i)" + customReplace[0], customReplace[1]);
        }

        event.setCanceled(true);
        if (!abort) {
            Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessage(new StringTextComponent(s));
        }
    }
}

