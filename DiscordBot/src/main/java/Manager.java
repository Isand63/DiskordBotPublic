import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import java.util.Arrays;
import java.util.List;

public class Manager extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event){
//        if(!event.getAuthor().isBot()){
//            String messageSent = event.getMessage().getContentRaw();
//            event.getChannel().sendMessage("вы написали: "+ messageSent).queue();
//            event.getJDA().getCategories();
//        }
        Member member = event.getMember();
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        MessageChannel channel2 = event.getChannel();
        String[] com = event.getMessage().getContentRaw().split(" ");
//        if(event.getMessage().getContentRaw().startsWith("!join")){
//            final String content = event.getMessage().getContentRaw();
//            final List<String> command = Arrays.asList(content.split(" "));
//            Guild guild = event.getGuild();
//            VoiceChannel channel = guild.getVoiceChannelsByName(command.get(1), true).get(0);
//            AudioManager audioManager = guild.getAudioManager();
//            audioManager.openAudioConnection(channel);
//        }

            switch (com[0]) {
                case ("!play"):
                    musicManager.scheduler.player.stopTrack();
                    musicManager.scheduler.queue.clear();
                    final String content = event.getMessage().getContentRaw();
                    final List<String> command = Arrays.asList(content.split(" "));
                    Guild guild = event.getGuild();
                    try {
                    VoiceChannel channel = member.getVoiceState().getChannel().asVoiceChannel();
                    final AudioManager audioManager = event.getGuild().getAudioManager();
                    audioManager.openAudioConnection(channel);
                    PlayerManager.getInstance().loadAndPlay(guild, command.get(1), channel2);
                }catch (Exception e) {
                    channel2.sendMessage("Вы не в канале").queue();
                }
                    break;
                case ("!stop"):
                    musicManager.scheduler.player.stopTrack();
                    musicManager.scheduler.queue.clear();
                    channel2.sendMessage("Трек остановлен").queue();
                    break;

                case ("!skip"):
                    musicManager.scheduler.nextTrack();
                    channel2.sendMessage("Музыка пропущенна").queue();
                    break;

                case ("!pause"):
                    musicManager.scheduler.player.setPaused(true);
                    channel2.sendMessage("Музыка приостановлена").queue();
                    break;

                case ("!resume"):
                    musicManager.scheduler.player.setPaused(false);
                    channel2.sendMessage("Музыка возобнавлена").queue();
                    break;

                case ("!leave"):
                    musicManager.scheduler.player.stopTrack();
                    musicManager.scheduler.queue.clear();
                    AudioManager aud = event.getGuild().getAudioManager();
                    aud.closeAudioConnection();
                    channel2.sendMessage("Бот покинул чат");
                    break;

                default:
                    channel2.sendMessage("Неправильная комманда");
                    break;
            }


        switch (com[0]) {
            case ("!help"):
                channel2.sendMessage("!play <ссылка> - бот подключится и по ссылке запустит музыку\n" +
                        "!stop - остановит трек и очистит очередь\n" +
                        "!skip - пропустит играющий в данную минуту трек\n" +
                        "!pause - приостановит играющую музыку\n" +
                        "!resume - возобновит трек\n" +
                        "!leave - насильно отключит бота если он напрягает").queue();
                break;
        }
        }
    }
