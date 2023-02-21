import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {
    private  static PlayerManager INSTANCE;
    private  final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;
    public String img(String link){
        int index = link.indexOf('=')+1;
        int index1 = link.indexOf('&');
        String minimg;
        if(index ==0){
            index = link.lastIndexOf('/')+1;
        }
        if(index1 ==-1){
            minimg = link.substring(index);
        } else {
            minimg = link.substring(index,index1);
        }
        minimg = "https://img.youtube.com/vi/"+minimg+"/maxresdefault.jpg";
        return minimg;
    }
    public String img2(String link){
        int index = link.indexOf('=')+1;
        int index1 = link.indexOf('&');
        String minimg2;
        if(index ==0){
            index = link.lastIndexOf('&');
        }
        if(index1 ==-1){
            minimg2 = link.substring(index);
        } else {
            minimg2 = link.substring(index,index1);
        }
        minimg2 = "https://img.youtube.com/vi/"+minimg2+"/maxresdefault.jpg";
        return minimg2;
    }
    public  PlayerManager(){
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }
    public GuildMusicManager getMusicManager(Guild guild){
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId)->{
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            return guildMusicManager;
        });
    }
    public void loadAndPlay(Guild channel, String trackUrl, MessageChannel channel2){
        final GuildMusicManager musicManager = this.getMusicManager(channel);
        this.audioPlayerManager.loadItem(trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                musicManager.scheduler.queue(track);
                EmbedBuilder eb = new EmbedBuilder()
                .setColor(Color.red)
                .setTitle(track.getInfo().title)
                .setDescription(Utils.DurationFormatLong(track.getInfo().length))
                .setAuthor(track.getInfo().author)
                        .setImage(img(trackUrl));
                channel2.sendMessageEmbeds(eb.build()).queue();
            }
            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                long time = 0;
                final List<AudioTrack> tracks = playlist.getTracks();
                for (final AudioTrack track : tracks){
                    musicManager.scheduler.queue(track);
                    time+=track.getInfo().length;
                }
                EmbedBuilder eb = new EmbedBuilder()
                        .setColor(Color.red)
                        .setTitle(playlist.getName())
                        .setDescription(Utils.DurationFormatLong(time))
                        .setImage(img2(trackUrl));
                channel2.sendMessageEmbeds(eb.build()).queue();
            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException e) {

            }
        });
    }

    public static PlayerManager getInstance(){
        if(INSTANCE== null){
            INSTANCE = new PlayerManager();
        }
        return  INSTANCE;
    }
}
