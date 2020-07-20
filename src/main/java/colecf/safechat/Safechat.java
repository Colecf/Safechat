package colecf.safechat;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

@Mod("colecf_safechat")
public class Safechat {
    public Safechat() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    // For some reason this doesn't seem to work with @SubscribeEvent, and instead
    // had to be registered with FMLJavaModLoadingContext
    public void setup(FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new ChatEvents(loadProperties()));
    }

    public static Properties loadProperties() {
        Properties swearingConfigFile = new Properties();
        String path = Minecraft.getInstance().gameDir.toString() + "/config/safechat.properties";

        try {
            try (FileInputStream fip = new FileInputStream(path)) {
                swearingConfigFile.load(fip);
            }
        } catch (FileNotFoundException e) {
            try {
                String content = "# Default settings for safechat for minecraft 1.16.1\n\n"
                        + "# What will appear instead of swear words, if deleteMessage is false\n"
                        + "replaceText=***\n\n"
                        + "# For deleteMessage, you can put the following:\n"
                        + "# hide: doesn't show the message, as if it never existed\n"
                        + "# delete: deletes the message but lets you see the username\n"
                        + "# nochat: disables chat entirely. This will also prevent you from seeing commands\n"
                        + "#         like /home, but you'll still be able to use them. You won't be able to see yourself\n"
                        + "#         talk, but others will\n# false: Simply **** out the words\n"
                        + "deleteMessage=false\n\n"
                        + "# If deleteWholeWord is true, and it finds a swear word that's part of another word,\n"
                        + "# (For example, swearwordalicious) it will replace the rest of the word as well.\n"
                        + "deleteWholeWord=false\n\n"
                        + "# Phrases to replace with other phrases:\n"
                        + "customReplace=a_safechat_test:replaced,another_safechat_test:replaced2\n\n"
                        + "# The swear words (or phrases).\n# They are separated by commas, so they can't contain commas themselves.\n"
                        + "# They support java regular expressions: http://docs.oracle.com/javase/tutorial/essential/regex/\n"
                        + "# Backslashes are used to escape newlines in this config file, so you may have to double backslash some things.\n"
                        + "words=fucking,fucked,8=+D~*,ahole,anus,ash0le,ash0les,asholes,\\\\bass\\\\b,Ass Monkey,Assface,assh0le,assh0lez,asshole,assholes,assholz,asswipe,azzhole,bassterds,bastard,bastards,bastardz,basterds,basterdz,Biatch,bitch,bitches,Blow Job,boffing,butthole,buttwipe,c0ck,c0cks,c0k,Carpet Muncher,cawk,cawks,Clit,cnts,cntz,cock,cockhead,cock-head,cocks,CockSucker,cock-sucker,crap, cum ,cunt,cunts,cuntz,dick,dild0,dild0s,dildo,dildos,dilld0,dilld0s,dominatricks,dominatrics,dominatrix,dyke,enema,f u c k,f u c k e r,fag,fag1t,faget,fagg1t,faggit,faggot,fagit,fags,fagz,faig,faigs,flipping the bird,fuck,fucker,fuckin,fucking,fucks,Fudge Packer,fuk,Fukah,Fuken,fuker,Fukin,Fukk,Fukkah,Fukken,Fukker,Fukkin,g00k,gay,gayboy,gaygirl,gays,gayz,God-damned,h00r,h0ar,h0re,hells,hoar,hoor,hoore,jackoff,jap,japs,jerk-off,jisim,jiss,jizm,jizz,knob,knobs,knobz,kunt,kunts,kuntz,Lesbian,Lezzian,Lipshits,Lipshitz,masochist,masokist,massterbait,masstrbait,masstrbate,masterbaiter,masterbate,masterbates,Motha Fucker,Motha Fuker,Motha Fukkah,Motha Fukker,Mother Fucker,Mother Fukah,Mother Fuker,Mother Fukkah,Mother Fukker,mother-fucker,Mutha Fucker,Mutha Fukah,Mutha Fuker,Mutha Fukkah,Mutha Fukker,n1gr,nastt,nigger;,nigur;,niiger;,niigr;,orafis,orgasim;,orgasm,orgasum,oriface,orifice,orifiss,packi,packie,packy,paki,pakie,paky,pecker,peeenus,peeenusss,peenus,peinus,pen1s,penas,penis,penis-breath,penus,penuus,Phuc,Phuck,Phuk,Phuker,Phukker,polac,polack,polak,Poonani,pr1c,pr1ck,pr1k,pusse,pussee,pussy,puuke,puuker,queers,queerz,qweers,qweerz,qweir,recktum,rectum,retard,sadist,scank,schlong,screwing,semen,sex,sexy,Sh\\!t,sh1t,sh1ter,sh1ts,sh1tter,sh1tz,shit,shits,shitter,Shitty,Shity,shitz,Shyt,Shyte,Shytty,Shyty,skanck,skank,skankee,skankey,skanks,Skanky,slut,sluts,Slutty,slutz,son-of-a-bitch, tit ,turd,va1jina,vag1na,vagiina,vagina,vaj1na,vajina,vullva,vulva,w0p,wh00r,wh0re,whore,xrated,xxx,b\\!+ch,bitch,blowjob,clit,arschloch,fuck,shit,asshole,b\\!tch,b17ch,b1tch,bastard,bi+ch,boiolas,buceta,c0ck,cawk,chink,cipa,clits,cock,cum,cunt,dildo,dirsa,ejakulate,fatass,fcuk,fuk,fux0r,hoer,hore,jism,kawk,l3itch,l3i+ch,lesbian,masturbate,masterbat,masterbat3,motherfucker,mofo,nazi,nigga,nigger,nutsack,phuck,pimpis,pusse,pussy,scrotum,sh!t,shemale,slut,smut,teets,tits,boobs,b00bs,teez,testical,testicle,titt,w00se,jackoff,wank,whoar,whore,damn,dyke,fuck,shit,@$$,amcik,andskota,arse,assrammer,ayir,bi7ch,bitch,bollock,butt-pirate,cabron,cazzo,chraa,chuj,Cock,cunt,d4mn,daygo,dego,dick,dike,dupa,dziwka,ejackulate,Ekrem,Ekto,enculer,faen,fag,fanculo,fanny,feces,feg,Felcher,ficken,fitt,Flikker,foreskin,Fotze,fuk,futkretzn,gay,gook,guiena,h0r,helvete,hoer,honkey,Huevon,hui,injun,jizz,kanker,kike,klootzak,kraut,knulle,kuk,kuksuger,Kurac,kurwa,kusi,kyrpa,lesbo,mamhoon,masturbat,merd,mibun,monkleigh,mouliewop,muie,mulkku,muschi,nazis,nepesaurio,nigger,orospu,paska,perse,picka,pierdol,pillu,pimmel,piss,pizda,poontsee,poop,porn,p0rn,pr0n,preteen,pula,pule,puta,puto,qahbeh,queef,rautenberg,schaffer,scheiss,schlampe,schmuck,screw,sh!t,sharmuta,sharmute,shipal,shiz,skribz,skurwysyn,sphencter,spic,spierdalaj,splooge,suka,b00b,testicle,titt,twat,vittu,wank,wetback,wichser,wop,\\\\byed\\\\b,zabourah\n\n";

                try (FileOutputStream fop = new FileOutputStream(path)) {
                    fop.write(content.getBytes());
                }

                try (FileInputStream fip = new FileInputStream(path)) {
                    swearingConfigFile.load(fip);
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return swearingConfigFile;
    }
}